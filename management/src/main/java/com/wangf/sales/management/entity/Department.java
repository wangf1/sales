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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "DEPARTMENT_NAME_ID", referencedColumnName = "ID")
	private DepartmentName name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Hospital hospital;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private List<ProductInstallLocation> installLocations;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_DEPARTMENT_ID", referencedColumnName = "ID")
	private List<SalesRecord> salesRecords;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private List<DepartmentMeeting> departmentMeeting;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public List<DepartmentMeeting> getDepartmentMeeting() {
		return departmentMeeting;
	}

	public void setDepartmentMeeting(List<DepartmentMeeting> departmentMeeting) {
		this.departmentMeeting = departmentMeeting;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name)
				.add("hospital", hospital.getName()).toString();
		return string;
	}
}
