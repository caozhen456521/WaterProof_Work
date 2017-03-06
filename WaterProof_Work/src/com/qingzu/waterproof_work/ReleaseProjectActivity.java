package com.qingzu.waterproof_work;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.AddressItem;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.Member;
import com.qingzu.entity.ProjectInfo;
import com.qingzu.entity.RecruitDayWorker;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.WheelViewActivity;
import com.qingzu.wheel.widget.OnWheelChangedListener;
import com.qingzu.wheel.widget.WheelView;
import com.qingzu.wheel.widget.adapters.ArrayWheelAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 发布工程
 * 
 * @author Administrator
 * 
 */
public class ReleaseProjectActivity extends WheelViewActivity implements
		OnClickListener, OnWheelChangedListener {

	private MyTitleView mtvReleaseProjectTitle = null;// 发布工程标题
	private EditText etProjectName = null;// 填写项目名称
	private EditText etConstructionArea = null;// 填写建筑面积
	private TextView tvSelectChengBaoWays = null;// 选择承包方式
	private LinearLayout llSelectChengBaoWays = null;// 选择承包方式布局
	private TextView tvSelectStartTime = null;// 开工时间
	private LinearLayout llSelectStartTime = null;// 开工时间布局
	private TextView tvSelectConstructionPlace = null;// 选择施工地点
	private LinearLayout llSelectConstructionPlace = null;// 选择施工地点布局
	private TextView tvDetailAddress = null;// 定位详细地址
	private LinearLayout llDetailAddress = null;// 定位详细地址布局
	private EditText etContactName = null;// 填写联系人
	private EditText ContactPhone = null;// 填写联系电话
	private EditText etOtherRequest = null;// 填写其他要求
	private Button btReleaseInformation = null;// 发布消息按钮
	private WheelMain wheelMain = null;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private String UserToken = null;
	private ProjectInfo projectInfo = null;
	private boolean IsRealName = false;

	private String ContactTel = null;
	private String NickName = null;
	private String from = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SharedPreferences preferences = null;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release_project);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		preferences = ReleaseProjectActivity.this.getSharedPreferences(
				"UserToken", Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		ContactTel = preferences.getString("Phone", "");
		NickName = preferences.getString("NickName", "");
		Intent intent = getIntent();
		from = intent.getStringExtra("FROM");
		mtvReleaseProjectTitle = (MyTitleView) findViewById(R.id.release_project_title);
		etProjectName = (EditText) findViewById(R.id.rp_project_name_et);
		etConstructionArea = (EditText) findViewById(R.id.rp_construction_area_et);
		tvSelectChengBaoWays = (TextView) findViewById(R.id.rp_select_chengbao_ways_tv);
		llSelectChengBaoWays = (LinearLayout) findViewById(R.id.rp_select_chengbao_ways_ll);
		tvSelectStartTime = (TextView) findViewById(R.id.rp_select_start_time_tv);
		llSelectStartTime = (LinearLayout) findViewById(R.id.rp_select_start_time_ll);
		tvSelectConstructionPlace = (TextView) findViewById(R.id.rp_select_construction_place_tv);
		llSelectConstructionPlace = (LinearLayout) findViewById(R.id.rp_select_construction_place_ll);
		tvDetailAddress = (TextView) findViewById(R.id.rp_detail_address_tv);
		llDetailAddress = (LinearLayout) findViewById(R.id.rp_detail_address_ll);
		etContactName = (EditText) findViewById(R.id.rp_contact_name_et);
		ContactPhone = (EditText) findViewById(R.id.rp_contact_phone_et);
		etOtherRequest = (EditText) findViewById(R.id.rp_other_request_et);
		btReleaseInformation = (Button) findViewById(R.id.release_information_bt);

		etContactName.setText(NickName);
		ContactPhone.setText(ContactTel);

		getCertification();
		
		btReleaseInformation.setOnClickListener(this);
		llSelectChengBaoWays.setOnClickListener(this);
		llSelectStartTime.setOnClickListener(this);
		llSelectConstructionPlace.setOnClickListener(this);

		/**
		 * 定位工程地点
		 */
		tvDetailAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String str2 = tvSelectConstructionPlace.getText().toString()
						.trim();
				if (tvSelectConstructionPlace.getText().toString().trim()
						.length() > 0) {
					String[] str = tvSelectConstructionPlace.getText()
							.toString().trim().split("/");
					String mCurrentCityName = str[1];
					if (mCurrentCityName != null
							&& !mCurrentCityName.equals("")) {
						Intent intent = new Intent(ReleaseProjectActivity.this,
								SelectedPositionActivity.class);
						Bundle bundle = new Bundle();
						AddressItem addressItem = new AddressItem();
						addressItem.setCityName(mCurrentCityName);
						if (!tvDetailAddress.getText().toString().equals("")) {
							addressItem.setAddress(tvDetailAddress.getText()
									.toString().trim());
							addressItem.setCityName(mCurrentCityName);
							bundle.putSerializable(
									SelectedPositionActivity.FIND_PERSON_NO,
									addressItem);

							intent.putExtra("type",
									SelectedPositionActivity.FIND_PERSON_NO);
							intent.putExtras(bundle);

						} else {
							addressItem.setAddress(tvDetailAddress.getText()
									.toString().trim());
							addressItem.setCityName(mCurrentCityName);
							bundle.putSerializable(
									SelectedPositionActivity.FIND_PERSON,
									addressItem);

							intent.putExtra("type",
									SelectedPositionActivity.FIND_PERSON);
							intent.putExtras(bundle);
							// startActivity(intent);

						}
						startActivityForResult(intent,
								StatusTool.BOOS_SELECT_PLACE);
					}
				} else {
					T.showToast(ReleaseProjectActivity.this, "请选择施工地点");
				}

			}
		});

		mtvReleaseProjectTitle.setOnLeftClickListener(new OnClickListener() {

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
		LayoutInflater inflater = LayoutInflater
				.from(ReleaseProjectActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(ReleaseProjectActivity.this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();

		Calendar calendar = Calendar.getInstance();

		try {
			calendar.setTime(dateFormat.parse(new Date().toLocaleString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int year;
		int month;
		int day;
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.release_information_bt:
			release();
			break;

		case R.id.rp_select_chengbao_ways_ll:
			MyTextDialog(getString(R.string.Choose_Contracting_Mode),
					tvSelectChengBaoWays, tvSelectChengBaoWays.getText()
							.toString().trim());

			break;

		case R.id.rp_select_start_time_ll:

			if (tvSelectStartTime.getText().toString().trim() != null
					&& !tvSelectStartTime.getText().toString().trim()
							.equals("")) {
				String StartTime = tvSelectStartTime.getText().toString()
						.trim();
				year = Integer.parseInt(StartTime.substring(0,
						StartTime.indexOf("年")));
				month = Integer.parseInt(StartTime.substring(
						StartTime.indexOf("年") + 1, StartTime.indexOf("月")));
				day = Integer.parseInt(StartTime.substring(
						StartTime.indexOf("月") + 1, StartTime.indexOf("日")));
			} else {
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
			}
			wheelMain.initDateTimePicker(year, month, day);
			new HintDialog.Builder(ReleaseProjectActivity.this)
					.setTitle("开始时间")
					.setContentView(timepickerview)
					.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tvSelectStartTime.setText(wheelMain
											.getTime());
									dialog.dismiss();
								}
							})
					.setNegativeButton(R.string.No,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();

			break;
		case R.id.rp_select_construction_place_ll:
			MyTextDialog(getString(R.string.Select_City),
					tvSelectConstructionPlace, tvSelectConstructionPlace
							.getText().toString().trim());

			break;
		case R.id.rp_detail_address_ll:

			break;

		default:
			break;
		}

	}

	private void release() {
		// TODO Auto-generated method stub
		if (etProjectName.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请输入项目名称");
			return;
		} else if (etConstructionArea.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请输入建筑面积");
			return;
		} else if (tvSelectChengBaoWays.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请选择承包方式");
			return;
		} else if (tvSelectStartTime.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请选择开工时间");
			return;
		} else if (tvSelectConstructionPlace.getText().toString().trim()
				.length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请选择施工地点");
			return;
		} else if (tvDetailAddress.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请定位工程地点");
			return;
		} else if (etContactName.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请填写联系人");
			return;
		} else if (ContactPhone.getText().toString().trim().length() <= 0) {
			T.showToast(ReleaseProjectActivity.this, "请填写联系电话");
			return;
		}

		else if (!Tools.isMobileNO(ContactPhone.getText().toString().trim())) {
			T.showToast(ReleaseProjectActivity.this, "手机号码输入有误");
			return;
		}

		else {
			String[] region = tvSelectConstructionPlace.getText().toString()
					.trim().split("/");
			projectInfo = new ProjectInfo();
			projectInfo.setInfoTitle(etProjectName.getText().toString().trim());
			projectInfo.setProvinceName(region[0]);
			projectInfo.setCityName(region[1]);
			projectInfo.setAreaName(region[2]);
			projectInfo.setAddress(tvDetailAddress.getText().toString().trim());
			projectInfo.setContactName(etContactName.getText().toString()
					.trim());
			projectInfo.setContactTel(ContactPhone.getText().toString().trim());
			projectInfo.setBuildingArea(etConstructionArea.getText().toString()
					.trim());
			projectInfo.setContractMode(tvSelectChengBaoWays.getText()
					.toString().trim());
			projectInfo.setStartTime(tvSelectStartTime.getText().toString()
					.trim());
			projectInfo.setRemark(etOtherRequest.getText().toString().trim());
			projectInfo.setLat(MyApplication.latLonPoint.getLatitude() + "");
			projectInfo.setLon(MyApplication.latLonPoint.getLongitude() + "");
			Gson gson = new Gson();
			String json = gson.toJson(projectInfo);
			CreateProjectInfo(json);
		}

	}

	/**
	 * 提交防水工程项目信息
	 * 
	 * @param json
	 */
	private void CreateProjectInfo(String json) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.CreateProjectInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ProjectInfo> interfaceReturn = new InterfaceReturn<ProjectInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ProjectInfo.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(ReleaseProjectActivity.this,
								interfaceReturn.getMessage());
						// recruitDayWorker =
						// interfaceReturn.getResults().get(0);
						// Intent intent = new Intent();
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("ConstructionTeam",
						// constructionTeam);
						// intent.putExtras(bundle);
						// setResult(MainActivity.CREATE_TEAM, intent);
						if (null != from && from.equals("MAIN_ACTIVITY")) {
							setResult(MainActivity.MAIN_ISSUE_PROJECT);
						}
						finish();
					} else {
						T.showToast(ReleaseProjectActivity.this,
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

	public void MyTextDialog(final String Hint, final TextView textView,
			String JobText) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		LinearLayout llWheelView = (LinearLayout) layout
				.findViewById(R.id.id_wheelview_ll);
		LinearLayout llJob = (LinearLayout) layout
				.findViewById(R.id.det_ll_job);
		final CheckBox cbQing = (CheckBox) layout
				.findViewById(R.id.det_cb_qing);
		final CheckBox cbDa = (CheckBox) layout.findViewById(R.id.det_cb_da);
		final CheckBox cbZong = (CheckBox) layout
				.findViewById(R.id.det_cb_zong);
		final EditText add = (EditText) layout.findViewById(R.id.et_nickname);
		cbQing.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbDa.setChecked(false);
					cbZong.setChecked(false);
					cbQing.setChecked(true);
				}
			}
		});
		cbDa.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbDa.setChecked(true);
					cbZong.setChecked(false);
					cbQing.setChecked(false);
				}
			}
		});
		cbZong.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbDa.setChecked(false);
					cbZong.setChecked(true);
					cbQing.setChecked(false);
				}
			}
		});
		if (Hint.equals(getString(R.string.Select_City))) {
			add.setVisibility(View.GONE);
			llWheelView.setVisibility(View.VISIBLE);
			mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
			mViewCity = (WheelView) layout.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) layout.findViewById(R.id.id_district);
			setUpListener();
			setUpData();
		}
		if (Hint.equals(getString(R.string.Choose_Contracting_Mode))) {
			llJob.setVisibility(View.VISIBLE);
			if (JobText.equals(R.string.Clear_Bag)) {
				cbQing.setChecked(true);
			} else if (JobText.equals(R.string.Big_Bag)) {
				cbDa.setChecked(true);
			} else if (JobText.equals(R.string.General_Contracting)) {
				cbZong.setChecked(true);
			}
			add.setVisibility(View.GONE);
			llWheelView.setVisibility(View.GONE);
		}
		new HintDialog.Builder(layout.getContext())

				.setTitle(Hint)
				.setContentView(layout)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (Hint.equals(getString(R.string.Please_Choose_City))) {
									showSelectedResult(textView);
								}
								if (Hint.equals(getString(R.string.Choose_Contracting_Mode))) {
									if (cbQing.isChecked()) {
										textView.setText(cbQing.getText()
												.toString().trim());
									} else if (cbDa.isChecked()) {
										textView.setText(cbDa.getText()
												.toString().trim());
									} else if (cbZong.isChecked()) {
										textView.setText(cbZong.getText()
												.toString().trim());
									}

								}
								dialog.dismiss();
								return;

							}

						})
				.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create().show();

	}

	private void showSelectedResult(TextView textView) {
		textView.setText(mCurrentProviceName + "/" + mCurrentCityName + "/"
				+ mCurrentDistrictName);
		if (!tvDetailAddress.getText().toString().equals("")) {
			tvDetailAddress.setText("");
		}

		System.out.println(mCurrentCityName
				+ "==============mCurrentCityName============================");
	}

	private void setUpListener() {
		// 添加change事件

		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				ReleaseProjectActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
		}

	}

	class userInfo implements Serializable {
		private Member member;

		public Member getMember() {
			return member;
		}

		public void setMember(Member member) {
			this.member = member;
		}
	}

	/**
	 * 判断是否实名认证
	 */
	private void getCertification() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(HttpUtil.UserInfo);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		Xutils.Get(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<userInfo> interfaceReturn = new InterfaceReturn<userInfo>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						userInfo.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {

						IsRealName = interfaceReturn.getResults().get(0)
								.getMember().isRealName();
						if (!IsRealName) {
							HintDialog.Builder builder = new HintDialog.Builder(
									ReleaseProjectActivity.this);
							builder.setMessage("您还没有实名认证,是否去认证 ?");
							builder.setTitle(R.string.Hint);
							builder.setNegativeButton(
									R.string.Yes,
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											startActivity(new Intent(
													ReleaseProjectActivity.this,
													MemberAttestationActivity.class));
											dialog.dismiss();

										}
									});
							builder.setPositiveButton(R.string.No,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							builder.create().show();
						}

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
	
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	// if (requestCode == StatusTool.BOOS_SELECT_PLACE) {
	// if (resultCode == StatusTool.SELECT_PLACE) {
	// tvDetailAddress.setText(data.getExtras().getString("address"));
	// }
	// }
	// switch (resultCode) {
	// case 1:
	// // if (data.getStringExtra("state").equals("ProjectImg")) {
	// // tvPersonalPhoto.setText(Bimp.imgsHashMap.get("ProjectImg")
	// // .size()
	// // + getString(R.string.Images_has_Uploading_suceessful));
	// // }
	// break;
	// case 6:
	// tvDayWorkerUseMaterial.setText(data.getStringExtra("positionlist"));
	// if (tvDayWorkerUseMaterial.getText().toString().trim() != null
	// && !tvDayWorkerUseMaterial.getText().toString().trim()
	// .equals("")) {
	// tvDayWorkerUseMaterial.setVisibility(View.VISIBLE);
	// }
	// break;
	// case 7:
	//
	// tvDayWorkerConstructionPart.setText(data
	// .getStringExtra("conspartlist"));
	// if (tvDayWorkerConstructionPart.getText().toString().trim() != null
	// && !tvDayWorkerConstructionPart.getText().toString().trim()
	// .equals("")) {
	// tvDayWorkerConstructionPart.setVisibility(View.VISIBLE);
	// }
	// break;
	//
	// default:
	// break;
	// }
	// }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		if (WantDayWorkerActivity.SELECTMAP) {
			tvDetailAddress.setText(preferences.getString("address", ""));
			WantDayWorkerActivity.SELECTMAP = !WantDayWorkerActivity.SELECTMAP;

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.latLonPoint = null;
		WantDayWorkerActivity.SELECTMAP = false;
	}
}
