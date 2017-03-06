package com.qingzu.fragment;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.custom.control.CheckSwitchButton;
import com.qingzu.entity.FindWorkInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.MemberAuthentication;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ForumActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity;
import com.qingzu.waterproof_work.MyInformationActivity;
import com.qingzu.waterproof_work.MyShareActivity;
import com.qingzu.waterproof_work.PointsActivity;
import com.qingzu.waterproof_work.ProjectPushActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.SelectRoleActivity;
import com.qingzu.waterproof_work.SettingActivity;
import com.qingzu.waterproof_work.SunEngineeringActitvty;
import com.qingzu.waterproof_work.WebViewActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工长第四Fragment
 * 
 * @author Johnson
 * 
 */
public class CaptainFourthFragment extends Fragment implements OnClickListener {

	private RoundImageView fourth_riv_member_photo = null;// 头像
	private TextView fourth_tv_member_name = null;// 用户名
	private ImageView fourth_iv_vpi = null;// vpi
	private ImageView fourth_iv_attestation = null;// 认证
	private RatingBar fourth_rb_member_rate = null;// 评分
	private TextView fourth_tv_member_rate = null;// 评分
	private TextView fourth_tv_order_number = null;// 下单次数
	private ImageView fourth_iv_help = null;// 帮助
	private RelativeLayout fourth_rl_title = null;// 标题layout
	private RelativeLayout fourth_rl_attestation = null;// 实名认证
	private TextView fourth_tv_attestation = null;// 认证状态
	private RelativeLayout fourth_rl_project_push = null;// 工程推送
	private RelativeLayout fourth_rl_show_porject = null;// 晒工程
	private RelativeLayout fourth_rl_score = null;// 积分
	private TextView fourth_tv_score = null;// 积分
	private RelativeLayout fourth_rl_technical_support = null;// 问技术
	private RelativeLayout fourth_rl_share = null;// 我要分享
	private RelativeLayout fourth_rl_switch_identity = null;// 身份切换
	private RelativeLayout fourth_rl_set = null;// 设置
	private CheckSwitchButton fourth_csb_push = null;// 免打扰SBT
	private boolean firstS = true;

	private String UserToken = null;

	private View v = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_fourth, container, false);
		initView();
		return v;
	}

	private void initView() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		fourth_riv_member_photo = (RoundImageView) v
				.findViewById(R.id.fourth_riv_member_photo);
		fourth_tv_member_name = (TextView) v
				.findViewById(R.id.fourth_tv_member_name);
		fourth_iv_vpi = (ImageView) v.findViewById(R.id.fourth_iv_vpi);
		fourth_iv_attestation = (ImageView) v
				.findViewById(R.id.fourth_iv_attestation);
		fourth_rb_member_rate = (RatingBar) v
				.findViewById(R.id.fourth_rb_member_rate);
		fourth_tv_member_rate = (TextView) v
				.findViewById(R.id.fourth_tv_member_rate);
		fourth_tv_order_number = (TextView) v
				.findViewById(R.id.fourth_tv_order_number);
		fourth_iv_help = (ImageView) v.findViewById(R.id.fourth_iv_help);
		fourth_rl_title = (RelativeLayout) v.findViewById(R.id.fourth_rl_title);
		fourth_rl_attestation = (RelativeLayout) v
				.findViewById(R.id.fourth_rl_attestation);
		fourth_tv_attestation = (TextView) v
				.findViewById(R.id.fourth_tv_attestation);
		fourth_rl_project_push = (RelativeLayout) v
				.findViewById(R.id.fourth_rl_project_push);
		fourth_rl_show_porject = (RelativeLayout) v
				.findViewById(R.id.fourth_rl_show_porject);
		fourth_rl_score = (RelativeLayout) v.findViewById(R.id.fourth_rl_score);
		fourth_tv_score = (TextView) v.findViewById(R.id.fourth_tv_score);
		fourth_rl_technical_support = (RelativeLayout) v
				.findViewById(R.id.fourth_rl_technical_support);
		fourth_rl_share = (RelativeLayout) v.findViewById(R.id.fourth_rl_share);
		fourth_rl_switch_identity = (RelativeLayout) v
				.findViewById(R.id.fourth_rl_switch_identity);
		fourth_rl_set = (RelativeLayout) v.findViewById(R.id.fourth_rl_set);
		fourth_csb_push = (CheckSwitchButton) v
				.findViewById(R.id.fourth_csb_push);

		fourth_csb_push
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							UpdateFindWorkState("2");
						} else {
							UpdateFindWorkState("1");
						}
					}

				});

		fourth_riv_member_photo.setOnClickListener(this);
		fourth_iv_help.setOnClickListener(this);
		fourth_rl_title.setOnClickListener(this);
		fourth_rl_attestation.setOnClickListener(this);
		fourth_rl_project_push.setOnClickListener(this);
		fourth_rl_show_porject.setOnClickListener(this);
		fourth_rl_score.setOnClickListener(this);
		fourth_rl_technical_support.setOnClickListener(this);
		fourth_rl_share.setOnClickListener(this);
		fourth_rl_switch_identity.setOnClickListener(this);
		fourth_rl_set.setOnClickListener(this);
		firstS = false;
		if (Tools.isConnectingToInternet()) {
			UserInfo();
			MyFindWorkInfo();
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

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
	 * 当前登录用户获取用工次数
	 */
	private void MyFindWorkInfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.MyFindWorkInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<FindWorkInfo> interfaceReturn = new InterfaceReturn<FindWorkInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfo.class);
				if (interfaceReturn.isStatus()) {

//					fourth_tv_order_number.setText("干活次数:"
//							+ interfaceReturn.getResults().get(0)
//									.getWorkCount());
					if (interfaceReturn.getResults().get(0).getWorkState() == 2) {
						// 空闲
						fourth_csb_push.setChecked(true);
					} else if (interfaceReturn.getResults().get(0)
							.getWorkState() == 1) {
						// 繁忙
						fourth_csb_push.setChecked(false);
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
	 * 当前登录用户获取自己的用户信息
	 * 
	 * @author Johnson
	 */
	private void UserInfo() {
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<MemberModel> interfaceReturn = new InterfaceReturn<CaptainFourthFragment.MemberModel>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						MemberModel.class);
				if (interfaceReturn.isStatus()) {
					ImageLoader.getInstance().displayImage(
							interfaceReturn.getResults().get(0).getMember()
									.getMemberPhoto(),
							fourth_riv_member_photo,
							MyApplication.getInstance().getOptions(
									R.drawable.defa));
					if (interfaceReturn.getResults().get(0).getMember()
							.getNickName().length() > 0) {
						fourth_tv_member_name.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());
					} else {
						fourth_tv_member_name.setText(interfaceReturn
								.getResults().get(0).getMember().getUserName());
					}
					if (interfaceReturn.getResults().get(0).getMember()
							.getIsPayingMembers() == 1) {
						fourth_iv_vpi.setVisibility(View.VISIBLE);
					} else {
						fourth_iv_vpi.setVisibility(View.GONE);
					}
					if (interfaceReturn.getResults().get(0).getMember()
							.isRealName()) {
						fourth_iv_attestation.setVisibility(View.VISIBLE);
						fourth_tv_attestation.setText("已认证");
						fourth_rl_attestation.setEnabled(false);
					} else {
//						fourth_iv_attestation.setVisibility(View.GONE);
//						fourth_tv_attestation.setText("未认证");
//						fourth_rl_attestation.setEnabled(true);
						MyMemberAuthentication();
						
					}
					fourth_rb_member_rate.setRating((float) interfaceReturn
							.getResults().get(0).getMember().getWorkerLevel());
					fourth_tv_member_rate.setText(interfaceReturn.getResults()
							.get(0).getMember().getWorkerLevel()
							+ "分");
					 fourth_tv_order_number.setText("接单次数:"
					 + interfaceReturn.getResults().get(0).getMember()
					 .getDealNumber());
					fourth_tv_score.setText(interfaceReturn.getResults().get(0)
							.getMember().getIntegralNumber()
							+ "");

					SharedPreferences sharedPreferences = getActivity()
							.getSharedPreferences("UserToken",
									Activity.MODE_PRIVATE);
					Editor edit = sharedPreferences.edit();
					edit.putInt("MemberId", interfaceReturn.getResults().get(0)
							.getMember().getId());
					edit.putString("Phone", interfaceReturn.getResults().get(0)
							.getMember().getContactTel());
					edit.putString("UserName", interfaceReturn.getResults()
							.get(0).getMember().getUserName());
					edit.putString("UserPhoto", interfaceReturn.getResults()
							.get(0).getMember().getMemberPhoto());
					edit.putInt("IsCheck", interfaceReturn.getResults().get(0)
							.getMember().getIsCheck());
					edit.putBoolean("IsRealName", interfaceReturn.getResults()
							.get(0).getMember().isRealName());
					edit.putString("NickName", interfaceReturn.getResults()
							.get(0).getMember().getNickName());
					edit.putInt("IntegralNumber", interfaceReturn.getResults()
							.get(0).getMember().getIntegralNumber());
					if (interfaceReturn.getResults().get(0).getMember()
							.getDefaultRoleId() == 1) {
						edit.putInt("identity", 2);
					} else if (interfaceReturn.getResults().get(0).getMember()
							.getDefaultRoleId() == 2) {
						edit.putInt("identity", 0);
					} else if (interfaceReturn.getResults().get(0).getMember()
							.getDefaultRoleId() == 6) {
						edit.putInt("identity", 1);
					}

					edit.commit();

				} else {

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
						fourth_tv_attestation.setText("待审核");
						fourth_iv_attestation.setVisibility(View.GONE);
						fourth_rl_attestation.setEnabled(false);
						break;
					case 1:

						break;
					case 2:
						fourth_tv_attestation.setText("证件照模糊");
						fourth_iv_attestation.setVisibility(View.GONE);
						fourth_rl_attestation.setEnabled(true);
						break;
					case 3:
						fourth_tv_attestation.setText("身份证号错误");
						fourth_iv_attestation.setVisibility(View.GONE);
						fourth_rl_attestation.setEnabled(true);
						break;

					}
				} else if (null != interfaceReturn && !interfaceReturn.isStatus()) {
					fourth_tv_attestation.setText("未认证");
					fourth_iv_attestation.setVisibility(View.GONE);
					fourth_rl_attestation.setEnabled(true);
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
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fourth_riv_member_photo:

			break;
		case R.id.fourth_iv_help:
			Intent intent = new Intent();
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", "http://m.5ifangshui.com/help/");

			getActivity().startActivity(intent);
			break;
		case R.id.fourth_rl_title:
			startActivity(new Intent(getActivity(), MyInformationActivity.class));
			break;
		case R.id.fourth_rl_attestation:
			startActivity(new Intent(getActivity(),
					MemberAttestationActivity.class));
			break;
		case R.id.fourth_rl_project_push:// 工程推送
			startActivity(new Intent(getActivity(), ProjectPushActivity.class));
			break;
		case R.id.fourth_rl_show_porject:
			startActivity(new Intent(getActivity(),
					SunEngineeringActitvty.class));

			break;
		case R.id.fourth_rl_score:
			startActivity(new Intent(getActivity(), PointsActivity.class));
			break;
		case R.id.fourth_rl_technical_support:
			startActivity(new Intent(getActivity(), ForumActivity.class)
					.putExtra("VIEWPAGE_S", 0));
			break;
		case R.id.fourth_rl_share:
			// 分享
			startActivity(new Intent(getActivity(), MyShareActivity.class));
			break;

		case R.id.fourth_rl_switch_identity:
			startActivity(new Intent(getActivity(), SelectRoleActivity.class));
			break;
		case R.id.fourth_rl_set:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		if (!firstS) {
			if (Tools.isConnectingToInternet()) {
				UserInfo();
				MyFindWorkInfo();
			} else {
				T.showToast(getActivity(), "请检查网络");
			}
		}
		super.onResume();
	}

}