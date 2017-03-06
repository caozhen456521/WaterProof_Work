package com.qingzu.entity;

import java.io.Serializable;

public class MemberIntegralDetailed implements Serializable {
	private String id;// 会员积分明细id
	private String memberId;// 会员id
	private String integralNumber;// 积分值
	private String typeId;// 赚取积分的类别id
	private String typeName;// 赚取积分的类别名称
	private String stateId;// 积分状态(0未用,1已用,2过期)
	private String issueTime;// 挣得积分时间
	private String infoId;// 找人信息id

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getIntegralNumber() {
		return integralNumber;
	}

	public void setIntegralNumber(String integralNumber) {
		this.integralNumber = integralNumber;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

}
