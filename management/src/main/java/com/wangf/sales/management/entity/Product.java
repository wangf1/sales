package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Company company;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<ProductInstallLocation> installLocations;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<ProductPrice> prices;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "AGENCYEVENT_PRODUCT", joinColumns = {
			@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "AGENCYEVENT_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "AGENCYEVENT_ID", "PRODUCT_ID" }) })
	private List<AgencyEvent> agencyEvents;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "BID_PRODUCT", joinColumns = {
			@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "BID_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "BID_ID", "PRODUCT_ID" }) })
	private List<Bid> bids;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<DepartmentMeeting> departmentMeetings;

	/**
	 * For internal use. Used for filter products by usage type.
	 */
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<ProductInstallLocation> getInstallLocations() {
		return installLocations;
	}

	public void setInstallLocations(List<ProductInstallLocation> installLocations) {
		this.installLocations = installLocations;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

	public List<AgencyEvent> getAgencyRecruit() {
		return agencyEvents;
	}

	public void setAgencyRecruit(List<AgencyEvent> agencyEvents) {
		this.agencyEvents = agencyEvents;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public List<DepartmentMeeting> getDepartmentMeetings() {
		return departmentMeetings;
	}

	public void setDepartmentMeetings(List<DepartmentMeeting> departmentMeetings) {
		this.departmentMeetings = departmentMeetings;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name).toString();
		return string;
	}
}
