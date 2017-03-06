package com.qingzu.entity;

import java.io.Serializable;

public class BeInviteJoinTeamList implements Serializable{
	
	
	private InviteJoinTeamRecord inviteJoinTeam;
	private ConstructionTeam constructionTeam;
	
	
	public InviteJoinTeamRecord getInviteJoinTeam() {
		return inviteJoinTeam;
	}
	public void setInviteJoinTeam(InviteJoinTeamRecord inviteJoinTeam) {
		this.inviteJoinTeam = inviteJoinTeam;
	}
	public ConstructionTeam getConstructionTeam() {
		return constructionTeam;
	}
	public void setConstructionTeam(ConstructionTeam constructionTeam) {
		this.constructionTeam = constructionTeam;
	}
	

}
