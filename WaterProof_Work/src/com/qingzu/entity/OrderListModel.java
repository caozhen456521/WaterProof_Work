package com.qingzu.entity;

import java.io.Serializable;

public class OrderListModel implements Serializable {
	private Member member;
	private RecruitmentInfo recruitmentInfo;
	private RecruitmentPhoto recruitmentPhoto;
	private Order infoOrder;

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

	public Order getInfoOrder() {
		return infoOrder;
	}

	public void setInfoOrder(Order infoOrder) {
		this.infoOrder = infoOrder;
	}

	public OrderListModel(Member member, RecruitmentInfo recruitmentInfo,
			RecruitmentPhoto recruitmentPhoto, Order infoOrder) {
		super();
		this.member = member;
		this.recruitmentInfo = recruitmentInfo;
		this.recruitmentPhoto = recruitmentPhoto;
		this.infoOrder = infoOrder;
	}

	public OrderListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
