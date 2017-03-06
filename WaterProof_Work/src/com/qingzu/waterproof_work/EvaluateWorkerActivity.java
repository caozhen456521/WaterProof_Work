package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.AppManager;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.EvaluateBossActivity.MemberModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class EvaluateWorkerActivity extends Activity implements OnClickListener {

	private MyTitleView mtvEvaluateWorkerTitle = null;// 评价工人标题
	private ImageView rivEvaluateWorkerImg = null;// 工人的头像
	private TextView tvWorkerName = null;// 工人名字
	private ImageView WorkerCertificationImg = null;// 工人实名认证图片
	private RatingBar WorkerTotalAssessRatingbar = null;// 工人的评分进度
	private TextView tvWorkerAssessTotalPoints = null;// 工人的评分
	private TextView tvWorkerCompleteNumber = null;// 给你个人接单次数
	private RatingBar WorkerAssessRatingbar = null;// 对工人评价分数
	private EditText etAdviceWorker = null;// 给工人提建议
	private Button btWorkerCommitEvaluate = null;// 提交评价
private TextView tvWorkerAsses=null;
	private String UserToken = null;
	private Integer OrderId = null;
	private Integer MemberId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_worker);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		tvWorkerAsses=(TextView) findViewById(R.id.assess_worker_tv);
		mtvEvaluateWorkerTitle = (MyTitleView) findViewById(R.id.evaluate_worker_title);
		rivEvaluateWorkerImg = (ImageView) findViewById(R.id.riv_evaluate_worker_img);
		tvWorkerName = (TextView) findViewById(R.id.worker_name_tv);
		WorkerCertificationImg = (ImageView) findViewById(R.id.worker_certification_img);
		WorkerTotalAssessRatingbar = (RatingBar) findViewById(R.id.worker_total_assess_ratingbar);
		tvWorkerAssessTotalPoints = (TextView) findViewById(R.id.worker_assess_total_points_tv);
		tvWorkerCompleteNumber = (TextView) findViewById(R.id.worker_complete_number_tv);
		WorkerAssessRatingbar = (RatingBar) findViewById(R.id.worker_assess_ratingbar);
		etAdviceWorker = (EditText) findViewById(R.id.advice_worker_et);
		btWorkerCommitEvaluate = (Button) findViewById(R.id.worker_commit_evaluate_bt);
		Intent intent = getIntent();
		OrderId = intent.getIntExtra("ORDER_ID", 0);
		MemberId = intent.getIntExtra("MEMBER_ID", 0);
		UserInfoByID(MemberId);
		btWorkerCommitEvaluate.setOnClickListener(this);
		
		
		
		
		WorkerAssessRatingbar
		.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1,
					boolean arg2) {
				// TODO Auto-generated method stub
				if (!Tools.formatString(WorkerAssessRatingbar.getRating())
						.equals("0.0")) {
					tvWorkerAsses.setText(Tools
							.formatString(WorkerAssessRatingbar.getRating()));
				} else if (Tools.formatString(WorkerAssessRatingbar.getRating())
						.equals("0.0")) {
					WorkerAssessRatingbar.setRating(1);
					tvWorkerAsses.setText(Tools
							.formatString(WorkerAssessRatingbar.getRating()));
					T.showToast(EvaluateWorkerActivity.this, getString(R.string.Least_One_Star));
				}

				// rbBossassess.setRating(5f);

			}
		});

		mtvEvaluateWorkerTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.worker_commit_evaluate_bt:
			CreateAppraiseInfo(OrderId,
					(int) WorkerAssessRatingbar.getRating(), etAdviceWorker
							.getText().toString().trim());
			break;

		default:
			break;
		}
	}

	class MemberModel {
		private Member member;

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}
	}

	/**
	 * 按照用户ID获取用户信息 GET
	 * 
	 * @param Id
	 * @author Johnson
	 */
	private void UserInfoByID(int Id) {
		RequestParams params = new RequestParams(HttpUtil.UserInfoByID.replace(
				"{userID}", Id + ""));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MemberModel> interfaceReturn = new InterfaceReturn<MemberModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberModel.class);
				if (interfaceReturn.isStatus()) {
					ImageLoaderUtil.loadXUtilImage(interfaceReturn.getResults()
							.get(0).getMember().getMemberPhoto(),
							rivEvaluateWorkerImg);
					if (interfaceReturn.getResults().get(0).getMember()
							.getNickName().length() > 0) {
						tvWorkerName.setText(interfaceReturn.getResults()
								.get(0).getMember().getNickName());
					} else if (interfaceReturn.getResults().get(0).getMember()
							.getContactName().length() > 0) {
						tvWorkerName.setText(interfaceReturn.getResults()
								.get(0).getMember().getContactName());
					} else {
						tvWorkerName.setText(interfaceReturn.getResults()
								.get(0).getMember().getUserName());
					}
					WorkerTotalAssessRatingbar
							.setRating((float) interfaceReturn.getResults()
									.get(0).getMember().getWorkerLevel());
					tvWorkerAssessTotalPoints.setText(interfaceReturn
							.getResults().get(0).getMember().getWorkerLevel()
							+ "分");
					// tvWorkerCompleteNumber.setText(interfaceReturn.getResults()
					// .get(0).getMember().getPushCount()
					// + "");
				} else {
					T.showToast(EvaluateWorkerActivity.this,
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

	/**
	 * 当前登录用户评价老板信息 POST
	 * 
	 * @param InfoOrderId
	 * @param StarNumber
	 * @param CommentContent
	 * @author Johnson
	 */
	private void CreateAppraiseInfo(int InfoOrderId, int StarNumber,
			String CommentContent) {
		RequestParams params = new RequestParams(
				HttpUtil.WorkAppraiseCreateAppraiseInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("InfoOrderId", InfoOrderId + "");
		params.addBodyParameter("StarNumber", StarNumber + "");
		params.addBodyParameter("CommentContent", CommentContent);
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<String> interfaceReturn = new InterfaceReturn<String>();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn.isStatus()) {
					T.showToast(EvaluateWorkerActivity.this,
							interfaceReturn.getMessage());
					finish();
				} else {
					T.showToast(EvaluateWorkerActivity.this,
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
