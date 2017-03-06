package com.qingzu.waterproof_work;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ListConstructionTeam;
import com.qingzu.entity.LoginMember;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JoinConstructionTeamActivity extends Activity implements
		OnClickListener {
	private MyTitleView mtvMyConstructionTeamTitle = null;// 加入施工队标题

	private LinearLayout two_ll = null;

	private ZListView mListView = null;// 加入施工队列表
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private InterfaceReturn<ListConstructionTeam> interfaceReturn = new InterfaceReturn<ListConstructionTeam>();
	private MyAdapter<ListConstructionTeam> myAdapter = null;
	private List<ListConstructionTeam> list = new ArrayList<ListConstructionTeam>();

	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private String UserToken = null;
	private String MemberId = null;
	private String TeamId = null;
	private String CityName = null;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_construction_team);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mtvMyConstructionTeamTitle = (MyTitleView) findViewById(R.id.my_construction_team_title);
		mtvMyConstructionTeamTitle.setRightVidibility(View.GONE);
		mtvMyConstructionTeamTitle.setText("加入施工队");

		two_ll = (LinearLayout) findViewById(R.id.two_ll);
		two_ll.setVisibility(View.GONE);

		mListView = (ZListView) findViewById(R.id.construction_team_list_zlv);
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		SharedPreferences sp = getSharedPreferences("city",
				Activity.MODE_PRIVATE);
		CityName = sp.getString("city", "未知");
		MemberId = Tools.formatString(preferences.getInt("MemberId", 0));
		getConstructionTeamList("1", "10", CityName, REFRESH_DATA_FINISH);
		mListView.setPullLoadEnable(true);

		myAdapter = new MyAdapter<ListConstructionTeam>(
				JoinConstructionTeamActivity.this, list,
				R.layout.layout_join_construction_team_item) {
			RoundImageView team_img = null;
			TextView TeamName = null;
			ImageView vip_img = null;
			RatingBar assess_ratingbar = null;
			Button apply_bt = null;
			TextView points_tv = null;
			TextView jcomplete_number_tv = null; // 接单次数
			TextView worker_number_tv = null;// 用工数
			RelativeLayout team_rl_room = null;

			@Override
			public void convert(ViewHolder view, final int position,
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
				// apply_bt.setVisibility(View.GONE);
				team_rl_room = view
						.getView(R.id.join_construction_team_rl_room);

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

				TeamId = Tools.formatString(list.get(position)
						.getConstructionTeam().getId());

				apply_bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// TODO Auto-generated method stub
						CreateApplyJoinTeam(MemberId, item
								.getConstructionTeam().getId() + "");

					}

				});

				team_rl_room.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								JoinConstructionTeamActivity.this,
								ConstructionDetailActivity.class);

						intent.putExtra("show", "");
						intent.putExtra("recruit", item.getConstructionTeam()
								.getId() + "");

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
				getConstructionTeamList("1", "10", CityName,
						REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getConstructionTeamList(CountStr, "10", CityName,
						LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
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

		mtvMyConstructionTeamTitle
				.setOnRightClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
		mListView.setAdapter(myAdapter);

	}

	/**
	 * 提交工人申请加入施工队记录 视图模型
	 * 
	 * @param memberId
	 * @param teamId
	 */

	private void CreateApplyJoinTeam(String memberId, String teamId) {
		RequestParams params = new RequestParams(HttpUtil.CreateApplyJoinTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("MemberId", MemberId);
		params.addBodyParameter("TeamId", teamId);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(String result) {

				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						if (interfaceReturn.getResults() != null) {

						}
						Toast.makeText(JoinConstructionTeamActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(JoinConstructionTeamActivity.this,
								interfaceReturn.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}
			};

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

	private void getConstructionTeamList(String Page, String Size,
			String CityName, final int State) {
		String urlCityName = null;
		try {
			urlCityName = URLEncoder.encode(CityName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(
				HttpUtil.ConstructionTeamListByCityName
						.replace("{CityName}", urlCityName)
						.replace("{Page}", Page).replace("{Size}", Size));
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
					} else {
						T.showToast(JoinConstructionTeamActivity.this,
								interfaceReturn.getMessage());
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
