package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VipCenterActivity extends Activity implements OnClickListener {

	public static final int SELECT_PRIVILEGE = 7;
	private MyTitleView mtvVipCenterTitle = null;// 会员中心标题
	private TextView tvPeriodOfValidity = null;// 有效期限
	private RelativeLayout rlTenPrivilege = null;// 十项特权
	private EditText etVipCenterAdvice = null;// 会员反馈建议
	private Button btVipCenterCommit = null;// 提交
	private TextView avc_tv_privilege = null;
	private Integer ServiceId = null;
	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_center);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initview();
	}

	private void initview() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mtvVipCenterTitle = (MyTitleView) findViewById(R.id.vip_center_title);
		tvPeriodOfValidity = (TextView) findViewById(R.id.period_of_validity_tv);
		rlTenPrivilege = (RelativeLayout) findViewById(R.id.ten_privilege_rl);
		etVipCenterAdvice = (EditText) findViewById(R.id.vip_center_advice_et);
		btVipCenterCommit = (Button) findViewById(R.id.vip_center_commit_bt);
		avc_tv_privilege = (TextView) findViewById(R.id.avc_tv_privilege);

		rlTenPrivilege.setOnClickListener(this);
		btVipCenterCommit.setOnClickListener(this);

		mtvVipCenterTitle.setOnLeftClickListener(new OnClickListener() {

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
		switch (v.getId()) {
		case R.id.vip_center_commit_bt:
			if (ServiceId == null || ServiceId == 0) {
				T.showToast(VipCenterActivity.this, "请先去选择要服务的特权");
				return;
			}
			CreateVIPDemandInfo(ServiceId, etVipCenterAdvice.getText()
					.toString().trim());
			break;
		case R.id.ten_privilege_rl:
			startActivityForResult(
					new Intent(this, PrivilegeListActivity.class),
					SELECT_PRIVILEGE);
			break;

		default:
			break;
		}

	}

	/**
	 * 创建付费会员VIP会员服务项需求信息 POST
	 * 
	 * @param serviceId
	 * @param remark
	 * @author Johnson
	 */
	private void CreateVIPDemandInfo(Integer serviceId, String remark) {
		RequestParams params = new RequestParams(HttpUtil.CreateVIPDemandInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("ServiceId", serviceId + "");
		params.addBodyParameter("Remark", remark);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = interfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(VipCenterActivity.this,
							interfaceReturn.getMessage());
					finish();
				} else {
					T.showToast(VipCenterActivity.this,
							interfaceReturn.getMessage());
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
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == SELECT_PRIVILEGE) {
			avc_tv_privilege.setText(data.getStringExtra("SERVICE_NAME"));
			ServiceId = data.getIntExtra("SERVICE_ID", 0);
		}
	}

}
