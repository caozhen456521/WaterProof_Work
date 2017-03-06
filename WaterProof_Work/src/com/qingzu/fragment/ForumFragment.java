package com.qingzu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ChannelItem;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.QuestionsClass;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.uitest.view.ColumnHorizontalScrollView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.ShowProgressDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.ForumDetailsActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForumFragment extends Fragment {

	private View v = null;
	private ZListView mListView = null;
	private MyAdapter<QuestionsListByPage> myAdapter = null;
	private ShowProgressDialog dialog = null;
	private String UserToken = null;
	private int classId = 0;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView = null;

	private RelativeLayout rl_column;
	private LinearLayout mRadioGroup_content = null;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	public static List<QuestionsClass> classes = new ArrayList<QuestionsClass>();
	private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	private LayoutInflater layoutInflater = null;
	private List<QuestionsListByPage> list = new ArrayList<QuestionsListByPage>();
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					mListView.setStateNotData(true);
				}
				break;
			case LOAD_DATA_FINISH:
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
				if (Count * 10 >= PageCount) {
					System.out.println("Count==========================="
							+ Count);
					System.out.println("PageCount======================="
							+ PageCount);
					mListView.setStateNotData(false);
				} else {
					mListView.stopLoadMore(); // 加载更多完成
				}
				break;
			default:
				break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_forum, container, false);
		initView();
		return v;
	}

	private void initView() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		dialog = getProDialog();
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");

		layoutInflater = LayoutInflater.from(getActivity());
		mListView = (ZListView) v.findViewById(R.id.forum_lv);
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) v
				.findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) v
				.findViewById(R.id.mRadioGroup_content);
		shade_left = (ImageView) v.findViewById(R.id.shade_left);
		shade_right = (ImageView) v.findViewById(R.id.shade_right);
		rl_column = (RelativeLayout) v.findViewById(R.id.rl_column);
		mListView.setPullLoadEnable(true);

		myAdapter = new MyAdapter<QuestionsListByPage>(getActivity(), list,
				R.layout.forum_item) {

			ImageView UserPhoto;
			TextView UserName;
			TextView BrowseNumber;
			TextView Comment;
			TextView Title;
			TextView Content;
			TextView Time;
			LinearLayout Item;

			@Override
			public void convert(ViewHolder view, int position,
					QuestionsListByPage item) {
				// TODO Auto-generated method stub
				UserPhoto = view.getView(R.id.fi_riv_user_photo);
				UserName = view.getView(R.id.fi_tv_user_name);
				BrowseNumber = view.getView(R.id.fi_tv_browse);
				Comment = view.getView(R.id.fi_tv_comment);
				Title = view.getView(R.id.fi_tv_title);
				Content = view.getView(R.id.fi_tv_content);
				Time = view.getView(R.id.fi_tv_time);
				Item = view.getView(R.id.fi_room);

				final QuestionsListByPage questions = list.get(position);

				ImageLoaderUtil.loadXUtilImage(questions.getMember()
						.getMemberPhoto(), UserPhoto);
				UserName.setText(questions.getMember().getNickName());
				BrowseNumber.setText(questions.getQuestions().getBrowseCount()
						+ "");
				Comment.setText(questions.getQuestions().getAnswerCount() + "");
				Title.setText(questions.getQuestions().getqTitle());
				Content.setText(Html
						.fromHtml(questions.getQuestions().getqRemark())
						.toString().replaceAll("\t", "").replaceAll("\n", ""));
				Time.setText(Tools.getTimestampString(Tools
						.strToDateT(questions.getQuestions().getIssueTime())));

				Item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("QuestionsListByPage", questions);
						Intent intent = new Intent(getActivity(),
								ForumDetailsActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});

			}

		};

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				if (classId == 0) {
					getMessage("1", "10", REFRESH_DATA_FINISH);
				} else {
					QuestionsListByClassId("1", "10", classId + "",
							REFRESH_DATA_FINISH);
				}

				Count = 1;
				// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// TODO 加载更多
				Count = Count + 1;
				CountStr = Count + "";
				if (classId == 0) {
					getMessage(CountStr, "10", LOAD_DATA_FINISH);
				} else {
					QuestionsListByClassId(CountStr, "10", classId + "",
							LOAD_DATA_FINISH);
				}
//				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
//				myAdapter.notifyDataSetChanged();
			}
		});

		if (Tools.isConnectingToInternet()) {
			ClassIdList();
			getMessage("1", "10", MainActivity.LOAD_DATA);
		} else {
			// 无网无数据,提示信息
			T.showToast(getActivity(), getString(R.string.LinkNetwork));
		}

		mListView.setAdapter(myAdapter);
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, null, rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			// TextView localTextView = (TextView)
			// mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(getActivity());
			columnTextView.setTextAppearance(getActivity(),
					R.style.top_category_scroll_view_item_text);
			// localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
			if (columnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							classId = localView.getId();
							if (classId == 0) {
								list.clear();
								getMessage("1", "10", REFRESH_DATA_FINISH);
								Count = 1;
								// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
							} else {
								list.clear();
								QuestionsListByClassId("1", "10", classId + "",
										REFRESH_DATA_FINISH);
								Count = 1;
								// mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
							}
							selectTab(i);
						}
					}
					Toast.makeText(getActivity(),
							userChannelList.get(v.getId()).getName(),
							Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}

	/**
	 * 选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	/**
	 * 获取所有分类列表
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void ClassIdList() {
		RequestParams params = new RequestParams(HttpUtil.ClassIdList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<QuestionsClass> interfaceReturn = new InterfaceReturn<QuestionsClass>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						QuestionsClass.class);
				// PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						classes = interfaceReturn.getResults();
						List<QuestionsClass> class1List = new ArrayList<QuestionsClass>();
						class1List = interfaceReturn.getResults();
						QuestionsClass class1 = new QuestionsClass();
						class1.setId(0);
						class1.setQcTitle("全  部");
						class1List.add(0, class1);
						for (int i = 0; i < class1List.size(); i++) {
							ChannelItem channelItem = new ChannelItem();
							channelItem.setId(class1List.get(i).getId());
							channelItem.setName(class1List.get(i).getQcTitle());
							channelItem.setOrderId(i);
							channelItem.setSelected(1);
							userChannelList.add(channelItem);
						}
						initTabColumn();
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});

	}

	/**
	 * 获取问题列表数据信息
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void getMessage(String Page, String Size, final int State) {

		RequestParams params = new RequestParams(HttpUtil.QuestionsListByPage
				.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<QuestionsListByPage> interfaceReturn = new InterfaceReturn<QuestionsListByPage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						QuestionsListByPage.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));

						}
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
					if (State == REFRESH_DATA_FINISH) {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						// mListView.onShowText();
						// System.out.println(Count);
						// mListView.setStateNotData();
					} else if (State == LOAD_DATA_FINISH) {
						//

						mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
					} else {
						myAdapter.notifyDataSetChanged();
					}
				}
			}

			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});
	}

	/**
	 * 根据ClassId获取问题列表数据信息
	 * 
	 * @param Page
	 * @param Size
	 * @param ClassId
	 */
	public void QuestionsListByClassId(String Page, String Size,
			String ClassId, final int State) {
		RequestParams params = new RequestParams(
				HttpUtil.QuestionsListByClassId.replace("{Page}", Page)
						.replace("{Size}", Size).replace("{ClassId}", ClassId));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<QuestionsListByPage> interfaceReturn = new InterfaceReturn<QuestionsListByPage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						QuestionsListByPage.class);
				// PageCount = interfaceReturn.getTotalCount();
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));

						}
						
						
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
					if (State == REFRESH_DATA_FINISH) {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						// mListView.onShowText();
						// System.out.println(Count);
						// mListView.setStateNotData();
					} else if (State == LOAD_DATA_FINISH) {
						//

						mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
					} else {
						myAdapter.notifyDataSetChanged();
					}
				}
			}

			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
						.show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					System.out.println("responseMsg:" + responseMsg
							+ "=====errorResult:" + errorResult
							+ "=====responseCode" + responseCode);
					// ...
				} else { // 其他错误
					// ...
				}
			}
		});

	}

	private ShowProgressDialog getProDialog() {
		ShowProgressDialog dialog = new ShowProgressDialog(getActivity(),
				R.style.progress_dialog, getString(R.string.Loading));

		return dialog;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		classes.clear();
	}
}
