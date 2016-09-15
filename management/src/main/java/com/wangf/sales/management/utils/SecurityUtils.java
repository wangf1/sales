package com.wangf.sales.management.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

	public static UserDetails getCurrentUserDetails() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails principal = (UserDetails) securityContext.getAuthentication().getPrincipal();
		return principal;
	}

	public static String getCurrentUserName() {
		UserDetails userDetails = getCurrentUserDetails();
		String userName = userDetails.getUsername();
		return userName;
	}
}
