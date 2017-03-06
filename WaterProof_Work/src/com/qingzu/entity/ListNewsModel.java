package com.qingzu.entity;

import java.io.Serializable;
import java.util.List;

public class ListNewsModel implements Serializable {
	private List<News> materialBrand;
	private List<News> safetyProduct;
	private List<News> toolProduct;
	private List<News> topProduct;
	private List<News> goodProduct;
	private List<News> popularProduct;

	public List<News> getMaterialBrand() {
		return materialBrand;
	}

	public void setMaterialBrand(List<News> materialBrand) {
		this.materialBrand = materialBrand;
	}

	public List<News> getSafetyProduct() {
		return safetyProduct;
	}

	public void setSafetyProduct(List<News> safetyProduct) {
		this.safetyProduct = safetyProduct;
	}

	public List<News> getToolProduct() {
		return toolProduct;
	}

	public void setToolProduct(List<News> toolProduct) {
		this.toolProduct = toolProduct;
	}

	public List<News> getTopProduct() {
		return topProduct;
	}

	public void setTopProduct(List<News> topProduct) {
		this.topProduct = topProduct;
	}

	public List<News> getGoodProduct() {
		return goodProduct;
	}

	public void setGoodProduct(List<News> goodProduct) {
		this.goodProduct = goodProduct;
	}

	public List<News> getPopularProduct() {
		return popularProduct;
	}

	public void setPopularProduct(List<News> popularProduct) {
		this.popularProduct = popularProduct;
	}

}
