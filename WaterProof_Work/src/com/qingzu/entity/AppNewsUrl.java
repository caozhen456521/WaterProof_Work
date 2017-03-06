package com.qingzu.entity;

import java.io.Serializable;

public class AppNewsUrl implements Serializable {

	private String material ; //防水材料
	private String assistmaterial ; //辅材
	private String tool; // 劳保用品

	public AppNewsUrl(String material, String assistmaterial, String tool) {
		super();
		this.material = material;
		this.assistmaterial = assistmaterial;
		this.tool = tool;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getAssistmaterial() {
		return assistmaterial;
	}

	public void setAssistmaterial(String assistmaterial) {
		this.assistmaterial = assistmaterial;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}
	
}
