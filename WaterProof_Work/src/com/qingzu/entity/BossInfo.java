package com.qingzu.entity;

import java.io.Serializable;

public class BossInfo implements Serializable{
	private int id;//老板认证信息编号
	private String ProvinceName;//省
	private String CityName;//市
	private String AreaName;//区
	private String Address;//详细地址
	private  int IsContract;//是否有用工合同(0没有,1有)
	private  int IsProvideInsurance;//是否提供保险(0不提供,1提供)
	private  int MemberId;//会员编号
	private  String MemberName;//会员名
	private String ContactName;//联系人
	private String ContactTel;//联系电话
	private  String IssueTime;//添加时间
	private  String UpdateTime;//修改时间
	private  String BossLevel;//信誉评星数
	private  String IsAuthentication;//是否核实认证（0未核实，1已核实，2信息不真实）
	private  int employCount;//发布招工信息量
	private  int   projectCount;//发布工程信息量
	private  String  ServiceMoney;//服务费（10000元/年）
	private  String Unit;//服务费单位（元、万元）
	private  int VIPLevel;//VIP等级
	private  String ServiceStartDay;//服务开始日期
	private  String ServiceEndDay;//服务结束日期
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getAreaName() {
		return AreaName;
	}
	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getIsContract() {
		return IsContract;
	}
	public void setIsContract(int isContract) {
		IsContract = isContract;
	}
	public int getIsProvideInsurance() {
		return IsProvideInsurance;
	}
	public void setIsProvideInsurance(int isProvideInsurance) {
		IsProvideInsurance = isProvideInsurance;
	}
	public int getMemberId() {
		return MemberId;
	}
	public void setMemberId(int memberId) {
		MemberId = memberId;
	}
	public String getMemberName() {
		return MemberName;
	}
	public void setMemberName(String memberName) {
		MemberName = memberName;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	public String getContactTel() {
		return ContactTel;
	}
	public void setContactTel(String contactTel) {
		ContactTel = contactTel;
	}
	public String getIssueTime() {
		return IssueTime;
	}
	public void setIssueTime(String issueTime) {
		IssueTime = issueTime;
	}
	public String getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}
	public String getBossLevel() {
		return BossLevel;
	}
	public void setBossLevel(String bossLevel) {
		BossLevel = bossLevel;
	}
	public String getIsAuthentication() {
		return IsAuthentication;
	}
	public void setIsAuthentication(String isAuthentication) {
		IsAuthentication = isAuthentication;
	}
	
	
	public int getEmployCount() {
		return employCount;
	}
	public void setEmployCount(int employCount) {
		this.employCount = employCount;
	}
	public int getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}
	public String getServiceMoney() {
		return ServiceMoney;
	}
	public void setServiceMoney(String serviceMoney) {
		ServiceMoney = serviceMoney;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public int getVIPLevel() {
		return VIPLevel;
	}
	public void setVIPLevel(int vIPLevel) {
		VIPLevel = vIPLevel;
	}
	public String getServiceStartDay() {
		return ServiceStartDay;
	}
	public void setServiceStartDay(String serviceStartDay) {
		ServiceStartDay = serviceStartDay;
	}
	public String getServiceEndDay() {
		return ServiceEndDay;
	}
	public void setServiceEndDay(String serviceEndDay) {
		ServiceEndDay = serviceEndDay;
	}
}
