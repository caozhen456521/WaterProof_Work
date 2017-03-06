package com.qingzu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.QuestionsListByPage;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.SelectRewardActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyIssueFragment extends Fragment {
	private View v = null;
	private ZListView mListView = null;
	private String UserToken = null;

	public static final int MY_ISSUE = 7;

	private SharedPreferences preferences = null;
	private LayoutInflater layoutInflater = null;
	private List<QuestionsListByPage> list = new ArrayList<QuestionsListByPage>();
	private MyAdapter<QuestionsListByPage> adapter = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int MY_SPANNED = 22;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFRESH_DATA_FINISH:

				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				mListView.stopRefresh(); // 下拉刷新完成
				if (Count * 10 < PageCount) {
					mListView.setStateNotData(true);
				}
				// else if (PageCount == 0) {
				// mListView.setNoData();
				// }
				break;
			case LOAD_DATA_FINISH:
				if (adapter != null) {
					adapter.notifyDataSetChanged();
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
		v = inflater.inflate(R.layout.fragment_my_issue, container, false);
		initView();
		return v;
	}

	private void initView() {
		preferences = getActivity().getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		layoutInflater = LayoutInflater.from(getActivity());
		mListView = (ZListView) v.findViewById(R.id.m_issue_lv);
		adapter = new MyAdapter<QuestionsListByPage>(getActivity(), list,
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
			public void convert(ViewHolder view, final int position,
					final QuestionsListByPage item) {
				UserPhoto = view.getView(R.id.fi_riv_user_photo);
				Time = view.getView(R.id.fi_tv_time);
				BrowseNumber = view.getView(R.id.fi_tv_browse);
				Comment = view.getView(R.id.fi_tv_comment);
				Content = view.getView(R.id.fi_tv_content);
				Title = view.getView(R.id.fi_tv_title);
				UserName = view.getView(R.id.fi_tv_user_name);
				Item = view.getView(R.id.fi_room);

				ImageLoader.getInstance()
						.displayImage(
								item.getMember().getMemberPhoto(),
								UserPhoto,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
				UserName.setText(item.getMember().getNickName());
				Comment.setText(item.getQuestions().getAnswerCount() + "");
				BrowseNumber.setText(item.getQuestions().getBrowseCount() + "");
				Title.setText(item.getQuestions().getqTitle());
				Content.setText(Html.fromHtml(item.getQuestions().getqRemark())
						.toString().replaceAll("\t", "").replaceAll("\n", ""));
				Time.setText(Tools.getTimestampString(Tools.strToDateT(item
						.getQuestions().getIssueTime())));
				Item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("QuestionsListByPage", item);
						Intent intent = new Intent(getActivity(),
								SelectRewardActivity.class);
						intent.putExtras(bundle);
						intent.putExtra("POSITION", position);
						startActivityForResult(intent, MY_ISSUE);
					}
				});
			}
		};
		mListView.setPullLoadEnable(true);

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				getMessage("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				adapter.notifyDataSetChanged();
			}
		});

		getMessage("1", "10", MainActivity.LOAD_DATA);
		mListView.setAdapter(adapter);
	}

	/**
	 * 加载数据
	 * 
	 * @param PageIndex
	 * @param strWere
	 */
	public void getMessage(String Page, String Size, final int State) {
		RequestParams params = new RequestParams(HttpUtil.MyQuestionsListByPage
				.replace("{Size}", Size).replace("{Page}", Page));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<QuestionsListByPage> interfaceReturn = new InterfaceReturn<QuestionsListByPage>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						QuestionsListByPage.class);
				if (interfaceReturn.isStatus()) {
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						list.add(interfaceReturn.getResults().get(i));
					}
					PageCount = interfaceReturn.getResultsCount();

					// messageAdapter.notifyDataSetChanged();
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
					adapter.notifyDataSetChanged();
				}
			}

			@Override
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

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == MY_ISSUE) {
			Bundle bundle = data.getExtras();
			QuestionsListByPage questionsListByPage = (QuestionsListByPage) bundle
					.get("QuestionsListByPage");
			list.get(data.getIntExtra("POSITION", 0)).setQuestions(
					questionsListByPage.getQuestions());
			adapter.notifyDataSetChanged();
		}
	}

}
