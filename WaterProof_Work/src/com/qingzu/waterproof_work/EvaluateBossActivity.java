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

import android.annotation.SuppressLint;
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
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluateBossActivity extends Activity implements OnClickListener {
	private MyTitleView mtvValuateBossTitle = null;// 评价老板标题
	private ImageView rivEvaluateBossImg = null;// 老板的头像
	private TextView tvBossName = null;// 老板名字
	private ImageView BossCertificationImg = null;// 老板实名认证图片
	private RatingBar BossTotalAssessRatingbar = null;// 老板的评分进度
	private TextView tvBossAssessTotalPoints = null;// 老板的评分
	private TextView tvBossReleaseNumber = null;// 老板发布次数
	private RatingBar BossAssessRatingbar = null;// 对老板评价分数
	private EditText etAdviceBoss = null;// 给老板提建议
	private Button btBossCommitEvaluate = null;// 提交评价
	
	private TextView tvBossasses=null;

	private String UserToken = null;
	private Integer OrderId = null;
	private Integer MemberId = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_boss);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		mtvValuateBossTitle = (MyTitleView) findViewById(R.id.evaluate_boss_title);
		rivEvaluateBossImg = (ImageView) findViewById(R.id.riv_evaluate_boss_img);
		tvBossName = (TextView) findViewById(R.id.boss_name_tv);
		BossCertificationImg = (ImageView) findViewById(R.id.boss_certification_img);
		BossTotalAssessRatingbar = (RatingBar) findViewById(R.id.boss_total_assess_ratingbar);
		tvBossAssessTotalPoints = (TextView) findViewById(R.id.boss_assess_total_points_tv);
		tvBossReleaseNumber = (TextView) findViewById(R.id.boss_release_number_tv);
		BossAssessRatingbar = (RatingBar) findViewById(R.id.boss_assess_ratingbar);
		etAdviceBoss = (EditText) findViewById(R.id.advice_boss_et);
		tvBossasses=(TextView) findViewById(R.id.assess_boss_tv);
		btBossCommitEvaluate = (Button) findViewById(R.id.boss_commit_evaluate_bt);
		
		BossAssessRatingbar
		.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1,
					boolean arg2) {
				// TODO Auto-generated method stub
				if (!Tools.formatString(BossAssessRatingbar.getRating())
						.equals("0.0")) {
					tvBossasses.setText(Tools
							.formatString(BossAssessRatingbar.getRating()));
				} else if (Tools.formatString(BossAssessRatingbar.getRating())
						.equals("0.0")) {
					BossAssessRatingbar.setRating(1);
					tvBossasses.setText(Tools
							.formatString(BossAssessRatingbar.getRating()));
					T.showToast(EvaluateBossActivity.this, getString(R.string.Least_One_Star));
				}

				// rbBossassess.setRating(5f);

			}
		});

		
		
		

		Intent intent = getIntent();
		OrderId = intent.getIntExtra("ORDER_ID", 0);
		MemberId = intent.getIntExtra("MEMBER_ID", 0);
		UserInfoByID(MemberId);
		btBossCommitEvaluate.setOnClickListener(this);

		mtvValuateBossTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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
				InterfaceReturn<MemberModel> interfaceReturn = new InterfaceReturn<EvaluateBossActivity.MemberModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberModel.class);
				if (interfaceReturn.isStatus()) {
					ImageLoaderUtil.loadXUtilImage(interfaceReturn.getResults()
							.get(0).getMember().getMemberPhoto(),
							rivEvaluateBossImg);
					if (interfaceReturn.getResults().get(0).getMember()
							.getNickName().length() > 0) {
						tvBossName.setText(interfaceReturn.getResults().get(0)
								.getMember().getNickName());
					} else if (interfaceReturn.getResults().get(0).getMember()
							.getContactName().length() > 0) {
						tvBossName.setText(interfaceReturn.getResults().get(0)
								.getMember().getContactName());
					} else {
						tvBossName.setText(interfaceReturn.getResults().get(0)
								.getMember().getUserName());
					}
					BossTotalAssessRatingbar.setRating((float) interfaceReturn
							.getResults().get(0).getMember().getBossLevel());
					tvBossAssessTotalPoints.setText(interfaceReturn
							.getResults().get(0).getMember().getBossLevel()
							+ "分");
					tvBossReleaseNumber.setText(interfaceReturn.getResults()
							.get(0).getMember().getPushCount()
							+ "");
				} else {
					T.showToast(EvaluateBossActivity.this,
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.boss_commit_evaluate_bt:
			CreateAppraiseInfo(OrderId, (int) BossAssessRatingbar.getRating(),
					etAdviceBoss.getText().toString().trim());
			break;

		default:
			break;
		}
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
		RequestParams params = new RequestParams(HttpUtil.CreateAppraiseInfo);
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
					T.showToast(EvaluateBossActivity.this,
							interfaceReturn.getMessage());
					finish();
				} else {
					T.showToast(EvaluateBossActivity.this,
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
