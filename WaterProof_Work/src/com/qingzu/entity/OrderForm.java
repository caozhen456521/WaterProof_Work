package com.qingzu.entity;

import java.io.Serializable;

/**
 * 材料、商品订单信息
 * 
 * @author Johnson
 * 
 */
public class OrderForm implements Serializable{
	private int id;
	private String orderCode;
	private int memberId;
	private int memberAddressId;
	private int companyId;
	private int sellerMemberId;
	private Double freight;
	private String expressName;
	private String expressCode;
	private String remark;
	private int tradingType;
	private int tradingStatus;
	private int bargainFlag;
	private int invoiceType;
	private String invoiceName;
	private String contactName;
	private String contactTel;
	private String issueTime;
	private String updateTime;
	private Double totalAmount;
	private String paymentChannel;
	private String serialNumber;
	private String paymentRemark;

	public OrderForm(int id, String orderCode, int memberId,
			int memberAddressId, int companyId, int sellerMemberId,
			Double freight, String expressName, String expressCode,
			String remark, int tradingType, int tradingStatus, int bargainFlag,
			int invoiceType, String invoiceName, String contactName,
			String contactTel, String issueTime, String updateTime,
			Double totalAmount, String paymentChannel, String serialNumber,
			String paymentRemark) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.memberId = memberId;
		this.memberAddressId = memberAddressId;
		this.companyId = companyId;
		this.sellerMemberId = sellerMemberId;
		this.freight = freight;
		this.expressName = expressName;
		this.expressCode = expressCode;
		this.remark = remark;
		this.tradingType = tradingType;
		this.tradingStatus = tradingStatus;
		this.bargainFlag = bargainFlag;
		this.invoiceType = invoiceType;
		this.invoiceName = invoiceName;
		this.contactName = contactName;
		this.contactTel = contactTel;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
		this.totalAmount = totalAmount;
		this.paymentChannel = paymentChannel;
		this.serialNumber = serialNumber;
		this.paymentRemark = paymentRemark;
	}

	public OrderForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberAddressId() {
		return memberAddressId;
	}

	public void setMemberAddressId(int memberAddressId) {
		this.memberAddressId = memberAddressId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getSellerMemberId() {
		return sellerMemberId;
	}

	public void setSellerMemberId(int sellerMemberId) {
		this.sellerMemberId = sellerMemberId;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getTradingType() {
		return tradingType;
	}

	public void setTradingType(int tradingType) {
		this.tradingType = tradingType;
	}

	public int getTradingStatus() {
		return tradingStatus;
	}

	public void setTradingStatus(int tradingStatus) {
		this.tradingStatus = tradingStatus;
	}

	public int getBargainFlag() {
		return bargainFlag;
	}

	public void setBargainFlag(int bargainFlag) {
		this.bargainFlag = bargainFlag;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPaymentRemark() {
		return paymentRemark;
	}

	public void setPaymentRemark(String paymentRemark) {
		this.paymentRemark = paymentRemark;
	}

}
