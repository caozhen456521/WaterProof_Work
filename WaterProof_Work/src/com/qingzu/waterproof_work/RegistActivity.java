package com.qingzu.waterproof_work;

import java.util.Timer;
import java.util.TimerTask;

import org.xutils.x;
import org.xutils.common.util.DensityUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.qingzu.application.AppManager;
import com.qingzu.entity.Captcha;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener {

	private MyTitleView regist_title = null;// 注册标题

	private Timer tm;
	private TimerTask tk;
	private int count = 120;

	private EditText regist_et_phone_number = null;
	private EditText regist_et_auth_code = null;
	private Button regist_bt_get_auth_code = null;
	private EditText regist_et_pass_word = null;
	private Button regist_bt_commit = null;

	private Captcha captcha = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		captcha = new Captcha();
		regist_title = (MyTitleView) findViewById(R.id.regist_title);
		regist_et_phone_number = (EditText) findViewById(R.id.regist_et_phone_number);
		regist_et_auth_code = (EditText) findViewById(R.id.regist_et_auth_code);
		regist_bt_get_auth_code = (Button) findViewById(R.id.regist_bt_get_auth_code);
		regist_et_pass_word = (EditText) findViewById(R.id.regist_et_pass_word);
		regist_bt_commit = (Button) findViewById(R.id.regist_bt_commit);

		regist_bt_get_auth_code.setOnClickListener(this);
		regist_bt_commit.setOnClickListener(this);
		regist_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_bt_get_auth_code:
			if (regist_et_phone_number.getText().toString().trim() == null
					|| regist_et_phone_number.getText().toString().trim()
							.equals("")) {
				Toast.makeText(RegistActivity.this, "手机号码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!Tools.isMobileNO(regist_et_phone_number.getText()
					.toString().trim())) {
				Toast.makeText(RegistActivity.this, "手机号码输入有误",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				CheckByCellPhone(regist_et_phone_number.getText().toString()
						.trim());
			}
			break;
		case R.id.regist_bt_commit:
			if (regist_et_phone_number.getText().toString().trim() == null
					|| regist_et_phone_number.getText().toString().trim()
							.equals("")) {
				Toast.makeText(RegistActivity.this, "手机号码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!Tools.isMobileNO(regist_et_phone_number.getText()
					.toString().trim())) {
				Toast.makeText(RegistActivity.this, "手机号码输入有误",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (regist_et_auth_code.getText().toString().trim() == null
					|| regist_et_auth_code.getText().toString().trim()
							.equals("")) {
				Toast.makeText(RegistActivity.this, "验证码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (regist_et_pass_word.getText().toString().trim() == null
					|| regist_et_pass_word.getText().toString().trim()
							.equals("")) {
				Toast.makeText(RegistActivity.this, "密码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (18 < regist_et_pass_word.getText().toString().trim()
					.length()
					&& regist_et_pass_word.getText().toString().trim().length() < 6) {
				Toast.makeText(RegistActivity.this, "密码最短6位,最长18位",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				RegisterByPwd(regist_et_phone_number.getText().toString()
						.trim(), regist_et_auth_code.getText().toString()
						.trim(), regist_et_pass_word.getText().toString()
						.trim());
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 注册 - 用户名/密码/短信验证码
	 * 
	 * @param UserName
	 * @param ValidationCode
	 * @param UserPwd
	 */
	private void RegisterByPwd(final String UserName, String ValidationCode,
			String UserPwd) {
		RequestParams params = new RequestParams(HttpUtil.RegisterByPwd);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		params.addBodyParameter("UserName", UserName);
		params.addBodyParameter("ValidationCode", ValidationCode);
		params.addBodyParameter("UserPwd", UserPwd);
		params.addBodyParameter("RecommendCode", "");
		params.addBodyParameter("ConfirmUserPwd", UserPwd);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {
							startActivity(new Intent(RegistActivity.this,
									MainActivity.class));
							SharedPreferences sharedPreferences = getSharedPreferences(
									"UserToken", Activity.MODE_PRIVATE);
							Editor edit = sharedPreferences.edit();
							edit.putString("UserToken", interfaceReturn
									.getResults().get(0).getToken());
							edit.putLong("MemberId", interfaceReturn
									.getResults().get(0).getMember().getId());
							edit.putLong("tokenExpiredTimeStamp",
									interfaceReturn.getResults().get(0)
											.getTokenExpiredTimeStamp());
							edit.putString("Phone", interfaceReturn
									.getResults().get(0).getMember()
									.getContactTel());
							edit.putString("UserPhoto", interfaceReturn
									.getResults().get(0).getMember()
									.getMemberPhoto());
							edit.putString("NickName", interfaceReturn
									.getResults().get(0).getMember()
									.getNickName());
					
							JMessageClient.register(UserName, "112233", new BasicCallback() {
								
								@Override
								public void gotResult(int arg0, String arg1) {
									// TODO Auto-generated method stub
								if (arg0==0||arg0==898001) {
									System.out.println(arg1);
								T.showToast(RegistActivity.this, "注册成功");
							
								}	
								}
							});
							
							
							edit.commit();
						}
						Toast.makeText(RegistActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(RegistActivity.this,
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
	 * 按照手机号码检查是否帐号已经被注册
	 * 
	 * @param mobile
	 */
	private void CheckByCellPhone(String mobile) {
		RequestParams params = new RequestParams(
				HttpUtil.CheckByCellPhone.replace("{cellPhone}", mobile));
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		Xutils.Get(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (!interfaceReturn.isStatus()) {
						Toast.makeText(RegistActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						getSmsAuthCode();
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
	 * 获取短信验证码Dialog
	 */
	private void getSmsAuthCode() {
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
						SMSCode(regist_et_phone_number.getText().toString()
								.trim(), "1", captcha.getId() + "",
								lpac_et_cuth_code.getText().toString().trim(),
								dialog);
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();
	}

	/**
	 * 获取手机短信验证码,Status==true,表示发送成功
	 * 
	 * @param CellPhone
	 * @param CateID
	 * @param CaptchaID
	 * @param CaptchaInput
	 * @param dialog
	 */
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
						Toast.makeText(RegistActivity.this,
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
	 * 获取图片验证码
	 * 
	 * @param lpac_iv_photo
	 * @param lpac_tv_hint
	 * @param lpac_et_cuth_code
	 */
	private void Captcha(final ImageView lpac_iv_photo,
			final TextView lpac_tv_hint, final EditText lpac_et_cuth_code) {
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
							} else if (captcha.getTypeID() == 2) {
								lpac_et_cuth_code
										.setInputType(InputType.TYPE_CLASS_NUMBER);
							}

						}
					} else {
						Toast.makeText(RegistActivity.this,
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
				regist_bt_get_auth_code.setEnabled(false);

				regist_bt_get_auth_code.setBackgroundResource(R.color.yz_an);
				regist_bt_get_auth_code.setText(msg.arg1 + "秒");
				break;
			case 11:
				regist_bt_get_auth_code.setEnabled(true);
				regist_bt_get_auth_code.setBackgroundResource(R.color.white);
				regist_bt_get_auth_code.setText("再次获取验证码");
				tk.cancel();
				break;
			}
		};
	};

}
