package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.User;

public class UserPojo {
	private String id;

	private String password;

	private String firstName;
	private String lastName;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static UserPojo from(User user) {
		UserPojo pojo = new UserPojo();
		pojo.setId(user.getUserName());
		pojo.setFirstName(user.getFirstName());
		pojo.setLastName(user.getLastName());
		pojo.setPassword(user.getPassword());
		return pojo;
	}

}
