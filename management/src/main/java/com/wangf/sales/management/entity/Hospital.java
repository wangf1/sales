package com.wangf.sales.management.entity;

import java.util.ArrayList;
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

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID")
	private List<Department> departments;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID")
	private List<ProductPrice> prices;

	/**
	 * Hospitals managed by who
	 */
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_HOSPITAL", joinColumns = {
			@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "USERNAME", "HOSPITAL_ID" }) })
	private List<User> users;

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

	public List<User> getUsers() {
		if (users == null) {
			return new ArrayList<>();
		}
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<ProductPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hospital other = (Hospital) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name).toString();
		return string;
	}
}
