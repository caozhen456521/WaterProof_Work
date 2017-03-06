package com.qingzu.fragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.FindWorkInfoListModels;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Order;
import com.qingzu.entity.ProjectInfo;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.FindWorkerActivity;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;
import com.qingzu.waterproof_work.WorkerDetailActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工人详情（基本信息）
 * 
 * @author Administrator
 * 
 */
public class WorkerDetailBasciInfoFragment extends Fragment implements
		OnClickListener {
	private View v = null;
	private TextView find_worker_skill_tv = null;// 专业技能
	private TextView worker_whether_insurance_tv = null;// 是否有保险
	private TextView worker_cetification_tv = null;// 有无技工证
	private RoundImageView riv_worker_img = null;// 工人头像
	private TextView worker_contact_name_tv = null;// 联系人
	private TextView worker_jiedan_number_tv = null;// 接单次数
	private RelativeLayout worker_info_rl=null;
	private Button worker_chat_bt=null;//聊天
	private RatingBar worker_detail_assess_ratingbar = null;// 小星星
	private TextView worker_points_tv = null;// 分数
	private TextView worker_working_place_tv = null;// 工作地点
	private Button call_phone_bt = null;// 拨打电话
	private String UserToken = null;
	private String id;
	private FindWorkInfoListModels itemS;
	private String UserName = null;
	private int MemberId;
	private String  chatUserName=null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_worker_detail_basic_info,
				container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		UserName = preferences.getString("UserName", "");
		MemberId = preferences.getInt("MemberId", 0);
		find_worker_skill_tv = (TextView) v
				.findViewById(R.id.find_worker_skill_tv);
		worker_whether_insurance_tv = (TextView) v
				.findViewById(R.id.worker_whether_insurance_tv);
		worker_cetification_tv = (TextView) v
				.findViewById(R.id.worker_cetification_tv);
		riv_worker_img = (RoundImageView) v.findViewById(R.id.riv_worker_img);
		worker_contact_name_tv = (TextView) v
				.findViewById(R.id.worker_contact_name_tv);
		worker_jiedan_number_tv = (TextView) v
				.findViewById(R.id.worker_jiedan_number_tv);
		worker_info_rl=(RelativeLayout) v.findViewById(R.id.worker_info_rl);
		worker_chat_bt=(Button) v.findViewById(R.id.worker_chat_bt);
		worker_detail_assess_ratingbar = (RatingBar) v
				.findViewById(R.id.worker_detail_assess_ratingbar);
		worker_points_tv = (TextView) v.findViewById(R.id.worker_points_tv);
		worker_working_place_tv = (TextView) v
				.findViewById(R.id.worker_working_place_tv);
		call_phone_bt = (Button) v.findViewById(R.id.call_phone_bt);

		id = WorkerDetailActivity.id;
		getMessage(id);

		call_phone_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callPhone(itemS);
			}
		});
		
		worker_info_rl.setOnClickListener(new OnClickListener() {
			
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
		
		//聊天
		worker_chat_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (JMessageClient.getMyInfo()!=null) {
					if (chatUserName!=null) {
						Tools.getUserInfo(chatUserName, getActivity());	
					}	else {
						T.showToast(getActivity(), "未知错误,请重新登录");
					}
				}
			}
		});
	}

	/**
	 * 工人详情基本信息
	 * 
	 * @param MemberId
	 */
	private void getMessage(String MemberId) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.WorkerDetail.replace(
				"{MemberId}", MemberId));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn<FindWorkInfoListModels> interfaceReturn = new InterfaceReturn<FindWorkInfoListModels>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						FindWorkInfoListModels.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						chatUserName= interfaceReturn.getResults().get(0).getMember().getUserName();
						itemS = interfaceReturn.getResults().get(0);
						find_worker_skill_tv.setText(interfaceReturn
								.getResults().get(0).getFindWorkInfo()
								.getSkill());
						if (interfaceReturn.getResults().get(0)
								.getFindWorkInfo().getIsInsurance()) {
							worker_whether_insurance_tv.setText("有");
						} else {
							worker_whether_insurance_tv.setText("无");
						}

						if (interfaceReturn.getResults().get(0)
								.getFindWorkInfo().getIsCertificate()) {
							worker_cetification_tv.setText("有");
						} else {
							worker_cetification_tv.setText("无");
						}

						ImageLoader.getInstance().displayImage(
								interfaceReturn.getResults().get(0).getMember()
										.getMemberPhoto(),
								riv_worker_img,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
						worker_contact_name_tv.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getNickName());

						worker_jiedan_number_tv.setText(interfaceReturn
								.getResults().get(0).getFindWorkInfo()
								.getWorkCount()
								+ "");
						worker_detail_assess_ratingbar.setRating((float) Double
								.parseDouble(Tools.formatString(interfaceReturn
										.getResults().get(0).getMember()
										.getWorkerLevel())));

						worker_points_tv.setText(interfaceReturn.getResults()
								.get(0).getMember().getWorkerLevel()
								+ "分");
						worker_working_place_tv.setText(interfaceReturn
								.getResults().get(0).getMember()
								.getContactAddress());

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

	private void callPhone(final FindWorkInfoListModels item) {
		HintDialog.Builder builder = new HintDialog.Builder(getActivity());
		// builder.setMessage("该用户的手机号是" +
		// Mobile.substring(0,
		// 3) +
		// "****"
		// + Mobile.substring(7, 11) + "是否拔打");
		builder.setMessage("确认拨打电话?"
		// + "(如有异常,手动拨打电话:4000163235+分机号:"
		// + extensionNumber.getNumber().trim()
		// + ")"
		);
		builder.setTitle(R.string.Hint);
		builder.setNegativeButton(R.string.Yes,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (Tools.isConnectingToInternet()) {

							String str = "tel:"
									+ item.getFindWorkInfo().getMemberName()
											.trim();
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse(str));
							startActivityForResult(intent, 123);
							// State = 1; // 判断作用
						} else {
							T.showToast(getActivity(), "请检查网络");
						}
						dialog.dismiss();
					}
				});
		builder.setPositiveButton(R.string.No,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 123) {
			HintDialog.Builder builder = new HintDialog.Builder(
					getActivity());
			builder.setMessage("是否谈好成交 ?");
			builder.setTitle(R.string.Hint);
			builder.setNegativeButton(R.string.Yes,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Order order = new Order();
							order.setInfoId(Integer.parseInt(WorkerDetailActivity.infoId));
							order.setFromTelephone(UserName);
							order.setToTelephone(itemS.getMember()
									.getContactTel());
							order.setMemberId(MemberId);
							order.setToMemberId(itemS.getMember().getId());
							CreateInfoOrderByLeader(new Gson().toJson(order));
							// State = 0;
							dialog.dismiss();

						}
					});
			builder.setPositiveButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// State = 0;
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}
	
	private void CreateInfoOrderByLeader(String json) {

		RequestParams params = new RequestParams(
				HttpUtil.CreateInfoOrderByLeader);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {

				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(),
								interfaceReturn.getMessage());
						itemS = null;
					} else {
						T.showToast(getActivity(),
								interfaceReturn.getMessage());
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

	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
