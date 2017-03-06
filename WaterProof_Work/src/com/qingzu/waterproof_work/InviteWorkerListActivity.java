package com.qingzu.waterproof_work;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.qingzu.entity.FindWorkInfoListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.InviteJoinTeamRecord;
import com.qingzu.entity.ListConstructionTeam;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 一键邀请的工人列表
 * 
 * @author Administrator
 * 
 */
public class InviteWorkerListActivity extends Activity implements
		OnClickListener {
	private String strCity = null;
	private MyTitleView deal_worker_title = null;
	private ZListView mListView = null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private InterfaceReturn<FindWorkInfoListModels> interfaceReturn = new InterfaceReturn<FindWorkInfoListModels>();
	private MyAdapter<FindWorkInfoListModels> myAdapter = null;
	private List<FindWorkInfoListModels> list = new ArrayList<FindWorkInfoListModels>();
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private String UserToken = null;
	private String MemberId = null;

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
		setContentView(R.layout.activity_invite_worker_list);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		into();
		initView();
	}

	public void into() {
		SharedPreferences preferences2 = getSharedPreferences("city",
				Activity.MODE_PRIVATE);
		strCity = preferences2.getString("city", "");

	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		deal_worker_title = (MyTitleView) findViewById(R.id.deal_worker_title);
		mListView = (ZListView) findViewById(R.id.invite_worker_listview);
		getMessage("1", "10", strCity, MainActivity.LOAD_DATA);
		mListView.setPullLoadEnable(true);

		myAdapter = new MyAdapter<FindWorkInfoListModels>(
				InviteWorkerListActivity.this, list,
				R.layout.layout_invite_worker_item) {

			RoundImageView riv_invite_img = null;
			TextView invite_contact_name_tv = null;
			ImageView invite_certification_img = null;
			TextView invite_jiedan_number_tv = null;
			RatingBar invite_assess_ratingbar = null;
			ImageButton invite_imgbutton = null;

			@Override
			public void convert(ViewHolder view, int position,
					FindWorkInfoListModels item) {
				// TODO Auto-generated method stub
				riv_invite_img = view.getView(R.id.riv_invite_img);
				invite_contact_name_tv = view
						.getView(R.id.invite_contact_name_tv);
				invite_certification_img = view
						.getView(R.id.invite_certification_img);
				invite_jiedan_number_tv = view
						.getView(R.id.invite_jiedan_number_tv);
				invite_assess_ratingbar = view
						.getView(R.id.invite_assess_ratingbar);
				invite_imgbutton = view.getView(R.id.invite_imgbutton);

				ImageLoader
						.getInstance()
						.displayImage(
								list.get(position).getMember().getMemberPhoto(),
								riv_invite_img,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
				invite_contact_name_tv.setText(list.get(position).getMember()
						.getNickName());
				invite_jiedan_number_tv.setText("接单次数："
						+ list.get(position).getFindWorkInfo().getWorkCount());
				invite_assess_ratingbar.setRating((float) Double
						.parseDouble(Tools.formatString(list.get(position)
								.getMember().getWorkerLevel())));
				if(list.get(position).getMember().isRealName()){
					invite_certification_img.setVisibility(View.VISIBLE);
				}else{
					invite_certification_img.setVisibility(View.GONE);
				}

				MemberId = list.get(position).getMember().getId() + "";
				invite_imgbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CreateInviteJoinTeamByMemberID(MemberId);
					}

					/**
					 * 当前登录用户根据会员ID提交邀请工人入队记录信息post
					 * 
					 * @param MemberId
					 */
					private void CreateInviteJoinTeamByMemberID(String MemberId) {
						// TODO Auto-generated method stub

						RequestParams params = new RequestParams(
								HttpUtil.CreateInviteJoinTeamByMemberID);
						params.addHeader("EZFSToken",
								Tools.getEZFSToken(UserToken));
						params.addBodyParameter("MemberId", MemberId);
						params.setCharset("utf-8");
						Xutils.Post(params, new MyCallBack<String>() {
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub

								InterfaceReturn<InviteJoinTeamRecord> interfaceReturn = new InterfaceReturn<InviteJoinTeamRecord>();
								interfaceReturn = InterfaceReturn.fromJson(
										result, InviteJoinTeamRecord.class);
								if (interfaceReturn != null) {
									if (interfaceReturn.isStatus()) {
										T.showToast(
												InviteWorkerListActivity.this,
												interfaceReturn.getMessage());

									} else {
										T.showToast(
												InviteWorkerListActivity.this,
												interfaceReturn.getMessage());
									}
								}

							}

							@Override
							public void onError(Throwable ex,
									boolean isOnCallback) {
								Toast.makeText(x.app(), ex.getMessage(),
										Toast.LENGTH_LONG).show();
								if (ex instanceof HttpException) { // 网络错误
									HttpException httpEx = (HttpException) ex;
									int responseCode = httpEx.getCode();
									String responseMsg = httpEx.getMessage();
									String errorResult = httpEx.getResult();
									System.out.println("responseMsg:"
											+ responseMsg + "=====errorResult:"
											+ errorResult + "=====responseCode"
											+ responseCode);
									// ...
								} else { // 其他错误
									// ...
								}
							}
						});

					}
				});
			}

		};

		mListView.setAdapter(myAdapter);

		deal_worker_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMessage("1", "10", strCity, REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", strCity, LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});
		deal_worker_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(InviteWorkerListActivity.this,
						InviteListActivity.class));
			}
		});

	}

	private void getMessage(String Page, String Size, String strCity,
			final int State) {
		// TODO Auto-generated method stub
		String urlCityName = null;
		try {
			urlCityName = URLEncoder.encode(strCity, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams(HttpUtil.FindWorkByPage
				.replace("{Page}", Page).replace("{Size}", Size)
				.replace("{CityName}", urlCityName));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfoListModels.class);
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
						T.showToast(InviteWorkerListActivity.this,
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
