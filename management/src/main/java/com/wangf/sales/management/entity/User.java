package com.wangf.sales.management.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "USERS")
public class User {
	// userName, password and enabled are required by Spring Security, should
	// NOT be changed!
	@Id
	@Column(name = "username")
	private String userName;

	@JsonIgnore
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private boolean enabled = true;

	private String firstName;
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "MANAGER", referencedColumnName = "USERNAME")
	private User manager;

	@JsonIgnore
	@OneToMany(mappedBy = "manager")
	private List<User> employees;

	/**
	 * Sales persons who manage these hospitals.
	 */
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "USER_HOSPITAL", joinColumns = {
			@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME") }, inverseJoinColumns = {
					@JoinColumn(name = "HOSPITAL_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "USERNAME", "HOSPITAL_ID" }) })
	private List<Hospital> hospitals;

	/**
	 * authorities is required by spring security, see
	 * http://docs.spring.io/spring-security/site/docs/current/reference/html/
	 * appendix-schema.html
	 */
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
	private List<Authority> authorities;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private List<SalesRecord> salesRecords;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public List<Hospital> getHospitals() {
		if (hospitals == null) {
			hospitals = new ArrayList<>();
		}
		return hospitals;
	}

	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

	public List<SalesRecord> getSalesRecords() {
		return salesRecords;
	}

	public void setSalesRecords(List<SalesRecord> salesRecords) {
		this.salesRecords = salesRecords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {

		String string = MoreObjects.toStringHelper(this.getClass()).add("userName", userName)
				.add("firstName", firstName).add("lastName", lastName).toString();
		return string;
	}
}
