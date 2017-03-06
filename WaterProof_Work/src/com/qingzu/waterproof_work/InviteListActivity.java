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
import com.qingzu.entity.FindWorkInfoListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.InviteJoinTeamList;
import com.qingzu.entity.InviteJoinTeamRecord;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 邀请列表
 * @author Administrator
 *
 */
public class InviteListActivity extends Activity implements OnClickListener{
	
	private MyTitleView invite_list_title=null;
	private ZListView mListView=null;
	private int PageCount = 0; // 总页数
	private int Count = 1;
	private String CountStr = null;
	private InterfaceReturn<InviteJoinTeamList> interfaceReturn = new InterfaceReturn<InviteJoinTeamList>();
	private MyAdapter<InviteJoinTeamList> myAdapter = null;
	private List<InviteJoinTeamList> list = new ArrayList<InviteJoinTeamList>();
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
		setContentView(R.layout.activity_invite_list);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}


	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		invite_list_title=(MyTitleView) findViewById(R.id.invite_list_title);
		mListView=(ZListView) findViewById(R.id.invite_list_listview);
		getMessage("1", "10", MainActivity.LOAD_DATA);  
		mListView.setPullLoadEnable(true);
		
		myAdapter = new MyAdapter<InviteJoinTeamList>(
				InviteListActivity.this, list,
				R.layout.layout_invite_worker_item) {
			RoundImageView	riv_invite_img=null;
			TextView  invite_contact_name_tv=null;
			ImageView invite_certification_img=null;
			TextView  invite_jiedan_number_tv=null;
			RatingBar invite_assess_ratingbar=null;
			ImageButton invite_imgbutton=null;
			Button check_bt=null;
			Button has_joined_bt=null;
			Button has_refused_bt=null;
			RelativeLayout invite_rl=null;

					@Override
					public void convert(ViewHolder view, final int position,
							InviteJoinTeamList item) {
						// TODO Auto-generated method stub
						invite_rl=view.getView(R.id.invite_rl);
						riv_invite_img=view.getView(R.id.riv_invite_img);
						invite_contact_name_tv=view.getView(R.id.invite_contact_name_tv);
						invite_certification_img=view.getView(R.id.invite_certification_img);
						invite_jiedan_number_tv=view.getView(R.id.invite_jiedan_number_tv);
						invite_assess_ratingbar=view.getView(R.id.invite_assess_ratingbar);
						invite_imgbutton=view.getView(R.id.invite_imgbutton);
						check_bt=view.getView(R.id.check_bt);
						has_joined_bt=view.getView(R.id.has_joined_bt);
						has_refused_bt=view.getView(R.id.has_refused_bt);
						invite_imgbutton.setVisibility(View.GONE);
						
						ImageLoader.getInstance().displayImage(
								list.get(position).getMember().getMemberPhoto(),
										riv_invite_img,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
						
						
						invite_contact_name_tv.setText(list.get(position).getMember().getNickName());
						
						if(list.get(position).getMember().isRealName()){
							invite_certification_img.setVisibility(View.VISIBLE);
						}else{
							invite_certification_img.setVisibility(View.GONE);
						}
						
						invite_jiedan_number_tv.setText("接单次数："+list.get(position).getFindWorkInfo().getWorkCount());
						invite_assess_ratingbar.setRating((float) Double.parseDouble(Tools
								.formatString(list.get(position).getMember().getWorkerLevel())));
//						infoState;//邀请是否同意(0待确认、1已同意，2拒绝加入，-1邀请人取消邀请)
						
//						int str1=list.get(position).getInviteJoinTeam().getInfoState();
//						System.out.println(str1);
						if(list.get(position).getInviteJoinTeam().getInfoState()==0){
							check_bt.setVisibility(View.VISIBLE);
							has_joined_bt.setVisibility(View.GONE);
							has_refused_bt.setVisibility(View.GONE);
						}else if(list.get(position).getInviteJoinTeam().getInfoState()==1){
							has_joined_bt.setVisibility(View.VISIBLE);
							check_bt.setVisibility(View.GONE);
							has_refused_bt.setVisibility(View.GONE);
						}else if(list.get(position).getInviteJoinTeam().getInfoState()==2){
							has_refused_bt.setVisibility(View.VISIBLE);
							check_bt.setVisibility(View.GONE);
							has_joined_bt.setVisibility(View.GONE);
							
						}
						
						
						
						final String id=list.get(position).getInviteJoinTeam().getId()+"";
						invite_rl.setOnLongClickListener(new OnLongClickListener() {
							
							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								HintDialog.Builder builder = new HintDialog.Builder(
										InviteListActivity.this);
								builder.setTitle(R.string.Hint);
								builder.setMessage("是否删除邀请记录!");
								builder.setPositiveButton(R.string.Yes,
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int whichButton) {
												DeleteInviteJoinTeamRecordByID(id,position);
												dialog.dismiss();
											}

										

											
										});
								builder.setNegativeButton(R.string.No,
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int whichButton) {
												dialog.dismiss();
											}
										});
								builder.create().show();
								return false;
							}
						});
						
						
					}
			
		};
		mListView.setAdapter(myAdapter);
		
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				list.clear();
				getMessage("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
				mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Count = Count + 1;
				CountStr = Count + "";
				getMessage(CountStr, "10", LOAD_DATA_FINISH);
				mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
			}
		});
		
		invite_list_title.setOnLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

/**
 * 当前登录用户根据ID删除工长邀请入施工队记录
 * @param ID
 */
	private void DeleteInviteJoinTeamRecordByID(
			String ID,final int Position) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(
				HttpUtil.DeleteInviteJoinTeamRecordByID.replace("{ID}", ID));
		params.addHeader("EZFSToken",
				Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Delete(params, new MyCallBack<String>() {
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
								InviteListActivity.this,
								interfaceReturn.getMessage());
						list.remove(Position);
						myAdapter.notifyDataSetChanged();

					} else {
						T.showToast(
								InviteListActivity.this,
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
	
	
	/**
	 * 当前登录用户(工长)获取邀请工人入队记录列表信息(分页)get
	 * @param Page
	 * @param Size
	 * @param State
	 */
	private void getMessage(String Page, String Size, final int State) {
		// TODO Auto-generated method stub
		
		RequestParams params = new RequestParams(HttpUtil.InviteJoinListByPage.replace("{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				interfaceReturn = InterfaceReturn.fromJson(result,
						InviteJoinTeamList.class);
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
						T.showToast(InviteListActivity.this,
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

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		list.clear();
//		getMessage("1", "10", REFRESH_DATA_FINISH);
//		Count = 1;
//	}
}
