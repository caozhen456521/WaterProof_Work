package com.qingzu.entity;

import java.io.Serializable;

/**
 * 材料、商品详细信息表
 * 
 * @author Johnson
 * 
 */
public class OrderFormDetails implements Serializable {
	private int id;

	private String orderCode;

	private int productId;

	private int propertyId;

	private int productValueId;

	private int batchId;

	private int buyNumber;

	private double purchasePrice;

	private double marketPrice;

	private double salePrice;

	private double profitPrice;

	private int bargainFlag;

	private String issueTime;

	private String updateTime;

	private String remark;

	private int orderId;

	public OrderFormDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderFormDetails(int id, String orderCode, int productId,
			int propertyId, int productValueId, int batchId, int buyNumber,
			double purchasePrice, double marketPrice, double salePrice,
			double profitPrice, int bargainFlag, String issueTime,
			String updateTime, String remark, int orderId) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.productId = productId;
		this.propertyId = propertyId;
		this.productValueId = productValueId;
		this.batchId = batchId;
		this.buyNumber = buyNumber;
		this.purchasePrice = purchasePrice;
		this.marketPrice = marketPrice;
		this.salePrice = salePrice;
		this.profitPrice = profitPrice;
		this.bargainFlag = bargainFlag;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
		this.remark = remark;
		this.orderId = orderId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getPropertyId() {
		return this.propertyId;
	}

	public void setProductValueId(int productValueId) {
		this.productValueId = productValueId;
	}

	public int getProductValueId() {
		return this.productValueId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getBatchId() {
		return this.batchId;
	}

	public void setBuyNumber(int buyNumber) {
		this.buyNumber = buyNumber;
	}

	public int getBuyNumber() {
		return this.buyNumber;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getMarketPrice() {
		return this.marketPrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getSalePrice() {
		return this.salePrice;
	}

	public void setProfitPrice(double profitPrice) {
		this.profitPrice = profitPrice;
	}

	public double getProfitPrice() {
		return this.profitPrice;
	}

	public void setBargainFlag(int bargainFlag) {
		this.bargainFlag = bargainFlag;
	}

	public int getBargainFlag() {
		return this.bargainFlag;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getIssueTime() {
		return this.issueTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderId() {
		return this.orderId;
	}
}
