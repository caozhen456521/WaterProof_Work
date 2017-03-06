package com.qingzu.entity;

import java.io.Serializable;

public class InviteJoinTeamList implements Serializable  {
	private InviteJoinTeamRecord inviteJoinTeam;
	private FindWorkInfo  findWorkInfo;
	private Member member;
	
	

	public InviteJoinTeamRecord getInviteJoinTeam() {
		return inviteJoinTeam;
	}
	public void setInviteJoinTeam(InviteJoinTeamRecord inviteJoinTeam) {
		this.inviteJoinTeam = inviteJoinTeam;
	}
	public FindWorkInfo getFindWorkInfo() {
		return findWorkInfo;
	}
	public void setFindWorkInfo(FindWorkInfo findWorkInfo) {
		this.findWorkInfo = findWorkInfo;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}

}
