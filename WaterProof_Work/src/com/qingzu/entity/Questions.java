package com.qingzu.entity;

import java.io.Serializable;

public class Questions implements Serializable {
	private int id;// 问题编号
	private String qTitle;// 问题标题
	private String qRemark;// 问题内容
	private int browseCount;// 浏览量
	private int rewardPoints;// 悬赏分
	private int memberId;// 会员编号
	private int qState;// 审核状态（0未审核，1已审核）
	private int qSolve;// 问题解决状态（0待解决问题，1已解决问题，-1删除）
	private int answerCount;// 回答数量
	private int classId;// 类别ID
	private String qImages;// 问题图片
	private String qTag;// 问题标签
	private String qKeyWord;// 问题关键字
	private String issueTime;// 添加时间
	private String updateTime;// 修改时间
	private int commentsCount;
	/**
	 * 剩余悬赏分
	 */
	private int rewardPointsLeft;

	public int getRewardPointsLeft() {
		return rewardPointsLeft;
	}

	public void setRewardPointsLeft(int rewardPointsLeft) {
		this.rewardPointsLeft = rewardPointsLeft;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getqTitle() {
		return qTitle;
	}

	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}

	public String getqRemark() {
		return qRemark;
	}

	public void setqRemark(String qRemark) {
		this.qRemark = qRemark;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getqState() {
		return qState;
	}

	public void setqState(int qState) {
		this.qState = qState;
	}

	public int getqSolve() {
		return qSolve;
	}

	public void setqSolve(int qSolve) {
		this.qSolve = qSolve;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getqImages() {
		return qImages;
	}

	public void setqImages(String qImages) {
		this.qImages = qImages;
	}

	public String getqTag() {
		return qTag;
	}

	public void setqTag(String qTag) {
		this.qTag = qTag;
	}

	public String getqKeyWord() {
		return qKeyWord;
	}

	public void setqKeyWord(String qKeyWord) {
		this.qKeyWord = qKeyWord;
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

}
