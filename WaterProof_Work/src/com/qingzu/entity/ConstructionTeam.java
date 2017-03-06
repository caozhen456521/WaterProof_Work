package com.qingzu.entity;

import java.io.Serializable;

public class ConstructionTeam implements Serializable {
	private int id;// 施工队编号
	private int memberId;// 工长会员编号
	private String memberName;// 工长会员帐号
	private String teamName;// 施工队队名
	private String leaderName;// 工长姓名
	private String leaderTel;// 工长联系电话
	private String provinceName;// 省
	private String cityName;// 市
	private String areaName;// 区
	private String address;// 详细地址
	private String teamStrength;// 施工队实力介绍
	private double depositMoney;// 保证金
	private String unit;// 保证金单位（元、万元）
	private double constructionLevel;// 施工等级
	private int dealNumber;// 成交次数
	private int cancelNumber;// 失败次数
	private int creditStars;// 施工队信誉评星数
	private int workState;// 工作状态（1空闲，2忙碌）
	private int infoState;// 施工队状态（0待审核，1已审核，-1已删除）
	private int collectionCount;// 收藏次数
	private int browseCount;// 浏览次数
	private String issueTime;// 添加时间
	private String updateTime;// 修改时间
	private String alreadyNumber;//工人数量
	public String getAlreadyNumber() {
		return alreadyNumber;
	}

	public void setAlreadyNumber(String alreadyNumber) {
		this.alreadyNumber = alreadyNumber;
	}

	private int maxNumber;// 队员数量上线

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getLeaderTel() {
		return leaderTel;
	}

	public void setLeaderTel(String leaderTel) {
		this.leaderTel = leaderTel;
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

	public String getTeamStrength() {
		return teamStrength;
	}

	public void setTeamStrength(String teamStrength) {
		this.teamStrength = teamStrength;
	}

	public double getDepositMoney() {
		return depositMoney;
	}

	public void setDepositMoney(double depositMoney) {
		this.depositMoney = depositMoney;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getConstructionLevel() {
		return constructionLevel;
	}

	public void setConstructionLevel(double constructionLevel) {
		this.constructionLevel = constructionLevel;
	}

	public int getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(int dealNumber) {
		this.dealNumber = dealNumber;
	}

	public int getCancelNumber() {
		return cancelNumber;
	}

	public void setCancelNumber(int cancelNumber) {
		this.cancelNumber = cancelNumber;
	}

	public int getCreditStars() {
		return creditStars;
	}

	public void setCreditStars(int creditStars) {
		this.creditStars = creditStars;
	}

	public int getWorkState() {
		return workState;
	}

	public void setWorkState(int workState) {
		this.workState = workState;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
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

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public ConstructionTeam(int id, int memberId, String memberName,
			String teamName, String leaderName, String leaderTel,
			String provinceName, String cityName, String areaName,
			String address, String teamStrength, double depositMoney,
			String unit, double constructionLevel, int dealNumber,
			int cancelNumber, int creditStars, int workState, int infoState,
			int collectionCount, int browseCount, String issueTime,
			String updateTime, int maxNumber) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.memberName = memberName;
		this.teamName = teamName;
		this.leaderName = leaderName;
		this.leaderTel = leaderTel;
		this.provinceName = provinceName;
		this.cityName = cityName;
		this.areaName = areaName;
		this.address = address;
		this.teamStrength = teamStrength;
		this.depositMoney = depositMoney;
		this.unit = unit;
		this.constructionLevel = constructionLevel;
		this.dealNumber = dealNumber;
		this.cancelNumber = cancelNumber;
		this.creditStars = creditStars;
		this.workState = workState;
		this.infoState = infoState;
		this.collectionCount = collectionCount;
		this.browseCount = browseCount;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
		this.maxNumber = maxNumber;
	}

	public ConstructionTeam() {
		super();
		// TODO Auto-generated constructor stub
	}

}
