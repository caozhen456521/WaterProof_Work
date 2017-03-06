package com.qingzu.entity;

import java.io.Serializable;

public class RecruitModel implements Serializable {
	private Member member;
	private RecruitmentInfo recruitmentInfo;
	private RecruitmentPhoto recruitmentPhoto;
	private BossInfo  bossInfo;
	
	
	public BossInfo getBossInfo() {
		return bossInfo;
	}
	public void setBossInfo(BossInfo bossInfo) {
		this.bossInfo = bossInfo;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}

	public RecruitmentInfo getRecruitmentInfo() {
		return recruitmentInfo;
	}
	public void setRecruitmentInfo(RecruitmentInfo recruitmentInfo) {
		this.recruitmentInfo = recruitmentInfo;
	}
	public RecruitmentPhoto getRecruitmentPhoto() {
		return recruitmentPhoto;
	}
	public void setRecruitmentPhoto(RecruitmentPhoto recruitmentPhoto) {
		this.recruitmentPhoto = recruitmentPhoto;
	}
}
