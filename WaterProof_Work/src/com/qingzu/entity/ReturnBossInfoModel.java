package com.qingzu.entity;

import java.io.Serializable;

public class ReturnBossInfoModel implements Serializable {
	private BossInfo bossInfo;

	public BossInfo getBossInfo() {
		return bossInfo;
	}

	public void setBossInfo(BossInfo bossInfo) {
		this.bossInfo = bossInfo;
	}

}
