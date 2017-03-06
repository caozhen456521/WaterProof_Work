package com.qingzu.fragment;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;



import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.BossInfo;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.LoginMember;
import com.qingzu.entity.Member;
import com.qingzu.entity.MemberAuthentication;
import com.qingzu.entity.MemberIntegralDetailed;
import com.qingzu.entity.ReturnBossInfoModel;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.ImageLoaderUtil;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ForumActivity;
import com.qingzu.waterproof_work.LoginActivity;
import com.qingzu.waterproof_work.MemberAttestationActivity;
import com.qingzu.waterproof_work.MyInformationActivity;
import com.qingzu.waterproof_work.MyShareActivity;
import com.qingzu.waterproof_work.PayingMembersActivity;
import com.qingzu.waterproof_work.PointsActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.SelectRoleActivity;
import com.qingzu.waterproof_work.SettingActivity;
import com.qingzu.waterproof_work.VipCenterActivity;
import com.qingzu.waterproof_work.WebViewActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老板第五
 * 
 * @author Johnson
 * 
 */
public class BossFifthFragment extends Fragment implements OnClickListener {
	private View v = null;
	private RelativeLayout rlFifthBossInfo = null;// 标题layout
	private RoundImageView rivFifthBossPhoto = null;// 头像
	// private ImageView imageView = null;
	private TextView tvFifthBossName = null;// 昵称
	private ImageView imgFifthBossVip = null;// vip图片
	private ImageView imgFifthBossCertifition = null;// 实名认证图片
	private RatingBar fifth_boss_total_ratingbar = null;// 小星星
	private TextView tvFifthBossPoints = null;// 信誉评分数
	private TextView tvFifthBossReleaseNumber = null;// 发布工程数
	private ImageView imgFifthBossHelp = null;// 帮助
	private TextView tvFifithBossPoints = null;
	private RelativeLayout rlFifthBossCertification = null;// 实名认证layout
	private RelativeLayout rlFifthBossPayVip = null;// 付费会员layout
	private RelativeLayout rlFifthBossPoints = null;// 积分layout
	private RelativeLayout rlFifthBosskingTechnology = null;// 问技术layout
	private RelativeLayout rlFifthBossShare = null;// 我要分享layout
	private RelativeLayout fifth_user_switch_identity_rl=null;//身份切换
	private RelativeLayout rlFifthBossSetting = null;// 设置layout
	private TextView tvFifthAuthentication = null;
	private int isPayingMembers;// 付费会员的状态（0不是，1是付10000元/年付费会员）

	private String ReleaseNumber = null;
	private int MemberId;
	private String UserToken = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_fifth, container, false);
		initView();

		return v;
	}

	private void initView() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		MemberId = preferences.getInt("MemberId", 0);

		rlFifthBossInfo = (RelativeLayout) v
				.findViewById(R.id.fifth_user_info_rl);
		rivFifthBossPhoto = (RoundImageView) v
				.findViewById(R.id.fifth_user_photo);
		tvFifthBossName = (TextView) v.findViewById(R.id.fifth_user_name);
		imgFifthBossVip = (ImageView) v.findViewById(R.id.fifth_user_vip_img);
		imgFifthBossCertifition = (ImageView) v
				.findViewById(R.id.fifth_user_certifition_img);
		fifth_boss_total_ratingbar = (RatingBar) v
				.findViewById(R.id.fifth_boss_total_ratingbar);
		tvFifthBossPoints = (TextView) v
				.findViewById(R.id.fifth_user_points_tv);
		tvFifthBossReleaseNumber = (TextView) v
				.findViewById(R.id.fifth_user_release_project_num_tv);
		imgFifthBossHelp = (ImageView) v.findViewById(R.id.fifth_user_help_img);
		rlFifthBossCertification = (RelativeLayout) v
				.findViewById(R.id.fifth_user_certification_rl);
		rlFifthBossPayVip = (RelativeLayout) v
				.findViewById(R.id.fifth_user_pay_vip_rl);
		rlFifthBossPoints = (RelativeLayout) v
				.findViewById(R.id.fifth_user_points_rl);
		rlFifthBosskingTechnology = (RelativeLayout) v
				.findViewById(R.id.fifth_user_asking_technology_rl);
		rlFifthBossShare = (RelativeLayout) v
				.findViewById(R.id.fifth_user_my_share_rl);
		fifth_user_switch_identity_rl=(RelativeLayout) v.findViewById(R.id.fifth_user_switch_identity_rl);
		rlFifthBossSetting = (RelativeLayout) v
				.findViewById(R.id.fifth_user_setting_rl);
		tvFifithBossPoints = (TextView) v
				.findViewById(R.id.fifith_boss_points_tv);
		tvFifthAuthentication = (TextView) v
				.findViewById(R.id.fifth_authentication_tv);

		// ReleaseNumber = ProjectDetailActivity.ReleaseNumber;
		// tvFifthBossReleaseNumber.setText(ReleaseNumber);

		if (Tools.isConnectingToInternet()) {
		//	BossInfoByMemberID(MemberId);
			getUserInfo();
		} else {
			T.showToast(getActivity(), "请检查网络");
		}

		rlFifthBossInfo.setOnClickListener(this);
		imgFifthBossHelp.setOnClickListener(this);
		rlFifthBossCertification.setOnClickListener(this);
		rlFifthBossPayVip.setOnClickListener(this);
		rlFifthBossPoints.setOnClickListener(this);
		rlFifthBosskingTechnology.setOnClickListener(this);
		rlFifthBossShare.setOnClickListener(this);
		fifth_user_switch_identity_rl.setOnClickListener(this);
		rlFifthBossSetting.setOnClickListener(this);

	}

	/**
	 * 获取老板的发布工程数
	 * 
	 * @param MemberId
	 */
//	private void BossInfoByMemberID(int MemberId) {
//		// TODO Auto-generated method stub
//
//		RequestParams params = new RequestParams(
//				HttpUtil.BossInfoByMemberID
//						.replace("{MemberID}", MemberId + ""));
//		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
//		params.setCharset("utf-8");
//		Xutils.Get(params, new MyCallBack<String>() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onSuccess(String result) {
//
//				InterfaceReturn<BossInfo> interfaceReturn = new InterfaceReturn<BossInfo>();
//				interfaceReturn = InterfaceReturn.fromJson(result,
//						BossInfo.class);
//				if (interfaceReturn != null) {
//					if (interfaceReturn.isStatus()) {
//						String str = interfaceReturn.getResults().get(0)
//								.getProjectCount()
//								+ "";
//
//						tvFifthBossReleaseNumber.setText(interfaceReturn
//								.getResults().get(0)
//								.getProjectCount()
//								+ "");
//
//					} else {
//						T.showToast(getActivity(), interfaceReturn.getMessage());
//					}
//				}
//			}
//
//			public void onError(Throwable ex, boolean isOnCallback) {
//				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG)
//						.show();
//				if (ex instanceof HttpException) { // 网络错误
//					HttpException httpEx = (HttpException) ex;
//					int responseCode = httpEx.getCode();
//					String responseMsg = httpEx.getMessage();
//					String errorResult = httpEx.getResult();
//					System.out.println("responseMsg:" + responseMsg
//							+ "=====errorResult:" + errorResult
//							+ "=====responseCode" + responseCode);
//					// ...
//				} else { // 其他错误
//					// ...
//				}
//			}
//		});
//
//	}

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

						// ImageLoaderUtil.loadXUtilImage(interfaceReturn
						// .getResults().get(0).getMember()
						// .getMemberPhoto(), rivFifthBossPhoto);
						ImageLoader.getInstance().displayImage(
								interfaceReturn.getResults().get(0).getMember()
										.getMemberPhoto(),
								rivFifthBossPhoto,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

						tvFifthBossName.setText(interfaceReturn.getResults()
								.get(0).getMember().getNickName());
						tvFifthBossReleaseNumber.setText(interfaceReturn.getResults()
								.get(0).getMember().getPushCount()+"");

						if (interfaceReturn.getResults().get(0).getMember()
								.getIsPayingMembers() == 1) {
							imgFifthBossVip.setVisibility(View.VISIBLE);
						} else if (interfaceReturn.getResults().get(0)
								.getMember().getIsPayingMembers() == 0) {
							imgFifthBossVip.setVisibility(View.GONE);
						}
						if (interfaceReturn.getResults().get(0).getMember()
								.isRealName()) {
							imgFifthBossCertifition.setVisibility(View.VISIBLE);
							tvFifthAuthentication.setText("已认证");
							rlFifthBossCertification.setEnabled(false);
						} else {
							MyMemberAuthentication();

						}

						fifth_boss_total_ratingbar.setRating((float) Double
								.parseDouble(Tools.formatString(interfaceReturn
										.getResults().get(0).getMember()
										.getBossLevel())));

						tvFifthBossPoints.setText(interfaceReturn.getResults()
								.get(0).getMember().getBossLevel()
								+ "分");

						tvFifithBossPoints.setText(interfaceReturn.getResults()
								.get(0).getMember().getIntegralNumber()
								+ "");

						isPayingMembers = interfaceReturn.getResults().get(0)
								.getMember().getIsPayingMembers();

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
						tvFifthAuthentication.setText("待审核");
						imgFifthBossCertifition.setVisibility(View.GONE);
						rlFifthBossCertification.setEnabled(false);
						break;
					case 1:

						break;
					case 2:
						tvFifthAuthentication.setText("证件照模糊");
						imgFifthBossCertifition.setVisibility(View.GONE);
						rlFifthBossCertification.setEnabled(true);
						break;
					case 3:
						tvFifthAuthentication.setText("身份证号错误");
						imgFifthBossCertifition.setVisibility(View.GONE);
						rlFifthBossCertification.setEnabled(true);
						break;

					}
				} else if (null != interfaceReturn
						&& !interfaceReturn.isStatus()) {
					tvFifthAuthentication.setText("未认证");
					imgFifthBossCertifition.setVisibility(View.GONE);
					rlFifthBossCertification.setEnabled(true);
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
		switch (v.getId()) {
		case R.id.fifth_user_info_rl:
			startActivity(new Intent(getActivity(), MyInformationActivity.class));
			break;
		case R.id.fifth_user_help_img:
			Intent intent = new Intent();
			intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("url", "http://m.5ifangshui.com/help/");
			getActivity().startActivity(intent);
			break;
		case R.id.fifth_user_certification_rl:
			startActivity(new Intent(getActivity(),
					MemberAttestationActivity.class));
			break;
		case R.id.fifth_user_pay_vip_rl:

//			startActivity(new Intent(getActivity(), VipCenterActivity.class));

			 if(isPayingMembers==0){
			 startActivity(new Intent(getActivity(),
			 PayingMembersActivity.class));
			 }else if(isPayingMembers==1){
			 startActivity(new Intent(getActivity(),
			 VipCenterActivity.class));
			 }

			break;
			//测试聊天
		case R.id.fifth_user_points_rl:
		//	startActivity(new Intent(getActivity(), AboutActivity.class));
			startActivity(new Intent(getActivity(), PointsActivity.class));
			break;
		case R.id.fifth_user_asking_technology_rl:
			startActivity(new Intent(getActivity(), ForumActivity.class)
					.putExtra("VIEWPAGE_S", 0));
			break;
		case R.id.fifth_user_my_share_rl:
			startActivity(new Intent(getActivity(), MyShareActivity.class));
			break;
			
		case R.id.fifth_user_switch_identity_rl:
			startActivity(new Intent(getActivity(), SelectRoleActivity.class));
			break;
		case R.id.fifth_user_setting_rl:
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