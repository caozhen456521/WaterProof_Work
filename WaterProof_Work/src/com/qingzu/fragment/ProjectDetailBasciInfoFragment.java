package com.qingzu.fragment;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.RecruitModel;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.waterproof_work.ProjectDetailActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.RecruitDetailActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * 工程详情 页面
 * 
 * @author Administrator
 * 
 */
public class ProjectDetailBasciInfoFragment extends Fragment implements
		OnClickListener {

	private View v = null;

	private TextView tvProConstructionArea = null;// 施工面积
	private TextView tvChenbaoWays = null;// 承包方式
	private TextView tvProjectStartTime = null;// 工程开工时间
	private TextView tvProjectOtherRequest = null;// 其他要求
	private TextView tvProjectChuliState = null;// 处理状态
	private RoundImageView rivProjectContactImg = null;// 工程联系人的头像
	private TextView tvProjectContactName = null;// 联系人的昵称
	private TextView tvProjectReleaseNumber = null;// 发布次数
	private RatingBar ratingProjectAssess = null;// 小星星
	private TextView tvProjectPoints = null;// 分数
	private TextView tvProjectWorkingPlace = null;// 工作地点
	private ImageView imgProjectDetail = null;// 地图
	private Button btProjectCancelRelease = null;// 取消发布

	private String id;
	private String UserToken = null;
	private InterfaceReturn<ProjectModel> interfaceReturn = new InterfaceReturn<ProjectModel>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_project_detail_basic_info,
				container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub

		SharedPreferences preferences = getActivity().getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");

		tvProConstructionArea = (TextView) v
				.findViewById(R.id.project_detail_construction_area_tv);
		// tvProBrowerNumber=(TextView)
		// v.findViewById(R.id.project_detail_brower_number_tv);
		tvChenbaoWays = (TextView) v.findViewById(R.id.chenbao_ways_tv);
		tvProjectStartTime = (TextView) v
				.findViewById(R.id.project_cons_start_time_tv);
		tvProjectOtherRequest = (TextView) v
				.findViewById(R.id.project_cons_other_request_tv);
		tvProjectChuliState = (TextView) v
				.findViewById(R.id.project_chuli_state_tv);
		rivProjectContactImg = (RoundImageView) v
				.findViewById(R.id.riv_project_contact_img);
		tvProjectContactName = (TextView) v
				.findViewById(R.id.project_contact_name_tv);
		tvProjectReleaseNumber = (TextView) v
				.findViewById(R.id.project_release_number_tv);
		ratingProjectAssess = (RatingBar) v
				.findViewById(R.id.project_detail_assess_ratingbar);
		tvProjectPoints = (TextView) v.findViewById(R.id.project_points_tv);
		tvProjectWorkingPlace = (TextView) v
				.findViewById(R.id.project_detail_working_place_tv);
		imgProjectDetail = (ImageView) v.findViewById(R.id.project_detail_img);
		btProjectCancelRelease = (Button) v
				.findViewById(R.id.project_cancel_release_bt);

//		id = ProjectDetailActivity.id;
//		Intent intent = getIntent();
//		 id =  intent.getStringExtra("project");

		getMessage(id);

	}

	private void getMessage(String Id) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams(HttpUtil.ProjectInfo.replace(
				"{ID}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectModel.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						tvProConstructionArea.setText(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getBuildingArea());
						tvChenbaoWays.setText(interfaceReturn.getResults()
								.get(0).getProjectInfo().getContractMode());

						String starttime = dateToStrLong(strToDate(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getStartTime()));
						tvProjectStartTime.setText(starttime);
						tvProjectOtherRequest.setText(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getRemark());
						tvProjectChuliState.setText(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getInfoState());
						tvProjectContactName.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());
						ratingProjectAssess.setRating((float) Double
								.parseDouble(Tools.formatString(interfaceReturn
										.getResults().get(0).getMember()
										.getBossLevel())));
						
						tvProjectPoints.setText(interfaceReturn
										.getResults().get(0).getMember().getBossLevel()+"分");
						tvProjectReleaseNumber.setText(interfaceReturn
										.getResults().get(0).getBossInfo().getProjectCount()+"");
						tvProjectWorkingPlace.setText(interfaceReturn
										.getResults().get(0).getProjectInfo().getAddress());
						

					} else {
						T.showToast(getActivity(), interfaceReturn.getMessage());
					}
				}
			}

			/**
			 * 时间格式转换
			 * 
			 * @param dateDate
			 * @return
			 */

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

}
