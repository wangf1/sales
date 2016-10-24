package com.wangf.sales.management.rest.auth;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangf.sales.management.auth.Permission;
import com.wangf.sales.management.auth.ResourcePermission;
import com.wangf.sales.management.auth.RoleResourcePermissionMapping;
import com.wangf.sales.management.auth.RoleResourcePermissions;

public class RoleResourcePermissionMappingTest {
	@Test
	public void testUnmarshal() throws Exception {
		List<String> roles = new ArrayList<>();
		roles.add("Admin");
		roles.add("User");
		roles.add("Manager");
		ResourcePermission permission = RoleResourcePermissions.getResourcePermissionForRoles(roles);
		Assert.assertTrue(permission.getUser().isCreate());
		System.out.println(permission);
	}

	@Test
	public void testMarshal() throws Exception {
		List<RoleResourcePermissionMapping> mappings = new ArrayList<>();
		RoleResourcePermissionMapping mapping = new RoleResourcePermissionMapping();
		mapping.setRole("Admin");
		ResourcePermission resourcePermission = new ResourcePermission();
		Permission department = new Permission();
		resourcePermission.setDepartment(department);
		mapping.setResourcePermission(resourcePermission);
		mappings.add(mapping);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(mappings);
		System.out.println(json);
	}
}
