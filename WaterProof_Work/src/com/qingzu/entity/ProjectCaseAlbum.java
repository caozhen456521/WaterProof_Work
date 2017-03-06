package com.qingzu.entity;

import java.io.Serializable;

public class ProjectCaseAlbum implements Serializable {
	private int id; // 工程案例相册编号
	private int prCaseId; // 工程案例Id
	private String casePhoto; // 施工现场照片
	private int photoLength; // 照片长度
	private int photoWidth; // 照片宽度
	private int infoState; //  	照片状态(0未审核，1已审核，-1删除)

	public int getId() {
		return id;
	}

	public ProjectCaseAlbum(int id, int prCaseId, String casePhoto,
			int photoLength, int photoWidth, int infoState) {
		super();
		this.id = id;
		this.prCaseId = prCaseId;
		this.casePhoto = casePhoto;
		this.photoLength = photoLength;
		this.photoWidth = photoWidth;
		this.infoState = infoState;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setCasePhoto(String casePhoto) {
		this.casePhoto = casePhoto;
	}

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

	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}
}
