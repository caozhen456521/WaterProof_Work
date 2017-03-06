package com.qingzu.entity;

import java.io.Serializable;

public class WorkInfo implements Serializable {
	private int id; // 工人工长详细信息编号ID
	private String skill; // 本人专业技能
	private int experience; // 本人防水经验
	private String certificateImg; // 技能证照片
	private int workState; // 工作状态（1正在找活，2暂不找活）
	private int isInsurance; // 是否有保险

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSkill() {
		return skill;
	}

	public int getIsInsurance() {
		return isInsurance;
	}

	public void setIsInsurance(int isInsurance) {
		this.isInsurance = isInsurance;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}


	public String getCertificateImg() {
		return certificateImg;
	}

	public void setCertificateImg(String certificateImg) {
		this.certificateImg = certificateImg;
	}

	public int getWorkState() {
		return workState;
	}

	public void setWorkState(int workState) {
		this.workState = workState;
	}

}
