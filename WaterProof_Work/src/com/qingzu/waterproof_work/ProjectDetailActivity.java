package com.qingzu.waterproof_work;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import cn.jpush.im.android.api.JMessageClient;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qingzu.adapter.ProjectDetailPagerAdapter;
import com.qingzu.adapter.RecruitDetailPagerAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProjectInfo;
import com.qingzu.entity.ProjectModel;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends Activity implements OnClickListener {

	private MyTitleView mtvProjectDetailTitle = null;// 工程详情标题
	private TextView tvProConstructionArea = null;// 施工面积
	private TextView tvChenbaoWays = null;// 承包方式
	private TextView tvProjectStartTime = null;// 工程开工时间
	private TextView tvProjectOtherRequest = null;// 其他要求
	private TextView tvProjectChuliState = null;// 处理状态
	private RoundImageView rivProjectContactImg = null;// 工程联系人的头像
	private TextView tvProjectContactName = null;// 联系人的昵称
	private TextView tvProjectReleaseNumber = null;// 发布次数
//	private Button project_chat_bt=null;//聊天
	private RatingBar ratingProjectAssess = null;// 小星星
	private TextView tvProjectPoints = null;// 分数
	private TextView tvProjectWorkingPlace = null;// 工作地点
	private ImageView imgProjectDetail = null;// 地图
	private Button btProjectCancelRelease = null;// 取消发布
	private ProjectInfo projectInfo = null;
	public static String ReleaseNumber;
    private String lol=null;
	private String id = null;
	private String UserToken = null;
	private InterfaceReturn<ProjectModel> interfaceReturn = new InterfaceReturn<ProjectModel>();
	private String  chatUserName=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		AppManager.getAppManager().addActivity(this);
		initView();// 实例化控件
	}

	private void initView() {
		// TODO Auto-generated method stub、
		mtvProjectDetailTitle = (MyTitleView) findViewById(R.id.project_detail_title);
		SharedPreferences preferences = ProjectDetailActivity.this
				.getSharedPreferences("UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");

		tvProConstructionArea = (TextView) findViewById(R.id.project_detail_construction_area_tv);
		// tvProBrowerNumber=(TextView)
		// v.findViewById(R.id.project_detail_brower_number_tv);
		tvChenbaoWays = (TextView) findViewById(R.id.chenbao_ways_tv);
		tvProjectStartTime = (TextView) findViewById(R.id.project_cons_start_time_tv);
		tvProjectOtherRequest = (TextView) findViewById(R.id.project_cons_other_request_tv);
		tvProjectChuliState = (TextView) findViewById(R.id.project_chuli_state_tv);
		rivProjectContactImg = (RoundImageView) findViewById(R.id.riv_project_contact_img);
		tvProjectContactName = (TextView) findViewById(R.id.project_contact_name_tv);
		tvProjectReleaseNumber = (TextView) findViewById(R.id.project_release_number_tv);
		ratingProjectAssess = (RatingBar) findViewById(R.id.project_detail_assess_ratingbar);
		tvProjectPoints = (TextView) findViewById(R.id.project_points_tv);
		tvProjectWorkingPlace = (TextView) findViewById(R.id.project_detail_working_place_tv);
		imgProjectDetail = (ImageView) findViewById(R.id.project_detail_img);
		btProjectCancelRelease = (Button) findViewById(R.id.project_cancel_release_bt);
//		project_chat_bt=(Button) findViewById(R.id.project_chat_bt);

		Intent intent = getIntent();
		id = intent.getStringExtra("project");
//		lol=intent.getStringExtra("lol");
		
	
		getMessage(id);
		

		imgProjectDetail.setOnClickListener(this);
		btProjectCancelRelease.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				UpdateProjectInfoState(id);
				
			}

			
		});
		
//		//聊天
//		project_chat_bt.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (JMessageClient.getMyInfo()!=null) {
//					if (chatUserName!=null) {
//						Tools.getUserInfo(chatUserName, ProjectDetailActivity.this);	
//					}	else {
//						T.showToast(ProjectDetailActivity.this, "未知错误,请重新登录");
//					}
//				}
//			}
//		});

		mtvProjectDetailTitle.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	
	/**
	 * 根据防水工程项目ID取消发布项目
	 * @param Id
	 */
	
	private void UpdateProjectInfoState(String Id) {
		// TODO Auto-generated method stub
		
		
		RequestParams params = new RequestParams(HttpUtil.UpdateProjectInfoState.replace(
				"{ID}", Id));
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				
				InterfaceReturn interfaceReturn = new InterfaceReturn();
				interfaceReturn = InterfaceReturn.fromJsonModel(result);
				
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						
						T.showToast(ProjectDetailActivity.this,
								interfaceReturn.getMessage());
						finish();
					
					} else {
						T.showToast(ProjectDetailActivity.this,
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
		
	}
	
	
	/**
	 * 获取防水工程项目详情
	 * @param Id
	 */
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
				projectInfo = new ProjectInfo();
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						chatUserName= interfaceReturn.getResults().get(0).getMember().getUserName();
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
						
						
						ImageLoader.getInstance().displayImage(
								interfaceReturn.getResults().get(0).getMember()
										.getMemberPhoto(),
										rivProjectContactImg,
								MyApplication.getInstance().getOptions(
										R.drawable.defa));
						
						
						if(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getInfoState().equals("0")){
							tvProjectChuliState.setText("正在审核中");
							btProjectCancelRelease.setVisibility(View.VISIBLE);
							
						}else if(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getInfoState().equals("1")){
							
							tvProjectChuliState.setText("审核通过");
						}else if(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getInfoState().equals("2")){
							tvProjectChuliState.setText("对接成功");
							
						}else if(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getInfoState().equals("3")){
							tvProjectChuliState.setText("已完成");
							
						}
						
						
						tvProjectContactName.setText(interfaceReturn
								.getResults().get(0).getMember().getNickName());
						ratingProjectAssess.setRating((float) Double
								.parseDouble(Tools.formatString(interfaceReturn
										.getResults().get(0).getMember()
										.getBossLevel())));

						tvProjectPoints.setText(interfaceReturn.getResults()
								.get(0).getMember().getBossLevel()
								+ "分");
						tvProjectReleaseNumber.setText(interfaceReturn
								.getResults().get(0).getBossInfo()
								.getProjectCount()
								+ "");

						ReleaseNumber = interfaceReturn.getResults().get(0)
								.getBossInfo().getProjectCount()
								+ "";
						tvProjectWorkingPlace.setText(interfaceReturn
								.getResults().get(0).getProjectInfo()
								.getAddress());
						projectInfo = interfaceReturn.getResults().get(0)
								.getProjectInfo();

						String sjhu=interfaceReturn.getResults().get(0).getProjectInfo().getLat();
						String hyo=interfaceReturn.getResults().get(0).getProjectInfo()
								.getLon();
						
						
						if (!interfaceReturn.getResults().get(0).getProjectInfo().getLat()
								.equals("0.0")
								|| !interfaceReturn.getResults().get(0).getProjectInfo()
										.getLon().equals("0.0")) {
							
							imgProjectDetail.setVisibility(View.VISIBLE);
						}
					} else {
						T.showToast(ProjectDetailActivity.this,
								interfaceReturn.getMessage());
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
		switch (v.getId()) {
		case R.id.project_detail_img:
			RecruitmentInfo recruitmentInfo =new RecruitmentInfo();
			recruitmentInfo.setLat(projectInfo.getLat());
			recruitmentInfo.setLon(projectInfo.getLon());
			recruitmentInfo.setAddress(projectInfo.getAddress());
			Intent intent = new Intent(ProjectDetailActivity.this,
					ShowMapAddressActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(SelectedPositionActivity.SEARCH, recruitmentInfo);
//			intent.putExtra("type", SelectedPositionActivity.SEARCH_1);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
