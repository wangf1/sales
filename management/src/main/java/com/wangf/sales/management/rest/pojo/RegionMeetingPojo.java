package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import com.wangf.sales.management.entity.RegionMeeting;

public class RegionMeetingPojo {
	private long id;

	private Date date;

	private String salesPerson;

	private String name;

	private String region;

	private String province;

	/**
	 * 会议性质
	 */
	private String type;

	/**
	 * 会议形式
	 */
	private String form;

	private int numberOfPeople;

	private String status;

	private double satelliteMeetingCost;
	private double exhibitionCost;
	private double speakerCost;
	private double otherCost;
	private double otherTAndE;

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

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getSatelliteMeetingCost() {
		return satelliteMeetingCost;
	}

	public void setSatelliteMeetingCost(double satelliteMeetingCost) {
		this.satelliteMeetingCost = satelliteMeetingCost;
	}

	public double getExhibitionCost() {
		return exhibitionCost;
	}

	public void setExhibitionCost(double exhibitionCost) {
		this.exhibitionCost = exhibitionCost;
	}

	public double getSpeakerCost() {
		return speakerCost;
	}

	public void setSpeakerCost(double speakerCost) {
		this.speakerCost = speakerCost;
	}

	public double getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(double otherCost) {
		this.otherCost = otherCost;
	}

	public double getOtherTAndE() {
		return otherTAndE;
	}

	public void setOtherTAndE(double otherTAndE) {
		this.otherTAndE = otherTAndE;
	}

	@Override
	public String toString() {
		return "RegionMeetingPojo [id=" + id + ", date=" + date + ", salesPerson=" + salesPerson + ", name=" + name
				+ ", region=" + region + ", province=" + province + ", type=" + type + ", form=" + form
				+ ", numberOfPeople=" + numberOfPeople + ", status=" + status + ", satelliteMeetingCost="
				+ satelliteMeetingCost + ", exhibitionCost=" + exhibitionCost + ", speakerCost=" + speakerCost
				+ ", otherCost=" + otherCost + ", otherTAndE=" + otherTAndE + "]";
	}

	public static RegionMeetingPojo from(RegionMeeting r) {
		RegionMeetingPojo pojo = new RegionMeetingPojo();
		pojo.setDate(r.getDate());
		pojo.setExhibitionCost(r.getExhibitionCost());
		pojo.setForm(r.getForm());
		pojo.setId(r.getId());
		pojo.setName(r.getName());
		pojo.setNumberOfPeople(r.getNumberOfPeople());
		pojo.setOtherCost(r.getOtherCost());
		pojo.setOtherTAndE(r.getOtherTAndE());
		pojo.setProvince(r.getProvince().getName());
		pojo.setRegion(r.getProvince().getRegion());
		pojo.setSalesPerson(r.getSalesPerson().getUserName());
		pojo.setSatelliteMeetingCost(r.getSatelliteMeetingCost());
		pojo.setSpeakerCost(r.getSpeakerCost());
		pojo.setStatus(r.getStatus());
		pojo.setType(r.getType());
		return pojo;
	}
}
