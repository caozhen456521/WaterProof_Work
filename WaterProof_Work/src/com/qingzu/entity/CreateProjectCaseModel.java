package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

public class CreateProjectCaseModel implements Serializable {
	private List<ProjectPhoto> projectPhoto;
	private ProjectCase1 projectCase;

	public List<ProjectPhoto> getProjectPhoto() {
		return projectPhoto;
	}

	public void setProjectPhoto(List<ProjectPhoto> projectPhoto) {
		this.projectPhoto = projectPhoto;
	}

	public ProjectCase1 getProjectCase() {
		return projectCase;
	}

	public void setProjectCase(ProjectCase1 projectCase) {
		this.projectCase = projectCase;
	}

}
