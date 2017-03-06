package com.qingzu.entity;

import java.util.List;

public class CityModel {
	private String id;
	private String name;
	private List<DistrictModel> districtList;

	public CityModel() {
		super();
	}

	public CityModel(String id, String name, List<DistrictModel> districtList) {
		super();
		this.id = id;
		this.name = name;
		this.districtList = districtList;
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

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}

}
