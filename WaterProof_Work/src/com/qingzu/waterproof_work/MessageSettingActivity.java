package com.qingzu.waterproof_work;

import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 消息设置
 * @author Administrator
 *
 */
public class MessageSettingActivity extends Activity {
	
	private MyTitleView mtvMessageSettingTitle=null;//消息设置
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_setting);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mtvMessageSettingTitle=(MyTitleView) findViewById(R.id.message_setting_title);
	}

	
}
