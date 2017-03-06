package com.qingzu.waterproof_work;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.multi_image_selector.view.BasePhotoPreviewActivity;
import multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qingzu.application.AppManager;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.MemberAuthentication;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MemberAttestationActivity extends Activity implements
		OnClickListener {

	protected static final int REQUEST_CAPTURE = 0; // 拍照requestCode
	protected static final int REQUEST_SELECT = 1; // 选照片requestCode
	protected static final int REQUEST_CROP = 3;// 压缩requestCode
	protected Uri mImageCaptureUri;// 图片uri路径
	private File file = null;
	private String photoState = null;
	private String frontPhoto = null;
	private String reversePhoto = null;
	private File frontPhotoFile = null;
	private File reversePhotoFile = null;
	private int number = 2;
	private ArrayList<String> mSelectPath;
	private MyTitleView ama_title = null;
	private EditText ama_et_id_number = null;
	private ImageView ama_iv_front = null;
	private ImageView ama_iv_reverse = null;
	private String UserToken = null;
	private String QiniuToken = null;
	private ShowProgressDialog dialog;
	private MemberAuthentication authentication;
	private boolean commitStatus = true;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_attestation);
		Tools.setNavigationBarColor(this, R.color.title_background_blue);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = MemberAttestationActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog();
		ama_title = (MyTitleView) findViewById(R.id.ama_title);
		ama_et_id_number = (EditText) findViewById(R.id.ama_et_id_number);
		ama_iv_front = (ImageView) findViewById(R.id.ama_iv_front);
		ama_iv_reverse = (ImageView) findViewById(R.id.ama_iv_reverse);

		ama_iv_front.setOnClickListener(this);
		ama_iv_reverse.setOnClickListener(this);
		ama_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ama_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				commit();
			}
		});

		getQiniuToken();
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

	private void commit() {
		if (commitStatus) {
			if (ama_et_id_number.getText().toString().trim().length() <= 0) {
				T.showToast(MemberAttestationActivity.this, "请输入身份证号码");
				return;
			} else if (!Tools.isIdCard(ama_et_id_number.getText().toString()
					.trim())) {
				T.showToast(MemberAttestationActivity.this, "身份证号码有误");
				return;
			} else if (frontPhoto == null || frontPhoto.length() <= 0) {
				T.showToast(MemberAttestationActivity.this, "请选择或拍摄手持身份证正面照片");
				return;
			} else if (reversePhoto == null || reversePhoto.length() <= 0) {
				T.showToast(MemberAttestationActivity.this, "请选择或拍摄手持身份证反面照片");
				return;
			} else {
				dialog.show();
				uploadImg(frontPhotoFile, frontPhoto);
				uploadImg(reversePhotoFile, reversePhoto);
				commitStatus = !commitStatus;
			}

		} else {
			T.showToast(MemberAttestationActivity.this, "正在上传请耐心等待");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ama_iv_front:
			photoState = "front";
			setImgMode();
			break;
		case R.id.ama_iv_reverse:
			photoState = "reverse";
			setImgMode();
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

	/**
	 * 
	 * @ClassName: PopupWindows
	 * @Description: 弹出拍照选择PopupWindow
	 * @author lmw
	 * @date 2015年3月30日 上午9:30:41
	 */
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {
			super(mContext);
			View view = View.inflate(mContext, R.layout.layout_popupwindows,
					null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPhoto();
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	/**
	 * 从相册选择图片
	 * 
	 * @param view
	 */
	protected void selectPhoto() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, REQUEST_SELECT);
	}

	/**
	 * 照相
	 * 
	 * @param view
	 */
	protected void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_contact_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		try {
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CAPTURE);
		} catch (ActivityNotFoundException e) {
		}
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	@SuppressWarnings("unused")
	private void uploadImg(final File file, final String fileName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, fileName, QiniuToken,
						new UpCompletionHandler() {

							@Override
							public void complete(String arg0,
									com.qiniu.android.http.ResponseInfo arg1,
									JSONObject arg2) {
								// TODO Auto-generated method stub
								if (arg1.isOK()) {

									number = number - 1;
									if (number == 0) {
										frontPhoto = "http://7xoiij.com2.z0.glb.qiniucdn.com/"
												+ frontPhoto;
										reversePhoto = "http://7xoiij.com2.z0.glb.qiniucdn.com/"
												+ reversePhoto;
										authentication = new MemberAuthentication();
										authentication
												.setIdCard(ama_et_id_number
														.getText().toString()
														.trim());
										authentication
												.setIdCardFront(frontPhoto);
										authentication
												.setIdCardBack(reversePhoto);
										Gson gson = new Gson();
										String json = gson
												.toJson(authentication);
										CreateMemberAuthentication(json);
										number = 2;
									}
								}
							}

						}, new UploadOptions(null, null, false,
								new UpProgressHandler() {

									@Override
									public void progress(String key,
											double percent) {

									}
								}, null));
			
			}
		}).start();

	}

	/**
	 * 当前登录用户提交会员实名认证信息
	 * 
	 * @author Johnson
	 * @date 2016-1-19
	 * @param json
	 */
	public void CreateMemberAuthentication(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.CreateMemberAuthentication);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				InterfaceReturn<MemberAuthentication> interfaceReturn = new InterfaceReturn<MemberAuthentication>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberAuthentication.class);
				if (interfaceReturn != null) {
					commitStatus = !commitStatus;
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							SharedPreferences sharedPreferences = getSharedPreferences(
									"UserToken", Activity.MODE_PRIVATE);
							Editor edit = sharedPreferences.edit();
							edit.putLong("IsCheck", 1);
							edit.commit();
						}
						Toast.makeText(MemberAttestationActivity.this,
								"提交认证成功.请耐心等待", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(MemberAttestationActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				dialog.dismiss();
				commitStatus = !commitStatus;
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

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
//				String mAlbumPicturePath = null;
				Bitmap mLogPhoto = null;
//				String fileName = null;
				switch (resultCode) {
//				case REQUEST_CAPTURE:
//					mAlbumPicturePath = getPath(MemberAttestationActivity.this,
//							mImageCaptureUri);
//
//					mLogPhoto = Tools.comp(BitmapFactory
//							.decodeFile(mAlbumPicturePath));
//
//					fileName = String.valueOf(System.currentTimeMillis());
//					file = FileUtils.saveBitmap(mLogPhoto, fileName);
//					// uploadImg(file);
//
//					if (photoState != null && photoState.equals("front")) {
//						ama_iv_front.setImageBitmap(mLogPhoto);
//						frontPhoto = UUID.randomUUID() + "";
//						frontPhotoFile = file;
//					} else if (photoState != null
//							&& photoState.equals("reverse")) {
//						ama_iv_reverse.setImageBitmap(mLogPhoto);
//						reversePhoto = UUID.randomUUID() + "";
//						reversePhotoFile = file;
//					}
//					photoState = null;
//
//					// dialog.show();
//					break;
//
//				case REQUEST_SELECT:
//					mAlbumPicturePath = getPath(MemberAttestationActivity.this,
//							data.getData());
//					mLogPhoto = Tools.comp(BitmapFactory
//							.decodeFile(mAlbumPicturePath));
//
//					fileName = String.valueOf(System.currentTimeMillis());
//					file = FileUtils.saveBitmap(mLogPhoto, fileName);
//					if (photoState != null && photoState.equals("front")) {
//						
//						ama_iv_front.setImageBitmap(mLogPhoto);
//						frontPhoto = UUID.randomUUID() + "";
//						frontPhotoFile = file;
//					} else if (photoState != null
//							&& photoState.equals("reverse")) {
//						ama_iv_reverse.setImageBitmap(mLogPhoto);
//						reversePhoto = UUID.randomUUID() + "";
//						reversePhotoFile = file;
//					}
//					photoState = null;
//					// dialog.show();
//					break;
					
				case RESULT_OK:
					mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
					
					if (photoState != null && photoState.equals("front")) {
						mLogPhoto = createThumbnail(mSelectPath.get(0), 10);
						ama_iv_front.setImageBitmap(mLogPhoto);
						frontPhoto = UUID.randomUUID() + "";
						frontPhotoFile = new File(mSelectPath.get(0));
					} else if (photoState != null
							&& photoState.equals("reverse")) {
						mLogPhoto = createThumbnail(mSelectPath.get(0), 10);
						ama_iv_reverse.setImageBitmap(mLogPhoto);
						reversePhoto = UUID.randomUUID() + "";
						reversePhotoFile = new File(mSelectPath.get(0));
					}
					photoState = null;
				}
			
		
	}
	private Bitmap createThumbnail(String filepath, int i) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = i;
		return BitmapFactory.decodeFile(filepath, options);
	}
	private void setImgMode() {
		Intent intent = new Intent(MemberAttestationActivity.this,
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
		startActivityForResult(intent, StatusTool.MEMBER_ATTESTATION);
	}
	
	private ShowProgressDialog getProDialog() {
		// TODO Auto-generated method stub
		ShowProgressDialog dialog = new ShowProgressDialog(
				MemberAttestationActivity.this, R.style.progress_dialog,
				getString(R.string.Loading));
		return dialog;
	}

}
