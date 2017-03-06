package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.fragment.CaptainSecondFragment;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.SystemBarTintManager;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowStrengthActivity extends Activity implements OnClickListener {

	private MyTitleView ass_title = null;
	private EditText ass_et_strength_show = null;
	private Button ass_bt_commit = null;
	private ConstructionTeam constructionTeam = null;

	private String UserToken = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		setContentView(R.layout.activity_show_strength);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = ShowStrengthActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		constructionTeam = MainActivity.constructionTeam;
		ass_title = (MyTitleView) findViewById(R.id.ass_title);
		ass_et_strength_show = (EditText) findViewById(R.id.ass_et_strength_show);
		ass_bt_commit = (Button) findViewById(R.id.ass_bt_commit);

		if (constructionTeam != null
				&& constructionTeam.getTeamStrength().length() > 0) {
			ass_et_strength_show.setText(constructionTeam.getTeamStrength());
		}

		ass_bt_commit.setOnClickListener(this);
		ass_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ass_bt_commit:
			commit();
			break;

		default:
			break;
		}
	}

	private void commit() {
		if (ass_et_strength_show.getText().toString().trim().length() > 0) {
			constructionTeam.setTeamStrength(ass_et_strength_show.getText()
					.toString().trim());
			UpdateConstructionTeam(InterfaceReturn.getGson().toJson(
					constructionTeam));
		} else {
			T.showToast(ShowStrengthActivity.this, "实力描述不能为空");
		}
	}

	/**
	 * 当前登录用户编辑工程施工队信息
	 * 
	 * @param strength
	 * @author Johnson
	 */
	private void UpdateConstructionTeam(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.UpdateConstructionTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionTeam> interfaceReturn = new InterfaceReturn<ConstructionTeam>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeam.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ShowStrengthActivity.this,
								interfaceReturn.getMessage());
						MainActivity.constructionTeam = interfaceReturn
								.getResults().get(0);
						setResult(CaptainSecondFragment.SHOW_STRENGTH);
						finish();
					} else {
						T.showToast(ShowStrengthActivity.this,
								interfaceReturn.getMessage());
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

}
