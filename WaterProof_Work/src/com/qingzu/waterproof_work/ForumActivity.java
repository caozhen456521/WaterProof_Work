package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;

//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.Tools.ConnectionDetector;
import com.qingzu.adapter.RecruitDetailPagerAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ChannelItem;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.Questions;
import com.qingzu.entity.QuestionsClass;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.entity.RecruitModel;
import com.qingzu.fragment.ForumFragment;
import com.qingzu.fragment.MyIssueFragment;
import com.qingzu.fragment.RecruitDetailBasciInfoFragment;
import com.qingzu.fragment.RecruitDetailPhotoShowFragment;
import com.qingzu.uitest.view.ColumnHorizontalScrollView;
import com.qingzu.uitest.view.CustomListView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.uitest.view.CustomListView.OnLoadListener;
import com.qingzu.uitest.view.CustomListView.OnRefreshListener;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 论坛
 * 
 * @author Johnson
 * 
 */
public class ForumActivity extends FragmentActivity implements OnClickListener {

	private ImageView ivForumBack = null;
	private TextView tvForumFind = null;
	private Button btForumIssue = null;

	private ForumAdapter adapter = null;

	private Button forum_bt_info = null;
	private Button forum_bt_my_issue = null;
	private ViewPager pager = null;
	private Integer viewPageS = 0;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum);
		Tools.setNavigationBarColor(this, R.color.title_background_blue);
		AppManager.getAppManager().addActivity(this);
		initView();
		initDate();
	}

	private void initView() {
		Intent intent = getIntent();
		viewPageS = intent.getIntExtra("VIEWPAGE_S", 0);
		ivForumBack = (ImageView) findViewById(R.id.forum_back_img);
		tvForumFind = (TextView) findViewById(R.id.forum_find);
		btForumIssue = (Button) findViewById(R.id.forum_issue);
		forum_bt_info = (Button) findViewById(R.id.forum_bt_info);
		forum_bt_my_issue = (Button) findViewById(R.id.forum_bt_my_issue);
		pager = (ViewPager) findViewById(R.id.forum_vp_content);
		forum_bt_info.setOnClickListener(this);
		forum_bt_my_issue.setOnClickListener(this);
		ivForumBack.setOnClickListener(this);
		tvForumFind.setOnClickListener(this);
		btForumIssue.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.forum_bt_info:
			setCurrentPage(0);
			break;
		case R.id.forum_bt_my_issue:
			setCurrentPage(1);
			break;
		case R.id.forum_back_img:
			finish();
			break;
		case R.id.forum_find:
			intent.setClass(ForumActivity.this, ForumSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.forum_issue:
			// 提问
			intent.setClass(ForumActivity.this, ReleaseIssueActivity.class);
			intent.putExtra("FROM_ACTIVITY", "FORUM");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void initDate() {
		// TODO Auto-generated method stub
		// 必须继承FragmentActivity才能用getSupportFragmentManager()；
		adapter = new ForumAdapter(getSupportFragmentManager());
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

		setCurrentPage(viewPageS);// 默认选中样式
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
			forum_bt_info.setBackgroundResource(R.drawable.edit_bg_gray);
			pager.setCurrentItem(0);// 默认选中
			break;
		case 1:
			forum_bt_my_issue.setBackgroundResource(R.drawable.edit_bg_gray);
			pager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	private void resetBtn() {
		forum_bt_info.setBackgroundResource(R.drawable.edit_bg_ngray);
		forum_bt_my_issue.setBackgroundResource(R.drawable.edit_bg_ngray);
	}

	class ForumAdapter extends FragmentPagerAdapter {
		private List<Fragment> list_fragments;

		public ForumAdapter(FragmentManager fm) {
			super(fm);
			list_fragments = new ArrayList<Fragment>();
			list_fragments.add(new ForumFragment());
			list_fragments.add(new MyIssueFragment());

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
