package com.qingzu.entity;

import java.io.Serializable;

public class Information implements Serializable{
	private int id;//公司信息自增id
	private int typeId;//信息类型（1公司简介，2联系我们，3招贤纳士，4帮助文档，5法律声明，6公司公告）
	private String informationTitle;//信息标题
	private String informationRemark;//内容
	private int state;//状态:0是未审核,1已审核
	private Boolean isOutUrl;//是否外链（0不是，1是）
	private String outUrl;//外链跳转URL地址
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getInformationTitle() {
		return informationTitle;
	}
	public void setInformationTitle(String informationTitle) {
		this.informationTitle = informationTitle;
	}
	public String getInformationRemark() {
		return informationRemark;
	}
	public void setInformationRemark(String informationRemark) {
		this.informationRemark = informationRemark;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Boolean getIsOutUrl() {
		return isOutUrl;
	}
	public void setIsOutUrl(Boolean isOutUrl) {
		this.isOutUrl = isOutUrl;
	}
	public String getOutUrl() {
		return outUrl;
	}
	public void setOutUrl(String outUrl) {
		this.outUrl = outUrl;
	}
	

}
