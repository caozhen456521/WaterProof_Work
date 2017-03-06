package com.qingzu.entity;

import java.io.Serializable;

public class ProjectPushRecord implements Serializable {
	private int id;//工程信息推荐日志编号
	private int projectId;//工程信息Id
	private int teamId;//施工队Id
	private int isDeal;//成交状态（0待处理,1已接单,2未谈成）
	private String  remark;//对接问题备注
	private String  pushTime;//推送时间
	private String  checkTime;//工程队审核时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public int getIsDeal() {
		return isDeal;
	}
	public void setIsDeal(int isDeal) {
		this.isDeal = isDeal;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPushTime() {
		return pushTime;
	}
	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	

}
