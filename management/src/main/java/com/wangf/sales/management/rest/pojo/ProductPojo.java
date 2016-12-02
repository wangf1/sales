package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.Product;

public class ProductPojo {
	private long id;
	private String name;
	private String company;
	private String usageType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	@Override
	public String toString() {
		return "ProductPojo [id=" + id + ", name=" + name + ", company=" + company + "]";
	}

	public static ProductPojo from(Product prod) {
		ProductPojo pojo = new ProductPojo();
		pojo.setId(prod.getId());
		pojo.setName(prod.getName());
		pojo.setCompany(prod.getCompany().getName());
		pojo.setUsageType(prod.getUsageType());
		return pojo;
	}

}
