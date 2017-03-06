package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import com.qingzu.application.AppManager;
import com.qingzu.fragment.order.AllFragment;
import com.qingzu.fragment.order.UnderwayFragment;
import com.qingzu.fragment.order.WaitAffirmFragment;
import com.qingzu.fragment.order.WaitEvaluateFragment;
import com.qingzu.fragment.order.YetAffirmFragment;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.Tools;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WorkOrderActivity extends FragmentActivity implements
		OnClickListener {
	public static final int DATA_FINISH = 7;
	private MyTitleView awo_title = null;
	private ViewPager pager = null;
	private Button awo_bt_all = null;
	private Button awo_bt_wait_affirm = null;
	private Button awo_bt_yet_affirm = null;
	private Button awo_bt_underway = null;
	private Button awo_bt_wait_evaluate = null;

	private OrderViewPageAdapter adapter = null;
	public String UserToken = null;
	public int state;// 0:老板 1:工长 2: 工人
	private int page = 0;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_order);
		AppManager.getAppManager().addActivity(this);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		initView();
		initDate();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		Intent intent = getIntent();
		state = preferences.getInt("identity", 0);
		page = intent.getIntExtra("PAGE", 0);
		awo_title = (MyTitleView) findViewById(R.id.awo_title);
		pager = (ViewPager) findViewById(R.id.awo_viewpage);
		awo_bt_all = (Button) findViewById(R.id.awo_bt_all);
		awo_bt_wait_affirm = (Button) findViewById(R.id.awo_bt_wait_affirm);
		awo_bt_yet_affirm = (Button) findViewById(R.id.awo_bt_yet_affirm);
		awo_bt_underway = (Button) findViewById(R.id.awo_bt_underway);
		awo_bt_wait_evaluate = (Button) findViewById(R.id.awo_bt_wait_evaluate);

		awo_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		awo_bt_all.setOnClickListener(this);
		awo_bt_wait_affirm.setOnClickListener(this);
		awo_bt_yet_affirm.setOnClickListener(this);
		awo_bt_underway.setOnClickListener(this);
		awo_bt_wait_evaluate.setOnClickListener(this);

	}

	private void initDate() {
		// TODO Auto-generated method stub
		// 必须继承FragmentActivity才能用getSupportFragmentManager()；
		adapter = new OrderViewPageAdapter(getSupportFragmentManager());
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

		setCurrentPage(page);// 默认选中样式
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.awo_bt_all:
			setCurrentPage(0);
			break;
		case R.id.awo_bt_wait_affirm:
			setCurrentPage(1);
			break;
		case R.id.awo_bt_yet_affirm:
			setCurrentPage(2);
			break;
		case R.id.awo_bt_underway:
			setCurrentPage(3);
			break;
		case R.id.awo_bt_wait_evaluate:
			setCurrentPage(4);
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
	@SuppressLint("ResourceAsColor")
	private void setCurrentPage(int arg0) {
		resetBtn();
		switch (arg0) {
		case 0:
			awo_bt_all.setBackgroundResource(R.drawable.edit_bg_gray);
			awo_bt_all.setTextColor(this.getResources().getColor(
					R.color.order_orange));
			pager.setCurrentItem(0);// 默认选中
			break;
		case 1:
			awo_bt_wait_affirm.setBackgroundResource(R.drawable.edit_bg_gray);
			awo_bt_wait_affirm.setTextColor(this.getResources().getColor(
					R.color.order_orange));
			pager.setCurrentItem(1);
			break;
		case 2:
			awo_bt_yet_affirm.setBackgroundResource(R.drawable.edit_bg_gray);
			awo_bt_yet_affirm.setTextColor(this.getResources().getColor(
					R.color.order_orange));
			pager.setCurrentItem(2);
			break;
		case 3:
			awo_bt_underway.setBackgroundResource(R.drawable.edit_bg_gray);
			awo_bt_underway.setTextColor(this.getResources().getColor(
					R.color.order_orange));
			pager.setCurrentItem(3);
			break;
		case 4:
			awo_bt_wait_evaluate.setBackgroundResource(R.drawable.edit_bg_gray);
			awo_bt_wait_evaluate.setTextColor(this.getResources().getColor(
					R.color.order_orange));
			pager.setCurrentItem(4);
			break;
		default:
			break;
		}
	}

	@SuppressLint("ResourceAsColor")
	private void resetBtn() {
		awo_bt_all.setBackgroundResource(R.drawable.edit_bg_ngray);
		awo_bt_all.setTextColor(this.getResources().getColor(
				R.color.my_darkgray));
		awo_bt_wait_affirm.setBackgroundResource(R.drawable.edit_bg_ngray);
		awo_bt_wait_affirm.setTextColor(this.getResources().getColor(
				R.color.my_darkgray));
		awo_bt_yet_affirm.setBackgroundResource(R.drawable.edit_bg_ngray);
		awo_bt_yet_affirm.setTextColor(this.getResources().getColor(
				R.color.my_darkgray));
		awo_bt_underway.setBackgroundResource(R.drawable.edit_bg_ngray);
		awo_bt_underway.setTextColor(this.getResources().getColor(
				R.color.my_darkgray));
		awo_bt_wait_evaluate.setBackgroundResource(R.drawable.edit_bg_ngray);
		awo_bt_wait_evaluate.setTextColor(this.getResources().getColor(
				R.color.my_darkgray));
	}

	class OrderViewPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> list_fragments;

		public OrderViewPageAdapter(FragmentManager fm) {
			super(fm);
			list_fragments = new ArrayList<Fragment>();
			list_fragments.add(new AllFragment());
			list_fragments.add(new WaitAffirmFragment());
			list_fragments.add(new YetAffirmFragment());
			list_fragments.add(new UnderwayFragment());
			list_fragments.add(new WaitEvaluateFragment());
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list_fragments.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_fragments != null ? list_fragments.size() : 0;
		}
	}
}
