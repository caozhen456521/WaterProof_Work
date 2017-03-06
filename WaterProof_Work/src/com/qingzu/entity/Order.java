package com.qingzu.entity;

import java.io.Serializable;

public class Order implements Serializable {
	private int id;// 订单编号
	private String orderCode;// 订单编码
	private int infoId;// 招天工信息Id
	private String infoName;// 招天工项目名
	private int memberId;// 工人/工长会员Id
	private String memberName;// 工人/工长会员帐号
	private String issueTime;// 创建时间
	private String updateTime;// 更新时间
	private int toMemberId;// 老板会员Id
	private String toMemberName;// 老板会员帐号
	private int stateId;// 状态(0待确认，1已确认，2进行中,3待评价，4已完成，-1不显示)
	private String fromTelephone;// 工人/工长呼出电话
	private String toTelephone;// 老板被呼入电话
	private int isEnable;// 是否显示(0显示，-1不显示)
	private int lastCommentMemberId;// 最后评价者会员Id
	private int teamId;// 施工队Id
	private int dispatchMan;// 派工人数
	private Double payMoney;// 支付工钱金额
	private int isCheckout;// 是否结账(0未结账，1已结账)
	private int bossIsAppraise;// 老板是否评价(0未评价，1已评价)
	private int workerIsAppraise;// 工人是否评价(0未评价，1已评价)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
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

	public int getToMemberId() {
		return toMemberId;
	}

	public void setToMemberId(int toMemberId) {
		this.toMemberId = toMemberId;
	}

	public String getToMemberName() {
		return toMemberName;
	}

	public void setToMemberName(String toMemberName) {
		this.toMemberName = toMemberName;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getFromTelephone() {
		return fromTelephone;
	}

	public void setFromTelephone(String fromTelephone) {
		this.fromTelephone = fromTelephone;
	}

	public String getToTelephone() {
		return toTelephone;
	}

	public void setToTelephone(String toTelephone) {
		this.toTelephone = toTelephone;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getLastCommentMemberId() {
		return lastCommentMemberId;
	}

	public void setLastCommentMemberId(int lastCommentMemberId) {
		this.lastCommentMemberId = lastCommentMemberId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getDispatchMan() {
		return dispatchMan;
	}

	public void setDispatchMan(int dispatchMan) {
		this.dispatchMan = dispatchMan;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public int getIsCheckout() {
		return isCheckout;
	}

	public void setIsCheckout(int isCheckout) {
		this.isCheckout = isCheckout;
	}

	public int getBossIsAppraise() {
		return bossIsAppraise;
	}

	public void setBossIsAppraise(int bossIsAppraise) {
		this.bossIsAppraise = bossIsAppraise;
	}

	public int getWorkerIsAppraise() {
		return workerIsAppraise;
	}

	public void setWorkerIsAppraise(int workerIsAppraise) {
		this.workerIsAppraise = workerIsAppraise;
	}

	public Order(int id, String orderCode, int infoId, String infoName,
			int memberId, String memberName, String issueTime,
			String updateTime, int toMemberId, String toMemberName,
			int stateId, String fromTelephone, String toTelephone,
			int isEnable, int lastCommentMemberId, int teamId, int dispatchMan,
			Double payMoney, int isCheckout, int bossIsAppraise,
			int workerIsAppraise) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.infoId = infoId;
		this.infoName = infoName;
		this.memberId = memberId;
		this.memberName = memberName;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
		this.toMemberId = toMemberId;
		this.toMemberName = toMemberName;
		this.stateId = stateId;
		this.fromTelephone = fromTelephone;
		this.toTelephone = toTelephone;
		this.isEnable = isEnable;
		this.lastCommentMemberId = lastCommentMemberId;
		this.teamId = teamId;
		this.dispatchMan = dispatchMan;
		this.payMoney = payMoney;
		this.isCheckout = isCheckout;
		this.bossIsAppraise = bossIsAppraise;
		this.workerIsAppraise = workerIsAppraise;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

}
