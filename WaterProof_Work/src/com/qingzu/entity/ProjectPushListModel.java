package com.qingzu.entity;

import java.io.Serializable;

public class ProjectPushListModel implements Serializable {

	private ProjectPushRecord projectPushRecord;//工程推送消息实体信息
	private ProjectInfo projectInfo;//防水工程实体信息
	
	
	public ProjectPushRecord getProjectPushRecord() {
		return projectPushRecord;
	}
	public void setProjectPushRecord(ProjectPushRecord projectPushRecord) {
		this.projectPushRecord = projectPushRecord;
	}
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

}
