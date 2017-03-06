package com.qingzu.fragment;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingzu.custom.control.CheckSwitchButton;
import com.qingzu.entity.FindWorkInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.MemberAuthentication;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ForumActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity;
import com.qingzu.waterproof_work.MyConstructionTeamActivity;
import com.qingzu.waterproof_work.MyInformationActivity;
import com.qingzu.waterproof_work.MyShareActivity;
import com.qingzu.waterproof_work.PointsActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.SelectRoleActivity;
import com.qingzu.waterproof_work.SettingActivity;
import com.qingzu.waterproof_work.SkillApprove;
import com.qingzu.waterproof_work.SunEngineeringActitvty;
import com.qingzu.waterproof_work.WebViewActivity;

/**
 * 工人第三个fragment
 * 
 * @author Administrator
 * 
 */
public class WorkerThirdFragment extends Fragment implements OnClickListener {

	private View v = null;
	private RelativeLayout worker_third_rl_title = null;// 标题layout

	private ImageView worker_third_riv_member_photo = null;// 头像
	private TextView worker_third_name_tv = null;// 用户名
	private ImageView worker_third_certification_img = null;// 实名认证
	private TextView worker_third_tv_certification = null;
	private RatingBar worker_third_total_assess_ratingbar = null;// 小星星
	private TextView worker_third_assess_total_points_tv = null;// 评分
	private TextView worker_third_complete_number_tv = null;// 干活次数
	private ImageView worker_third_help_img = null;// 帮助
	private ImageView worker_third_arrows_img = null;// 箭头
	private TextView worker_third_tv_score = null;// 积分
	private RelativeLayout worker_third_rl_certification = null;// 实名认证layout
	private RelativeLayout worker_third_rl_team = null;// 我的施工队layout
	private RelativeLayout worker_third_rl_superiority = null;// 我的优势layout
	private RelativeLayout worker_third_rl_show_porject = null;// 晒工程layout
	private RelativeLayout worker_third__rl_score = null;// 积分layout
	private RelativeLayout worker_third_rl_technical_support = null;// 问技术layout
	private RelativeLayout worker_third_rl_share = null;// 我要分享layout
	private RelativeLayout worker_third_rl_switch_identity = null;// 身份切换
	private RelativeLayout worker_third_rl_set = null;// 设置layout
	private CheckSwitchButton worker_third_csb_push = null;// 推送 免打扰

	private String UserToken = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_worker_third, container, false);
		initView();

		if (Tools.isConnectingToInternet()) {

			getUserInfo();
			MyFindWorkInfo();// 获取工人干活次数
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub

		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		worker_third_rl_title = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_title);
		worker_third_riv_member_photo = (ImageView) v
				.findViewById(R.id.worker_third_riv_member_photo);
		worker_third_name_tv = (TextView) v
				.findViewById(R.id.worker_third_name_tv);
		worker_third_certification_img = (ImageView) v
				.findViewById(R.id.worker_third_certification_img);
		worker_third_total_assess_ratingbar = (RatingBar) v
				.findViewById(R.id.worker_third_total_assess_ratingbar);
		worker_third_assess_total_points_tv = (TextView) v
				.findViewById(R.id.worker_third_assess_total_points_tv);
		worker_third_complete_number_tv = (TextView) v
				.findViewById(R.id.worker_third_complete_number_tv);
		worker_third_help_img = (ImageView) v
				.findViewById(R.id.worker_third_help_img);
		worker_third_arrows_img = (ImageView) v
				.findViewById(R.id.worker_third_arrows_img);
		worker_third_tv_score = (TextView) v
				.findViewById(R.id.worker_third_tv_score);
		worker_third_rl_certification = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_certification);
		worker_third_rl_team = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_team);
		worker_third_rl_superiority = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_superiority);
		worker_third_rl_show_porject = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_show_porject);
		worker_third__rl_score = (RelativeLayout) v
				.findViewById(R.id.worker_third__rl_score);
		worker_third_rl_technical_support = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_technical_support);
		worker_third_rl_share = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_share);
		worker_third_rl_switch_identity = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_switch_identity);
		worker_third_rl_set = (RelativeLayout) v
				.findViewById(R.id.worker_third_rl_set);
		worker_third_tv_certification = (TextView) v
				.findViewById(R.id.worker_third_tv_certification);
		worker_third_csb_push = (CheckSwitchButton) v
				.findViewById(R.id.worker_third_csb_push);

		worker_third_rl_show_porject.setOnClickListener(this);
		worker_third_rl_title.setOnClickListener(this);
		worker_third_riv_member_photo.setOnClickListener(this);
		worker_third_help_img.setOnClickListener(this);
		worker_third_rl_certification.setOnClickListener(this);
		worker_third_rl_team.setOnClickListener(this);
		worker_third_rl_superiority.setOnClickListener(this);
		worker_third__rl_score.setOnClickListener(this);
		worker_third_rl_technical_support.setOnClickListener(this);
		worker_third_rl_share.setOnClickListener(this);
		worker_third_rl_switch_identity.setOnClickListener(this);
		worker_third_rl_set.setOnClickListener(this);
		
		worker_third_csb_push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					UpdateFindWorkState("2");
				} else {
					UpdateFindWorkState("1");
				}
			}
		});

	}
	
	/**
	 * 当前登录用户修改找活数据[工作状态]为空闲或者忙碌
	 * 
	 * @author Johnson
	 */
	private void UpdateFindWorkState(String State) {
		RequestParams params = new RequestParams(
				HttpUtil.UpdateFindWorkState.replace("{State}", State));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<FindWorkInfo> interfaceReturn = new InterfaceReturn<FindWorkInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfo.class);
				if (interfaceReturn.isStatus()) {
					if (interfaceReturn.getResults().get(0).getWorkState() == 2) {
						// 忙碌
						// fourth_csb_push.setChecked(false);
						T.showToast(getActivity(), "当前状态:忙碌");
					} else if (interfaceReturn.getResults().get(0)
							.getWorkState() == 1) {
						// 空闲
						// fourth_csb_push.setChecked(true);
						T.showToast(getActivity(), "当前状态:空闲");
					}
				} else {
					T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 当前登录用户获取自己的用户信息
	 */
	private void getUserInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<LoginMember> interfaceReturn = new InterfaceReturn<LoginMember>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						LoginMember.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						// rivFifthBossPhoto

						// ImageLoader.getInstance().displayImage(
						// interfaceReturn.getResults().get(0).getMember()
						// .getMemberPhoto(),
						// rivFifthBossPhoto,
						// MyApplication.getInstance().getOptions(
						// R.drawable.defaultimg));

						ImageLoaderUtil.loadXUtilImage(interfaceReturn
								.getResults().get(0).getMember()
								.getMemberPhoto(),
								worker_third_riv_member_photo);

						worker_third_name_tv.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());
						worker_third_complete_number_tv.setText(interfaceReturn
								.getResults().get(0).getMember().getDealNumber()+"");

						if (interfaceReturn.getResults().get(0).getMember()
								.isRealName()) {
							worker_third_certification_img
									.setVisibility(View.VISIBLE);
							worker_third_tv_certification.setText("已认证");
							worker_third_rl_certification.setEnabled(false);

						} else {
//							worker_third_certification_img
//									.setVisibility(View.GONE);
//							worker_third_tv_certification.setText("未认证");
//							worker_third_rl_certification.setEnabled(true);
							MyMemberAuthentication();
						}

						worker_third_total_assess_ratingbar
								.setRating((float) Double.parseDouble(Tools
										.formatString(interfaceReturn
												.getResults().get(0)
												.getMember().getBossLevel())));

						worker_third_assess_total_points_tv
								.setText(interfaceReturn.getResults().get(0)
										.getMember().getBossLevel()
										+ "分");

						worker_third_tv_score.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getIntegralNumber()
								+ "");

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

				/**
	 * 当前登录用户获取会员实名认证信息
	 */
	private void MyMemberAuthentication() {
		RequestParams params = new RequestParams(
				HttpUtil.MyMemberAuthentication);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MemberAuthentication> interfaceReturn = new InterfaceReturn<MemberAuthentication>();
				interfaceReturn = interfaceReturn.fromJson(result,
						MemberAuthentication.class);
				if (null != interfaceReturn && interfaceReturn.isStatus()) {
					switch (interfaceReturn.getResults().get(0).getInfoState()) {
					case 0:
						worker_third_tv_certification.setText("待审核");
						worker_third_certification_img.setVisibility(View.GONE);
						worker_third_rl_certification.setEnabled(false);
						break;
					case 1:

						break;
					case 2:
						worker_third_tv_certification.setText("证件照模糊");
						worker_third_certification_img.setVisibility(View.GONE);
						worker_third_rl_certification.setEnabled(true);
						break;
					case 3:
						worker_third_tv_certification.setText("身份证号错误");
						worker_third_certification_img.setVisibility(View.GONE);
						worker_third_rl_certification.setEnabled(true);
						break;

					}
				} else if (null != interfaceReturn && !interfaceReturn.isStatus()) {
					worker_third_tv_certification.setText("未认证");
					worker_third_certification_img.setVisibility(View.GONE);
					worker_third_rl_certification.setEnabled(true);
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
	 * 获取工人的干活次数
	 */
	private void MyFindWorkInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.MyFindWorkInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setCharset("utf-8");
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<FindWorkInfo> interfaceReturn = new InterfaceReturn<FindWorkInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfo.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
//						worker_third_complete_number_tv.setText(interfaceReturn
//								.getResults().get(0).getWorkCount()
//								+ "");
						if (interfaceReturn.getResults().get(0).getWorkState() == 2) {
							// 空闲
							worker_third_csb_push.setChecked(true);
						} else if (interfaceReturn.getResults().get(0)
								.getWorkState() == 1) {
							// 繁忙
							worker_third_csb_push.setChecked(false);
						}
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

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
		switch (v.getId()) {
		case R.id.worker_third_rl_title:
			startActivity(new Intent(getActivity(), MyInformationActivity.class));

			break;
		case R.id.worker_third_riv_member_photo:

			break;

		case R.id.worker_third_rl_show_porject:

			startActivity(new Intent(getActivity(),
					SunEngineeringActitvty.class));

			break;

		case R.id.worker_third_help_img:
			Intent intent = new Intent();
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", "http://m.5ifangshui.com/help/");

			getActivity().startActivity(intent);
			break;
		case R.id.worker_third_rl_certification:
			startActivity(new Intent(getActivity(),
					MemberAttestationActivity.class));
			break;
		case R.id.worker_third_rl_team:
			startActivity(new Intent(getActivity(),
					MyConstructionTeamActivity.class));
			break;
		case R.id.worker_third_rl_superiority:
			startActivity(new Intent(getActivity(), SkillApprove.class));
			break;
		case R.id.worker_third__rl_score:
			startActivity(new Intent(getActivity(), PointsActivity.class));
			break;
		case R.id.worker_third_rl_technical_support:
			startActivity(new Intent(getActivity(), ForumActivity.class)
					.putExtra("VIEWPAGE_S", 0));
			break;
		case R.id.worker_third_rl_share:
			startActivity(new Intent(getActivity(), MyShareActivity.class));
			break;

		case R.id.worker_third_rl_switch_identity:
			startActivity(new Intent(getActivity(), SelectRoleActivity.class));
			break;
		case R.id.worker_third_rl_set:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		getUserInfo();
		super.onResume();
	}
}
