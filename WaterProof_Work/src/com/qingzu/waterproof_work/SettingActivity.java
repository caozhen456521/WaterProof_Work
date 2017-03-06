package com.qingzu.waterproof_work;

import java.io.File;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.qingzu.application.AppManager;
import com.qingzu.Tools.DataCleanManager;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.FileUtils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {
	private MyTitleView mtvSettingTitle = null;// 设置标题
	private RelativeLayout rlSettingUpdates = null;// 检查更新
	private RelativeLayout rlSettingAdviceFeedback = null;// 意见反馈
//	private RelativeLayout rlSettingChangeId = null;// 切换身份
	private RelativeLayout rlSettingChangePassword = null;// 重置密码
	private RelativeLayout rlSettingClearCache = null;// 清除缓存
	private RelativeLayout rlSettingAboutEzu = null;// 关于e族
	private Button btSettingLoginOut = null;// 退出登录
	private TextView setting_clear_cache_tx = null; // 缓存大小
	private ShowProgressDialog dialog = null;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				try {
					dialog.dismiss();
					setting_clear_cache_tx.setText(DataCleanManager
							.getCachesSize(new File(FileUtils.SDPATH)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mtvSettingTitle = (MyTitleView) findViewById(R.id.setting_title);
		rlSettingUpdates = (RelativeLayout) findViewById(R.id.setting_check_for_updates_rl);
		rlSettingAdviceFeedback = (RelativeLayout) findViewById(R.id.setting_advice_feedback_rl);
//		rlSettingChangeId = (RelativeLayout) findViewById(R.id.setting_change_id_rl);
		rlSettingChangePassword = (RelativeLayout) findViewById(R.id.setting_change_password_rl);
		rlSettingClearCache = (RelativeLayout) findViewById(R.id.setting_clear_cache_rl);
		rlSettingAboutEzu = (RelativeLayout) findViewById(R.id.setting_about_ezu_rl);
		btSettingLoginOut = (Button) findViewById(R.id.setting_login_out_bt);
		setting_clear_cache_tx = (TextView) findViewById(R.id.setting_clear_cache_tx);
		dialog = getProDialog();

		rlSettingUpdates.setOnClickListener(this);
		rlSettingAdviceFeedback.setOnClickListener(this);
//		rlSettingChangeId.setOnClickListener(this);
		rlSettingChangePassword.setOnClickListener(this);
		rlSettingClearCache.setOnClickListener(this);
		rlSettingAboutEzu.setOnClickListener(this);
		btSettingLoginOut.setOnClickListener(this);
		rlSettingUpdates.setOnClickListener(this);

		try {
			setting_clear_cache_tx.setText(DataCleanManager
					.getCachesSize(new File(FileUtils.SDPATH)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mtvSettingTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}

	public void cleanFile() throws InterruptedException {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DataCleanManager.cleanApplicationData(SettingActivity.this,
						FileUtils.SDPATH);
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		}).start();

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(
				SettingActivity.this, R.style.progress_dialog,
				getString(R.string.Cleanile));

		return dialog;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_check_for_updates_rl:
			BDAutoUpdateSDK.cpUpdateCheck(getApplicationContext(),
					new MyCPCheckUpdateCallback());

			break;
		case R.id.setting_advice_feedback_rl:
			startActivity(new Intent(SettingActivity.this, HelpActivity.class));

			break;
//		case R.id.setting_change_id_rl:
//			startActivity(new Intent(SettingActivity.this,
//					SelectRoleActivity.class));
//			break;
		case R.id.setting_change_password_rl:
			startActivity(new Intent(SettingActivity.this,
					ResetPasswordActivity.class).putExtra("State", "Setting"));
			break;

		case R.id.setting_clear_cache_rl:
			dialog.show();
			try {
				cleanFile();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.setting_about_ezu_rl:
			startActivity(new Intent(SettingActivity.this, AboutActivity.class));

			break;
		case R.id.setting_login_out_bt:
			HintDialog.Builder builder = new HintDialog.Builder(this);
			builder.setTitle(R.string.Hint);
			builder.setMessage(R.string.Wheather_Exit_Login);
			builder.setPositiveButton(R.string.Yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							SharedPreferences preferences = getSharedPreferences("LoginFrist", MODE_PRIVATE);
							Editor edit = preferences.edit();
							edit.remove("isFristLogin");
							edit.commit();
							SharedPreferences settings = getSharedPreferences(
									"UserToken", MODE_PRIVATE);
							SharedPreferences.Editor editor = settings.edit();
							editor.remove("UserToken");
							editor.remove("MemberId");
							editor.remove("tokenExpiredTimeStamp");
							editor.remove("Phone");
							editor.remove("UserName");
							editor.remove("UserPhoto");
							editor.remove("IsCheck");
							editor.remove("IsRealName");
							editor.remove("NickName");
							editor.remove("IntegralNumber");
							editor.commit();
							startActivity(new Intent(SettingActivity.this,
									LoginActivity.class));
							dialog.dismiss();
							finish();
						}
					});
			builder.setNegativeButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			
			break;

		default:
			break;
		}

	}

	private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

		@Override
		public void onCheckUpdateCallback(AppUpdateInfo info,
				AppUpdateInfoForInstall infoForInstall) {
			if (info != null) {
				BDAutoUpdateSDK.uiUpdateAction(getApplicationContext(),
						new UICheckUpdateCallback() {

							@Override
							public void onCheckComplete() {

							}
						});

			} else {
				T.showToast(getApplicationContext(), "已是最新版本");
			}

		}

	}

}
