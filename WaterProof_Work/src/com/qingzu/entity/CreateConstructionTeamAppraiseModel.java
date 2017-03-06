package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 对施工队评价实体信息 视图模型
 * 
 * @author Johnson
 * 
 */
public class CreateConstructionTeamAppraiseModel implements Serializable {

	private TeamAppraise teamAppraise;
	private List<WorkInfoAppraise> workInfoAppraiseList;

	public TeamAppraise getTeamAppraise() {
		return teamAppraise;
	}

	public void setTeamAppraise(TeamAppraise teamAppraise) {
		this.teamAppraise = teamAppraise;
	}

	public List<WorkInfoAppraise> getWorkInfoAppraiseList() {
		return workInfoAppraiseList;
	}

	public void setWorkInfoAppraiseList(
			List<WorkInfoAppraise> workInfoAppraiseList) {
		this.workInfoAppraiseList = workInfoAppraiseList;
	}

	public CreateConstructionTeamAppraiseModel(TeamAppraise teamAppraise,
			List<WorkInfoAppraise> workInfoAppraiseList) {
		super();
		this.teamAppraise = teamAppraise;
		this.workInfoAppraiseList = workInfoAppraiseList;
	}

	public CreateConstructionTeamAppraiseModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
