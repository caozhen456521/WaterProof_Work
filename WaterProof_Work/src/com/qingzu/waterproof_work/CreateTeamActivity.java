package com.qingzu.waterproof_work;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.AddressItem;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.InterfaceReturn;

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

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTeamActivity extends WheelViewActivity implements
		OnClickListener, OnWheelChangedListener {

	private MyTitleView act_title = null;
	private EditText act_et_team_name = null;
	private EditText act_et_contacts = null;
	private EditText act_et_phone = null;
	private TextView act_tv_location = null;
	private TextView act_tv_detail_location = null;
	private EditText act_et_strength_show = null;
	private ConstructionTeam constructionTeam = null;
	private SharedPreferences preferences = null;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private String UserToken = null;
	private int MemberId = 0;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_team);
		Tools.setNavigationBarColor(this, R.color.title_background_black);
		AppManager.getAppManager().addActivity(this);
		initView();

	}

	private void initView() {
		preferences = CreateTeamActivity.this.getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		MemberId = preferences.getInt("MemberId", 0);

		act_title = (MyTitleView) findViewById(R.id.act_title);
		act_et_team_name = (EditText) findViewById(R.id.act_et_team_name);
		act_et_contacts = (EditText) findViewById(R.id.act_et_contacts);
		act_et_phone = (EditText) findViewById(R.id.act_et_phone);
		act_tv_location = (TextView) findViewById(R.id.act_tv_location);
		act_tv_detail_location = (TextView) findViewById(R.id.act_tv_detail_location);
		act_et_strength_show = (EditText) findViewById(R.id.act_et_strength_show);

		act_title.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		act_title.setOnRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				commit();
			}
		});
		act_tv_location.setOnClickListener(this);
		act_tv_detail_location.setOnClickListener(this);

	}

	private void commit() {
		if (act_et_team_name.getText().toString().trim().length() <= 0) {
			T.showToast(CreateTeamActivity.this, "请输入施工队名");
			return;
		} else if (act_et_contacts.getText().toString().trim().length() <= 0) {
			T.showToast(CreateTeamActivity.this, "请输入联系人");
			return;
		} else if (act_et_phone.getText().toString().trim().length() <= 0) {
			T.showToast(CreateTeamActivity.this, "请输入手机号码");
			return;
		} else if (!Tools.isMobileNO(act_et_phone.getText().toString().trim())) {
			T.showToast(CreateTeamActivity.this, "手机号码有误");
			return;
		} else if (act_tv_location.getText().toString().trim().length() <= 0) {
			T.showToast(CreateTeamActivity.this, "请选择所在城市");
			return;
		} else if (act_tv_detail_location.getText().toString().trim().length() <= 0) {
			T.showToast(CreateTeamActivity.this, "请选择详细地址");
			return;
		} else {
			String[] region = act_tv_location.getText().toString().trim()
					.split("/");
			constructionTeam = new ConstructionTeam();
			constructionTeam.setMemberId(MemberId);
			constructionTeam.setTeamName(act_et_team_name.getText().toString()
					.trim());
			constructionTeam.setLeaderName(act_et_contacts.getText().toString()
					.trim());
			constructionTeam.setLeaderTel(act_et_phone.getText().toString()
					.trim());
			constructionTeam.setProvinceName(region[0]);
			constructionTeam.setCityName(region[1]);
			constructionTeam.setAreaName(region[2]);
			constructionTeam.setAddress(act_tv_detail_location.getText()
					.toString().trim());
			constructionTeam.setTeamStrength(act_et_strength_show.getText()
					.toString().trim());
			Gson gson = new Gson();
			String json = gson.toJson(constructionTeam);
			CreateConstructionTeam(json);
		}
	}

	/**
	 * 当前登录用户创建工程施工队信息
	 * 
	 * @param json
	 * @author Johnson
	 */
	private void CreateConstructionTeam(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.CreateConstructionTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Post(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionTeam> interfaceReturn = new InterfaceReturn<ConstructionTeam>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeam.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(CreateTeamActivity.this,
								interfaceReturn.getMessage());
						constructionTeam = interfaceReturn.getResults().get(0);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("ConstructionTeam",
								constructionTeam);
						intent.putExtras(bundle);
						setResult(MainActivity.CREATE_TEAM, intent);
						finish();
					} else {
						T.showToast(CreateTeamActivity.this,
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_tv_location:
			MyTextDialog(act_tv_location);
			break;
		case R.id.act_tv_detail_location:
			if (act_tv_location.getText().toString().trim().length() > 0) {
				String[] str = act_tv_location.getText().toString().trim()
						.split("/");
				String mCurrentCityName = str[1];
				if (mCurrentCityName != null && mCurrentCityName != "") {

					Intent intent = new Intent();
					intent.setClass(CreateTeamActivity.this,
							SelectedPositionActivity.class);
					Bundle bundle = new Bundle();
					AddressItem addressItem = new AddressItem();
					if (!(act_tv_detail_location.getText().toString().trim()
							.equals(""))) {
						addressItem.setCityName(mCurrentCityName);
						addressItem.setAddress(act_tv_detail_location.getText()
								.toString().trim());
						bundle.putSerializable(
								SelectedPositionActivity.FIND_PERSON_NO,
								addressItem);
						intent.putExtra("type",
								SelectedPositionActivity.FIND_PERSON_NO);
						intent.putExtras(bundle);
					} else {
						addressItem.setCityName(mCurrentCityName);
						addressItem.setAddress(act_tv_detail_location.getText()
								.toString().trim());
						bundle.putSerializable(
								SelectedPositionActivity.FIND_PERSON_NO,
								addressItem);
						intent.putExtra("type",
								SelectedPositionActivity.FIND_PERSON_NO);
						intent.putExtras(bundle);
					}

					startActivityForResult(intent, StatusTool.BOOS_SELECT_PLACE);
				}
			} else {
				T.showToast(CreateTeamActivity.this, "请选择所在地");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * edittext dialog
	 * 
	 * @author Johnson
	 * @date 2016-1-5
	 * @param Hint
	 * @param textView
	 * @param Text
	 */
	private void MyTextDialog(final TextView textView) {

		// TODO Auto-generated method stub
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) findViewById(R.id.dialog));
		LinearLayout llWheelView = (LinearLayout) layout
				.findViewById(R.id.id_wheelview_ll);
		llWheelView.setVisibility(View.VISIBLE);
		mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
		mViewCity = (WheelView) layout.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) layout.findViewById(R.id.id_district);
		setUpListener();
		setUpData();
		new HintDialog.Builder(layout.getContext())
				.setTitle("请选择城市")
				.setContentView(layout)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								showSelectedResult(textView);
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
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				CreateTeamActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();

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

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener((OnWheelChangedListener) this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == StatusTool.BOOS_SELECT_PLACE) {
			if (resultCode == StatusTool.SELECT_PLACE) {
				act_tv_detail_location.setText(data.getExtras().getString(
						"address"));
			}
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (WantDayWorkerActivity.SELECTMAP) {
			act_tv_detail_location
					.setText(preferences.getString("address", ""));
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
