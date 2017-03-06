package com.qingzu.entity;

import java.io.Serializable;

public class ApplyJoinTeamRecordListModels implements Serializable {
	private ApplyJoinTeamRecord applyJoinTeamRecord;
	private Member member;

	public ApplyJoinTeamRecord getApplyJoinTeamRecord() {
		return applyJoinTeamRecord;
	}

	public void setApplyJoinTeamRecord(ApplyJoinTeamRecord applyJoinTeamRecord) {
		this.applyJoinTeamRecord = applyJoinTeamRecord;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ApplyJoinTeamRecordListModels(
			ApplyJoinTeamRecord applyJoinTeamRecord, Member member) {
		super();
		this.applyJoinTeamRecord = applyJoinTeamRecord;
		this.member = member;
	}

	public ApplyJoinTeamRecordListModels() {
		super();
		// TODO Auto-generated constructor stub
	}

}
