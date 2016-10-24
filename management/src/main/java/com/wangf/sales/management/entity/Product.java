package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<AgencyRecruit> agencyRecruit;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<Bid> bids;

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

	public List<AgencyRecruit> getAgencyRecruit() {
		return agencyRecruit;
	}

	public void setAgencyRecruit(List<AgencyRecruit> agencyRecruit) {
		this.agencyRecruit = agencyRecruit;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name).toString();
		return string;
	}
}
