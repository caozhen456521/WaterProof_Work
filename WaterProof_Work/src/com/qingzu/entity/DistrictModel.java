package com.qingzu.entity;

public class DistrictModel {
	private String name;
	private String id;

	public DistrictModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DistrictModel(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
