package com.qingzu.entity;

import java.io.Serializable;

/**
 * 施工队组员对象实体详细信息列表
 * 
 * @author Johnson
 * 
 */
public class TeamMemberListModel implements Serializable {
	private TeamMember teamMember;
	private Member member;
	private int workState;

	public TeamMember getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getWorkState() {
		return workState;
	}

	public void setWorkState(int workState) {
		this.workState = workState;
	}

	public TeamMemberListModel(TeamMember teamMember, Member member,
			int workState) {
		super();
		this.teamMember = teamMember;
		this.member = member;
		this.workState = workState;
	}

	public TeamMemberListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}