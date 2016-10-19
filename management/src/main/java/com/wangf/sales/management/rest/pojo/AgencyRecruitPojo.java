package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import com.wangf.sales.management.entity.AgencyRecruit;

public class AgencyRecruitPojo {
	private long id;

	private Date date;

	private String region;

	private String province;

	private String agency;

	private String product;

	private String salesPerson;

	private String level;

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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	@Override
	public String toString() {
		return "AgencyRecruitPojo [id=" + id + ", date=" + date + ", region=" + region + ", province=" + province
				+ ", agency=" + agency + ", product=" + product + ", salesPerson=" + salesPerson + ", level=" + level
				+ "]";
	}

	public static AgencyRecruitPojo from(AgencyRecruit entity) {
		AgencyRecruitPojo pojo = new AgencyRecruitPojo();
		pojo.setAgency(entity.getAgency().getName());
		pojo.setDate(entity.getDate());
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getAgency().getLevel());
		pojo.setProduct(entity.getProduct().getName());
		pojo.setProvince(entity.getAgency().getProvince().getName());
		pojo.setRegion(entity.getAgency().getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());

		return pojo;
	}
}