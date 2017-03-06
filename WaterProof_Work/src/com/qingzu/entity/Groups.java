package com.qingzu.entity;

import java.io.Serializable;

/**
 * 施工队分组对象实体信息
 * 
 * @author Johnson
 * 
 */
public class Groups implements Serializable {
	private int id;// 施工队创建组编号
	private int teamId;// 施工队Id
	private String groupName;// 组名称
	private String issueTime;// 创建时间
	private boolean isDefault;// 是否默认组

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Groups(int id, int teamId, String groupName, String issueTime,
			boolean isDefault) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.groupName = groupName;
		this.issueTime = issueTime;
		this.isDefault = isDefault;
	}

	public Groups() {
		super();
		// TODO Auto-generated constructor stub
	}

}
