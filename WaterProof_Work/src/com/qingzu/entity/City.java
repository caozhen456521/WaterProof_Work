package com.qingzu.entity;

public class City {
	private int id;
	private int teamId;
	private int regionId;
	private String regionName;
	private String regionPath;
	private String issueTime;

	public City(int id, int teamId, int regionId, String regionName,
			String regionPath, String issueTime) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.regionId = regionId;
		this.regionName = regionName;
		this.regionPath = regionPath;
		this.issueTime = issueTime;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionPath() {
		return regionPath;
	}

	public void setRegionPath(String regionPath) {
		this.regionPath = regionPath;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

}
