package com.qingzu.entity;

import java.io.Serializable;

public class QuestionsClass implements Serializable {
	private int id;// 类别编号
	private String qcTitle;// 类别标题
	private String qcRemark;// 类别描述
	private int sortId;// 排序
	private int isView;// 是否显示

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQcTitle() {
		return qcTitle;
	}

	public void setQcTitle(String qcTitle) {
		this.qcTitle = qcTitle;
	}

	public String getQcRemark() {
		return qcRemark;
	}

	public void setQcRemark(String qcRemark) {
		this.qcRemark = qcRemark;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getIsView() {
		return isView;
	}

	public void setIsView(int isView) {
		this.isView = isView;
	}

}
