package com.qingzu.waterproof_work;

import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.application.AppManager;
import com.qingzu.callback.OnLocationListener;
import com.qingzu.callback.OnPushStateListener;
import com.qingzu.fragment.LeftFragment;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.Tools;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class FifthMyMessageActivity extends FragmentActivity {
	private ConnectionDetector cd;
	private SlidingMenu sm = null;
	private ActionBar actionBar;
	boolean isfirst = true;
	boolean isOpened = false;
	private Fragment mContent = null;
	private MyTitleView mtvTitle = null;
	private OnPushStateListener onPushStateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fifth_my_message);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initSlidingMenu(savedInstanceState);
		// initActionBar("");
		sm.setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				System.out.println("wwwwww");
				isOpened = true;
			}
		});
		sm.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				isOpened = false;
			}
		});

	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		// if (mContent == null) {
		// mContent = new TodayFragment();
		// }

		// 设置左侧滑动菜单
		// setBehindContentView(R.layout.menu_frame_left);

		// 实例化滑动菜单对象
		sm = new SlidingMenu(this);
		// 设置可以左右滑动的菜单

		sm.setMode(SlidingMenu.LEFT);
		sm.setMenu(R.layout.menu_frame_left);
		// 设置滑动阴影的宽度
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		sm.setShadowWidth(15);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		// sm.setBehindOffset(this.getWindowManager().getDefaultDisplay()
		// .getWidth() / 7 * 4);
		sm.setBehindOffset(0);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

		sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT); // 使SlidingMenu附加在Activity上
	}

	@SuppressLint("NewApi")
	private void initActionBar(String title) {
		actionBar = super.getActionBar();
		actionBar.show();
		// actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setHomeAsUpIndicator(R.drawable.selector_title_back);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_title, null);
		mtvTitle = (MyTitleView) view.findViewById(R.id.actionbar_title);
		// TextView tvTitle = new TextView(this);
		// tvTitle.setText(title);
		// tvTitle.setTextColor(Color.WHITE);
		// tvTitle.setTextSize(18);
		// tvTitle.setGravity(Gravity.CENTER);
		// LayoutParams params = new
		// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		// tvTitle.setLayoutParams(params);
		mtvTitle.setText(title);
		if (isOpened == false) {
			mtvTitle.setText("消息盒子");
		}
		mtvTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isOpened == true) {

					finish();
				} else if (isOpened == false) {
					mtvTitle.setText("消息盒子");
					sm.toggle();

				}
			}
		});

		actionBar.setCustomView(view);
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);

	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		initActionBar(title);
		if (isfirst == true) {
			sm.showMenu();
			isfirst = false;

		} else {
			sm.showContent();
		}

		// topTextView.setText(title);
		// onPushStateListener.OnPushState("3");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// if (isOpened == true) {
			// sm.toggle();
			//
			// } else if (isOpened == false) {
			//
			// finish();
			//
			// }
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// 捕获返回键的方法1
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 按下BACK，同时没有重复
			// Log.d(TAG, "onKeyDown()");
			if (isOpened == true) {

				finish();
			} else if (isOpened == false) {
				mtvTitle.setText("消息盒子");
				sm.toggle();

			}
		}

		return super.onKeyDown(keyCode, event);
	}

}
