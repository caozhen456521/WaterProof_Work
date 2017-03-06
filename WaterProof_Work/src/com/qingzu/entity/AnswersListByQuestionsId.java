package com.qingzu.entity;

import java.io.Serializable;

public class AnswersListByQuestionsId implements Serializable {
	private Answers answers;
	private Member member;
	private int ommentsCount;
	private boolean isLike;

	public int getOmmentsCount() {
		return ommentsCount;
	}

	public void setOmmentsCount(int ommentsCount) {
		this.ommentsCount = ommentsCount;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public Answers getAnswers() {
		return answers;
	}

	public void setAnswers(Answers answers) {
		this.answers = answers;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
