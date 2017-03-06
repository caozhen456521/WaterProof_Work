package com.qingzu.entity;

import java.io.Serializable;

/**
 * 对施工队评价实体信息
 * 
 * @author Johnson
 * 
 */
public class TeamAppraise implements Serializable {
	private Integer infoOrderId;
	private String commentContent;
	private Integer starNumber;

	public Integer getInfoOrderId() {
		return infoOrderId;
	}

	public void setInfoOrderId(Integer infoOrderId) {
		this.infoOrderId = infoOrderId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Integer getStarNumber() {
		return starNumber;
	}

	public void setStarNumber(Integer starNumber) {
		this.starNumber = starNumber;
	}

	public TeamAppraise(Integer infoOrderId, String commentContent,
			Integer starNumber) {
		super();
		this.infoOrderId = infoOrderId;
		this.commentContent = commentContent;
		this.starNumber = starNumber;
	}

	public TeamAppraise() {
		super();
		// TODO Auto-generated constructor stub
	}

}
