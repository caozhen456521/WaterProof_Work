package com.qingzu.utils.tools;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.services.core.LatLonPoint;
import com.qingzu.application.MyApplication;
import com.qingzu.callback.OnLocationListener;


import android.content.Context;

public class AMapLocations {
	private Context mContext = null;
	// 声明AMapLocationClient类对象
	public AMapLocationClient mlocationClient = null;
	// 声明定位回调监听器
	public AMapLocationListener mLocationListener = null;

	private static AMapLocations AMapLocation = null;

	private OnLocationListener onLocationListener = null;

	private AMapLocations(Context context) {
		mContext = context;
		mLocationListener = new MyLocationListener();
		mlocationClient = new AMapLocationClient(mContext);
		// 设置定位回调监听
		InitLocation();
		mlocationClient.setLocationListener(mLocationListener);

	}

	public static AMapLocations getAMapLocations(Context context) {
		//if (AMapLocation == null) {
			AMapLocation = new AMapLocations(context);
	//	}
		return AMapLocation;

	}

	public void setOnLocationListener(OnLocationListener listener) {
		onLocationListener = listener;
	}

	public void InitLocation() {

		// 声明mLocationOption对象
		AMapLocationClientOption mLocationOption = null;
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(true);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		// 给定位客户端对象设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mlocationClient.startLocation();

	}

	public class MyLocationListener implements AMapLocationListener {

		@Override
		public void onLocationChanged(
				com.amap.api.location.AMapLocation amapLocation) {
			// TODO Auto-generated method stub
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					// 定位成功回调信息，设置相关消息

					if (amapLocation.getAddress() != null) {
						
					MyApplication .Longitude = amapLocation.getLongitude();
					MyApplication .Latitude =amapLocation.getLatitude();
//					MyApplication .latLonPoint.setLatitude(arg0)
						onLocationListener.OnLocationSuccesed(amapLocation);
					}
				}
			} else {
				onLocationListener.OnLocationFailed(amapLocation);
			}
			mlocationClient.stopLocation();
		}

	}

}
