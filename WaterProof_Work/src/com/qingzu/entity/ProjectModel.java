package com.qingzu.entity;

import java.io.Serializable;

public class ProjectModel implements Serializable {
	private Member member;
	private ProjectInfo projectInfo;
	private BossInfo bossInfo;
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
	public BossInfo getBossInfo() {
		return bossInfo;
	}
	public void setBossInfo(BossInfo bossInfo) {
		this.bossInfo = bossInfo;
	}

}
