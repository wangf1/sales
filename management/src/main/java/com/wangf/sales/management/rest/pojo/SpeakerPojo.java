package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import com.wangf.sales.management.entity.Speaker;

public class SpeakerPojo extends PoJoBase {
	private long id;

	private Date date;

	private String region;

	private String province;

	private String salesPerson;

	private String hospital;

	private String department;

	private String speakerName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	@Override
	public String toString() {
		return "SpeakerPojo [id=" + id + ", date=" + date + ", region=" + region + ", province=" + province
				+ ", salesPerson=" + salesPerson + ", hospital=" + hospital + ", department=" + department
				+ ", speakerName=" + speakerName + "]";
	}

	public static SpeakerPojo from(Speaker entity) {
		SpeakerPojo pojo = new SpeakerPojo();
		pojo.setDate(entity.getDate());
		pojo.setHospital(entity.getHospital().getName());
		pojo.setId(entity.getId());
		pojo.setProvince(entity.getProvince().getName());
		pojo.setRegion(entity.getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));
		pojo.setDepartment(entity.getDepartment().getName().getName());
		pojo.setSpeakerName(entity.getSpeakerName());
		return pojo;
	}
}
