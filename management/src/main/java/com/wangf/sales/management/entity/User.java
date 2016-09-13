package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "USERS")
public class User {
	// userName, password and enabled are required by Spring Security, should
	// NOT be changed!
	@Id
	@Column(name = "username")
	private String userName;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private boolean enabled = true;

	private String firstName;
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "MANAGER", referencedColumnName = "USERNAME")
	private User manager;

	@OneToMany(mappedBy = "manager")
	private List<User> employees;

	/**
	 * authorities is required by spring security, see
	 * http://docs.spring.io/spring-security/site/docs/current/reference/html/
	 * appendix-schema.html
	 */
	@OneToMany
	@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
	private List<Authority> authorities;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
