package com.example.imagedemo;

import java.util.ArrayList;

import com.qingzu.entity.CreateFindWorkPhotoInfo;
import com.qingzu.waterproof_work.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private ImagePagerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		Intent intent =getIntent();
		@SuppressWarnings("unchecked")
		ArrayList<CreateFindWorkPhotoInfo> RemarkList = (ArrayList<CreateFindWorkPhotoInfo>) intent.getSerializableExtra("image_urls");
		

		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), RemarkList);
		mAdapter.notifyDataSetChanged();
		mPager.setAdapter(mAdapter);

		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<CreateFindWorkPhotoInfo> RemarkList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<CreateFindWorkPhotoInfo> RemarkList) {
			super(fm);
			this.RemarkList = RemarkList;
		}

		@Override
		public int getCount() {
			return RemarkList == null ? 0 : RemarkList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = RemarkList.get(position).getPhotoUrl();
			return ImageDetailFragment.newInstance(url);
		}

	}
}
