package com.qingzu.entity;

import java.io.Serializable;

public class RecruitmentInfo implements Serializable {

	private int id;// 招天工信息编号
	private String infoTitle;// 标题
	private String peopleNumber;// 需要人数
	private String provinceName;// 省
	private String cityName;// 市
	private String areaName;// 区
	private String address;// 详细地址
	private String remark;// 备注
	private int infoState;// 信息状态（0未处理，1已推送，2已完成，-1删除）
	private String contactName;// 联系人
	private String contactTel;// 联系电话
	private String issueTime;// 添加时间
	private String updateTime;// 修改时间
	private String startTime;// 用工开始时间
	private int collectionCount;// 收藏次数
	private int browseCount;// 浏览次数
	
   private  String memberName;
	public String getMemberName() {
	return memberName;
}

public void setMemberName(String memberName) {
	this.memberName = memberName;
}

	private String lon;// 经度
	private String lat;// 纬度
	private int dealNumber;// 成交人数
	private int applyNumber;// 报名人数
	private String constructionParts;// 施工部位(多个逗号分割)
	private String claimMaterial;// 材料要求(多个逗号分割)
	private String constructionArea;// 施工面积

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(String peopleNumber) {
		this.peopleNumber = peopleNumber;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(int dealNumber) {
		this.dealNumber = dealNumber;
	}

	public int getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(int applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getConstructionParts() {
		return constructionParts;
	}

	public void setConstructionParts(String constructionParts) {
		this.constructionParts = constructionParts;
	}

	public String getClaimMaterial() {
		return claimMaterial;
	}

	public void setClaimMaterial(String claimMaterial) {
		this.claimMaterial = claimMaterial;
	}

	public String getConstructionArea() {
		return constructionArea;
	}

	public void setConstructionArea(String constructionArea) {
		this.constructionArea = constructionArea;
	}
}
