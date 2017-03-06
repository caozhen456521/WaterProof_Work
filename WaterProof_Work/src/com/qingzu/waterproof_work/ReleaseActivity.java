package com.qingzu.waterproof_work;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.example.multi_image_selector.ProducGridAdapter;
import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.CreateProjectCaseModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProjectCase1;
import com.qingzu.entity.ProjectPhoto;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
//import com.qingzu.waterproof_work.WantDayWorkerActivity.upload;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import me.multi_image_selector.view.BasePhotoPreviewActivity;
import me.multi_image_selector.view.HorizontalListView;
import multi_image_selector.MultiImageSelectorActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Administrator 发布
 */

public class ReleaseActivity extends Activity implements OnClickListener {

	private MyTitleView mtvReleaseTitle = null;
	private Button release_bt_commit; // 发布按钮
	private EditText release_edt; // 输入框
	private String UserToken = null;
	private String QiniuToken = null;
	// 启动actviity的请求码
	private static final int REQUEST_IMAGE = 2;
	// 存放图片路径的list
	private ArrayList<String> mSelectPath;
	// 存放加号的list
	List<Bitmap> jiaListBitMap;
	// 存放所有图片的list(不包括加号)
	List<Bitmap> listBitMap;
	List<ProjectPhoto> list = new ArrayList<ProjectPhoto>();
	ProducGridAdapter adapter = null;
	private HorizontalListView noScrollgridview;
	private int width;
	CreateProjectCaseModel caseModel;
	private ShowProgressDialog dialog = null;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release);
		Tools.setNavigationBarColor(this, R.color.title_background_blue);
		AppManager.getAppManager().addActivity(this);
		intiView();
		myOnclick();
		bindGridView();
	}

	public void intiView() {

		mtvReleaseTitle = (MyTitleView) findViewById(R.id.login_title);
		release_bt_commit = (Button) findViewById(R.id.release_bt_commit);
		release_edt = (EditText) findViewById(R.id.release_edt);
		noScrollgridview = (HorizontalListView) findViewById(R.id.noScrollgridview);

		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		getQiniuToken();
		jiaListBitMap = new ArrayList<Bitmap>();
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		dialog = getProDialog();
		release_bt_commit.setOnClickListener(this);
		release_edt.setOnClickListener(this);

		mtvReleaseTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		// noScrollgridview.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.release_bt_commit:
			dialog.show();
			// release_bt_commit.setEnabled(false);
			// if (release_edt.getText().toString().trim() == null
			// || release_edt.getText().toString().trim().contains("")) {
			// T.showToast(ReleaseActivity.this, "描述不能为空");
			//
			// } else if (listBitMap == null) {
			// T.showToast(ReleaseActivity.this, "图片不能为空");
			//
			// } else {

			// }
			caseModel = new CreateProjectCaseModel();
			if (release_edt.getText().toString().trim().equals("")
					|| release_edt.getText().toString().trim() == null) {
				T.showToast(ReleaseActivity.this, "描述不能为空");
				dialog.dismiss();
				return;
			}

			else if (mSelectPath != null) {
				new Thread(new Runnable() {
					public void run() {

						uploadPicture();
					};
				}).start();
			}

			else {
				caseModel = new CreateProjectCaseModel();
				ProjectCase1 projectCase = new ProjectCase1();
				projectCase.setCaseDescribe(release_edt.getText().toString()
						.trim());
				caseModel.setProjectCase(projectCase);
				Gson gson = new Gson();

				postCreateProjectCase(gson.toJson(caseModel));
			}
			break;
		case R.id.release_edt:

			break;
		default:
			break;
		}

	}

	/**
	 * 当前登录用户创建施工队/个人工程案例相册信息
	 */

	public void postCreateProjectCase(String josn) {
		RequestParams params = new RequestParams(HttpUtil.CreateProjectCase);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(josn);

		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<CreateProjectCaseModel> interfaceReturn = new InterfaceReturn<CreateProjectCaseModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						CreateProjectCaseModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ReleaseActivity.this,
								interfaceReturn.getMessage());
						// release_bt_commit.setEnabled(true);

						finish();
						// recruitDayWorker =
						// interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						// finish();
						// Gson gson = new Gson();
						// CreateRecruitmentPhotoListInfo(gson.toJson(list));

					} else {
						T.showToast(ReleaseActivity.this,
								interfaceReturn.getMessage());
						dialog.dismiss();
						// release_bt_commit.setEnabled(true);
					}
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				dialog.dismiss();
				// release_bt_commit.setEnabled(true);
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});

	}

	public void uploadPicture() {

		for (int i = 0; i < mSelectPath.size(); i++) {
			String PhotoName = UUID.randomUUID() + "";
			if (FileUtils.saveBitmap(
					Tools.comp(BitmapFactory.decodeFile(mSelectPath.get(i))),
					PhotoName) != null)
				;
			new upload().execute(FileUtils.SDPATH + PhotoName + ".png");
			// new upload().execute(mSelectPath.get(i));
		}

	}

	class upload extends AsyncTask<String, Integer, Result> {

		@Override
		protected Result doInBackground(final String... params) {
			// TODO Auto-generated method stub

			String PhotoName = UUID.randomUUID() + "";

			UploadManager uploadManager = new UploadManager();
			uploadManager.put(params[0], PhotoName, QiniuToken,
					new UpCompletionHandler() {

						@Override
						public void complete(String arg0, ResponseInfo arg1,
								JSONObject arg2) {
							// TODO Auto-generated method stub
							if (arg1.isOK()) {

								BitmapFactory.Options options = new BitmapFactory.Options();

								/**
								 * 最关键在此，把options.inJustDecodeBounds = true;
								 * 这里再decodeFile()，
								 * 返回的bitmap为空，但此时调用options.outHeight时
								 * ，已经包含了图片的高了
								 */
								options.inJustDecodeBounds = true;
								Bitmap bitmap = BitmapFactory.decodeFile(
										params[0], options); // 此时返回的bitmap为null

								ProjectPhoto projectPhoto = new ProjectPhoto();
								projectPhoto
										.setCasePhoto("http://7xoiij.com2.z0.glb.qiniucdn.com/"
												+ arg0);
								projectPhoto.setPhotoLength(options.outHeight);
								projectPhoto.setPhotoWidth(options.outWidth);
								list.add(projectPhoto);
								caseModel.setProjectPhoto(list);

								if (list.size() == mSelectPath.size()) {
									// release();
									ProjectCase1 projectCase = new ProjectCase1();
									projectCase.setCaseDescribe(release_edt
											.getText().toString().trim());
									caseModel.setProjectCase(projectCase);
									Gson gson = new Gson();

									postCreateProjectCase(gson
											.toJson(caseModel));

								}

							}
						}
					}, new UploadOptions(null, null, false,
							new UpProgressHandler() {

								@Override
								public void progress(String arg0, double arg1) {
									// TODO Auto-generated method stub

								}
							}, null));
			return null;
		}

	}

	/**
	 * 绑定加号
	 */
	private void bindGridView() {
		// 把加号放到list中并绑定，到gridView中
		jiaListBitMap = bindJia();
		hengping();
		// 把数据绑定一下
		adapter = new ProducGridAdapter(jiaListBitMap, this);
		noScrollgridview.setAdapter(adapter);
	}

	// 绑定加号
	private List<Bitmap> bindJia() {
		// 把加号放到list中并绑定，到gridView中
		InputStream is = getResources().openRawResource(
				R.raw.icon_addpic_unfocused);

		Bitmap myBitmapJia = BitmapFactory.decodeStream(is);
		jiaListBitMap.add(myBitmapJia);
		return jiaListBitMap;
	}

	/**
	 * 图片压缩，如果不压缩会导致图片溢出异常
	 */
	private Bitmap createThumbnail(String filepath, int i) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = i;
		return BitmapFactory.decodeFile(filepath, options);
	}

	private void hengping() {
		ViewGroup.LayoutParams params = noScrollgridview.getLayoutParams();
		// dishtype，welist为ArrayList
		WindowManager wm = this.getWindowManager();
		int dishtypes = jiaListBitMap.size();
		// 图片之间的距离
		params.width = width / 20 * 6 * dishtypes;
		Log.d("看看这个宽度", params.width + "" + jiaListBitMap.size());
		noScrollgridview.setLayoutParams(params);
		// 设置列数为得到的list长度
		// noScrollgridview.setNumColumns(jiaListBitMap.size());
	}

	/**
	 * 
	 * 拍照和相册返回的数据
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// 把数据转化成Bitmap,放选中的图片
				listBitMap = new ArrayList<Bitmap>();
				for (String path : mSelectPath) {
					Log.e("1", "本地路径" + path);
					// 压缩到1/20大小
					Bitmap bitmap = createThumbnail(path, 20);
					// 把bitmap放进去list中
					listBitMap.add(bitmap);
				}
				// 清空所有数据
				jiaListBitMap.clear();
				bindJia();
				// 把加号和图片放一起
				jiaListBitMap.addAll(0, listBitMap);
				// 当选择9张图片时，删除加号
				if (jiaListBitMap.size() == 10) {
					// 第10张图片下标是9
					jiaListBitMap.remove(9);
				}
				hengping();
				// 把数据绑定一下
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * /** 点击事件
	 * 
	 * 
	 */
	private void myOnclick() {

		noScrollgridview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						adapter.setSelectIndex(position);

						if (position == jiaListBitMap.size() - 1) {
							setImgMode();
						} else {
							if (mSelectPath != null && mSelectPath.size() > 0) {
								Intent intent = new Intent(
										ReleaseActivity.this,
										BasePhotoPreviewActivity.class);
								intent.putExtra(
										BasePhotoPreviewActivity.INTENT_STATE,
										0);
								intent.putExtra(
										MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
										mSelectPath);
								startActivityForResult(intent,
										StatusTool.BOOS_SELECT_PLACE);
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
	}

	// 相册，相机模式设置
	private void setImgMode() {
		Intent intent = new Intent(ReleaseActivity.this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
		// 选择模式，1表示多选,0表示单选
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					BasePhotoPreviewActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	/**
	 * 获取七牛上传TOKEN
	 * 
	 * @author Johnson
	 */
	private void getQiniuToken() {
		RequestParams params = new RequestParams(HttpUtil.QiniuToken);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {
				InterfaceReturn<getQiNiuToken> interfaceReturn = new InterfaceReturn<getQiNiuToken>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						getQiNiuToken.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						QiniuToken = interfaceReturn.getResults().get(0)
								.getToken();
						String Token = interfaceReturn.getResults().get(0)
								.getToken();
						System.out.println(Token);
					}
				}
			};

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;

					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});
	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				ReleaseActivity.this, R.style.progress_dialog, "正在上传信息,请稍候");

		return dialog;
	}

	class getQiNiuToken {

		private String token;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}
