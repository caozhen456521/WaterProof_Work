package com.qingzu.entity;
/**
 * 根据RegionID获取上级区域(包括本级区域)
 * @author Administrator
 *
 */
public class Region {
	private int regionId;//城市编号
	private int parentId;//上级城市编号
	private int regionLevel;//层级
	private int sortId;//排序值
	private int childrens;//子节点数
	private String regionName;//城市名称
	private String regionEnName;//城市英文名
	private String displayName;//城市简拼
	private String shortName;//城市简称
	private String regionPath;//路径
	private boolean isValid;//是否显示

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(int regionLevel) {
		this.regionLevel = regionLevel;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getChildrens() {
		return childrens;
	}

	public void setChildrens(int childrens) {
		this.childrens = childrens;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionEnName() {
		return regionEnName;
	}

	public void setRegionEnName(String regionEnName) {
		this.regionEnName = regionEnName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getRegionPath() {
		return regionPath;
	}

	public void setRegionPath(String regionPath) {
		this.regionPath = regionPath;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Region(int regionId, int parentId, int regionLevel, int sortId,
			int childrens, String regionName, String regionEnName,
			String displayName, String shortName, String regionPath,
			boolean isValid) {
		super();
		this.regionId = regionId;
		this.parentId = parentId;
		this.regionLevel = regionLevel;
		this.sortId = sortId;
		this.childrens = childrens;
		this.regionName = regionName;
		this.regionEnName = regionEnName;
		this.displayName = displayName;
		this.shortName = shortName;
		this.regionPath = regionPath;
		this.isValid = isValid;
	}

	public Region() {
		super();
		// TODO Auto-generated constructor stub
	}

}
