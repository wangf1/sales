package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.Province;

public class ProvincePojo {

	private long id;
	private String name;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "ProvincePojo [id=" + id + ", name=" + name + ", region=" + region + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
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
		ProvincePojo other = (ProvincePojo) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		return true;
	}

	public static ProvincePojo from(Province province) {
		ProvincePojo pojo = new ProvincePojo();
		pojo.setId(province.getId());
		pojo.setName(province.getName());
		pojo.setRegion(province.getRegion());
		return pojo;
	}

}
