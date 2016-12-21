package com.wangf.sales.management.rest.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wangf.sales.management.entity.Product;

public class AgencyEventPojo extends PoJoBase {
	private long id;

	private Date date;

	private String region;

	private String province;

	private String agency;

	private List<String> products;

	private String salesPerson;

	private String level;

	private Date lastModifyAt;

	private String lastModifyBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public List<String> getProducts() {
		if (products == null) {
			products = new ArrayList<>();
		}
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public static List<String> getProductNames(List<Product> products) {
		List<String> productNames = new ArrayList<>();
		for (Product product : products) {
			productNames.add(product.getName());
		}
		return productNames;
	}

	public String getProductNamesString() {
		String namesString = buildCommaStringFromStringList(getProducts());
		return namesString;
	}

	public Date getLastModifyAt() {
		return lastModifyAt;
	}

	public void setLastModifyAt(Date lastModifyAt) {
		this.lastModifyAt = lastModifyAt;
	}

	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	static String buildCommaStringFromStringList(List<String> strings) {
		String namesString = "";
		for (String name : strings) {
			if (namesString == "") {
				namesString = name;
			} else {
				namesString = namesString + ", " + name;
			}
		}
		return namesString;
	}

	@Override
	public String toString() {
		return "AgencyRecruitPojo [id=" + id + ", date=" + date + ", region=" + region + ", province=" + province
				+ ", agency=" + agency + ", products=" + products + ", salesPerson=" + salesPerson + ", level=" + level
				+ "]";
	}

}
