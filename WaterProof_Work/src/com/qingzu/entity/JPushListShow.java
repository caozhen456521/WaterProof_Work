package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

public class JPushListShow implements Serializable {
	private int bizTypeID;
	private String bizTypeName;
	private int newMsgCount;
	private int totalMsgCount;
	private List<MemberMessage> listMessage;

	public int getBizTypeID() {
		return bizTypeID;
	}

	public void setBizTypeID(int bizTypeID) {
		this.bizTypeID = bizTypeID;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public int getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(int newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public int getTotalMsgCount() {
		return totalMsgCount;
	}

	public void setTotalMsgCount(int totalMsgCount) {
		this.totalMsgCount = totalMsgCount;
	}

	public List<MemberMessage> getListMessage() {
		return listMessage;
	}

	public void setListMessage(List<MemberMessage> listMessage) {
		this.listMessage = listMessage;
	}
}
