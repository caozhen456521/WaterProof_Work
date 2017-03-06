package com.qingzu.waterproof_work;

import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.uitest.view.MyTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class WorkerListActivity extends Activity implements OnClickListener{
	
private 	MyTitleView mtvWorkerListTitle=null;//工人列表标题
private     ZListView  zlvWorkerList=null;//已派工人列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_list);
		AppManager.getAppManager().addActivity(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		mtvWorkerListTitle=(MyTitleView) findViewById(R.id.worker_list_title);
		zlvWorkerList=(ZListView) findViewById(R.id.worker_list_zlistview);
		
		mtvWorkerListTitle.setOnLeftClickListener(new OnClickListener() {
			
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
		
	}

	
}
