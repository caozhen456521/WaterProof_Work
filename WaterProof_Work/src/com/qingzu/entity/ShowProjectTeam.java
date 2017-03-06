package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Administrator 晒工程
 */
public class ShowProjectTeam implements Serializable {

	private ProjectCase projectCase;
	private List<ProjectCaseAlbum> projectCaseAlbum;

	public ProjectCase getProjectCase() {
		return projectCase;
	}

	public void setProjectCase(ProjectCase projectCase) {
		this.projectCase = projectCase;
	}

	public List<ProjectCaseAlbum> getProjectCaseAlbum() {
		return projectCaseAlbum;
	}

	public void setProjectCaseAlbum(List<ProjectCaseAlbum> projectCaseAlbum) {
		this.projectCaseAlbum = projectCaseAlbum;
	}

}
