package com.qingzu.waterproof_work;

import com.qingzu.application.AppManager;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 支付失败
 * @author Administrator
 *
 */
public class PayDefeatActivity extends Activity implements OnClickListener{
private Button btDefeat=null;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_defeat);
		Tools.setNavigationBarColor(this, R.color.title_background_blue);
		AppManager.getAppManager().addActivity(this);
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		btDefeat=(Button) findViewById(R.id.pay_defeat_completed_bt);
		btDefeat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
}
