package com.wangf.sales.management.auth;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class RoleResourcePermissions {
	private static List<RoleResourcePermissionMapping> roleResourcePermissionMappings = getRoleResourcePermissionMappings();

	private static List<RoleResourcePermissionMapping> getRoleResourcePermissionMappings() {
		if (roleResourcePermissionMappings != null) {
			return roleResourcePermissionMappings;
		}
		Gson gson = new Gson();
		Reader reader = new InputStreamReader(
				RoleResourcePermissions.class.getResourceAsStream("role_resource_action.json"));
		roleResourcePermissionMappings = gson.fromJson(reader, new TypeToken<List<RoleResourcePermissionMapping>>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return roleResourcePermissionMappings;
	}

	public static ResourcePermission getResourcePermissionForRoles(List<String> roles) {
		List<ResourcePermission> availablePermissions = new ArrayList<>();
		for (String role : roles) {
			for (RoleResourcePermissionMapping mapping : roleResourcePermissionMappings) {
				if (role.equals(mapping.getRole())) {
					availablePermissions.add(mapping.getResourcePermission());
				}
			}
		}
		ResourcePermission permission = mergePermissions(availablePermissions);
		return permission;
	}

	private static ResourcePermission mergePermissions(List<ResourcePermission> availablePermissions) {
		ResourcePermission result = new ResourcePermission();
		for (ResourcePermission resourcePermission : availablePermissions) {
			Permission permission = resourcePermission.getDepartment();
			Permission merged = result.getDepartment();
			mergePermission(permission, merged);

			permission = resourcePermission.getDepartmentName();
			merged = result.getDepartmentName();
			mergePermission(permission, merged);

			permission = resourcePermission.getHospital();
			merged = result.getHospital();
			mergePermission(permission, merged);

			permission = resourcePermission.getProduct();
			merged = result.getProduct();
			mergePermission(permission, merged);

			permission = resourcePermission.getProvince();
			merged = result.getProvince();
			mergePermission(permission, merged);

			permission = resourcePermission.getSalesRecord();
			merged = result.getSalesRecord();
			mergePermission(permission, merged);

			permission = resourcePermission.getUser();
			merged = result.getUser();
			mergePermission(permission, merged);
		}
		return result;
	}

	private static void mergePermission(Permission permission, Permission merged) {
		if (permission.isCreate()) {
			merged.setCreate(true);
		}
		if (permission.isDelete()) {
			merged.setDelete(true);
		}
		if (permission.isRead()) {
			merged.setRead(true);
		}
		if (permission.isUpdate()) {
			merged.setUpdate(true);
		}
	}
}
