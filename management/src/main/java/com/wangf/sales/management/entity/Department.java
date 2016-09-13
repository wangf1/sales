package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToMany
	@JoinTable(name = "HOSPITAL_DEPARTMENT", joinColumns = {
			@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "DEPARTMENT_ID", "HOSPITAL_ID" }) })
	private List<Hospital> hospitals;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Hospital> getHospitals() {
		return hospitals;
	}

	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
