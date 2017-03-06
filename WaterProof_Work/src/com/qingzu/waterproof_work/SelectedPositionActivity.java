package com.qingzu.waterproof_work;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMapTouchListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.PoiOverlay;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.qingzu.adapter.SelectedPositionAdapter;
import com.qingzu.application.AppManager;
import com.qingzu.application.MyApplication;
import com.qingzu.application.StatusTool;
import com.qingzu.entity.AddressItem;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectedPositionActivity extends Activity implements
		OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener,
		OnMapTouchListener, OnMapLoadedListener, AMapLocationListener,
		LocationSource, OnGeocodeSearchListener, OnPoiSearchListener {
	private ProgressDialog progDialog = null;

	private AMap aMap;
	private MapView mapView;
	private Marker mPositionMark;
	private LatLng latlng = new LatLng(34.341568, 108.940174);
	private OnLocationChangedListener mListener;
	private AMapLocation aLocation;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private boolean isOnTouch = false;
	private RelativeLayout frameLayout = null;
	private TextView textView = null;
	private ImageView image = null;
	private Animation mShakeAnimation;
	private GeocodeSearch geocoderSearch;
	private List<PoiItem> listPoiItems = new ArrayList<PoiItem>();
	private PoiResult poiResult; // poi返回的结果
	private PoiOverlay poiOverlay;// poi图层
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private SelectedPositionAdapter adapter = null;
	private List<AddressItem> listdata = new ArrayList<AddressItem>();

	private EditText textView2 = null;

	private ListView listView = null;

	public static final String FIND_PERSON = "1";
	public static final String FIND_PERSON_NO = "3";
	public static final String SEARCH = "2";
	public static final String SEARCH_1 = "3";

	// 初始化定位

	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_position);
		AppManager.getAppManager().addActivity(this);

		mapView = (MapView) findViewById(R.id.map);
		frameLayout = (RelativeLayout) findViewById(R.id.frame);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		listView = (ListView) findViewById(R.id.listview);
		textView2 = (EditText) findViewById(R.id.etSearch);
		adapter = new SelectedPositionAdapter(this, listdata, 0);
		listView.setAdapter(adapter);
		initMap();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				SharedPreferences preferences = getSharedPreferences(
						"UserToken", Activity.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("address", listdata.get(arg2).getAddress());
				editor.commit();
				WantDayWorkerActivity.SELECTMAP = true;
				
				listdata.get(arg2).getAddressDetails();
				MyApplication.latLonPoint = listdata.get(arg2).getPoint();
				finish();
			}
		});

		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SelectedPositionActivity.this,
						SearchActivity.class));
				finish();
			}
		});
	}

	protected void doSearchQuery(String address, String CityName) {
		showDialog();// 显示进度框
		// 府学胡同小学
		query = new PoiSearch.Query(address,
				"风景名胜|商务住宅|政府机构及社会团体|科教文化服务|道路附属设施", CityName);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页
		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 初始化AMap对象
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			// setUpMap();
		}

		geocoderSearch = new GeocodeSearch(this);// 地理编码
		geocoderSearch.setOnGeocodeSearchListener(this);

		aMap.setMyLocationEnabled(true);
		aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器
		aMap.getUiSettings().setZoomControlsEnabled(true);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		aMap.setOnMapLoadedListener(this);
		aMap.runOnDrawFrame();
		aMap.setOnCameraChangeListener(this);
		aMap.setLocationSource(this);
		progDialog = new ProgressDialog(this);

		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.dingwei));

		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(Color.TRANSPARENT);
		// 去掉范围显示
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(0);
		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);

		String type = getIntent().getStringExtra("type");
		if (type.equals(SelectedPositionActivity.SEARCH)) {
			AddressItem addressItem = (AddressItem) getIntent()
					.getSerializableExtra(SelectedPositionActivity.SEARCH);

			getAddress(new LatLonPoint(addressItem.getLatitude(),
					addressItem.getLongitude()));
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
					addressItem.getLatitude(), addressItem.getLongitude())));
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
			return;
		} else if (type.equals(SelectedPositionActivity.FIND_PERSON)) {
			AddressItem addressItem = (AddressItem) getIntent()
					.getSerializableExtra(SelectedPositionActivity.FIND_PERSON);
			doSearchQuery(addressItem.getCityName(), addressItem.getCityName());
			return;
		} else if (type.equals(SelectedPositionActivity.FIND_PERSON_NO)) {
			AddressItem addressItem = (AddressItem) getIntent()
					.getSerializableExtra(
							SelectedPositionActivity.FIND_PERSON_NO);
			doSearchQuery(addressItem.getAddress(), addressItem.getCityName());
			return;
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

	}

	public void addlayet(MapView mapView, int bmpH, int bmpw) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0, 0, 0, 0);
		layout.setLayoutParams(new ViewGroup.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.marker_ico, null);
		textView = (TextView) view.findViewById(R.id.textview);
		image = (ImageView) view.findViewById(R.id.img);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(view);

		layout.setLayoutParams(params);
		frameLayout.addView(layout);
	}

	private void addMarkerToMap() {

		// MarkerOptions markerOptions = new MarkerOptions();
		// markerOptions.setFlat(true);
		// markerOptions.anchor(0.5f, 0.5f);
		// markerOptions.position(new LatLng(0, 0));
		// markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
		// .decodeResource(getResources(), R.drawable.zuobiao)));

		// mPositionMark = aMap.addMarker(markerOptions);
		Resources r = getResources();
		// 以数据流的方式读取资源
		InputStream is = r.openRawResource(R.drawable.zuobiao);
		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		Bitmap bmp = bmpDraw.getBitmap();
		// addlayet(mapView, bmp.getHeight());
		// mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
		// mapView.getHeight() / 2 - bmp.getHeight() / 2);
		// mPositionMark.setToTop();
		addlayet(mapView, bmp.getHeight(), bmp.getWidth());
		// System.out.println("高度========" + mapView.getHeight() / 2);
		// System.out.println("宽度========" + mapView.getWidth() / 2);

		Intent intent = new Intent();

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouch(MotionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.describeContents());
		isOnTouch = true;

	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		if (aMap != null && isOnTouch == true) {
			textView.setVisibility(View.INVISIBLE);

		}

	}

	@Override
	public void onCameraChangeFinish(CameraPosition position) {

		if (aMap != null && isOnTouch == true) {
			shakeAnimation(1);
			textView.setVisibility(View.VISIBLE);
			getAddress(new LatLonPoint(position.target.latitude,
					position.target.longitude));
			isOnTouch = false;
		}

	}

	public void shakeAnimation(int CycleTimes) {
		if (null == mShakeAnimation) {
			mShakeAnimation = new TranslateAnimation(0, 0, 0, 10);
			mShakeAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
			mShakeAnimation.setDuration(1000);
			mShakeAnimation.setRepeatMode(Animation.REVERSE);// 设置反方向执行

		}
		image.startAnimation(mShakeAnimation);
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		addMarkerToMap();
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		// TODO Auto-generated method stub
		// mListener.onLocationChanged(aLocation);
		if (mListener != null && aLocation != null) {
			if (aLocation != null && aLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(aLocation);// 显示系统小蓝点

				getAddress(new LatLonPoint(aLocation.getLatitude(),
						aLocation.getLongitude()));

				mlocationClient.stopLocation();
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();

		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final PoiItem poiItem) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(poiItem.getLatLonPoint(),
				1000, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
		listPoiItems.add(poiItem);
		if (listPoiItems != null && listPoiItems.size() > 0) {
			aMap.clear();// 清理之前的图标
			poiOverlay = new PoiOverlay(aMap, listPoiItems);
			poiOverlay.removeFromMap();
			// poiOverlay.addToMap();
			poiOverlay.zoomToSpan();
			// System.out.println(lonPoint);
		}
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		dismissDialog();

		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				if (listdata != null) {
					listdata.clear();
				}
				String city = result.getRegeocodeAddress().getPois().get(0)
						.getCityName();
				System.out.println(city);

				for (int i = 0; i < result.getRegeocodeAddress().getPois()
						.size(); i++) {
					AddressItem addressItem = new AddressItem();
					String data = result.getRegeocodeAddress().getPois().get(i)
							.toString();
					addressItem.setAddress(data);
					addressItem.setPoint(result.getRegeocodeAddress().getPois()
							.get(i).getLatLonPoint());
					addressItem.setAddressDetails(result.getRegeocodeAddress()
							.getPois().get(i).getSnippet());
					listdata.add(addressItem);
				}
				adapter.notifyDataSetChanged();
			//	System.out.println(listdata);

			}
		}
	}

	/**
	 * POI信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					listPoiItems.add(poiResult.getPois().get(0));
					LatLonPoint lonPoint = poiResult.getPois().get(0)
							.getLatLonPoint();
					getAddress(poiResult.getPois().get(0));
					// if (listPoiItems != null && listPoiItems.size() > 0) {
					// aMap.clear();// 清理之前的图标
					// poiOverlay = new PoiOverlay(aMap, listPoiItems);
					// poiOverlay.removeFromMap();
					// // poiOverlay.addToMap();
					// poiOverlay.zoomToSpan();
					// System.out.println(lonPoint);
					//
					// }
				}

			}
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
