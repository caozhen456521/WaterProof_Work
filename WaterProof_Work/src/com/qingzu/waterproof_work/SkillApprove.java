package com.qingzu.waterproof_work;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.FindWorkInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.WorkInfo;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import me.multi_image_selector.view.BasePhotoPreviewActivity;
import multi_image_selector.MultiImageSelectorActivity;
import com.qingzu.application.StatusTool;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.application.StatusTool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 我有优势
 * @see 曹振
 */

public class SkillApprove extends Activity implements OnClickListener {

	private MyTitleView boss_second_title = null;

	private ShowProgressDialog dialog;
	private LinearLayout professional_skills_rl = null;
	private LinearLayout Insurance_rl = null;
	private TextView professional_skills_tx = null;
	private TextView Insurance_tx = null;
	private ArrayList<String> mSelectPath;
	private LinearLayout workman_rl = null;
	private ImageView workman_im = null;
	private Button workman_bt_commit = null;
	private String UserToken = null;
	private String QiniuToken = null;
	private int number = 2;
    private boolean IsInsurance =false;
	// private RelativeLayout professional_skills_rl = null;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skill_approve);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		boss_second_title = (MyTitleView) findViewById(R.id.boss_second_title);

		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		dialog = getProDialog();
		UserToken = preferences.getString("UserToken", "");
		getQiniuToken();
		MyFindWorkInfo();
		professional_skills_rl = (LinearLayout) findViewById(R.id.professional_skills_rl);
		Insurance_rl = (LinearLayout) findViewById(R.id.Insurance_rl);
		workman_rl = (LinearLayout) findViewById(R.id.workman_rl);
		workman_im = (ImageView) findViewById(R.id.workman_im);
		professional_skills_tx = (TextView) findViewById(R.id.professional_skills_tx);
		Insurance_tx = (TextView) findViewById(R.id.Insurance_tx);
		workman_bt_commit = (Button) findViewById(R.id.workman_bt_commit);

		workman_bt_commit.setOnClickListener(this);
		workman_rl.setOnClickListener(this);
		professional_skills_rl.setOnClickListener(this);
		Insurance_rl.setOnClickListener(this);
		// professional_skills_tx.setOnClickListener(this);

		boss_second_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.professional_skills_rl:
			intent.setClass(this, ProfessionalSkillActivity.class);
			intent.putExtra("conspart", professional_skills_tx.getText()
					.toString().trim());
			startActivityForResult(intent, 6);
			break;

		case R.id.Insurance_rl:
			if (IsInsurance==false) {
				new AlertDialog.Builder(this)
				.setTitle("是否有保险")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] { "是", "否" }, 1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Insurance_tx.setText("是");
									break;
								case 1:
									Insurance_tx.setText("否");
									break;
								default:
									break;
								}

								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();	
			}else{
				new AlertDialog.Builder(this)
				.setTitle("是否有保险")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] { "是", "否" }, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Insurance_tx.setText("是");
									break;
								case 1:
									Insurance_tx.setText("否");
									break;
								default:
									break;
								}

								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();	
			}
			
			break;
		case R.id.workman_rl:
			setImgMode();

			break;
		case R.id.workman_bt_commit:
		//	workman_bt_commit.setEnabled(false);
			dialog.show();
			
			String PhotoName = UUID.randomUUID() + "";
			if ( mSelectPath!=null) {
				uploadImg(mSelectPath.get(0), PhotoName);
				return;
			} else {
//				dialog.show();
//				workman_bt_commit.setEnabled(true);
//				Toast.makeText(SkillApprove.this, "证件照不能为空", Toast.LENGTH_SHORT)
//						.show();
				WorkInfo workInfo = new WorkInfo();
				if (professional_skills_tx.getText()
						.toString().trim() == null
						|| professional_skills_tx.getText()
								.toString().trim()
								.equals("")) {
					Toast.makeText(SkillApprove.this,
							"技能不能为空", Toast.LENGTH_SHORT)
							.show();
					dialog.dismiss();
					return;
				} else {

					workInfo.setSkill(professional_skills_tx
							.getText().toString().trim());
				
					if (Insurance_tx.getText().toString()
							.trim().equals("否")) {
						workInfo.setIsInsurance(0);
					} else
						workInfo.setIsInsurance(1);
				}

				// workInfo.setSkill(professional_skills_tx.getText())

				// frontPhoto =
				// "http://7xoiij.com2.z0.glb.qiniucdn.com/"
				// + frontPhoto;
				// reversePhoto =
				// "http://7xoiij.com2.z0.glb.qiniucdn.com/"
				// + reversePhoto;
				// authentication = new
				// MemberAuthentication();
				// authentication.setIdCard(ama_et_id_number
				// .getText().toString().trim());
				// authentication.setIdCardFront(frontPhoto);
				// authentication.setIdCardBack(reversePhoto);
				Gson gson = new Gson();
				String json = gson.toJson(workInfo);
				UpdateFindWorkInfo(json);
				
			}

			break;
		default:
			break;
		}
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
						System.out.println(QiniuToken);
					}
				}
			};

			public void onError(Throwable ex, boolean isOnCallback) {

			}
		}

		);
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

	// 相册，相机模式设置
	private void setImgMode() {
		Intent intent = new Intent(SkillApprove.this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
		// 选择模式，1表示多选,0表示单选
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 0);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					BasePhotoPreviewActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, StatusTool.BOOS_SELECT_PLACE);
	}

	/**
	 * 图片压缩，如果不压缩会导致图片溢出异常
	 */
	private Bitmap createThumbnail(String filepath, int i) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = i;
		return BitmapFactory.decodeFile(filepath, options);
	}

	
	
	/**
	 * 当前登录用户根获取工人工长详细信息
	 */
	private void MyFindWorkInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.MyFindWorkInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<FindWorkInfo> interfaceReturn = new InterfaceReturn<FindWorkInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfo.class);
				
				
				if (interfaceReturn.isStatus()) {
					IsInsurance=interfaceReturn.getResults().get(0).getIsInsurance();
					if (IsInsurance==false) {
						Insurance_tx.setText("否");
					}else{
						Insurance_tx.setText("是");
					}
					
					professional_skills_tx.setText(interfaceReturn.getResults().get(0).getSkill());
					if (interfaceReturn.getResults().get(0).getCertificateImg()!=null&&interfaceReturn.getResults().get(0).getCertificateImg()!="") {
						  ImageLoaderUtil.loadNoXUtilImage(interfaceReturn.getResults().get(0).getCertificateImg(), workman_im);	
					}
					
					
				  
				} else {
					T.showToast(SkillApprove.this, interfaceReturn.getMessage());
				}
			}

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
	
	/**
	 * 当前登录用户根编辑工人工长详细信息
	 */

	private void UpdateFindWorkInfo(String json) {
		dialog.dismiss();
		RequestParams params = new RequestParams(HttpUtil.UpdateFindWorkInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {

					Toast.makeText(SkillApprove.this,
							interfaceReturn.getMessage(), Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {

				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				// TODO Auto-generated method stub

				// commitStatus = !commitStatus;
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
	
	
	

	@SuppressWarnings("unused")
	private void uploadImg(final String file, final String fileName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, fileName, QiniuToken,
						new UpCompletionHandler() {
							public void complete(String arg0,
									com.qiniu.android.http.ResponseInfo arg1,
									org.json.JSONObject arg2) {

								if (arg1.isOK()) {
									WorkInfo workInfo = new WorkInfo();
									if (professional_skills_tx.getText()
											.toString().trim() == null
											|| professional_skills_tx.getText()
													.toString().trim()
													.equals("")) {
										Toast.makeText(SkillApprove.this,
												"技能不能为空", Toast.LENGTH_SHORT)
												.show();
										dialog.dismiss();
										return;
									} else {

										workInfo.setSkill(professional_skills_tx
												.getText().toString().trim());
										workInfo.setCertificateImg("http://7xoiij.com2.z0.glb.qiniucdn.com/"
												+ arg0);
										if (Insurance_tx.getText().toString()
												.trim().equals("否")) {
											workInfo.setIsInsurance(0);
										} else
											workInfo.setIsInsurance(1);
									}

									// workInfo.setSkill(professional_skills_tx.getText())

									// frontPhoto =
									// "http://7xoiij.com2.z0.glb.qiniucdn.com/"
									// + frontPhoto;
									// reversePhoto =
									// "http://7xoiij.com2.z0.glb.qiniucdn.com/"
									// + reversePhoto;
									// authentication = new
									// MemberAuthentication();
									// authentication.setIdCard(ama_et_id_number
									// .getText().toString().trim());
									// authentication.setIdCardFront(frontPhoto);
									// authentication.setIdCardBack(reversePhoto);
									Gson gson = new Gson();
									String json = gson.toJson(workInfo);
									UpdateFindWorkInfo(json);

								} 
									//dialog.dismiss();

							};
						},

						new UploadOptions(null, null, false,
								new UpProgressHandler() {

									@Override
									public void progress(String key,
											double percent) {

									}
								}, null));

			}
		}).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (resultCode) {
		case 6:
			professional_skills_tx.setText(data.getStringExtra("positionlist"));
			if (professional_skills_tx.getText().toString().trim() != null
					&& !professional_skills_tx.getText().toString().trim()
							.equals("")) {
				professional_skills_tx.setVisibility(View.VISIBLE);
			}
			break;
		case RESULT_OK:
			mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			// 把数据转化成Bitmap,放选中的图片

			for (String path : mSelectPath) {
				Log.e("1", "本地路径" + path);
				// 压缩到1/20大小
				Bitmap bitmap = createThumbnail(path, 10);
				// 把bitmap放进去list中

				workman_im.setImageBitmap(bitmap);
			}

			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private ShowProgressDialog getProDialog() {
		// TODO Auto-generated method stub
		ShowProgressDialog dialog = new ShowProgressDialog(SkillApprove.this,
				R.style.progress_dialog, getString(R.string.Loading));
		return dialog;
	}
}
