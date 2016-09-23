package com.wangf.sales.management.rest.pojo;

import java.util.List;

import com.wangf.sales.management.entity.Authority;
import com.wangf.sales.management.entity.User;

public class UserPojo {
	private String id;

	private String userName;

	private String password;

	private String firstName;
	private String lastName;

	private String roles;

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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public static UserPojo from(User user) {
		UserPojo pojo = new UserPojo();
		pojo.setId(user.getUserName());
		pojo.setUserName(user.getUserName());
		pojo.setFirstName(user.getFirstName());
		pojo.setLastName(user.getLastName());
		pojo.setPassword(user.getPassword());
		List<Authority> authorities = user.getAuthorities();
		String roles = "";
		if (authorities != null) {
			for (Authority authority : authorities) {
				roles += (authority.getAuthority() + ",");
			}
		}
		if (roles.endsWith(",")) {
			roles = roles.substring(0, roles.length() - 1);
		}
		pojo.setRoles(roles);
		return pojo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
