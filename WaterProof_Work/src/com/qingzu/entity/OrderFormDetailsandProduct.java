package com.qingzu.entity;

import java.io.Serializable;

/**
 * 材料、商品订单详细记录信息 视图模型
 * 
 * @author Johnson
 */
public class OrderFormDetailsandProduct implements Serializable {
	private OrderFormDetails orderFormDetails;
	private Product product;

	public OrderFormDetails getOrderFormDetails() {
		return orderFormDetails;
	}

	public void setOrderFormDetails(OrderFormDetails orderFormDetails) {
		this.orderFormDetails = orderFormDetails;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public OrderFormDetailsandProduct(OrderFormDetails orderFormDetails,
			Product product) {
		super();
		this.orderFormDetails = orderFormDetails;
		this.product = product;
	}

	public OrderFormDetailsandProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

}