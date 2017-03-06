package com.qingzu.entity;

import java.util.List;

public class ProvinceModel {
	private String id;
	private String name;
	private List<CityModel> cityList;

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String id, String name, List<CityModel> cityList) {
		super();
		this.id = id;
		this.name = name;
		this.cityList = cityList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}

}
