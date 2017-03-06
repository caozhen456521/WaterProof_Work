package com.qingzu.entity;

import java.io.Serializable;

/**
 * vip服务项对象实体信息
 * 
 * @author Johnson
 * 
 */
public class VIPServiceItem implements Serializable {
	private int id;// VIP会员服务项自增Id
	private String serviceName;// 服务名称
	private int rootId;// 顶级编号
	private int parentId;// 父级编号
	private String classPath;// 路径
	private int depth;// 深度
	private int childrens;// 子节点数
	private int orderId;// 排序(越大越靠前)
	private int isView;// 是否显示(0不显示，1显示)
	private String remark;// 服务项说明

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getRootId() {
		return rootId;
	}

	public void setRootId(int rootId) {
		this.rootId = rootId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getChildrens() {
		return childrens;
	}

	public void setChildrens(int childrens) {
		this.childrens = childrens;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getIsView() {
		return isView;
	}

	public void setIsView(int isView) {
		this.isView = isView;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public VIPServiceItem(int id, String serviceName, int rootId, int parentId,
			String classPath, int depth, int childrens, int orderId,
			int isView, String remark) {
		super();
		this.id = id;
		this.serviceName = serviceName;
		this.rootId = rootId;
		this.parentId = parentId;
		this.classPath = classPath;
		this.depth = depth;
		this.childrens = childrens;
		this.orderId = orderId;
		this.isView = isView;
		this.remark = remark;
	}

	public VIPServiceItem() {
		super();
		// TODO Auto-generated constructor stub
	}

}
