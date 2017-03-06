package com.qingzu.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	// �����б�
	private ArrayList<View> views = null;

	public ViewPagerAdapter(ArrayList<View> views) {
		this.views = views;
	}

	/**
	 * ��õ�ǰ������
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
	 * ��ʼ��positionλ�õĽ���
	 */
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(views.get(position));
		return views.get(position);

	}

	/**
	 * �ж��Ƿ��ɶ������ɽ���
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return (arg0 == arg1);
	}

	/**
	 * ����positionλ�õĽ���
	 */
	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(views.get(position));
	}

}
