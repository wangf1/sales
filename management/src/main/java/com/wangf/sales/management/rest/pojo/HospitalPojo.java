package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.Hospital;

public class HospitalPojo {
	private long id;
	private String name;
	private String level;

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "HospitalPojo [id=" + id + ", name=" + name + ", level=" + level + "]";
	}

	public static HospitalPojo from(Hospital hospital) {
		HospitalPojo pojo = new HospitalPojo();
		pojo.setId(hospital.getId());
		pojo.setName(hospital.getName());
		pojo.setLevel(hospital.getLevel().getName());
		return pojo;
	}

}
