package com.qingzu.waterproof_work;

//import android.support.v7.app.ActionBarActivity;
import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * 消息盒子
 * @author Administrator
 *
 */
public class MessageBoxActivity extends Activity implements OnClickListener{
	private MyTitleView mtvMessageBoxTitle=null;//消息盒子
	private ZListView lvMessageBox = null;// 消息盒子列表
	private String UserToken = null;
	@SuppressLint("ResourceAsColor") protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_box);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = MessageBoxActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mtvMessageBoxTitle=(MyTitleView) findViewById(R.id.message_box_title);
		lvMessageBox=(ZListView) findViewById(R.id.lv_message_box);
		
		mtvMessageBoxTitle.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mtvMessageBoxTitle.setOnRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MessageBoxActivity.this,MessageSettingActivity.class));
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
