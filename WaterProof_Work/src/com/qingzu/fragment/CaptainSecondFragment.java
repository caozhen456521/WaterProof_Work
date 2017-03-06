package com.qingzu.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import com.qingzu.application.StatusTool;
import com.qingzu.entity.AddressItem;
import com.qingzu.entity.CityModel;
import com.qingzu.entity.ConstructionTeam;
import com.qingzu.entity.DistrictModel;
import com.qingzu.entity.InterfaceReturn;
import com.qingzu.entity.ProvinceModel;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.http.HttpUtil;
import com.qingzu.utils.http.data.MyCallBack;
import com.qingzu.utils.http.data.Xutils;
import com.qingzu.utils.tools.CustomDialog;
import com.qingzu.utils.tools.HintDialog;
import com.qingzu.utils.tools.MyAdapter;
import com.qingzu.utils.tools.T;
import com.qingzu.utils.tools.Tools;
import com.qingzu.utils.tools.ViewHolder;
import com.qingzu.utils.tools.XmlParserHandler;
import com.qingzu.utils.tools.CustomDialog.Builder;
import com.qingzu.waterproof_work.CreateTeamActivity;
import com.qingzu.waterproof_work.MainActivity;
import com.qingzu.waterproof_work.R;
import com.qingzu.waterproof_work.SelectedPositionActivity;
import com.qingzu.waterproof_work.ServeCityActivity;
import com.qingzu.waterproof_work.ShowStrengthActivity;
import com.qingzu.waterproof_work.TeamManageActivity;
import com.qingzu.waterproof_work.WantDayWorkerActivity;
import com.qingzu.wheel.widget.OnWheelChangedListener;
import com.qingzu.wheel.widget.WheelView;
import com.qingzu.wheel.widget.adapters.ArrayWheelAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工长第二Fragment
 * 
 * @author Johnson
 * 
 */
public class CaptainSecondFragment extends Fragment implements OnClickListener,
		OnWheelChangedListener {

	public static final int SHOW_STRENGTH = 7;
	private MyTitleView csf_team_title = null;// 标题
	private RelativeLayout csf_rl_team_name = null;// 队名
	private TextView csf_tv_team_name = null;// 队名
	private RelativeLayout csf_rl_contacts = null;// 联系人
	private TextView csf_tv_contacts = null;// 联系人
	private RelativeLayout csf_rl_phone = null;// 联系电话
	private TextView csf_tv_phone = null;// 联系电话
	private RelativeLayout csf_rl_location = null;// 所在地
	private TextView csf_tv_location = null;// 所在地
	private RelativeLayout csf_rl_detail_addr = null;// 详细地址
	private TextView csf_tv_detail_addr = null;// 详细地址
	private RelativeLayout csf_rl_strength = null;// 实力描述
	private RelativeLayout csf_rl_service_city = null;// 服务城市
	private RelativeLayout csf_rl_team_manage = null;// 队员管理
	private ConstructionTeam constructionTeam = null;

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private SharedPreferences preferences = null;
	private String UserToken = null;

	private View v = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_second, container, false);
		initView();
		return v;
	}

	private void initView() {
		preferences = getActivity().getSharedPreferences("UserToken",
				Activity.MODE_PRIVATE);
		UserToken = preferences.getString("UserToken", "");
		constructionTeam = MainActivity.constructionTeam;
		csf_team_title = (MyTitleView) v.findViewById(R.id.csf_team_title);
		csf_rl_team_name = (RelativeLayout) v
				.findViewById(R.id.csf_rl_team_name);
		csf_tv_team_name = (TextView) v.findViewById(R.id.csf_tv_team_name);
		csf_rl_contacts = (RelativeLayout) v.findViewById(R.id.csf_rl_contacts);
		csf_tv_contacts = (TextView) v.findViewById(R.id.csf_tv_contacts);
		csf_rl_phone = (RelativeLayout) v.findViewById(R.id.csf_rl_phone);
		csf_tv_phone = (TextView) v.findViewById(R.id.csf_tv_phone);
		csf_rl_location = (RelativeLayout) v.findViewById(R.id.csf_rl_location);
		csf_tv_location = (TextView) v.findViewById(R.id.csf_tv_location);
		csf_rl_detail_addr = (RelativeLayout) v
				.findViewById(R.id.csf_rl_detail_addr);
		csf_tv_detail_addr = (TextView) v.findViewById(R.id.csf_tv_detail_addr);
		csf_rl_strength = (RelativeLayout) v.findViewById(R.id.csf_rl_strength);
		csf_rl_service_city = (RelativeLayout) v
				.findViewById(R.id.csf_rl_service_city);
		csf_rl_team_manage = (RelativeLayout) v
				.findViewById(R.id.csf_rl_team_manage);

		csf_rl_team_name.setOnClickListener(this);
		csf_rl_contacts.setOnClickListener(this);
		csf_rl_phone.setOnClickListener(this);
		csf_rl_location.setOnClickListener(this);
		csf_rl_detail_addr.setOnClickListener(this);
		csf_rl_strength.setOnClickListener(this);
		csf_rl_service_city.setOnClickListener(this);
		csf_rl_team_manage.setOnClickListener(this);

		setData();
	}

	private void setData() {
		if (MainActivity.constructionTeam != null) {
			constructionTeam = MainActivity.constructionTeam;
			csf_tv_team_name.setText(constructionTeam.getTeamName());
			csf_tv_contacts.setText(constructionTeam.getLeaderName());
			csf_tv_phone.setText(constructionTeam.getLeaderTel());
			csf_tv_location.setText(constructionTeam.getProvinceName() + "/"
					+ constructionTeam.getCityName() + "/"
					+ constructionTeam.getAreaName());
			csf_tv_detail_addr.setText(constructionTeam.getAddress());
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.csf_rl_team_name:
			TextDialog("请输入队名");
			break;
		case R.id.csf_rl_contacts:
			TextDialog("请输入联系人");
			break;
		case R.id.csf_rl_phone:

			break;
		case R.id.csf_rl_location:
			MyTextDialog(csf_tv_location);
			break;
		case R.id.csf_rl_detail_addr:
			if (csf_tv_location.getText().toString().trim().length() > 0) {
				String[] str = csf_tv_location.getText().toString().trim()
						.split("/");
				String mCurrentCityName = str[1];
				if (mCurrentCityName != null && mCurrentCityName != "") {

					Intent intent = new Intent();
					intent.setClass(getActivity(),
							SelectedPositionActivity.class);
					Bundle bundle = new Bundle();
					AddressItem addressItem = new AddressItem();
					if (!(csf_tv_detail_addr.getText().toString().trim()
							.equals(""))) {
						addressItem.setCityName(mCurrentCityName);
						addressItem.setAddress(csf_tv_detail_addr.getText()
								.toString().trim());
						bundle.putSerializable(
								SelectedPositionActivity.FIND_PERSON_NO,
								addressItem);
						intent.putExtra("type",
								SelectedPositionActivity.FIND_PERSON_NO);
						intent.putExtras(bundle);
					} else {
						addressItem.setCityName(mCurrentCityName);
						addressItem.setAddress(csf_tv_detail_addr.getText()
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
				T.showToast(getActivity(), "请选择所在地");
			}
			break;
		case R.id.csf_rl_strength:
			startActivityForResult(new Intent(getActivity(),
					ShowStrengthActivity.class), SHOW_STRENGTH);
			break;
		case R.id.csf_rl_service_city:
			startActivity(new Intent(getActivity(), ServeCityActivity.class));
			break;
		case R.id.csf_rl_team_manage:
			startActivity(new Intent(getActivity(), TeamManageActivity.class));
			break;

		default:
			break;
		}
	}

	/**
	 * 当前登录用户编辑工程施工队信息
	 * 
	 * @param strength
	 * @author Johnson
	 */
	private void UpdateConstructionTeam(String json) {
		RequestParams params = new RequestParams(
				HttpUtil.UpdateConstructionTeam);
		params.addHeader("EZFSToken", Tools.getEZFSToken(UserToken));
		params.setAsJsonContent(true);
		params.setBodyContent(json);
		params.setCharset("utf-8");
		Xutils.Put(params, new MyCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				InterfaceReturn<ConstructionTeam> interfaceReturn = new InterfaceReturn<ConstructionTeam>();
				interfaceReturn = InterfaceReturn.fromJson(result,
						ConstructionTeam.class);
				if (interfaceReturn != null) {
					if (interfaceReturn.isStatus()) {
						T.showToast(getActivity(), interfaceReturn.getMessage());
						MainActivity.constructionTeam = interfaceReturn
								.getResults().get(0);
						setData();
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
	 * TextDialog
	 * 
	 * @author Johnson
	 */
	protected void TextDialog(final String title) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_custom_dialog, null);
		final EditText lcd_et = (EditText) view.findViewById(R.id.lcd_et);
		lcd_et.setVisibility(View.VISIBLE);
		final Builder builder = new CustomDialog.Builder(getActivity())
				.setTitle(title)
				.setContentView(view)
				.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (title.equals("请输入队名")) {
									if (lcd_et.getText().toString().trim()
											.length() <= 0) {
										T.showToast(getActivity(), title);
										return;
									}
									// /
									constructionTeam.setTeamName(lcd_et
											.getText().toString().trim());
									UpdateConstructionTeam(InterfaceReturn
											.getGson().toJson(constructionTeam));
								} else if (title.equals("请输入联系人")) {
									if (lcd_et.getText().toString().trim()
											.length() <= 0) {
										T.showToast(getActivity(), title);
										return;
									}
									// /
									constructionTeam.setLeaderName(lcd_et
											.getText().toString().trim());
									UpdateConstructionTeam(InterfaceReturn
											.getGson().toJson(constructionTeam));
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
						});
		builder.create().show();
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
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View layout = inflater.inflate(R.layout.layout_dialog_edit_text,
				(ViewGroup) v.findViewById(R.id.dialog));
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
		constructionTeam.setProvinceName(mCurrentProviceName);
		constructionTeam.setCityName(mCurrentCityName);
		constructionTeam.setAreaName(mCurrentDistrictName);
		UpdateConstructionTeam(InterfaceReturn.getGson().toJson(
				constructionTeam));
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				getActivity(), mProvinceDatas));
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
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(),
				cities));
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
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(
				getActivity(), areas));
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case SHOW_STRENGTH:
			setData();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (WantDayWorkerActivity.SELECTMAP) {
			if (!csf_tv_detail_addr.getText().toString().trim()
					.equals(preferences.getString("address", ""))) {
				constructionTeam.setAddress(preferences
						.getString("address", ""));
				UpdateConstructionTeam(InterfaceReturn.getGson().toJson(
						constructionTeam));
			}
			WantDayWorkerActivity.SELECTMAP = !WantDayWorkerActivity.SELECTMAP;
		}

	}

	private String RegionListAll = null;

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mIdDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentId = "";

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		try {
			if (XmlParserHandler.provinceList == null
					|| XmlParserHandler.provinceList.size() == 0) {
				AssetManager asset = getActivity().getAssets();
				InputStream input = asset.open("province_data.xml");
				// 创建一个解析xml的工厂对象
				SAXParserFactory spf = SAXParserFactory.newInstance();
				// 解析xml
				SAXParser parser = spf.newSAXParser();
				XmlParserHandler handler = new XmlParserHandler();
				parser.parse(input, handler);
				input.close();
				// 获取解析出来的数据
				provinceList = handler.getDataList();
			} else {
				provinceList = XmlParserHandler.provinceList;
			}

			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentId = districtList.get(0).getId();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getId());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mIdDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getId());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
}