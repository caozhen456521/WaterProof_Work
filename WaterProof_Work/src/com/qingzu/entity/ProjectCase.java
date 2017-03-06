package com.qingzu.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator 晒工程文字介绍
 */
public class ProjectCase implements Serializable {
	private int id; // 	施工队工程案例编号
	private int teamId;  // 施工队Id(为零就是个人晒工程)
	private int infoOrderId; //	干活订单Id 
	private String caseDescribe; // 案例描述
	private String issueTime; // 上传时间
	private int infoState; // 案例状态(0未审核，1已审核，-1删除)
	private int memberId; // 	会员Id

	public int getId() {
		return id;
	}

	public ProjectCase(int id, int teamId, int infoOrderId,
			String caseDescribe, String issueTime, int infoState, int memberId) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.infoOrderId = infoOrderId;
		this.caseDescribe = caseDescribe;
		this.issueTime = issueTime;
		this.infoState = infoState;
		this.memberId = memberId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getInfoOrderId() {
		return infoOrderId;
	}

	public void setInfoOrderId(int infoOrderId) {
		this.infoOrderId = infoOrderId;
	}

	public String getCaseDescribe() {
		return caseDescribe;
	}

	public void setCaseDescribe(String caseDescribe) {
		this.caseDescribe = caseDescribe;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

}
