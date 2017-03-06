package com.qingzu.entity;

import java.io.Serializable;

public class UserInfoModel implements Serializable {
	private Member member;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public UserInfoModel(Member member) {
		super();
		this.member = member;
	}

	public UserInfoModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
