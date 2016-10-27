package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.ProductPrice;

public class ProductPricePojo {
	private long id;
	private String product;
	private String region;
	private String province;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hospital == null) ? 0 : hospital.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPricePojo other = (ProductPricePojo) obj;
		if (hospital == null) {
			if (other.hospital != null)
				return false;
		} else if (!hospital.equals(other.hospital))
			return false;
		if (id != other.id)
			return false;
		return true;
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
		pojo.setRegion(price.getHospital().getProvince().getRegion());
		pojo.setProvince(price.getHospital().getProvince().getName());
		return pojo;
	}

}
