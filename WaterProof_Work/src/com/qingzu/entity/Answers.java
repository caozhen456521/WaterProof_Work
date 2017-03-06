package com.qingzu.entity;

import java.io.Serializable;

public class Answers implements Serializable {
	private int id;// 答案编号
	private int questionsId;// 问题编号
	private int memberId;// 会员编号
	private String aRemark;// 答案内容
	private String aImages;// 答案图片
	private Double adoptionRate;// 答案采纳率
	private int aState;// 审核状态（0未审核，1已审核）
	private int isAdoption;// 采纳状态（0未采纳，1已采纳）
	private String aTag;// 答案标签
	private String aKeyWord;// 答案关键字
	private String issueTime;// 回答时间
	private String updateTime;// 修改时间
	private int commentsCount;
	private int likeCount;
	private int score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionsId() {
		return questionsId;
	}

	public void setQuestionsId(int questionsId) {
		this.questionsId = questionsId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getaRemark() {
		return aRemark;
	}

	public void setaRemark(String aRemark) {
		this.aRemark = aRemark;
	}

	public String getaImages() {
		return aImages;
	}

	public void setaImages(String aImages) {
		this.aImages = aImages;
	}

	public Double getAdoptionRate() {
		return adoptionRate;
	}

	public void setAdoptionRate(Double adoptionRate) {
		this.adoptionRate = adoptionRate;
	}

	public int getaState() {
		return aState;
	}

	public void setaState(int aState) {
		this.aState = aState;
	}

	public int getIsAdoption() {
		return isAdoption;
	}

	public void setIsAdoption(int isAdoption) {
		this.isAdoption = isAdoption;
	}

	public String getaTag() {
		return aTag;
	}

	public void setaTag(String aTag) {
		this.aTag = aTag;
	}

	public String getaKeyWord() {
		return aKeyWord;
	}

	public void setaKeyWord(String aKeyWord) {
		this.aKeyWord = aKeyWord;
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

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
