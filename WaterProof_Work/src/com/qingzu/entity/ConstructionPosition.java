package com.qingzu.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *   施工部位
 */

public class ConstructionPosition implements Serializable {

	private int id;
	private String positionName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

}
