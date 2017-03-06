package com.qingzu.entity;

import java.io.Serializable;

public class ImsInfo implements Serializable {
	private String adTitle = null; // 广告标题
	private String imageUrl = null; // 广告图片地址
	private String adRemark = null; // 简介
	private String adUrl = null; // 广告转向地址
	private int adTypeId = 0; // 广告类型
	private int id;
	private int operatorId;
	private String operatorName;
	private int state;
	private String startTime;
	private String endTime;
	private String issueTime;
	private String updateTime;
	private String provinceName;
	private String cityName;
	private int isCountry;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getIsCountry() {
		return isCountry;
	}

	public void setIsCountry(int isCountry) {
		this.isCountry = isCountry;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAdRemark() {
		return adRemark;
	}

	public void setAdRemark(String adRemark) {
		this.adRemark = adRemark;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public int getAdTypeId() {
		return adTypeId;
	}

	public void setAdTypeId(int adTypeId) {
		this.adTypeId = adTypeId;
	}

}
