package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import com.qingzu.adapter.ProjectDetailPagerAdapter;
import com.qingzu.adapter.TeamMemberDetailPagerAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.fragment.TeamMemberDetailPhotoShowFragment;
import com.qingzu.fragment.TeamMemberDetailbasciInfoFragment;
import com.qingzu.uitest.view.MyTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TeamMemberDetailActivity extends FragmentActivity implements OnClickListener {
	private TeamMemberDetailPagerAdapter adapter;

	private ViewPager pager = null;
	private MyTitleView mtvTeamMemberDetailTitle = null;// 队员详情标题
	private Button btBasicInfo = null;// 基本信息
	private Button btPhotoShow = null;// 晒工程
	private List<Fragment> list_fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_member_detail);
		AppManager.getAppManager().addActivity(this);
		initView();// 实例化控件
		initDate();// 初始化内容
	}

	

	private void initView() {
		// TODO Auto-generated method stub
		pager=(ViewPager) findViewById(R.id.team_member_detail_vp_content);
		mtvTeamMemberDetailTitle=(MyTitleView) findViewById(R.id.team_member_detail_title);
		btBasicInfo=(Button) findViewById(R.id.team_member_detail_bascio_info_bt);
		btPhotoShow=(Button) findViewById(R.id.team_member_detail_photo_show_bt);
		
		btBasicInfo.setOnClickListener(this);
		btPhotoShow.setOnClickListener(this);
		
		mtvTeamMemberDetailTitle.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	private void initDate() {
		// TODO Auto-generated method stub
		
		 list_fragments = new ArrayList<Fragment>();
		 list_fragments.add(new TeamMemberDetailbasciInfoFragment());
		 list_fragments.add(new TeamMemberDetailPhotoShowFragment());
		
		// 必须继承FragmentActivity才能用getSupportFragmentManager()；
		adapter = new TeamMemberDetailPagerAdapter(getSupportFragmentManager(),list_fragments);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.team_member_detail_bascio_info_bt:
			setCurrentPage(0);
			break;
		case R.id.team_member_detail_photo_show_bt:
			setCurrentPage(1);
			break;

		default:
			break;
		}
		
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
