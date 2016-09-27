package com.wangf.sales.management.auth;

public class RoleResourcePermissionMapping {
	private String role;
	private ResourcePermission resourcePermission;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public ResourcePermission getResourcePermission() {
		return resourcePermission;
	}

	public void setResourcePermission(ResourcePermission resourcePermission) {
		this.resourcePermission = resourcePermission;
	}

}
