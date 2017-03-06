package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.custom.control.ZListView;
import com.qingzu.custom.control.ZListView.IXListViewListener;
import com.qingzu.entity.ApplyJoinTeamRecordListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class AuditTeamMemberActivity extends Activity {

	private MyTitleView aatm_title = null;
	private ZListView mListView = null;
	private List<ApplyJoinTeamRecordListModels> list = null;
	private MyAdapter<ApplyJoinTeamRecordListModels> myAdapter = null;
	private static final int LOAD_DATA_FINISH = 10; // 加载更多
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int BIND_ADAPTER = 7;// 绑定适配器
	private String UserToken = null;
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

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		setContentView(R.layout.activity_audit_team_member);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		list = new ArrayList<ApplyJoinTeamRecordListModels>();

		aatm_title = (MyTitleView) findViewById(R.id.aatm_title);
		mListView = (ZListView) findViewById(R.id.aatm_listview);
		mListView.setPullLoadEnable(true);

		aatm_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(TeamManageActivity.ADD_TEAM_MEMBER);
				finish();
			}
		});
		
		aatm_title.setOnRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AuditTeamMemberActivity.this,InviteWorkerListActivity.class));
			}
		});
		myAdapter = new MyAdapter<ApplyJoinTeamRecordListModels>(
				AuditTeamMemberActivity.this, list,
				R.layout.layout_team_manage_child) {
			TextView ltmc_tv_title;
			ImageView ltmc_iv_member_photo;
			RatingBar ltmc_rb_evaluate;
			ImageView ltmc_iv_delete_member;
			ImageView ltmc_iv_move_group;
			Button ltmc_bt_chat;

			@Override
			public void convert(ViewHolder view, int position,
					final ApplyJoinTeamRecordListModels item) {
				ltmc_bt_chat=view.getView(R.id.ltmc_bt_chat);
				ltmc_tv_title = view.getView(R.id.ltmc_tv_title);
				ltmc_iv_member_photo = view.getView(R.id.ltmc_iv_member_photo);
				ltmc_rb_evaluate = view.getView(R.id.ltmc_rb_evaluate);
				ltmc_iv_delete_member = view
						.getView(R.id.ltmc_iv_delete_member);
				ltmc_iv_move_group = view.getView(R.id.ltmc_iv_move_group);
				ltmc_iv_delete_member.setVisibility(View.VISIBLE);
				ltmc_iv_move_group.setVisibility(View.VISIBLE);
				ltmc_bt_chat.setVisibility(View.GONE);
				ltmc_tv_title
						.setText(item.getMember().getNickName().length() > 0 ? item
								.getMember().getNickName() : item.getMember()
								.getUserName());
				ltmc_rb_evaluate.setRating((float) item.getMember()
						.getWorkerLevel());
				ImageLoaderUtil.loadXUtilImage(item.getMember()
						.getMemberPhoto(), ltmc_iv_member_photo);
				ltmc_iv_delete_member.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CustomDialog.Builder(AuditTeamMemberActivity.this)
								.setTitle("提示")
								.setMessage("是否拒绝当前用户的申请")
								.setPositiveButton(R.string.Yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												NoPassApplyJoinTeam(item
														.getMember().getId());
												dialog.dismiss();
											}
										})
								.setNegativeButton(R.string.No,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												dialog.dismiss();
											}
										}).create().show();
					}
				});
				ltmc_iv_move_group.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						PassApplyJoinTeam(item.getMember().getId());
					}
				});
			}
		};
		MyListJoin("1", "10", BIND_ADAPTER);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				list.clear();
				MyListJoin("1", "10", REFRESH_DATA_FINISH);
				Count = 1;
			}

			@Override
			public void onLoadMore() {
				Count = Count + 1;
				CountStr = Count + "";
				MyListJoin(CountStr, "10", LOAD_DATA_FINISH);
			}
		});
	}

	/**
	 * 当前登录用户工人工长拒绝工人的加入施工队申请申请 PUT
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void NoPassApplyJoinTeam(int id) {
		RequestParams params = new RequestParams(
				HttpUtil.NoPassApplyJoinTeam.replace("{MemberId}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(x.app(), interfaceReturn.getMessage());
				} else {
					T.showToast(x.app(), interfaceReturn.getMessage());
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
	 * 当前登录用户工人工长通过组员的加入施工队申请 PUT
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void PassApplyJoinTeam(int id) {
		RequestParams params = new RequestParams(
				HttpUtil.PassApplyJoinTeam.replace("{MemberId}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(x.app(), interfaceReturn.getMessage());
				} else {
					T.showToast(x.app(), interfaceReturn.getMessage());
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
	 * 当前登录用户施工队的申请加入施工队记录列表信息 GET
	 * 
	 * @param Page
	 * @param Size
	 * @param State
	 * @author Johnson
	 */
	private void MyListJoin(String Page, String Size, final int State) {
		RequestParams params = new RequestParams(HttpUtil.MyListJoin.replace(
				"{Page}", Page).replace("{Size}", Size));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ApplyJoinTeamRecordListModels> interfaceReturn = new InterfaceReturn<ApplyJoinTeamRecordListModels>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ApplyJoinTeamRecordListModels.class);
				PageCount = interfaceReturn.getTotalCount();
				if (interfaceReturn.isStatus()) {
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						list.add(interfaceReturn.getResults().get(i));
					}
				} else {
				}
				if (State == REFRESH_DATA_FINISH) {
					mhandler.sendEmptyMessage(REFRESH_DATA_FINISH);
				} else if (State == LOAD_DATA_FINISH) {
					mhandler.sendEmptyMessage(LOAD_DATA_FINISH);
				} else if (State == BIND_ADAPTER) {
					mListView.setAdapter(myAdapter);
				} else {
					myAdapter.notifyDataSetChanged();
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
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getRepeatCount() == 0) {
		setResult(TeamManageActivity.ADD_TEAM_MEMBER);
    }
	return super.onKeyDown(keyCode, event);
}
}
