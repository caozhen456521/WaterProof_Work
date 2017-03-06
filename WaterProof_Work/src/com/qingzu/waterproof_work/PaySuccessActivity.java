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
 * 支付成功
 * @author Administrator
 *
 */
public class PaySuccessActivity extends Activity implements OnClickListener {
	private Button btSuccess=null;

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		Tools.setNavigationBarColor(this, R.color.title_background_blue);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		btSuccess=(Button) findViewById(R.id.pay_success_completed_bt);
		btSuccess.setOnClickListener(new OnClickListener() {
			
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
