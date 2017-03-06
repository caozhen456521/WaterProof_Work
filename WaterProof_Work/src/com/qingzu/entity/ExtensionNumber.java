package com.qingzu.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator 电话分机号 信息
 */
public class ExtensionNumber implements Serializable {
	private int id; // 分机号自增id
	private String number;// 分机号码
	private int infoState;// 使用状态(0未被占用，1已被占用)
	private int infoId;// 绑定信息Id
	private int infoType;// 绑定信息类型(1是招工信息，2是工程承包信息，3是采购，4是申请vip会员，5是找资质，6是找资金，7是找工作)
	private String contactTel;// 对应联系电话
	private String issueTime;//
	private String updateTime;//

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
