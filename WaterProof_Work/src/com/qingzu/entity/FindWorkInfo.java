package com.qingzu.entity;

import java.io.Serializable;

public class FindWorkInfo implements Serializable {
	private int id;

	private String provinceName;

	private String cityName;

	private String areaName;

	private String address;

	private String skill;

	private int experience;

	private String remark;

	private int workState;

	private int infoState;

	private double wageMoney;

	private String unit;

	private int memberId;

	private String memberName;

	private String contactName;

	private String contactTel;

	private String appointmentTime;

	private String issueTime;

	private String updateTime;

	private int collectionCount;

	private int browseCount;

	private double workerLevel;

	private boolean isInsurance;

	private String insuranceCode;

	private boolean isCertificate; //是否持证

	private String certificateImg;

	private int isAuthentication;

	private int workCount;

	private int isForeMan;

	private int vipLeve;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getSkill() {
		return this.skill;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getExperience() {
		return this.experience;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setWorkState(int workState) {
		this.workState = workState;
	}

	public int getWorkState() {
		return this.workState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	public int getInfoState() {
		return this.infoState;
	}

	public void setWageMoney(double wageMoney) {
		this.wageMoney = wageMoney;
	}

	public double getWageMoney() {
		return this.wageMoney;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberId() {
		return this.memberId;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberName() {
		return this.memberName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactTel() {
		return this.contactTel;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getAppointmentTime() {
		return this.appointmentTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getIssueTime() {
		return this.issueTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setCollectionCount(int collectionCount) {
		this.collectionCount = collectionCount;
	}

	public int getCollectionCount() {
		return this.collectionCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getBrowseCount() {
		return this.browseCount;
	}

	public void setWorkerLevel(double workerLevel) {
		this.workerLevel = workerLevel;
	}

	public double getWorkerLevel() {
		return this.workerLevel;
	}

	public void setIsInsurance(boolean isInsurance) {
		this.isInsurance = isInsurance;
	}

	public boolean getIsInsurance() {
		return this.isInsurance;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	public String getInsuranceCode() {
		return this.insuranceCode;
	}

	public void setIsCertificate(boolean isCertificate) {
		this.isCertificate = isCertificate;
	}

	public boolean getIsCertificate() {
		return this.isCertificate;
	}

	public void setCertificateImg(String certificateImg) {
		this.certificateImg = certificateImg;
	}

	public String getCertificateImg() {
		return this.certificateImg;
	}

	public void setIsAuthentication(int isAuthentication) {
		this.isAuthentication = isAuthentication;
	}

	public int getIsAuthentication() {
		return this.isAuthentication;
	}

	public void setWorkCount(int workCount) {
		this.workCount = workCount;
	}

	public int getWorkCount() {
		return this.workCount;
	}

	public void setIsForeMan(int isForeMan) {
		this.isForeMan = isForeMan;
	}

	public int getIsForeMan() {
		return this.isForeMan;
	}

	public void setVipLeve(int vipLeve) {
		this.vipLeve = vipLeve;
	}

	public int getVipLeve() {
		return this.vipLeve;
	}
}
