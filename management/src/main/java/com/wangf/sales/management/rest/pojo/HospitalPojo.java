package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.Hospital;

public class HospitalPojo {
	private long id;
	private String name;
	private String level;
	private String province;
	private String region;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "HospitalPojo [id=" + id + ", name=" + name + ", level=" + level + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HospitalPojo other = (HospitalPojo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static HospitalPojo from(Hospital hospital) {
		HospitalPojo pojo = new HospitalPojo();
		pojo.setId(hospital.getId());
		pojo.setName(hospital.getName());
		pojo.setLevel(hospital.getLevel().getName());
		pojo.setProvince(hospital.getProvince().getName());
		pojo.setRegion(hospital.getProvince().getRegion());
		return pojo;
	}

}
