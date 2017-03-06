package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Administrator 施工队信息 list集合
 * @author 曹振
 */
public class ListConstructionTeam implements Serializable {

	// private ConstructionTeam constructionTeam =null;
	ConstructionTeam constructionTeam;
	private Member  member;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ConstructionTeam getConstructionTeam() {
		return constructionTeam;
	}

	public void setConstructionTeam(ConstructionTeam constructionTeam) {
		this.constructionTeam = constructionTeam;
	}






}
