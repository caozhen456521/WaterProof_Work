package com.qingzu.entity;

import java.io.Serializable;

public class ProjectCase1 implements Serializable {
	private int memberId;
	private int teamId;
	private int infoOrderId;
	private String caseDescribe;

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

}
