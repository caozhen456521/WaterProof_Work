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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 选择角色
 * 
 * @author Johnson
 * 
 */
public class SelectRoleActivity extends Activity implements OnClickListener {
	private MyTitleView mtvSelectTitle = null;// 角色选择
	private LinearLayout llRoleBoss = null;// 老板ll
	private LinearLayout llRolePm = null;// 工长ll
	private LinearLayout llRoleWorker = null;// 工人ll
	private String UserToken = null;

	public static final String IDENTITY = "UserToken";

	public static final int BOOS_ID = 0;
	public static final int CAPTAIN_ID = 1;
	public static final int WORKER_ID = 2;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_role);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mtvSelectTitle = (MyTitleView) findViewById(R.id.select_role_title);
		llRoleBoss = (LinearLayout) findViewById(R.id.role_boss_ll);
		llRolePm = (LinearLayout) findViewById(R.id.role_pm_ll);
		llRoleWorker = (LinearLayout) findViewById(R.id.role_worker_ll);

		llRoleBoss.setOnClickListener(this);
		llRolePm.setOnClickListener(this);
		llRoleWorker.setOnClickListener(this);

		mtvSelectTitle.setOnLeftClickListener(new OnClickListener() {

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

		SharedPreferences preferences = getSharedPreferences(IDENTITY,
				Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();

		switch (v.getId()) {
		case R.id.role_boss_ll:
			editor.putInt("identity", BOOS_ID);
			UpdateUserRole(2 + "");
			break;
		case R.id.role_pm_ll:
			editor.putInt("identity", CAPTAIN_ID);
			UpdateUserRole(6 + "");
			break;
		case R.id.role_worker_ll:
			editor.putInt("identity", WORKER_ID);
			UpdateUserRole(1 + "");
			break;

		default:
			break;
		}
		editor.commit();
	}

	/**
	 * 当前登陆用户更新角色信息 PUT
	 * 
	 * @param defaultRoleId
	 * @author Johnson
	 */
	private void UpdateUserRole(String defaultRoleId) {
		RequestParams params = new RequestParams(HttpUtil.UpdateUserRole);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("defaultRoleId", defaultRoleId);
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("static-access")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = interfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					startActivity(new Intent(SelectRoleActivity.this,
							MainActivity.class));
				} else {
					T.showToast(SelectRoleActivity.this,
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
				} else { // 其他错误
					// ...
				}
			}
		});
	}

}
