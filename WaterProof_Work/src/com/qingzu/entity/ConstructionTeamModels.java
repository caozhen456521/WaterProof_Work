package com.qingzu.entity;

import java.io.Serializable;

public class ConstructionTeamModels implements Serializable {
	private ConstructionTeam constructionTeam;
	private Member member;

	public ConstructionTeam getConstructionTeam() {
		return constructionTeam;
	}

	public void setConstructionTeam(ConstructionTeam constructionTeam) {
		this.constructionTeam = constructionTeam;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ConstructionTeamModels(ConstructionTeam constructionTeam,
			Member member) {
		super();
		this.constructionTeam = constructionTeam;
		this.member = member;
	}

	public ConstructionTeamModels() {
		super();
	}

}
