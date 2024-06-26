package com.wangf.sales.management.rest.pojo;

import java.util.Date;
import java.util.List;

import com.wangf.sales.management.entity.Bid;

public class BidPojo extends PoJoBase {
	private long id;

	private Date date;

	private String province;

	private String region;

	private String salesPerson;

	private String description;

	private List<String> products;

	private double biddingPrice;

	/**
	 * The reason why names as bidStatus instead of status is to avoid UI
	 * problem. See comments in RegionMeetings.controller.js#onRefresh() for
	 * details.
	 */
	private String bidStatus;

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

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public double getBiddingPrice() {
		return biddingPrice;
	}

	public void setBiddingPrice(double price) {
		this.biddingPrice = price;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidSatus) {
		this.bidStatus = bidSatus;
	}

	public String getProductNamesString() {
		String namesString = AgencyEventPojo.buildCommaStringFromStringList(getProducts());
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

	@Override
	public String toString() {
		return "BidPojo [id=" + id + ", date=" + date + ", province=" + province + ", region=" + region
				+ ", salesPerson=" + salesPerson + ", description=" + description + ", product=" + products + ", price="
				+ biddingPrice + "]";
	}

	public static BidPojo from(Bid entity) {
		BidPojo pojo = new BidPojo();
		pojo.setDate(entity.getDate());
		pojo.setDescription(entity.getDescription());
		pojo.setId(entity.getId());
		pojo.setBiddingPrice(entity.getPrice());
		pojo.setProducts(AgencyEventPojo.getProductNames(entity.getProducts()));
		pojo.setProvince(entity.getProvince().getName());
		pojo.setRegion(entity.getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));
		pojo.setBidStatus(entity.getStatus());
		if (entity.getLastModifyBy() != null) {
			pojo.setLastModifyBy(PojoUtils.getFullName(entity.getLastModifyBy()));
		}
		pojo.setLastModifyAt(entity.getLastModifyAt());
		return pojo;
	}

}
