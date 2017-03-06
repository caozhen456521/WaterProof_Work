package com.qingzu.entity;

import java.io.Serializable;

public class QuestionsListByPage implements Serializable {
	private Questions questions;
	private Member member;

	public Questions getQuestions() {
		return questions;
	}

	public void setQuestions(Questions questions) {
		this.questions = questions;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
