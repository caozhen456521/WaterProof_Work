package com.qingzu.entity;

import java.io.Serializable;

public class ConstructionMateria implements Serializable {
  private int  id;
  private String materialName;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getMaterialName() {
	return materialName;
}
public void setMaterialName(String materialName) {
	this.materialName = materialName;
}

}
