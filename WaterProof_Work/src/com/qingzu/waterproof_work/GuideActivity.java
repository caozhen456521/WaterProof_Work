package com.qingzu.waterproof_work;

import java.util.ArrayList;

import com.qingzu.adapter.ViewPagerAdapter;
import com.qingzu.application.AppManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	// 定义ViewPager对象
	private ViewPager viewPager = null;

	// 定义ViewPager适配器
	private ViewPagerAdapter vpAdapter = null;

	// 定义一个ArrayList来存放View
	private ArrayList<View> views = null;

	// 定义各个界面View对象

	private View view1 = null;
	private View view2 = null;
	private View view3 = null;
	// 定义开始按钮对象
	private Button startBt = null;
	// 底部小点的图片
	private ImageView[] points = null;

	// 记录当前选中位置
	private int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guode);
		AppManager.getAppManager().addActivity(this);
		//PublicWay.activityList.add(this);
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化各个界面的布局对象
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);

		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		// 实例化ArrayList对象
		views = new ArrayList<View>();

		// 实例化ViewPager适配器
		vpAdapter = new ViewPagerAdapter(views);

		// 实例化开始按钮
		startBt = (Button) view3.findViewById(R.id.startBtn);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 将要分页显示的View装入数组中
		views.add(view1);
		views.add(view2);
		views.add(view3);

		// 设置监听
		viewPager.setOnPageChangeListener(this);
		// 设置适配器数据
		viewPager.setAdapter(vpAdapter);
		// 给开始按钮设置监听
		startBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startbutton();
			}
		});
		// 初始化底部小点
		initPoint();
	}

	/**
	 * 初始化底部小点
	 */
	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

		points = new ImageView[3];

		// 循环取得小点图片
		for (int i = 0; i < 3; i++) {
			// 得到一个LinearLayout下面的每一个子元素
			points[i] = (ImageView) linearLayout.getChildAt(i);
			// 默认都设为灰色
			points[i].setEnabled(true);
			// 给每个小点设置监听
			points[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			points[i].setTag(i);
		}

		// 设置当面默认的位置
		currentIndex = 0;
		// 设置为白色，即选中状态
		points[currentIndex].setEnabled(false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		// 设置底部小点选中状态
		setCurDot(position);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);

	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= 3) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	/**
	 * 设置当前的小点的位置
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > 3 - 1 || currentIndex == positon) {
			return;
		}
		points[positon].setEnabled(false);
		points[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	/**
	 * 相应按钮点击事件
	 */

	private void startbutton() {
		SharedPreferences sp = GuideActivity.this.getSharedPreferences(
				"qwertyu", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("qwertyu", "F");
		editor.commit();
//		SharedPreferences preferences = getSharedPreferences("city",
//				Activity.MODE_PRIVATE);
//		if (!preferences.getString("city", "").equals("")) {
			Intent intent = new Intent();
			intent.setClass(GuideActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();	
//		} else {
//			T.showToast(GuideActivity.this, "兄台这手速 在下佩服");
//		}
	
	}
}
