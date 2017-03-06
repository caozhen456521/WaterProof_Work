package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 材料、商品订单综合信息
 * 
 * @author Johnson
 * 
 */
public class OrderFormListModel implements Serializable {

	private OrderForm orderForm;
	private List<OrderFormDetailsandProduct> listDetail;

	public OrderFormListModel(OrderForm orderForm,
			List<OrderFormDetailsandProduct> listDetail) {
		super();
		this.orderForm = orderForm;
		this.listDetail = listDetail;
	}

	public OrderFormListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;
	}

	public List<OrderFormDetailsandProduct> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<OrderFormDetailsandProduct> listDetail) {
		this.listDetail = listDetail;
	}

}
