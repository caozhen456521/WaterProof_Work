package com.qingzu.entity;

import java.io.Serializable;

/**
 * 会员认证表
 * 
 * @author Johnson
 * 
 */
public class MemberAuthentication implements Serializable {
	private int id;
	private int memberId;
	private String idCard;
	private String fullName;
	private int sex;
	private String nationality;
	private String idCardFront;
	private String idCardBack;
	private String address;
	private String issuingUnit;
	private String birthday;
	private String startday;
	private String endday;
	private int infoState;
	private String issueTime;
	private String updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(String idCardBack) {
		this.idCardBack = idCardBack;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIssuingUnit() {
		return issuingUnit;
	}

	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getStartday() {
		return startday;
	}

	public void setStartday(String startday) {
		this.startday = startday;
	}

	public String getEndday() {
		return endday;
	}

	public void setEndday(String endday) {
		this.endday = endday;
	}

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
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

	public MemberAuthentication(int id, int memberId, String idCard,
			String fullName, int sex, String nationality, String idCardFront,
			String idCardBack, String address, String issuingUnit,
			String birthday, String startday, String endday, int infoState,
			String issueTime, String updateTime) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.idCard = idCard;
		this.fullName = fullName;
		this.sex = sex;
		this.nationality = nationality;
		this.idCardFront = idCardFront;
		this.idCardBack = idCardBack;
		this.address = address;
		this.issuingUnit = issuingUnit;
		this.birthday = birthday;
		this.startday = startday;
		this.endday = endday;
		this.infoState = infoState;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
	}

	public MemberAuthentication() {
		super();
		// TODO Auto-generated constructor stub
	}

}
