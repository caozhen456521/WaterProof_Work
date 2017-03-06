package com.qingzu.waterproof_work;

import java.util.Timer;
import java.util.TimerTask;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import com.qingzu.application.AppManager;
import com.qingzu.entity.Captcha;
import com.qingzu.entity.InterfaceReturn;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 短信验证码登录
 * @author Administrator
 *
 */
public class LoginBySmsCodeActivity extends Activity implements OnClickListener{
	private MyTitleView login_by_code_title=null;
	private EditText login_by_code_phone_et=null;
	private String UserToken = null;
	private ShowProgressDialog dialog;
	private Timer tm;
	private TimerTask tk;
	private int count = 120;
	private Captcha captcha = null;
	private String State;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_by_sms_code);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		captcha = new Captcha();
		SharedPreferences preferences = LoginBySmsCodeActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		dialog = getProDialog(getString(R.string.Loading));
		login_by_code_title=(MyTitleView) findViewById(R.id.login_by_code_title);
		login_by_code_phone_et=(EditText) findViewById(R.id.login_by_code_phone_et);
		
		login_by_code_title.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		if(login_by_code_phone_et.getText().toString().trim().equals("")){
//			tv.setTextColor(this.getResources().getColor(R.color.textColor_black));
//			Button.setEnabled(false);//设置这个属性	q
//			login_by_code_title.setRightText(text)
//		}
		
			login_by_code_title.setOnRightClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if (Tools.isNull(login_by_code_phone_et.getText().toString().trim())) {
						T.showToast(LoginBySmsCodeActivity.this,
								getString(R.string.Input_Phone));
						return;
					} else if (!Tools.isMobileNO(login_by_code_phone_et.getText().toString()
							.trim())) {
						T.showToast(LoginBySmsCodeActivity.this,
								getString(R.string.Phone_Number_Error));
						return;
					} else {
						
						getCheckByCellPhone(login_by_code_phone_et.getText().toString().trim());

//						getSmsAuthCode();

					}
					
				}

				

				
			});
		
		
		
	}
	/**
	 * 按照手机号码检查是否帐号已经被注册
	 * @param cellPhone
	 */
	private void getCheckByCellPhone(String cellPhone) {
		// TODO Auto-generated method stub
		
		RequestParams params = new RequestParams(HttpUtil.CheckByCellPhone.replace("{cellPhone}", cellPhone));
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		Xutils.Get(params, new MyCallBack<String>() {
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						Toast.makeText(LoginBySmsCodeActivity.this,
								"该手机号尚未注册",
								Toast.LENGTH_SHORT).show();
						
						startActivity(new Intent(LoginBySmsCodeActivity.this,RegistActivity.class)); 
					} else {
//						Toast.makeText(MyInformationActivity.this,
//								interfaceReturn.getMessage(),
//								Toast.LENGTH_SHORT).show();
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

						SMSCode(login_by_code_phone_et.getText().toString().trim(), "2",
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
	
	
	private void SMSCode(final String CellPhone, String CateID, String CaptchaID,
			String CaptchaInput, final DialogInterface dialog) {
		// TODO Auto-generated method stub
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
//						startCount();
						Intent intent=new Intent();
						intent.setClass(LoginBySmsCodeActivity.this, FillInCodeActivity.class);
						intent.putExtra("Phone", login_by_code_phone_et.getText().toString().trim());
						startActivity(intent);
//						startActivity(new Intent(LoginBySmsCodeActivity.this,FillInCodeActivity.class));
					} else {
						Toast.makeText(LoginBySmsCodeActivity.this,
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
	private void Captcha(final ImageView lpac_iv_photo, final TextView lpac_tv_hint,
			final EditText lpac_et_cuth_code) {
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
						Toast.makeText(LoginBySmsCodeActivity.this,
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	private ShowProgressDialog getProDialog(String string) {
		// TODO Auto-generated method stub
		ShowProgressDialog dialog = new ShowProgressDialog(
				LoginBySmsCodeActivity.this, R.style.progress_dialog, string);

		return dialog;
	}

	
}
