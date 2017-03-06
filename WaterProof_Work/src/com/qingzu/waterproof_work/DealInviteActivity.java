package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.BeInviteJoinTeamList;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.InviteJoinTeamRecord;
import com.qingzu.entity.ListConstructionTeam;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 邀请处理
 * @author Administrator
 *
 */
public class DealInviteActivity extends Activity implements OnClickListener {
	
	private MyTitleView deal_invite_title=null;
	private ZListView mListView=null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private MyAdapter<BeInviteJoinTeamList> myAdapter = null;
	private List<BeInviteJoinTeamList> list = new ArrayList<BeInviteJoinTeamList>();
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

	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_invite);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		deal_invite_title=(MyTitleView) findViewById(R.id.deal_invite_title);
		mListView=(ZListView) findViewById(R.id.deal_invite_listview);
		getBeInviteJoinList("1", "10", MainActivity.LOAD_DATA);
		mListView.setPullLoadEnable(true);
		
		
		myAdapter = new MyAdapter<BeInviteJoinTeamList>(
				DealInviteActivity.this, list,
				R.layout.layout_deal_invite) {
			TextView  deal_invite_name_tv=null;
			ImageView deal_invite_vip_img=null;
			TextView  deal_worker_number=null;
			RatingBar deal_invite_rb_evaluate=null;
			ImageView deal_accept_img=null;
			ImageView deal_refuse_img=null;
			
			

				@Override
				public void convert(ViewHolder view, int position,
							final BeInviteJoinTeamList item) {
						// TODO Auto-generated method stub
						
						deal_invite_name_tv=view.getView(R.id.deal_invite_name_tv);
						deal_invite_vip_img=view.getView(R.id.deal_invite_vip_img);
						deal_worker_number=view.getView(R.id.deal_worker_number);
						deal_invite_rb_evaluate=view.getView(R.id.deal_invite_rb_evaluate);
						deal_accept_img=view.getView(R.id.deal_accept_img);
						deal_refuse_img=view.getView(R.id.deal_refuse_img);
						
						deal_invite_name_tv.setText(list.get(position).getConstructionTeam().getTeamName());
						deal_worker_number.setText("工人数："+list.get(position).getConstructionTeam().getAlreadyNumber());
						deal_invite_rb_evaluate.setRating((float) Double.parseDouble(Tools
								.formatString(list.get(position).getConstructionTeam().getCreditStars())));
						
//						deal_invite_vip_img
						
						
						final String id=list.get(position).getInviteJoinTeam().getId()+"";
						
						deal_accept_img.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								PassInviteJoinById(id);
							}

							
						});
						
						
						deal_refuse_img.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								NoPassInviteJoinById(id);
							}

							
						});
					}
			
			
		};
		
		
		
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getBeInviteJoinList("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getBeInviteJoinList(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});
		deal_invite_title.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mListView.setAdapter(myAdapter);
	}
/**
 * 当前登录用户(工人)接受施工队入队邀请put
 */
	private void PassInviteJoinById(String ID) {
		// TODO Auto-generated method stub
		
		RequestParams params = new RequestParams(HttpUtil.PassInviteJoinById.replace("{Id}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
				InterfaceReturn<InviteJoinTeamRecord> interfaceReturn = new InterfaceReturn<InviteJoinTeamRecord>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						InviteJoinTeamRecord.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(DealInviteActivity.this,
								interfaceReturn.getMessage());
						
						myAdapter.notifyDataSetChanged();

						
					} else {
						T.showToast(DealInviteActivity.this,
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
	
	
	/**
	 * 当前登录用户(工人)拒绝施工队入队邀请put
	 */
	private void NoPassInviteJoinById(String ID) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.NoPassInviteJoinById.replace("{Id}", ID));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
				InterfaceReturn<InviteJoinTeamRecord> interfaceReturn = new InterfaceReturn<InviteJoinTeamRecord>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						InviteJoinTeamRecord.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(DealInviteActivity.this,
								interfaceReturn.getMessage());
						myAdapter.notifyDataSetChanged();
						
					} else {
						T.showToast(DealInviteActivity.this,
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
	
	
	
	
	/**
	 * 当前登录用户(工人)获取受邀请入队记录列表信息
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getBeInviteJoinList(String Page, String Size,
			final int State) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.BeInviteJoinList.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
				InterfaceReturn<BeInviteJoinTeamList> interfaceReturn = new InterfaceReturn<BeInviteJoinTeamList>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						BeInviteJoinTeamList.class);
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
						T.showToast(DealInviteActivity.this,
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
