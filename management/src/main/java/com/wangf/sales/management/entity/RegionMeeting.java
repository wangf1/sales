package com.wangf.sales.management.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.MoreObjects;

@Entity
public class RegionMeeting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Province province;

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

	private Date lastModifyAt;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "LAST_MODIFY_BY", referencedColumnName = "USERNAME")
	private User lastModifyBy;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private DepartmentName departmentName;

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

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
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

	public Date getLastModifyAt() {
		return lastModifyAt;
	}

	public void setLastModifyAt(Date lastModifyAt) {
		this.lastModifyAt = lastModifyAt;
	}

	public User getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(User lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public DepartmentName getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(DepartmentName departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name)
				.add("province", province.getName()).add("type", type).add("form", form).add("status", status)
				.toString();
		return string;
	}

}
