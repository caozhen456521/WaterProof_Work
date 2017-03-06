package com.qingzu.waterproof_work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.ConstructionTeamModels;
import com.qingzu.entity.CreateConstructionTeamAppraiseModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.TeamAppraise;
import com.qingzu.entity.WorkInfoAppraise;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluateConstructionTeamActivity extends Activity implements
		OnClickListener {

	private MyTitleView evaluate_construction_team_title = null;// 评价施工队标题
	private ImageView riv_evaluate_construction_team_img = null;// 工长头像
	private RatingBar worker_total_assess_ratingbar = null;// 工长的评分进度条
	private TextView worker_total_assess_tv = null;// 工长的评分
	private TextView jie_dan_number_tv = null;// 接单次数
	private RatingBar evaluate_worker_assess_ratingbar = null;// 施工队的评分
	private ListView lv_evaluate_construction_team = null;// 施工队人员列表
	
	private TextView tvWorkerasses=null;
	private EditText aect_et_assess_info = null;
	private Button commit_evaluate_bt = null;
	private TextView aect_tv_leader_name = null;
	private int InfoOrderId;
	private MyAdapter<Member> adapter = null;
	private List<Member> list = null;
	private HashMap<Integer, Integer> hashMapValue = null;

	private String UserToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_construction_team);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	@SuppressLint("UseSparseArrays")
	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		list = new ArrayList<Member>();
		hashMapValue = new HashMap<Integer, Integer>();
		tvWorkerasses=(TextView) findViewById(R.id.assess_worker1_tv);
		evaluate_construction_team_title = (MyTitleView) findViewById(R.id.evaluate_construction_team_title);
		riv_evaluate_construction_team_img = (ImageView) findViewById(R.id.riv_evaluate_construction_team_img);
		worker_total_assess_ratingbar = (RatingBar) findViewById(R.id.worker_total_assess_ratingbar);
		worker_total_assess_tv = (TextView) findViewById(R.id.worker_total_assess_tv);
		jie_dan_number_tv = (TextView) findViewById(R.id.jie_dan_number_tv);
		evaluate_worker_assess_ratingbar = (RatingBar) findViewById(R.id.evaluate_worker_assess_ratingbar);
		aect_et_assess_info = (EditText) findViewById(R.id.aect_et_assess_info);
		commit_evaluate_bt = (Button) findViewById(R.id.commit_evaluate_bt);
		aect_tv_leader_name = (TextView) findViewById(R.id.aect_tv_leader_name);
		lv_evaluate_construction_team = (ListView) findViewById(R.id.lv_evaluate_construction_team);
		adapter = new MyAdapter<Member>(this, list,
				R.layout.layout_team_manage_child) {

			TextView group_title;
			ImageView ltmc_iv_member_photo;
			RatingBar ltmc_rb_evaluate;
			Button ltmc_bt_chat;

			@Override
			public void convert(ViewHolder view, final int position, Member item) {
				ltmc_bt_chat=view.getView(R.id.ltmc_bt_chat);
				group_title = view.getView(R.id.ltmc_tv_title);
				ltmc_iv_member_photo = view.getView(R.id.ltmc_iv_member_photo);
				ltmc_rb_evaluate = view.getView(R.id.ltmc_rb_evaluate);
				ltmc_bt_chat.setVisibility(View.GONE);

				if (item.getContactName().length() > 0) {
					group_title.setText(item.getContactName());
				} else {
					group_title.setText(item.getNickName());
				}
				ImageLoaderUtil.loadXUtilImage(item.getMemberPhoto(),
						ltmc_iv_member_photo);
				ltmc_rb_evaluate.setStepSize(1.0f);
				ltmc_rb_evaluate.setIsIndicator(false);
				ltmc_rb_evaluate
						.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

							@Override
							public void onRatingChanged(RatingBar ratingBar,
									float rating, boolean fromUser) {
								if (fromUser) {
									hashMapValue.put(position, (int) rating);
								}
							}
						});
				Integer i = hashMapValue.get(position);
				ltmc_rb_evaluate
						.setRating(i == null ? evaluate_worker_assess_ratingbar
								.getRating() : hashMapValue.get(position));
			}
		};
		evaluate_worker_assess_ratingbar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						
						
						
						if (!Tools.formatString(evaluate_worker_assess_ratingbar.getRating())
								.equals("0.0")) {
							tvWorkerasses.setText(Tools
									.formatString(evaluate_worker_assess_ratingbar.getRating()));
						} else if (Tools.formatString(evaluate_worker_assess_ratingbar.getRating())
								.equals("0.0")) {
							evaluate_worker_assess_ratingbar.setRating(1);
							tvWorkerasses.setText(Tools
									.formatString(evaluate_worker_assess_ratingbar.getRating()));
							T.showToast(EvaluateConstructionTeamActivity.this, getString(R.string.Least_One_Star));
						}
						
						
						Log.e("RatingBar", "onRatingChanged");
						adapter.notifyDataSetChanged();
					}
				});
		Intent intent = getIntent();
		InfoOrderId = intent.getIntExtra("ORDER_ID", 0);
		ConstructionTeamByID(intent.getIntExtra("TEAM_ID", 0));
		DispatchMemberByInfoOrderId(InfoOrderId);

		commit_evaluate_bt.setOnClickListener(this);

		evaluate_construction_team_title
				.setOnLeftClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
	}

	/**
	 * 根据订单ID获取派遣施工人员信息 GET
	 * 
	 * @param infoOrderId
	 * @author Johnson
	 */
	private void DispatchMemberByInfoOrderId(int infoOrderId) {
		RequestParams params = new RequestParams(
				HttpUtil.DispatchMemberByInfoOrderId.replace("{InfoOrderId}",
						infoOrderId + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings({ "unchecked", "static-access" })
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<Member> interfaceReturn = new InterfaceReturn<Member>();
				interfaceReturn = interfaceReturn
						.fromJson(result, Member.class);
				if (interfaceReturn.isStatus()) {
					if (interfaceReturn.getResults().size() > 0) {
						for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
							list.add(interfaceReturn.getResults().get(i));
						}
						lv_evaluate_construction_team
								.setVisibility(View.VISIBLE);
						lv_evaluate_construction_team.setAdapter(adapter);
						setListViewHeightBasedOnChildren(lv_evaluate_construction_team);
					} else {
						lv_evaluate_construction_team.setVisibility(View.GONE);
					}

				} else {
					lv_evaluate_construction_team.setVisibility(View.GONE);
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
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 根据ID获取工程施工队信息 GET
	 * 
	 * @param id
	 * @author Johnson
	 */
	private void ConstructionTeamByID(int id) {
		RequestParams params = new RequestParams(
				HttpUtil.ConstructionTeamByID.replace("{ID}", id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionTeamModels> interfaceReturn = new InterfaceReturn<ConstructionTeamModels>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeamModels.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						ImageLoaderUtil.loadXUtilImage(interfaceReturn
								.getResults().get(0).getMember()
								.getMemberPhoto(),
								riv_evaluate_construction_team_img);
						worker_total_assess_ratingbar
								.setRating((float) interfaceReturn.getResults()
										.get(0).getConstructionTeam()
										.getConstructionLevel());
						worker_total_assess_tv.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getWorkerLevel()
								+ "分");
						jie_dan_number_tv.setText(interfaceReturn.getResults()
								.get(0).getConstructionTeam().getDealNumber()
								+ "");
						aect_tv_leader_name.setText(interfaceReturn
								.getResults().get(0).getConstructionTeam()
								.getTeamName());
					} else {
						T.showToast(EvaluateConstructionTeamActivity.this,
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
		switch (v.getId()) {
		case R.id.commit_evaluate_bt:
			WorkInfoAppraise workInfoAppraise = null;
			List<WorkInfoAppraise> workInfoAppraises = new ArrayList<WorkInfoAppraise>();
			for (int i = 0; i < list.size(); i++) {
				workInfoAppraise = new WorkInfoAppraise();
				workInfoAppraise.setInfoOrderId(InfoOrderId);
				workInfoAppraise.setToMemberId(list.get(i).getId());
				workInfoAppraise
						.setStarNumber((int) (hashMapValue.get(i) != null ? hashMapValue
								.get(i) : evaluate_worker_assess_ratingbar
								.getRating()));
				workInfoAppraises.add(workInfoAppraise);
			}
			TeamAppraise teamAppraise = new TeamAppraise();
			teamAppraise.setInfoOrderId(InfoOrderId);
			teamAppraise.setStarNumber((int) evaluate_worker_assess_ratingbar
					.getRating());
			teamAppraise.setCommentContent(aect_et_assess_info.getText()
					.toString().trim());
			CreateConstructionTeamAppraiseModel teamAppraiseModel = new CreateConstructionTeamAppraiseModel();
			teamAppraiseModel.setTeamAppraise(teamAppraise);
			teamAppraiseModel.setWorkInfoAppraiseList(workInfoAppraises);
			CreateConstructionTeamAppraise(InterfaceReturn.getGson().toJson(
					teamAppraiseModel));
			break;

		default:
			break;
		}
	}

	/**
	 * 当前登录用户创建施工队被评价记录信息
	 * 
	 * @param InfoOrderId
	 * @param CommentContent
	 * @param StarNumber
	 * @author Johnson
	 */
	private void CreateConstructionTeamAppraise(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.CreateConstructionTeamAppraise);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setCharset("utf-8");
		params.setBodyContent(json);
		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(EvaluateConstructionTeamActivity.this,
							interfaceReturn.getMessage());
					finish();
				} else {
					T.showToast(EvaluateConstructionTeamActivity.this,
							interfaceReturn.getMessage());
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

}
