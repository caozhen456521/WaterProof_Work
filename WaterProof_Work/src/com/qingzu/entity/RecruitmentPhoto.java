package com.qingzu.entity;

import java.io.Serializable;

public class RecruitmentPhoto implements Serializable{
	private int id;//图片编号
	private int infold;//找人信息Id
	private String photoUrl;//施工部位图片url地址
	private int typeId;//图片类型(0施工部位图，1其他)
	private String photoTitle;//图片名称
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInfold() {
		return infold;
	}
	public void setInfold(int infold) {
		this.infold = infold;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getPhotoTitle() {
		return photoTitle;
	}
	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
	}


}
