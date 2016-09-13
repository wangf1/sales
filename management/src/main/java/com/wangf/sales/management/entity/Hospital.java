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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private HospitalLevel level;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Province province;

	@ManyToMany
	@JoinTable(name = "HOSPITAL_DEPARTMENT", joinColumns = {
			@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "DEPARTMENT_ID", "HOSPITAL_ID" }) })
	private List<Department> departments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HospitalLevel getLevel() {
		return level;
	}

	public void setLevel(HospitalLevel level) {
		this.level = level;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
