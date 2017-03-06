package com.qingzu.entity;

import java.io.Serializable;

/**
 * 对工人评价实体信息
 * 
 * @author Johnson
 * 
 */
public class WorkInfoAppraise implements Serializable {
	private Integer toMemberId;
	private Integer infoOrderId;
	private Integer starNumber;
	private String commentContent;

	public Integer getToMemberId() {
		return toMemberId;
	}

	public void setToMemberId(Integer toMemberId) {
		this.toMemberId = toMemberId;
	}

	public Integer getInfoOrderId() {
		return infoOrderId;
	}

	public void setInfoOrderId(Integer infoOrderId) {
		this.infoOrderId = infoOrderId;
	}

	public Integer getStarNumber() {
		return starNumber;
	}

	public void setStarNumber(Integer starNumber) {
		this.starNumber = starNumber;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public WorkInfoAppraise(Integer toMemberId, Integer infoOrderId,
			Integer starNumber, String commentContent) {
		super();
		this.toMemberId = toMemberId;
		this.infoOrderId = infoOrderId;
		this.starNumber = starNumber;
		this.commentContent = commentContent;
	}

	public WorkInfoAppraise() {
		super();
		// TODO Auto-generated constructor stub
	}

}
