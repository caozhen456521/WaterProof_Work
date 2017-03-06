package com.qingzu.waterproof_work;

import com.qingzu.adapter.WorkerDetailPagerAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WorkerDetailActivity extends FragmentActivity implements OnClickListener {
	private String UserToken = null;
	private WorkerDetailPagerAdapter adapter;
	private ViewPager pager = null;
	private MyTitleView mtvWorkerDetailTitle = null;// 工人详情标题
	private Button btBasicInfo = null;// 基本信息
	private Button btPhotoShow = null;// 工程案例
	public static String id = null;
	public static String infoId = null;
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_detail);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();// 实例化控件
		initDate();// 初始化内容
	}

	private void initView() {
		mtvWorkerDetailTitle = (MyTitleView) findViewById(R.id.find_worker_detail_title);
		btBasicInfo = (Button) findViewById(R.id.find_worker_detail_bascio_info_bt);
		btPhotoShow = (Button) findViewById(R.id.find_worker_detail_part_picture_show_bt);
		pager = (ViewPager) findViewById(R.id.find_worker_detail_vp_content);

		btBasicInfo.setOnClickListener(this);
		btPhotoShow.setOnClickListener(this);
		
		 Intent intent = getIntent();
		 id =  intent.getStringExtra("id");
		 infoId=intent.getStringExtra("InfoId");
		
	
		mtvWorkerDetailTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.find_worker_detail_bascio_info_bt:
			setCurrentPage(0);
			break;
		case R.id.find_worker_detail_part_picture_show_bt:
			setCurrentPage(1);
			break;

		default:
			break;
		}

	}

	private void initDate() {
		// TODO Auto-generated method stub
		// 必须继承FragmentActivity才能用getSupportFragmentManager()；
		adapter = new WorkerDetailPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		// 监听页面变化
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setCurrentPage(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		setCurrentPage(0);// 默认选中样式
	}

	/**
	 * 页面与head标签一致（可以设置head的按钮样式）
	 * 
	 * @param arg0
	 */
	private void setCurrentPage(int arg0) {
		resetBtn();
		switch (arg0) {
		case 0:
			btBasicInfo.setBackgroundResource(R.drawable.edit_bg_gray);
			pager.setCurrentItem(0);// 默认选中
			break;
		case 1:
			btPhotoShow.setBackgroundResource(R.drawable.edit_bg_gray);
			pager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	private void resetBtn() {
		btBasicInfo.setBackgroundResource(R.drawable.edit_bg_ngray);
		btPhotoShow.setBackgroundResource(R.drawable.edit_bg_ngray);
	}
}
