package com.wangf.sales.management.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_USER = "User";
	public static final String ROLE_READONLY = "ReadOnly";

	private static UserDetails getCurrentUserDetails() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) {
			return null;
		}
		UserDetails principal = (UserDetails) authentication.getPrincipal();
		return principal;
	}

	public static String getCurrentUserName() {
		UserDetails userDetails = getCurrentUserDetails();
		if (userDetails == null) {
			return "";
		}
		String userName = userDetails.getUsername();
		return userName;
	}

	public static List<String> getCurrentUserRoles() {
		List<String> roles = new ArrayList<>();
		UserDetails userDetails = getCurrentUserDetails();
		if (userDetails == null) {
			return roles;
		}
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			roles.add(role);
		}
		return roles;
	}

	public static boolean isCurrentUserAdminOrReadOnlyUser() {
		boolean isAdmin = SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_ADMIN);
		boolean isReadOnly = SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_READONLY);
		return isAdmin || isReadOnly;
	}

	public static boolean isCurrentUserAdmin() {
		boolean isAdmin = SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_ADMIN);
		return isAdmin;
	}
}
