package com.qingzu.waterproof_work;
import com.qingzu.application.AppManager;
import java.util.Timer;
import java.util.TimerTask;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import com.qingzu.entity.Captcha;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 重置密码
 * 
 * @author Administrator
 * 
 */
public class ResetPasswordActivity extends Activity {
	
	private SharedPreferences sp=null;
	private ProgressDialog progressDialog;
	private MyTitleView mtvGetAuthCodeTitle = null;
	private EditText etUserPhone = null; // 手机号
	private EditText etAuthCode = null; // 验证码
	private Button btGetAuthCode = null; // 获取验证码
	private EditText etResetPassword = null;// 输入新密码
	private EditText etRepeatPassword = null;// 请再次输入密码
	private Button btComplete = null;// 完成
	private String UserToken = null;
	private ShowProgressDialog dialog;
	private Timer tm;
	private TimerTask tk;
	private int count = 120;
	private Captcha captcha = null;
	private String State;
     private boolean isFristLogin;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		captcha = new Captcha();
		SharedPreferences preferences = ResetPasswordActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		sp = getSharedPreferences(
				"LoginFrist", Activity.MODE_PRIVATE);
		isFristLogin=preferences.getBoolean("isFristLogin", true);
		
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog(getString(R.string.Loading));
		mtvGetAuthCodeTitle = (MyTitleView) findViewById(R.id.reset_password_title);
		etUserPhone = (EditText) findViewById(R.id.repassw_user_phone_et);
		etAuthCode = (EditText) findViewById(R.id.repassw_auth_code_et);
		btGetAuthCode = (Button) findViewById(R.id.repassw_get_auth_code_bt);
		etResetPassword = (EditText) findViewById(R.id.reset_password_et);
		etRepeatPassword = (EditText) findViewById(R.id.repeat_password_et);
		btComplete = (Button) findViewById(R.id.repassw_complete_bt);
		Intent intent = getIntent();
		State= intent.getExtras().getString("State");

		btGetAuthCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Tools.isNull(etUserPhone.getText().toString().trim())) {
					T.showToast(ResetPasswordActivity.this,
							getString(R.string.Input_Phone));
					return;
				} else if (!Tools.isMobileNO(etUserPhone.getText().toString()
						.trim())) {
					T.showToast(ResetPasswordActivity.this,
							getString(R.string.Phone_Number_Error));
					return;
				} else {

					getSmsAuthCode();

				}

			}

		});

btComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Tools.isNull(etAuthCode.getText().toString().trim())) {
					T.showToast(ResetPasswordActivity.this,
							getString(R.string.Input_Verification_Code));
					return;
				} else if (!Tools
						.isCode(etAuthCode.getText().toString().trim())) {
					T.showToast(ResetPasswordActivity.this,
							getString(R.string.Verification_Code_Format_Error));
					return;
				}
				else if (!etResetPassword.getText().toString().trim()
						.equals(etRepeatPassword.getText().toString().trim())) {
					T.showToast(ResetPasswordActivity.this,
							getString(R.string.Password_Not_Same));
				} else {
					if (Tools.isConnectingToInternet()) {
						UserResetPwdBySMSModel(etUserPhone.getText().toString()
								.trim(), etResetPassword.getText().toString()
								.trim(), etRepeatPassword.getText().toString()
								.trim(), etAuthCode.getText().toString().trim());
					} else {
						// 无网无数据,提示信息
						T.showToast(ResetPasswordActivity.this,
								getString(R.string.LinkNetwork));
					}
				}

			}
			
		});


		mtvGetAuthCodeTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void getSmsAuthCode() {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_photo_auth_code,
				null);
		final EditText lpac_et_cuth_code = (EditText) layout
				.findViewById(R.id.lpac_et_cuth_code);
		final ImageView lpac_iv_photo = (ImageView) layout
				.findViewById(R.id.lpac_iv_photo);
		final TextView lpac_tv_hint = (TextView) layout
				.findViewById(R.id.lpac_tv_hint);
		Captcha(lpac_iv_photo, lpac_tv_hint, lpac_et_cuth_code);
		lpac_iv_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Captcha(lpac_iv_photo, lpac_tv_hint, lpac_et_cuth_code);
			}
		});
		new CustomDialog.Builder(layout.getContext())
				.setTitle("请输入图形验证码")
				.setContentView(layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						SMSCode(etUserPhone.getText().toString().trim(), "3",
								captcha.getId() + "", lpac_et_cuth_code
										.getText().toString().trim(), dialog);
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();

	}

	private void SMSCode(String CellPhone, String CateID, String CaptchaID,
			String CaptchaInput, final DialogInterface dialog) {
		RequestParams params = new RequestParams(HttpUtil.SMSCode);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		params.addBodyParameter("CellPhone", CellPhone);
		params.addBodyParameter("CateID", CateID);
		params.addBodyParameter("CaptchaID", CaptchaID);
		params.addBodyParameter("CaptchaInput", CaptchaInput);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						dialog.dismiss();
						startCount();
					} else {
						Toast.makeText(ResetPasswordActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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

	/**
	 * 获取图形验证码
	 * 
	 * @param lpac_iv_photo
	 * @param lpac_tv_hint
	 * @param lpac_et_cuth_code
	 */
	private void Captcha(final ImageView lpac_iv_photo,
			final TextView lpac_tv_hint, final EditText lpac_et_cuth_code) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.Captcha);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<Captcha> interfaceReturn = new InterfaceReturn<Captcha>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						Captcha.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							captcha = interfaceReturn.getResults().get(0);
							ImageOptions imageOptions = new ImageOptions.Builder()
									.setLoadingDrawableId(R.drawable.icon_stub)
									// 加载中默认显示图片
									.setUseMemCache(true)
									// 设置使用缓存
									.setFailureDrawableId(R.drawable.icon_error)// 加载失败后默认显示图片
									.build();
							x.image().bind(lpac_iv_photo, captcha.getImgURL(),
									imageOptions);
							lpac_tv_hint.setText(captcha.getUserMsg());
							if (captcha.getTypeID() == 1) {
								lpac_et_cuth_code
										.setInputType(InputType.TYPE_CLASS_TEXT);
							} else if((captcha.getTypeID() == 2)) {
								lpac_et_cuth_code
										.setInputType(InputType.TYPE_CLASS_NUMBER);
							}

						}
					} else {
						Toast.makeText(ResetPasswordActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
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

	/**
	 * 用户通过短信验证码 重置密码并重新登录
	 * 
	 * @param UserName
	 * @param NewUserPwd
	 * @param ConfirmUserPwd
	 * @param ValidationCode
	 */
	protected void UserResetPwdBySMSModel(final String UserName, String NewUserPwd,
			String ConfirmUserPwd, String ValidationCode) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.UserResetPwdBySMSModel);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		params.addBodyParameter("UserName", UserName);
		params.addBodyParameter("NewUserPwd", NewUserPwd);
		params.addBodyParameter("ConfirmUserPwd", ConfirmUserPwd);
		params.addBodyParameter("ValidationCode", ValidationCode);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {
				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						SharedPreferences sharedPreferences = getSharedPreferences(
								"UserToken", Activity.MODE_PRIVATE);
						Editor edit = sharedPreferences.edit();
						edit.putString("UserToken", interfaceReturn
								.getResults().get(0).getToken());
						edit.putInt("MemberId", interfaceReturn.getResults()
								.get(0).getMember().getId());
						edit.putLong("tokenExpiredTimeStamp", interfaceReturn
								.getResults().get(0).getTokenExpiredTimeStamp());
						edit.putString("Phone", interfaceReturn.getResults()
								.get(0).getMember().getContactTel());
						edit.putString("UserName", interfaceReturn.getResults()
								.get(0).getMember().getUserName());
						edit.putString("UserPhoto", interfaceReturn
								.getResults().get(0).getMember()
								.getMemberPhoto());
						edit.putInt("IsCheck", interfaceReturn.getResults()
								.get(0).getMember().getIsCheck());
						edit.putBoolean("IsRealName", interfaceReturn
								.getResults().get(0).getMember().isRealName());
						edit.putString("NickName", interfaceReturn.getResults()
								.get(0).getMember().getNickName());
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
						if (State.equals("Login")) {
							if (interfaceReturn.getResults().get(0).getMember()
									.getDefaultRoleId() == 0) {
								startActivity(new Intent(
										ResetPasswordActivity.this,
										SelectRoleActivity.class));

							} else
								startActivity(new Intent(
										ResetPasswordActivity.this,
										MainActivity.class));
							
							
							LoginActivity.registerIM(UserName,isFristLogin, interfaceReturn
									.getResults().get(0).getMember()
									.getNickName(),  interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto(), ResetPasswordActivity.this, sp);
						}else{
							finish();
						}
					
						T.showToast(ResetPasswordActivity.this,
								interfaceReturn.getMessage());
					} else {
						T.showToast(ResetPasswordActivity.this,
								interfaceReturn.getMessage());
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

	private void startCount() {
		tm = new Timer();
		tk = new TimerTask() {

			@Override
			public void run() {
				Message msg = mHandler.obtainMessage();
				count--;
				if (count > 0) {
					msg.arg1 = count;
					msg.what = 0;
					msg.obj = count;
					mHandler.sendMessage(msg);
				} else {
					// 时间读完 初始秒数
					mHandler.sendEmptyMessage(11);
					count = 30;
				}

			}
		};
		tm.schedule(tk, 0, 1000);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				System.out.println();
				btGetAuthCode.setEnabled(false);

				btGetAuthCode.setBackgroundResource(R.color.yz_an);
				btGetAuthCode.setText(msg.arg1 + "秒");
				break;
			case 11:
				btGetAuthCode.setEnabled(true);
				btGetAuthCode.setBackgroundResource(R.color.white);
				btGetAuthCode
						.setText(getString(R.string.Again_Get_Verification_Code));
				tk.cancel();
				break;
			}
		};
	};

	

	
	
	private ShowProgressDialog getProDialog(String string) {
		// TODO Auto-generated method stub
		ShowProgressDialog dialog = new ShowProgressDialog(
				ResetPasswordActivity.this, R.style.progress_dialog, string);

		return dialog;
	}

}
