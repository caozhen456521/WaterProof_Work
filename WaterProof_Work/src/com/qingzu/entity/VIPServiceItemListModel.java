package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 返回vip服务项对象实体信息 视图模型
 * 
 * @author Johnson
 * 
 */
public class VIPServiceItemListModel implements Serializable {
	private VIPServiceItem vipServiceItem;
	private List<VIPServiceItem> list;

	public VIPServiceItem getVipServiceItem() {
		return vipServiceItem;
	}

	public void setVipServiceItem(VIPServiceItem vipServiceItem) {
		this.vipServiceItem = vipServiceItem;
	}

	public VIPServiceItemListModel(VIPServiceItem vipServiceItem,
			List<VIPServiceItem> list) {
		super();
		this.vipServiceItem = vipServiceItem;
		this.list = list;
	}

	public List<VIPServiceItem> getList() {
		return list;
	}

	public void setList(List<VIPServiceItem> list) {
		this.list = list;
	}

	public VIPServiceItemListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
