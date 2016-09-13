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

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Company company;

	@OneToMany
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private List<ProductInstallLocation> installLocations;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
