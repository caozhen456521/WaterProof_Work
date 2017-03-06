package com.qingzu.entity;

import java.io.Serializable;

public class FindWorkInfoListModels implements Serializable {
	
	private FindWorkInfo findWorkInfo;
	private  Member member;
	
	
	public FindWorkInfo getFindWorkInfo() {
		return findWorkInfo;
	}
	public void setFindWorkInfo(FindWorkInfo findWorkInfo) {
		this.findWorkInfo = findWorkInfo;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}

}
