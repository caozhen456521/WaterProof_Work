package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListConstructionTeam;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyConstructionTeamActivity extends Activity implements
		OnClickListener {
	private MyTitleView mtvMyConstructionTeamTitle = null;// 我的施工队标题
	private ImageView join_construction_team_img = null;// 加入施工队
	private ImageView deal_invite_img = null;// 邀请处理
	private ZListView mListView = null;// 我的施工队列表
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private InterfaceReturn<ListConstructionTeam> interfaceReturn = new InterfaceReturn<ListConstructionTeam>();
	private MyAdapter<ListConstructionTeam> myAdapter = null;
	private List<ListConstructionTeam> list = new ArrayList<ListConstructionTeam>();

	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private String UserToken = null;
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
				// else if (PageCount == 0) {
				// mListView.setNoData();
				// }
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

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_construction_team);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mtvMyConstructionTeamTitle = (MyTitleView) findViewById(R.id.my_construction_team_title);
		join_construction_team_img = (ImageView) findViewById(R.id.join_construction_team_img);
		deal_invite_img = (ImageView) findViewById(R.id.deal_invite_img);
		mListView = (ZListView) findViewById(R.id.construction_team_list_zlv);
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mListView.setPullLoadEnable(true);

		myAdapter = new MyAdapter<ListConstructionTeam>(
				MyConstructionTeamActivity.this, list,
				R.layout.layout_join_construction_team_item) {
			RoundImageView team_img = null;
			TextView TeamName = null;
			ImageView vip_img = null;
			RatingBar assess_ratingbar = null;
			Button apply_bt = null;
			TextView points_tv = null;
			TextView jcomplete_number_tv = null; // 接单次数
			TextView worker_number_tv = null;// 用工数
			RelativeLayout rlTeam = null;

			@Override
			public void convert(ViewHolder view, int position,
					final ListConstructionTeam item) {
				// TODO Auto-generated method stub
				team_img = view.getView(R.id.riv_join_construction_team_img);
				TeamName = view.getView(R.id.join_construction_team_name);
				vip_img = view.getView(R.id.join_construction_team_vip_img);
				assess_ratingbar = view
						.getView(R.id.join_construction_team_assess_ratingbar);
				apply_bt = view.getView(R.id.join_construction_team_apply_bt);
				points_tv = view.getView(R.id.join_construction_team_points_tv);
				jcomplete_number_tv = view
						.getView(R.id.join_construction_team_complete_number_tv);
				worker_number_tv = view
						.getView(R.id.join_construction_team_worker_number_tv);

				rlTeam = view.getView(R.id.join_construction_team_rl_room);
				apply_bt.setVisibility(View.GONE);

				ImageLoader
						.getInstance()
						.displayImage(
								list.get(position).getMember().getMemberPhoto(),
								team_img,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

				if (list.get(position).getMember().getIsPayingMembers() == 0) {
					vip_img.setVisibility(View.GONE);
				} else if (list.get(position).getMember().getIsPayingMembers() == 1) {
					vip_img.setVisibility(View.VISIBLE);
				}
				TeamName.setText(list.get(position).getConstructionTeam()
						.getTeamName());
				assess_ratingbar.setRating((float) Double.parseDouble(Tools
						.formatString(list.get(position).getConstructionTeam()
								.getCreditStars())));
				points_tv.setText(Tools.formatString(list.get(position)
						.getConstructionTeam().getCreditStars()
						+ "分"));
				jcomplete_number_tv.setText(Tools.formatString(list
						.get(position).getConstructionTeam().getDealNumber()));
				worker_number_tv.setText(Tools.formatString(list.get(position)
						.getConstructionTeam().getAlreadyNumber()));

				rlTeam.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(
								MyConstructionTeamActivity.this,
								ConstructionDetailActivity.class);
						intent.putExtra("recruit", item.getConstructionTeam()
								.getId() + "");
						intent.putExtra("show", "show");
						// show 显示退出施工队 否则 ""
						startActivity(intent);

					}
				});
			}
		};
		// myAdapter = new
		// MyAdapter<ConstructionTeam>(MyConstructionTeamActivity.this, list,
		// R.layout.layout_recruit_information_item);

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMyJoinTeamList("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getMyJoinTeamList(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});

		join_construction_team_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyConstructionTeamActivity.this,
						JoinConstructionTeamActivity.class));
			}
		});

		deal_invite_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(MyConstructionTeamActivity.this,
						DealInviteActivity.class));

			}
		});

		mtvMyConstructionTeamTitle
				.setOnLeftClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		// mtvMyConstructionTeamTitle
		// .setOnRightClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// startActivity(new Intent(
		// MyConstructionTeamActivity.this,
		// JoinConstructionTeamActivity.class));
		// }
		// });
		//
		mListView.setAdapter(myAdapter);
	}

	private void getMyJoinTeamList(String Page, String Size, final int State) {
		RequestParams params = new RequestParams(HttpUtil.MyJoinTeamList);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				interfaceReturn = InterfaceReturn.fromJson(result,
						ListConstructionTeam.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						// list.addAll(interfaceReturn.getResults());
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));
						}
					} else {
						// T.showToast(MyConstructionTeamActivity.this,
						// interfaceReturn.getMessage());
					}
					if (State == REFRESH_DATA_FINISH) {
						mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
						// mListView.onShowText();
						// System.out.println(Count);
						// mListView.setStateNotData();
					} else if (State == LOAD_DATA_FINISH) {
						mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
					} else {
						myAdapter.notifyDataSetChanged();
					}
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list.clear();
		getMyJoinTeamList("1", "10", REFRESH_DATA_FINISH);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
