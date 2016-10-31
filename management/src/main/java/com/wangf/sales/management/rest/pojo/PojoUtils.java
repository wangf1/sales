package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.User;

public class PojoUtils {
	public static String getFullName(User user) {
		if (user == null) {
			return "";
		}
		String fullName = user.getFirstName() + user.getLastName();
		return fullName;
	}
}
