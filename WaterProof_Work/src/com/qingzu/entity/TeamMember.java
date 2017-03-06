package com.qingzu.entity;

import java.io.Serializable;

/**
 * 施工队组员对象实体信息
 * 
 * @author Johnson
 * 
 */
public class TeamMember implements Serializable {

	private int id;// 施工成员编号
	private int teamId;// 施工队Id
	private int groupId;// 组Id
	private int memberId;// 成员会员Id
	private String memberName;// 成员会员名称
	private String remark;// 成员简介(预备字段)
	private String issueTime;// 录入时间

	public int getId() {
		return id;
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public TeamMember(int id, int teamId, int groupId, int memberId,
			String memberName, String remark, String issueTime) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.groupId = groupId;
		this.memberId = memberId;
		this.memberName = memberName;
		this.remark = remark;
		this.issueTime = issueTime;
	}

	public TeamMember() {
		super();
		// TODO Auto-generated constructor stub
	}

}
