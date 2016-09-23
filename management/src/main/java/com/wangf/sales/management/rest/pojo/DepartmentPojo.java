package com.wangf.sales.management.rest.pojo;

public class DepartmentPojo {
	private long id;
	private String name;
	private String hospital;

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

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	@Override
	public String toString() {
		return "DepartmentPojo [id=" + id + ", name=" + name + ", hospital=" + hospital + "]";
	}

}
