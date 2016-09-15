package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.DepartmentName;

public class DepartmentNamePojo {
	private long id;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DepartmentNamePojo [id=" + id + ", name=" + name + "]";
	}

	public static DepartmentNamePojo from(DepartmentName departmentName) {
		DepartmentNamePojo pojo = new DepartmentNamePojo();
		pojo.setId(departmentName.getId());
		pojo.setName(departmentName.getName());
		return pojo;
	}

}
