package com.qingzu.entity;

public class LoginMember {
	private Member member;
	private String token;
	private long tokenExpiredTimeStamp;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTokenExpiredTimeStamp() {
		return tokenExpiredTimeStamp;
	}

	public void setTokenExpiredTimeStamp(long tokenExpiredTimeStamp) {
		this.tokenExpiredTimeStamp = tokenExpiredTimeStamp;
	}

}
