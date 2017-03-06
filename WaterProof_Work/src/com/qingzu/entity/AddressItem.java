package com.qingzu.entity;

import java.io.Serializable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

/**
 * 
 * @author a 选中的地址以及坐标
 */

public class AddressItem implements Serializable {

	private static final long serialVersionUID = -7620435178023928252L;
	public String address = null;
	public String addressDetails = null;
	public LatLonPoint point = null;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String cityName = null;
	public double latitude = 0;
	public double longitude = 0;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public LatLonPoint getPoint() {
		return point;

	}

	public void setPoint(LatLonPoint point) {
		this.point = point;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

}
