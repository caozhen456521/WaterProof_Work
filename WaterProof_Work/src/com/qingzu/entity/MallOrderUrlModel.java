package com.qingzu.entity;

import java.io.Serializable;

/**
 * 商城订单外链集合 视图模型
 * 
 * @author Johnson
 * 
 */
public class MallOrderUrlModel implements Serializable {

	private String allOrder;// 全部订单外链
	private String obligationOrder;// 待付款订单外链
	private String identifiedOrder;// 付款待确认订单外链
	private String undeliveredOrder;// 待发货订单外链
	private String receivedOrder;// 待收货订单外链
	private String finishOrder;// 已完成订单外链
	private String cancelOrder;// 已取消订单外链

	public String getAllOrder() {
		return allOrder;
	}

	public void setAllOrder(String allOrder) {
		this.allOrder = allOrder;
	}

	public String getObligationOrder() {
		return obligationOrder;
	}

	public void setObligationOrder(String obligationOrder) {
		this.obligationOrder = obligationOrder;
	}

	public String getIdentifiedOrder() {
		return identifiedOrder;
	}

	public void setIdentifiedOrder(String identifiedOrder) {
		this.identifiedOrder = identifiedOrder;
	}

	public String getUndeliveredOrder() {
		return undeliveredOrder;
	}

	public void setUndeliveredOrder(String undeliveredOrder) {
		this.undeliveredOrder = undeliveredOrder;
	}

	public String getReceivedOrder() {
		return receivedOrder;
	}

	public void setReceivedOrder(String receivedOrder) {
		this.receivedOrder = receivedOrder;
	}

	public String getFinishOrder() {
		return finishOrder;
	}

	public void setFinishOrder(String finishOrder) {
		this.finishOrder = finishOrder;
	}

	public String getCancelOrder() {
		return cancelOrder;
	}

	public void setCancelOrder(String cancelOrder) {
		this.cancelOrder = cancelOrder;
	}

	public MallOrderUrlModel(String allOrder, String obligationOrder,
			String identifiedOrder, String undeliveredOrder,
			String receivedOrder, String finishOrder, String cancelOrder) {
		super();
		this.allOrder = allOrder;
		this.obligationOrder = obligationOrder;
		this.identifiedOrder = identifiedOrder;
		this.undeliveredOrder = undeliveredOrder;
		this.receivedOrder = receivedOrder;
		this.finishOrder = finishOrder;
		this.cancelOrder = cancelOrder;
	}

	public MallOrderUrlModel() {
		super();
		// TODO Auto-generated constructor stub
	}

}
