package com.qingzu.entity;

import java.io.Serializable;

public class InviteJoinTeamRecord implements Serializable {
	
	private int id;//工长邀请入施工队记录自增Id
	private int foremanMemberId;//工长会员Id
	private int teamId;//邀请加入施工队Id
	private String teamName;//邀请加入施工队名称
	private int memberId;//被邀请会员Id
	private int infoState;//邀请是否同意(0待确认、1已同意，2拒绝加入，-1邀请人取消邀请)
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getForemanMemberId() {
		return foremanMemberId;
	}
	public void setForemanMemberId(int foremanMemberId) {
		this.foremanMemberId = foremanMemberId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getInfoState() {
		return infoState;
	}
	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}
	
	

}
