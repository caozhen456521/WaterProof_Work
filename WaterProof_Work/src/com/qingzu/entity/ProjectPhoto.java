package com.qingzu.entity;

import java.io.Serializable;

public class ProjectPhoto implements Serializable {
	private int prCaseId;
	private String casePhoto;
	private int photoLength;
	private int photoWidth;

	public int getPhotoLength() {
		return photoLength;
	}

	public void setPhotoLength(int photoLength) {
		this.photoLength = photoLength;
	}

	public int getPhotoWidth() {
		return photoWidth;
	}

	public void setPhotoWidth(int photoWidth) {
		this.photoWidth = photoWidth;
	}

	public void setCasePhoto(String casePhoto) {
		this.casePhoto = casePhoto;
	}

	public int getPrCaseId() {
		return prCaseId;
	}

	public void setPrCaseId(int prCaseId) {
		this.prCaseId = prCaseId;
	}

	public String getCasePhoto() {
		return casePhoto;
	}

}
