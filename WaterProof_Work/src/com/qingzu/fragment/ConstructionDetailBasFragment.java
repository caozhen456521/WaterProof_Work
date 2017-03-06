package com.qingzu.fragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.City;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.ConstructionTeamListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ConstructionDetailActivity;
import com.qingzu.waterproof_work.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.JetPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工程队详情 基本信息
 * 
 * @author Administrator
 * 
 */
public class ConstructionDetailBasFragment extends Fragment implements
		OnClickListener {

	private View v = null;
	private TextView tvConstructionTeamName = null;// 施工队名称
	private TextView tvConstructionTeamBrower = null;// 浏览次数
	private TextView tvConstructionTeamNumber = null;// 队员人数
	private TextView tvConstructionTeamCreateTime = null;// 工程队创建时间
	private TextView tvConstructionTeamServiceCity = null;// 服务城市
	private TextView tvConstructionTeamDescription = null;// 实力描述
	private RoundImageView rivConstructionTeamPm = null;// 工长头像
	private TextView tvConstructionTeamPmNickname = null;// 工长昵称
	private RatingBar RatingConstructionTeamDetailAssess = null;// 小星星
	private TextView tvConstructionTeamPmPoints = null;// 评分
	private TextView tvConstructionTeamWorkingPlace = null;// 工作地点
	private Button btConstructionTeamMakingCalls = null;// 拨打电话
	private Button btChat=null;//聊天
	private String UserToken = null;
	private TextView tvConstructionteamCompleteNumber = null; // 接单次数
	private ConstructionTeam constructionTeam = null;
	// private ConstructionTeam constructionTeam;
	private Button construction_team_exit; // 退出施工队
	private SharedPreferences preferences = null;
	private String  chatUserName=null;
	private InterfaceReturn<ConstructionTeamListModels> interfaceReturn = new InterfaceReturn<ConstructionTeamListModels>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(
				R.layout.fragment_construction_team_detail_basic_info,
				container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		preferences = getActivity().getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");

		tvConstructionTeamName = (TextView) v
				.findViewById(R.id.construction_team_name_tv);
		tvConstructionTeamBrower = (TextView) v
				.findViewById(R.id.construction_team_brower_tv);
		tvConstructionTeamNumber = (TextView) v
				.findViewById(R.id.construction_team_number_tv);
		tvConstructionTeamCreateTime = (TextView) v
				.findViewById(R.id.construction_team_create_time_tv);
		tvConstructionTeamServiceCity = (TextView) v
				.findViewById(R.id.construction_team_service_city_tv);
		tvConstructionTeamDescription = (TextView) v
				.findViewById(R.id.construction_team_shili_description_tv);
		rivConstructionTeamPm = (RoundImageView) v
				.findViewById(R.id.riv_construction_team_pm_img);
		tvConstructionTeamPmNickname = (TextView) v
				.findViewById(R.id.construction_team_pm_nickname_tv);
		RatingConstructionTeamDetailAssess = (RatingBar) v
				.findViewById(R.id.construction_team_detail_assess_ratingbar);
		tvConstructionTeamPmPoints = (TextView) v
				.findViewById(R.id.construction_team_pm_points_tv);
		tvConstructionTeamWorkingPlace = (TextView) v
				.findViewById(R.id.construction_team_working_place_tv);
		btChat=(Button) v.findViewById(R.id.construction_team_chat_bt);

		// ImgConstructionTeamDetail = (ImageView) v
		// .findViewById(R.id.construction_team_detail_img);

		// ImgConstructionTeamDetail = (ImageView) v
		// .findViewById(R.id.construction_team_detail_img);

		construction_team_exit = (Button) v
				.findViewById(R.id.construction_team_exit);
		if (ConstructionDetailActivity.show.equals("show")) {
			construction_team_exit.setVisibility(View.VISIBLE);
		} else {
			construction_team_exit.setVisibility(View.GONE);
		}

		btConstructionTeamMakingCalls = (Button) v
				.findViewById(R.id.construction_team_making_calls_bt);
		tvConstructionteamCompleteNumber = (TextView) v
				.findViewById(R.id.construction_team_complete_number_tv);

		btConstructionTeamMakingCalls.setOnClickListener(this);
		construction_team_exit.setOnClickListener(this);
		getConstructionTeamByID(ConstructionDetailActivity.id);
		
		//聊天
		btChat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (JMessageClient.getMyInfo()!=null) {
					if (chatUserName!=null) {
						Tools.getUserInfo(chatUserName, getActivity());	
					}	
				}else {
					T.showToast(getActivity(), "未知错误,请重新登录");
				}
				
			}
		});
	}

	// "memberId": 1,
	// "teamId": 2,
	// "teamName": "sample string 3",
	// "typeId": 4,
	// "cause": "sample string 5"

	public void postLeaveTeamRecordByWorker() {
		RequestParams params = new RequestParams(
				HttpUtil.LeaveTeamRecordByWorker);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.addBodyParameter("memberId", preferences.getInt("MemberId", 0)
				+ "");

		params.addBodyParameter("typeId", "0");
		params.addBodyParameter("teamId", constructionTeam.getId() + "");
		params.addBodyParameter("teamName", constructionTeam.getTeamName());

		Xutils.Post(params, new MyCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(), "退出成功");

						// ListTeamMember(false, groupPosition);
					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
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
	 * 根据ID获取工程施工队信息
	 */
	private void getConstructionTeamByID(final String Id) {
		RequestParams params = new RequestParams(
				HttpUtil.ConstructionTeamByID.replace("{ID}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				super.onSuccess(result);
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeamListModels.class);
				constructionTeam = new ConstructionTeam();
				if (interfaceReturn != null) {

					if (interfaceReturn.isStatus()) {
						chatUserName= interfaceReturn.getResults().get(0).getMember().getUserName();
						constructionTeam = interfaceReturn.getResults().get(0)
								.getConstructionTeam();

						ImageLoader.getInstance().displayImage(
								interfaceReturn.getResults().get(0).getMember()
										.getMemberPhoto(),
								rivConstructionTeamPm,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));

						tvConstructionTeamName.setText(constructionTeam
								.getTeamName());
						tvConstructionTeamBrower.setText("浏览次数："
								+ constructionTeam.getBrowseCount() + "");
						tvConstructionTeamNumber.setText(constructionTeam
								.getAlreadyNumber() + "");
						tvConstructionTeamDescription.setText(constructionTeam
								.getTeamStrength() + "");
						tvConstructionTeamCreateTime
								.setText(dateToStrLong(strToDate(constructionTeam
										.getIssueTime())) + "");
						tvConstructionTeamPmNickname.setText(constructionTeam
								.getLeaderName() + "");
						ServeCityInfoByTeamId(Id);
						RatingConstructionTeamDetailAssess
								.setRating((float) Double.parseDouble(Tools
										.formatString(constructionTeam
												.getCreditStars())));
						tvConstructionTeamPmPoints.setText(Tools
								.formatString(constructionTeam.getCreditStars())

								+ "分");

						tvConstructionTeamWorkingPlace
								.setText((constructionTeam.getProvinceName()
										+ constructionTeam.getCityName()
										+ constructionTeam.getAreaName() + constructionTeam
											.getAddress()) + "");
						tvConstructionteamCompleteNumber.setText("接单次数："
								+ constructionTeam.getDealNumber() + "");
					}
				}
			}

			public String dateToStrLong(java.util.Date dateDate) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
				String dateString = formatter.format(dateDate);
				return dateString;
			}

			public java.util.Date strToDate(String strDate) {
				SimpleDateFormat formatter = new SimpleDateFormat(

				// "yyyy-MM-dd"
						"yyyy-MM-dd");
				ParsePosition pos = new ParsePosition(0);
				java.util.Date strtodate = formatter.parse(strDate, pos);
				return strtodate;
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				// TODO Auto-generated method stub
				super.onError(ex, isOnCallback);
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

	private void ServeCityInfoByTeamId(String TeamId) {
		RequestParams params = new RequestParams(
				HttpUtil.ServeCityInfoByTeamId.replace("{TeamId}", TeamId));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<City> interfaceReturn = new InterfaceReturn<City>();
				interfaceReturn = InterfaceReturn.fromJson(result, City.class);
				if (interfaceReturn.isStatus()) {
					String Citystr = "";
					for (int i = 0; i < interfaceReturn.getResults().size(); i++) {
						Citystr = Citystr
								+ interfaceReturn.getResults().get(i)
										.getRegionName() + "" + "  ";
					}
					tvConstructionTeamServiceCity.setText(Citystr);
				} else {

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
		if (constructionTeam != null) {
			switch (v.getId()) {
			case R.id.construction_team_making_calls_bt:

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ constructionTeam.getLeaderTel().trim()));
				startActivity(intent);

				break;

			case R.id.construction_team_exit:

				HintDialog.Builder builder = new HintDialog.Builder(
						getActivity());
				builder.setMessage("是否退出施工队 ?");
				builder.setTitle(R.string.Hint);
				builder.setNegativeButton(R.string.Yes,
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								postLeaveTeamRecordByWorker();
								dialog.dismiss();
								getActivity().finish();

							}
						});
				builder.setPositiveButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				builder.create().show();

				// Toast.makeText(getActivity(), "123",
				// Toast.LENGTH_SHORT).show();

				break;
			default:
				break;
			}
		} else {
			Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();

		}
	}
}
