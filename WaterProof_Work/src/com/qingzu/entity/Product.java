package com.qingzu.entity;

import java.io.Serializable;

/**
 * 材料、商品基本信息表
 * 
 * @author Johnson
 * 
 */
public class Product implements Serializable {
	private int id;

	private int classId;

	private int rootClassId;

	private String classPath;

	private int companyId;

	private int memberId;

	private String productName;

	private String productBrands;

	private String productFactory;

	private String productJianPin;

	private String productPhoto;

	private String productSearch;

	private String remark;

	private int isEnable;

	private String issueTime;

	private String updateTime;

	private boolean islinkingOut;

	private String linkingOut;

	private boolean isSplitZero;

	private int totalStock;

	private int sortValue;

	private boolean isTop;

	private boolean isGood;

	private boolean isPopular;

	private int salesType;

	private int saleCount;

	private int startSel;

	private int brandsId;

	private String startTime;

	private String endTime;

	private String productCode;

	private String productUnit;

	private double marketPrice;

	private double sellPrice;

	private double memberPrice;

	private double purchasePrice;

	private String productModel;

	private String priceUnit;

	private int paymentType;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(int id, int classId, int rootClassId, String classPath,
			int companyId, int memberId, String productName,
			String productBrands, String productFactory, String productJianPin,
			String productPhoto, String productSearch, String remark,
			int isEnable, String issueTime, String updateTime,
			boolean islinkingOut, String linkingOut, boolean isSplitZero,
			int totalStock, int sortValue, boolean isTop, boolean isGood,
			boolean isPopular, int salesType, int saleCount, int startSel,
			int brandsId, String startTime, String endTime, String productCode,
			String productUnit, double marketPrice, double sellPrice,
			double memberPrice, double purchasePrice, String productModel,
			String priceUnit, int paymentType) {
		super();
		this.id = id;
		this.classId = classId;
		this.rootClassId = rootClassId;
		this.classPath = classPath;
		this.companyId = companyId;
		this.memberId = memberId;
		this.productName = productName;
		this.productBrands = productBrands;
		this.productFactory = productFactory;
		this.productJianPin = productJianPin;
		this.productPhoto = productPhoto;
		this.productSearch = productSearch;
		this.remark = remark;
		this.isEnable = isEnable;
		this.issueTime = issueTime;
		this.updateTime = updateTime;
		this.islinkingOut = islinkingOut;
		this.linkingOut = linkingOut;
		this.isSplitZero = isSplitZero;
		this.totalStock = totalStock;
		this.sortValue = sortValue;
		this.isTop = isTop;
		this.isGood = isGood;
		this.isPopular = isPopular;
		this.salesType = salesType;
		this.saleCount = saleCount;
		this.startSel = startSel;
		this.brandsId = brandsId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.productCode = productCode;
		this.productUnit = productUnit;
		this.marketPrice = marketPrice;
		this.sellPrice = sellPrice;
		this.memberPrice = memberPrice;
		this.purchasePrice = purchasePrice;
		this.productModel = productModel;
		this.priceUnit = priceUnit;
		this.paymentType = paymentType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setRootClassId(int rootClassId) {
		this.rootClassId = rootClassId;
	}

	public int getRootClassId() {
		return this.rootClassId;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getCompanyId() {
		return this.companyId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberId() {
		return this.memberId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductBrands(String productBrands) {
		this.productBrands = productBrands;
	}

	public String getProductBrands() {
		return this.productBrands;
	}

	public void setProductFactory(String productFactory) {
		this.productFactory = productFactory;
	}

	public String getProductFactory() {
		return this.productFactory;
	}

	public void setProductJianPin(String productJianPin) {
		this.productJianPin = productJianPin;
	}

	public String getProductJianPin() {
		return this.productJianPin;
	}

	public void setProductPhoto(String productPhoto) {
		this.productPhoto = productPhoto;
	}

	public String getProductPhoto() {
		return this.productPhoto;
	}

	public void setProductSearch(String productSearch) {
		this.productSearch = productSearch;
	}

	public String getProductSearch() {
		return this.productSearch;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getIsEnable() {
		return this.isEnable;
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

	public void setIslinkingOut(boolean islinkingOut) {
		this.islinkingOut = islinkingOut;
	}

	public boolean getIslinkingOut() {
		return this.islinkingOut;
	}

	public void setLinkingOut(String linkingOut) {
		this.linkingOut = linkingOut;
	}

	public String getLinkingOut() {
		return this.linkingOut;
	}

	public void setIsSplitZero(boolean isSplitZero) {
		this.isSplitZero = isSplitZero;
	}

	public boolean getIsSplitZero() {
		return this.isSplitZero;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}

	public int getTotalStock() {
		return this.totalStock;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public int getSortValue() {
		return this.sortValue;
	}

	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}

	public boolean getIsTop() {
		return this.isTop;
	}

	public void setIsGood(boolean isGood) {
		this.isGood = isGood;
	}

	public boolean getIsGood() {
		return this.isGood;
	}

	public void setIsPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}

	public boolean getIsPopular() {
		return this.isPopular;
	}

	public void setSalesType(int salesType) {
		this.salesType = salesType;
	}

	public int getSalesType() {
		return this.salesType;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public int getSaleCount() {
		return this.saleCount;
	}

	public void setStartSel(int startSel) {
		this.startSel = startSel;
	}

	public int getStartSel() {
		return this.startSel;
	}

	public void setBrandsId(int brandsId) {
		this.brandsId = brandsId;
	}

	public int getBrandsId() {
		return this.brandsId;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductUnit() {
		return this.productUnit;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getMarketPrice() {
		return this.marketPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getSellPrice() {
		return this.sellPrice;
	}

	public void setMemberPrice(double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public double getMemberPrice() {
		return this.memberPrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductModel() {
		return this.productModel;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getPriceUnit() {
		return this.priceUnit;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public int getPaymentType() {
		return this.paymentType;
	}
}
