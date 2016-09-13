package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "DEPARTMENT_NAME_ID", referencedColumnName = "ID")
	private DepartmentName name;

	@ManyToOne
	private Hospital hospital;

	@OneToMany
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private List<ProductInstallLocation> installLocations;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_DEPARTMENT_ID", referencedColumnName = "ID")
	private List<SalesRecord> salesRecords;

	public DepartmentName getName() {
		return name;
	}

	public void setName(DepartmentName name) {
		this.name = name;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospitals(Hospital hospital) {
		this.hospital = hospital;
	}

	public List<ProductInstallLocation> getInstallLocations() {
		return installLocations;
	}

	public void setInstallLocations(List<ProductInstallLocation> installLocations) {
		this.installLocations = installLocations;
	}

	public List<SalesRecord> getSalesRecords() {
		return salesRecords;
	}

	public void setSalesRecords(List<SalesRecord> salesRecords) {
		this.salesRecords = salesRecords;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
