package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import com.wangf.sales.management.entity.Bid;

public class BidPojo extends PoJoBase {
	private long id;

	private Date date;

	private String province;

	private String region;

	private String salesPerson;

	private String description;

	private String product;

	private double price;

	/**
	 * The reason why names as bidStatus instead of status is to avoid UI
	 * problem. See comments in RegionMeetings.controller.js#onRefresh() for
	 * details.
	 */
	private String bidStatus;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidSatus) {
		this.bidStatus = bidSatus;
	}

	@Override
	public String toString() {
		return "BidPojo [id=" + id + ", date=" + date + ", province=" + province + ", region=" + region
				+ ", salesPerson=" + salesPerson + ", description=" + description + ", product=" + product + ", price="
				+ price + "]";
	}

	public static BidPojo from(Bid entity) {
		BidPojo pojo = new BidPojo();
		pojo.setDate(entity.getDate());
		pojo.setDescription(entity.getDescription());
		pojo.setId(entity.getId());
		pojo.setPrice(entity.getPrice());
		pojo.setProduct(entity.getProduct().getName());
		pojo.setProvince(entity.getProvince().getName());
		pojo.setRegion(entity.getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));
		pojo.setBidStatus(entity.getStatus());
		return pojo;
	}

}
