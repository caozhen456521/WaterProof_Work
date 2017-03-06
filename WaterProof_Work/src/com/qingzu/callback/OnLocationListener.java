package com.qingzu.callback;

import com.amap.api.location.AMapLocation;

public interface OnLocationListener {
	/**
	 * 
	 * @Title: OnLocationSuccesed
	 * @Description: 数据正常返回
	 * @author lmw
	 * @param status
	 * @return void 返回类型
	 */
	public void OnLocationSuccesed(AMapLocation amapLocation);
	
	
	/**
	 * 
	 * @Title: OnLocationSuccesed
	 * @Description: 数据正常返回
	 * @author lmw
	 * @param status
	 * @return void 返回类型
	 */
	public void OnLocationSuccesedGetCode(AMapLocation amapLocation);


	/**
	 * 
	 * @Title: OnLocationFailed
	 * @Description: 数据正常返回
	 * @author lmw
	 * @param status
	 * @return void 返回类型
	 */
	
	public void OnLocationFailed(AMapLocation amapLocation);

}
