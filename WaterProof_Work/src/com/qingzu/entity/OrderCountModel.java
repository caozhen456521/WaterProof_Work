package com.qingzu.entity;

import java.io.Serializable;

public class OrderCountModel implements Serializable{
	private int unConfirmOrder;//待确认订单数量
	private int onGoingOrder;//进行中订单数量
	private int finishOrder;//已完成订单数量
	private int noevaluatOrder;//待评价订单数量
	
	public int getUnConfirmOrder() {
		return unConfirmOrder;
	}
	public void setUnConfirmOrder(int unConfirmOrder) {
		this.unConfirmOrder = unConfirmOrder;
	}
	public int getOnGoingOrder() {
		return onGoingOrder;
	}
	public void setOnGoingOrder(int onGoingOrder) {
		this.onGoingOrder = onGoingOrder;
	}
	public int getFinishOrder() {
		return finishOrder;
	}
	public void setFinishOrder(int finishOrder) {
		this.finishOrder = finishOrder;
	}
	public int getNoevaluatOrder() {
		return noevaluatOrder;
	}
	public void setNoevaluatOrder(int noevaluatOrder) {
		this.noevaluatOrder = noevaluatOrder;
	}
	

}
