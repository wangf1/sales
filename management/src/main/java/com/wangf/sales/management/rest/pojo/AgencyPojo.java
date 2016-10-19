package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.Agency;

public class AgencyPojo {

	private long id;
	private String name;
	private String region;
	private String province;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "AgencyPojo [id=" + id + ", name=" + name + ", region=" + region + ", province=" + province + ", level="
				+ level + "]";
	}

	public static AgencyPojo from(Agency entity) {
		AgencyPojo pojo = new AgencyPojo();
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getLevel());
		pojo.setName(entity.getName());
		pojo.setProvince(entity.getProvince().getName());
		pojo.setRegion(entity.getProvince().getRegion());

		return pojo;
	}
}
