package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.ProductPrice;

public class ProductPricePojo {
	private long id;
	private String product;
	private String hospital;
	private double price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductPricePojo [id=" + id + ", product=" + product + ", hospital=" + hospital + ", price=" + price
				+ "]";
	}

	public static ProductPricePojo from(ProductPrice price) {
		ProductPricePojo pojo = new ProductPricePojo();
		pojo.setId(price.getId());
		pojo.setHospital(price.getHospital().getName());
		pojo.setProduct(price.getProduct().getName());
		pojo.setPrice(price.getPrice());
		return pojo;
	}

}