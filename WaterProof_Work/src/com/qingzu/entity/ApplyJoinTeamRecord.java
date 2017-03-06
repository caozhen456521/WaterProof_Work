package com.qingzu.entity;

import java.io.Serializable;

public class ApplyJoinTeamRecord implements Serializable {

	private int id;// 工人申请加入施工队记录自增Id
	private int memberId;// 申请人会员Id
	private int teamId;// 申请加入施工队Id
	private int infoState;// 申请是否通过(0待审核、1审核通过，2拒绝加入，-1申请人取消申请)
	private String issueTime;// 申请时间
	private String checkTime;// 申请审核时间

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

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public ApplyJoinTeamRecord(int id, int memberId, int teamId, int infoState,
			String issueTime, String checkTime) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.teamId = teamId;
		this.infoState = infoState;
		this.issueTime = issueTime;
		this.checkTime = checkTime;
	}

	public ApplyJoinTeamRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

}
