package com.qingzu.entity;

public class UploadPhoto {
	private Integer infoId;
	private String photoTitle;
	private String photoUrl;
	private Integer photoLength;
	private Integer photoWidth;

	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	public String getPhotoTitle() {
		return photoTitle;
	}

	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getPhotoLength() {
		return photoLength;
	}

	public void setPhotoLength(Integer photoLength) {
		this.photoLength = photoLength;
	}

	public Integer getPhotoWidth() {
		return photoWidth;
	}

	public void setPhotoWidth(Integer photoWidth) {
		this.photoWidth = photoWidth;
	}
}
