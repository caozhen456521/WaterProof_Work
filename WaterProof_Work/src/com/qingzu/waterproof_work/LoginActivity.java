package com.qingzu.waterproof_work;

import java.io.File;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.model.UserInfo.Field;
import cn.jpush.im.api.BasicCallback;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private MyTitleView login_title = null;
	private EditText login_et_phone_number = null;
	private EditText login_et_password = null;
	private Button login_bt_forget_password = null;
	private Button login_bt_commit = null;
	private TextView sms_login_tv = null;
	private InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
	public static int integralNumber;
	public boolean isFristLogin = true;
	private Button login_bt_register = null;
	private SharedPreferences preferences = null;
	private ProgressDialog progressDialog;
    private RelativeLayout Login_re;
	@SuppressLint("ResourceAsColor")
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		Login_re=(RelativeLayout) findViewById(R.id.Login_re);
	//	login_title = (MyTitleView) findViewById(R.id.login_title);
		login_et_phone_number = (EditText) findViewById(R.id.login_et_phone_number);
		login_et_password = (EditText) findViewById(R.id.login_et_password);
		login_bt_forget_password = (Button) findViewById(R.id.login_bt_forget_password);
		login_bt_commit = (Button) findViewById(R.id.login_bt_commit);
		login_bt_register = (Button) findViewById(R.id.login_bt_register);
		login_bt_register.getBackground().setAlpha((100));
		sms_login_tv = (TextView) findViewById(R.id.sms_login_tv);
		preferences = getSharedPreferences("LoginFrist", Activity.MODE_PRIVATE);
		isFristLogin = preferences.getBoolean("isFristLogin", true);
		final ScaleAnimation animation =new ScaleAnimation(1f, 1.4f, 1f, 1.4f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
				animation.setDuration(5000);//设置动画持续时间 
				animation.setRepeatCount(0);
				animation.setFillAfter(true);
				Login_re.setAnimation(animation);
				animation.startNow(); 
		
		login_et_password.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					if (login_et_phone_number.getText().toString().trim() == null
							|| login_et_phone_number.getText().toString()
									.trim().equals("")) {
						Toast.makeText(LoginActivity.this, "手机号码不能为空",
								Toast.LENGTH_SHORT).show();

					} else if (!Tools.isMobileNO(login_et_phone_number
							.getText().toString().trim())) {
						Toast.makeText(LoginActivity.this, "手机号码输入有误",
								Toast.LENGTH_SHORT).show();
					} else {
						getCheckByCellPhone(login_et_phone_number.getText()
								.toString().trim());
					}

					break;

				default:
					break;
				}
				return false;
			}
		});

		login_et_password.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					if (login_et_phone_number.getText().toString().trim() == null
							|| login_et_phone_number.getText().toString()
									.trim().equals("")) {
						Toast.makeText(LoginActivity.this, "手机号码不能为空",
								Toast.LENGTH_SHORT).show();

					} else if (!Tools.isMobileNO(login_et_phone_number
							.getText().toString().trim())) {
						Toast.makeText(LoginActivity.this, "手机号码输入有误",
								Toast.LENGTH_SHORT).show();
					} else {
						getCheckByCellPhone(login_et_phone_number.getText()
								.toString().trim());
					}

					break;

				default:
					break;
				}
				return false;
			}
		});

		login_bt_forget_password.setOnClickListener(this);
		login_bt_commit.setOnClickListener(this);
		login_bt_register.setOnClickListener(this);
		sms_login_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						LoginBySmsCodeActivity.class));
			}
		});

//		login_title.setOnLeftClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				AppManager.getAppManager().AppExit(MyApplication.getInstance());
//			}
//		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_bt_forget_password:
			startActivity(new Intent(LoginActivity.this,
					ResetPasswordActivity.class).putExtra("State", "Login"));
			break;
		case R.id.login_bt_commit:

			// if (login_et_phone_number.getText().toString().trim() == null
			// || login_et_phone_number.getText().toString().trim()
			// .equals("")) {
			// Toast.makeText(LoginActivity.this, "手机号码不能为空",
			// Toast.LENGTH_SHORT).show();
			// return;
			// } else if (!Tools.isMobileNO(login_et_phone_number.getText()
			// .toString().trim())) {
			// Toast.makeText(LoginActivity.this, "手机号码输入有误",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }

			if (login_et_password.getText().toString().trim() == null
					|| login_et_password.getText().toString().trim().equals("")) {
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {

				Logon(login_et_phone_number.getText().toString().trim(),
						login_et_password.getText().toString().trim());
			}
			break;
		case R.id.login_bt_register:
			startActivity(new Intent(LoginActivity.this, RegistActivity.class));
			break;

		default:
			break;
		}
	}

	private void getCheckByCellPhone(String cellPhone) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(
				HttpUtil.CheckByCellPhone.replace("{cellPhone}", cellPhone));
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		Xutils.Get(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Toast.makeText(LoginActivity.this, "该手机号尚未注册",
								Toast.LENGTH_SHORT).show();

						startActivity(new Intent(LoginActivity.this,
								RegistActivity.class));
					} else {

					}
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

	public static void registerIM(final String UserName, boolean flag,
			final String NickName, final String memberPhoto,
			final Context context, final SharedPreferences preferences) {

		if (flag == true) {
			JMessageClient.register(UserName, "112233", new BasicCallback() {

				@Override
				public void gotResult(int arg0, String arg1) {
					// TODO Auto-generated method stub
					if (arg0 == 0 || arg0 == 898001) {

						JMessageClient.login(UserName, "112233",
								new BasicCallback() {

									@Override
									public void gotResult(int arg0, String arg1) {
										// TODO Auto-generated method stub
										if (arg0 == 0) {
											UserInfo info = JMessageClient
													.getMyInfo();
											info.setNickname(NickName);
											JMessageClient.updateMyInfo(
													Field.nickname, info,
													new BasicCallback() {

														@Override
														public void gotResult(
																int status,
																String arg1) {
															// TODO
															// Auto-generated
															// method stub
															if (status == 0) {
																// T.showToast(context,
																// "修改用户名成功");
															} else {
																Log.i("LoginController",
																		"status = "
																				+ status);
																HandleResponseCode
																		.onHandle(
																				context,
																				status,
																				false);
															}

														}
													});
											if (memberPhoto != ""
													&& memberPhoto != null) {
												downloadFile(
														memberPhoto,
														FileUtils.SDPATH
																+ "memberPhoto.png",
														context, preferences);
											}

										}
									}
								});

					}
				}
			});
		}

	}

	private static void downloadFile(final String url, String path,
			final Context context, final SharedPreferences preferences) {
		// progressDialog = new ProgressDialog(this);
		RequestParams requestParams = new RequestParams(url);
		requestParams.setSaveFilePath(path);
		x.http().get(requestParams, new Callback.ProgressCallback<File>() {
			@Override
			public void onWaiting() {
			}

			@Override
			public void onStarted() {
			}

			@Override
			public void onLoading(long total, long current,
					boolean isDownloading) {
				// progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				// progressDialog.setMessage("亲，努力下载中。。。");
				// progressDialog.show();
				// progressDialog.setMax((int) total);
				// progressDialog.setProgress((int) current);
			}

			@Override
			public void onSuccess(File result) {
				// Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
				// progressDialog.dismiss();
				JMessageClient.updateUserAvatar(result, new BasicCallback() {

					@Override
					public void gotResult(int status, String arg1) {
						// TODO Auto-generated method stub
						if (status == 0) {
							// Toast.makeText(context, "头像更新成功",
							// Toast.LENGTH_SHORT).show();
							final Editor editor = preferences.edit();
							editor.putBoolean("isFristLogin", false);
							editor.commit();

						} else {
							Log.i("LoginController", "status = " + status);
							HandleResponseCode.onHandle(context, status, false);
						}
					}
				});
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				ex.printStackTrace();
				Toast.makeText(context, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT)
						.show();
				// progressDialog.dismiss();
			}

			@Override
			public void onCancelled(CancelledException cex) {
			}

			@Override
			public void onFinished() {
			}
		});
	}

	/**
	 * 登陆
	 * 
	 * @param UserName
	 * @param UserPwd
	 * @author Johnson
	 */
	private void Logon(final String UserName, final String UserPwd) {
		RequestParams params = new RequestParams(HttpUtil.Logon);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		params.addBodyParameter("UserName", UserName);
		params.addBodyParameter("UserPwd", UserPwd);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {

				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				// String NickName =null;

				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							SharedPreferences sharedPreferences = getSharedPreferences(
									"UserToken", Activity.MODE_PRIVATE);
							final Editor edit = sharedPreferences.edit();
							edit.putString("UserToken", interfaceReturn
									.getResults().get(0).getToken());
							edit.putInt("MemberId", interfaceReturn
									.getResults().get(0).getMember().getId());
							edit.putLong("tokenExpiredTimeStamp",
									interfaceReturn.getResults().get(0)
											.getTokenExpiredTimeStamp());
							edit.putString("Phone", interfaceReturn
									.getResults().get(0).getMember()
									.getContactTel());
							edit.putString("UserName", interfaceReturn
									.getResults().get(0).getMember()
									.getUserName());
							edit.putString("UserPhoto", interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto());
							edit.putInt("IsCheck", interfaceReturn.getResults()
									.get(0).getMember().getIsCheck());
							edit.putBoolean("IsRealName", interfaceReturn
									.getResults().get(0).getMember()
									.isRealName());
							edit.putString("NickName", interfaceReturn
									.getResults().get(0).getMember()
									.getNickName());
							edit.putInt("IntegralNumber", interfaceReturn
									.getResults().get(0).getMember()
									.getIntegralNumber());
							if (interfaceReturn.getResults().get(0).getMember()
									.getDefaultRoleId() == 1) {
								edit.putInt("identity", 2);
							} else if (interfaceReturn.getResults().get(0)
									.getMember().getDefaultRoleId() == 2) {
								edit.putInt("identity", 0);
							} else if (interfaceReturn.getResults().get(0)
									.getMember().getDefaultRoleId() == 6) {
								edit.putInt("identity", 1);
							}
							edit.commit();
							integralNumber = interfaceReturn.getResults()
									.get(0).getMember().getIntegralNumber();

							if (interfaceReturn.getResults().get(0).getMember()
									.getDefaultRoleId() == 0) {
								startActivity(new Intent(LoginActivity.this,
										SelectRoleActivity.class));
							} else {
								startActivity(new Intent(LoginActivity.this,
										MainActivity.class));
							}
							registerIM(UserName, isFristLogin, interfaceReturn
									.getResults().get(0).getMember()
									.getNickName(), interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto(), LoginActivity.this,
									preferences);
						}

						Toast.makeText(LoginActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(LoginActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().AppExit(LoginActivity.this);
			// finishAllActivity()
		}
		return false;
	}

}
