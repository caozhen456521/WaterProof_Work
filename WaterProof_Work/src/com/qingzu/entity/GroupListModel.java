package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 返回施工队分组队员对象实体信息 视图模型
 * 
 * @author Johnson
 * 
 */
public class GroupListModel implements Serializable {
	private Groups group;
	private List<TeamMemberListModel> list;

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public List<TeamMemberListModel> getList() {
		return list;
	}

	public void setList(List<TeamMemberListModel> list) {
		this.list = list;
	}

	public GroupListModel(Groups group, List<TeamMemberListModel> list) {
		super();
		this.group = group;
		this.list = list;
	}

	public GroupListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}

