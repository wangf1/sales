package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import com.wangf.sales.management.entity.DepartmentMeeting;

public class DepartmentMeetingPojo extends PoJoBase {
	private long id;

	private Date date;

	private String region;

	private String province;

	private String hospital;

	private String department;

	private String salesPerson;

	private String product;

	private String purpose;

	private String subject;

	private double planCost;

	private String status;

	private double actualCost;

	private Date lastModifyAt;

	private String lastModifyBy;

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

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public double getPlanCost() {
		return planCost;
	}

	public void setPlanCost(double planCost) {
		this.planCost = planCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}

	public Date getLastModifyAt() {
		return lastModifyAt;
	}

	public void setLastModifyAt(Date lastModifyAt) {
		this.lastModifyAt = lastModifyAt;
	}

	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Override
	public String toString() {
		return "DepartmentMeetingPojo [id=" + id + ", date=" + date + ", region=" + region + ", province=" + province
				+ ", hospital=" + hospital + ", department=" + department + ", salesPerson=" + salesPerson
				+ ", product=" + product + ", purpose=" + purpose + ", subject=" + subject + ", planCost=" + planCost
				+ ", status=" + status + ", actualCost=" + actualCost + "]";
	}

	public static DepartmentMeetingPojo from(DepartmentMeeting d) {
		DepartmentMeetingPojo pojo = new DepartmentMeetingPojo();
		pojo.setActualCost(d.getActualCost());
		pojo.setDate(d.getDate());
		pojo.setDepartment(d.getDepartment().getName().getName());
		pojo.setHospital(d.getDepartment().getHospital().getName());
		pojo.setId(d.getId());
		pojo.setPlanCost(d.getPlanCost());
		pojo.setProduct(d.getProduct().getName());
		pojo.setProvince(d.getDepartment().getHospital().getProvince().getName());
		pojo.setPurpose(d.getPurpose());
		pojo.setRegion(d.getDepartment().getHospital().getProvince().getRegion());
		pojo.setSalesPerson(d.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(d.getSalesPerson()));
		pojo.setStatus(d.getStatus());
		pojo.setSubject(d.getSubject());
		if (d.getLastModifyBy() != null) {
			pojo.setLastModifyBy(PojoUtils.getFullName(d.getLastModifyBy()));
		}
		pojo.setLastModifyAt(d.getLastModifyAt());

		return pojo;
	}
}
