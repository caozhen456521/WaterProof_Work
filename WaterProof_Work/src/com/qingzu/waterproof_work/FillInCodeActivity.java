package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 填写验证码
 * 
 * @author Administrator
 * 
 */
public class FillInCodeActivity extends Activity implements OnClickListener {
	private MyTitleView fill_in_code_title = null;
	private TextView fill_in_phone_tv = null;
	private EditText fill_in_code_et = null;
	private Button fill_in_bt_next = null;
	private String UserToken = null;
	private String State;
	private String phone;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_in_code);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = FillInCodeActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		fill_in_code_title = (MyTitleView) findViewById(R.id.fill_in_code_title);
		fill_in_phone_tv = (TextView) findViewById(R.id.fill_in_phone_tv);
		fill_in_code_et = (EditText) findViewById(R.id.fill_in_code_et);
		fill_in_bt_next = (Button) findViewById(R.id.fill_in_bt_next);
		Intent intent = getIntent();
		State = intent.getExtras().getString("State");
		phone = intent.getStringExtra("Phone");

		fill_in_phone_tv.setText(phone);
		fill_in_bt_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Tools.isNull(fill_in_code_et.getText().toString()
						.trim())) {
					T.showToast(FillInCodeActivity.this,
							getString(R.string.Input_Verification_Code));
					return;
				} else if (!Tools.isCode(fill_in_code_et.getText()
						.toString().trim())) {
					T.showToast(FillInCodeActivity.this,
							getString(R.string.Verification_Code_Format_Error));
					return;
				} else {
					if (Tools.isConnectingToInternet()) {
						LogonBySMS(phone, fill_in_code_et
								.getText().toString().trim());
					} else {
						// 无网无数据,提示信息
						T.showToast(FillInCodeActivity.this,
								getString(R.string.LinkNetwork));
					}
				}
			}

		});

	}

	private void LogonBySMS(String UserName, String ValidationCode) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.LogonBySMS);
		params.addHeader("EZFSToken", Tools.getEZFSToken(""));
		params.addBodyParameter("UserName", UserName);
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
						if (interfaceReturn.getResults().get(0).getMember()
								.getDefaultRoleId() == 0) {
							startActivity(new Intent(FillInCodeActivity.this,
									SelectRoleActivity.class));
							return;
						}
						startActivity(new Intent(FillInCodeActivity.this,
								MainActivity.class));

						T.showToast(FillInCodeActivity.this,
								interfaceReturn.getMessage());
					} else {
						T.showToast(FillInCodeActivity.this,
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
