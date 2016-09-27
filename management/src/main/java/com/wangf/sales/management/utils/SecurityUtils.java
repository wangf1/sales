package com.wangf.sales.management.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_USER = "User";

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

	public static List<String> getCurrentUserRoles() {
		List<String> roles = new ArrayList<>();
		UserDetails userDetails = getCurrentUserDetails();
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			roles.add(role);
		}
		return roles;
	}
}
