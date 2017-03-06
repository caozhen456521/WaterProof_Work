package com.qingzu.entity;

import java.io.Serializable;

public class CreateFindWorkPhotoInfo implements Serializable {
	
	
    private static final long serialVersionUID = -6680457902587956425L;  
	private String photoUrl;
	private String photoTitle;
	private Integer photoLength;
	private Integer photoWidth;
    private Integer id;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoTitle() {
		return photoTitle;
	}

	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
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
