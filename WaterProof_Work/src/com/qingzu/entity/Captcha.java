package com.qingzu.entity;

import java.io.Serializable;

/**
 * 图片验证码(图片)
 * @author Johnson
 *
 */
public class Captcha implements Serializable {
	private String id;
	private String imgURL;
	private int typeID;
	private String userMsg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public String getUserMsg() {
		return userMsg;
	}

	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}

}
