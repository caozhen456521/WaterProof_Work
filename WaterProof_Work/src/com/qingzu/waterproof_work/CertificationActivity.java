package com.qingzu.waterproof_work;

import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class CertificationActivity extends Activity implements OnClickListener{
	private MyTitleView mtvCertificationTitle=null;//实名认证标题
	private EditText etInputIdcard=null;//请输入身份证号
	private RelativeLayout rlIdcardFront=null;//手持身份证正面照
	private RelativeLayout rlIdcardBack=null;//手持身份证背面照
	private Button btCertificationComplete=null;//完成
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification);
		AppManager.getAppManager().addActivity(this);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		
		mtvCertificationTitle=(MyTitleView) findViewById(R.id.certification_title);
		etInputIdcard=(EditText) findViewById(R.id.input_idcard_et);
		rlIdcardFront=(RelativeLayout) findViewById(R.id.idcard_front_rl);
		rlIdcardBack=(RelativeLayout) findViewById(R.id.idcard_back_rl);
		btCertificationComplete=(Button) findViewById(R.id.certification_complete_bt);
		
		
		btCertificationComplete.setOnClickListener(this);
		
		
		mtvCertificationTitle.setOnLeftClickListener(new OnClickListener() {
			
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
		case R.id.certification_complete_bt:
			
			break;

		default:
			break;
		}
		
	}

	

	
}
