package com.qingzu.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	// 界面列表
	private ArrayList<View> views = null;

	public ViewPagerAdapter(ArrayList<View> views) {
		this.views = views;
	}

	/**
	 * 获得当前界面数
	 */
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 初始化position位置的界面
	 */
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(views.get(position));
		return views.get(position);

	}

	/**
	 * 判断是否由对象生成界面
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return (arg0 == arg1);
	}

	/**
	 * 销毁position位置的界面
	 */
	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(views.get(position));
	}

}
