package com.qingzu.waterproof_work;

import com.qingzu.adapter.ConstructionDetailPagerAdapter;
import com.qingzu.adapter.RecruitDetailPagerAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.uitest.view.MyTitleView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConstructionDetailActivity extends FragmentActivity implements
		OnClickListener {
	private String UserToken = null;
	private ConstructionDetailPagerAdapter adapter;

	private ViewPager pager = null;
	private MyTitleView mtvRecruitDetailTitle = null;// 招工详情标题
	private Button btBasicInfo = null;// 基本信息
	private Button btPhotoShow = null;// 图片展示
	public static String id = null;
	public static String show ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_construction_detail);
		AppManager.getAppManager().addActivity(this);
		initView();
		initDate();
	}

	private void initView() {
		mtvRecruitDetailTitle = (MyTitleView) findViewById(R.id.recruit_detail_title);
		btBasicInfo = (Button) findViewById(R.id.recruit_detail_bascio_info_bt);
		btPhotoShow = (Button) findViewById(R.id.recruit_detail_part_picture_show_bt);
		pager = (ViewPager) findViewById(R.id.recruit_detail_vp_content);

		btBasicInfo.setOnClickListener(this);
		btPhotoShow.setOnClickListener(this);

		Intent intent = getIntent();
		id = intent.getStringExtra("recruit");
		show = intent.getStringExtra("show");
		System.out.println(id);

		mtvRecruitDetailTitle.setOnLeftClickListener(new OnClickListener() {

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
		case R.id.recruit_detail_bascio_info_bt:
			setCurrentPage(0);
			break;
		case R.id.recruit_detail_part_picture_show_bt:
			setCurrentPage(1);
			break;

		default:
			break;
		}

	}

	private void initDate() {
		// TODO Auto-generated method stub
		// 必须继承FragmentActivity才能用getSupportFragmentManager()；
		adapter = new ConstructionDetailPagerAdapter(
				getSupportFragmentManager());
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
