package com.qingzu.waterproof_work;

import java.io.File;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;

import com.qingzu.application.AppManager;
import com.qingzu.callback.OnLocationListener;
import com.qingzu.entity.AddressItem;
import com.qingzu.entity.ProjectInfo;
import com.qingzu.entity.RecruitmentInfo;
import com.qingzu.uitest.view.MyTitleView;
import com.qingzu.utils.tools.AMapLocations;
import com.qingzu.utils.tools.T;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

public class ShowMapAddressActivity extends Activity implements
		InfoWindowAdapter, AMapLocationListener, LocationSource,
		OnMapLoadedListener, OnInfoWindowClickListener, OnMarkerClickListener,
		OnGeocodeSearchListener {

	private AMap aMap = null;
	private Marker mMarker = null;
	private MapView mapView = null;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private MyTitleView myTitleView = null;
	private LatLng latLng = new LatLng(34.341568, 108.940174);
	private GeocodeSearch geocoderSearch;
	private View infoContent;
	private RecruitmentInfo addressItem;

	// private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map_address);
		AppManager.getAppManager().addActivity(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		myTitleView = (MyTitleView) findViewById(R.id.mp_title);

		// cd = new ConnectionDetector(ShowMapAddressActivity.this);
		// if (cd.isConnectingToInternet()) {
		initMap();
		// }

		myTitleView.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		AMapLocations.getAMapLocations(this).setOnLocationListener(
				onLocationListener);
		aMap.setMyLocationEnabled(true);
		aMap.getUiSettings().setZoomControlsEnabled(true);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);

		aMap.setLocationSource(this);
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

	}

	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
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

	}


	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		// showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	public OnLocationListener onLocationListener = new OnLocationListener() {

		@Override
		public void OnLocationSuccesed(AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			// currentCity = amapLocation.getCity();
			// judgeCity();
			getAddress(new LatLonPoint(amapLocation.getLatitude(),
					amapLocation.getLongitude()));

		}

		@Override
		public void OnLocationFailed(AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			Log.e("AmapError",
					"location Error, ErrCode:" + amapLocation.getErrorCode()
							+ ", errInfo:" + amapLocation.getErrorInfo());
		}

		@Override
		public void OnLocationSuccesedGetCode(AMapLocation amapLocation) {
			// TODO Auto-generated method stub

		}
	};

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
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		// TODO Auto-generated method stub
		infoContent = getLayoutInflater().inflate(R.layout.marker, null);
//		TextView marker_title = (TextView) infoContent
//				.findViewById(R.id.marker_title);
		TextView marker_content = (TextView) infoContent
				.findViewById(R.id.marker_content);
		LinearLayout ll_getPosition = (LinearLayout) infoContent
				.findViewById(R.id.ll_getPosition);
		//marker_title.setText(marker.getTitle());
		marker_content.setText(marker.getSnippet());

		return infoContent;

	}

	/**
	 * marker 必须有设置图标，否则无效果
	 * 
	 * @param marker
	 */
	private void dropInto(final Marker marker) {

		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng markerLatlng = marker.getPosition();
		Projection proj = aMap.getProjection();
		Point markerPoint = proj.toScreenLocation(markerLatlng);
		Point startPoint = new Point(markerPoint.x, 0);// 从marker的屏幕上方下落
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 800;// 动画总时长

		final Interpolator interpolator = new AccelerateInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * markerLatlng.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * markerLatlng.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && aLocation != null) {
			if (aLocation != null && aLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(aLocation);// 显示系统小蓝点

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

	public void addMarkersToMap(LatLng latLng, String address) {
		mMarker = aMap
				.addMarker(new MarkerOptions()
						.title(getString(R.string.Project_Position))
						.snippet(address)
						.draggable(true)
						.position(latLng)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.zuobiao)));

		mMarker.showInfoWindow();

	}

	@Override
	public void onMapLoaded() {

		// String type = getIntent().getStringExtra("type");
		// if (type.equals(SelectedPositionActivity.SEARCH)) {

		addressItem = (RecruitmentInfo) getIntent().getSerializableExtra(
				SelectedPositionActivity.SEARCH);

		addMarkersToMap(
				new LatLng(Double.valueOf(addressItem.getLat()),
						Double.valueOf(addressItem.getLon())),
				addressItem.getAddress());
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double
				.valueOf(addressItem.getLat()), Double.valueOf(addressItem
				.getLon()))));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
		return;
		// } else if (type.equals(SelectedPositionActivity.SEARCH_1)) {
		// ProjectInfo addressItem = (ProjectInfo) getIntent()
		// .getSerializableExtra(SelectedPositionActivity.SEARCH_1);
		//
		// addMarkersToMap(new LatLng(Double.valueOf(addressItem.getLat()),
		// Double.valueOf(addressItem.getLon())),
		// addressItem.getAddress());
		// aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double
		// .valueOf(addressItem.getLat()), Double.valueOf(addressItem
		// .getLon()))));
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
		// return;
		// }
		// }
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		// if (marker.equals(mMarker)) {
		// if (aMap!=null) {
		// dropInto(mMarker);
		// }
		// }
		return false;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int arg1) {
		// TODO Auto-generated method stub

		if (arg1 == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				final AddressItem sAddressItem = new AddressItem();
				String data = result.getRegeocodeAddress().getPois().get(0)
						.toString();
				sAddressItem.setAddress(data);
				sAddressItem.setPoint(result.getRegeocodeAddress().getPois()
						.get(0).getLatLonPoint());

				infoContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Intent i1 = new Intent();

						double[] sbd_lat_lon = gaoDeToBaidu(sAddressItem
								.getPoint().getLongitude(), sAddressItem
								.getPoint().getLatitude());
						double[] dbd_lat_lon = gaoDeToBaidu(
								Double.valueOf(addressItem.getLon()),
								Double.valueOf(addressItem.getLat()));

						new PopupWindows(ShowMapAddressActivity.this, v, sAddressItem
								.getPoint().getLatitude()+"", sAddressItem
								.getPoint().getLongitude()+"", sAddressItem.getAddress(), sbd_lat_lon[1] + "", sbd_lat_lon[0] + "", sAddressItem.getAddress(), dbd_lat_lon[1] + "",
								dbd_lat_lon[0] + "");
						
//						
//						starBaiduAmap(sbd_lat_lon[1] + "", sbd_lat_lon[0] + "",
//								sAddressItem.getAddress(), dbd_lat_lon[1] + "",
//								dbd_lat_lon[0] + "");
					}
				});

			}
		}

	}

	private void starBaiduAmap(String Bslat, String Bslon, String Bsname,
			String Bdlat, String Bdlon) {
		Intent i1 = new Intent();

		// 公交路线规划

		i1.setData(Uri.parse("baidumap://map/direction?origin=name:" + Bsname
				+ "|latlng:" + Bslat + "," + Bslon + "&destination=name:"
				+ addressItem.getAddress() + "|latlng:" + Bdlat + "," + Bdlon +
				"&mode=transit&sy=3&index=0&target=1"));
//		Log.e("高德ditu++++++++++++++++++++++++++++", addressItem.getAddress() 
//				 +
//				  "");

		startActivity(i1);
	}

	private void startGaodeAmap(String Gslat, String Gslon, String Gsname) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		Uri uri = Uri.parse("androidamap://route?sourceApplication=e族防水&slat="
				+ Gslat + "" + "&slon=" + Gslon + "" + "&" + "sname=" + Gsname
				+ "" + "&dlat=" + addressItem.getLat() + "&dlon="
				+ addressItem.getLon() + "&dname=" + addressItem.getAddress()
				+ "&dev=1&t=1");
		intent.setPackage("com.autonavi.minimap");
		intent.setData(uri);
		 Log.e("高德ditu++++++++++++++++++++++++++++", uri + "");
		startActivity(intent);
	}
	
	
	/**
	 * 
	 * @ClassName: PopupWindows
	 * @Description: 弹出拍照选择PopupWindow
	 * @author lmw
	 * @date 2015年3月30日 上午9:30:41
	 */
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent,final String Gslat, final String Gslon, final String Gsname,final String Bslat, final String Bslon, final String Bsname,
				final String Bdlat, final String Bdlon) {
			super(mContext);
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			
			bt1.setText("高德地图");
			bt2.setText("百度地图");
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isInstallByread("com.autonavi.minimap")) {
						startGaodeAmap(Gslat, Gslon, Gsname);	
					}else{
						T.showToast(ShowMapAddressActivity.this, "请安装高德地图客户端");
					}
					
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					 if (isInstallByread("com.baidu.BaiduMap")) {
					starBaiduAmap(Bslat, Bslon, Bsname, Bdlat, Bdlon);
					 }else{
						 T.showToast(ShowMapAddressActivity.this, "请安装百度地图客户端");
					 }
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
    //判断是否安装目标应用
private boolean isInstallByread(String packageName) {
 return new File("/data/data/" + packageName)
         .exists();
}
	
	
/**
 * \
 * 高德坐标转百度
 * @param gd_lon
 * @param gd_lat
 * @return 
 */
	
	private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
		double[] bd_lat_lon = new double[2];
		double PI = 3.14159265358979324 * 3000.0 / 180.0;
		double x = gd_lon, y = gd_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
		bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
		bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
		return bd_lat_lon;
	}
}
