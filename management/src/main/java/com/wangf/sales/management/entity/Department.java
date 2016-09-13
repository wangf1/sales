package com.wangf.sales.management.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
