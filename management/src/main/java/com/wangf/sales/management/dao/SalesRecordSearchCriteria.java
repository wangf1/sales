package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SalesRecordSearchCriteria {
	private List<String> productNames;
	private List<String> salesPersonNames;
	private List<String> hospitalNames;
	private List<String> locationDepartmentNames;
	private List<String> orderDepartNames;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date endAt;

	public List<String> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}

	public List<String> getSalesPersonNames() {
		return salesPersonNames;
	}

	public void setSalesPersonNames(List<String> salesPersonNames) {
		this.salesPersonNames = salesPersonNames;
	}

	public List<String> getHospitalNames() {
		return hospitalNames;
	}

	public void setHospitalNames(List<String> hospitalNames) {
		this.hospitalNames = hospitalNames;
	}

	public List<String> getLocationDepartmentNames() {
		return locationDepartmentNames;
	}

	public void setLocationDepartmentNames(List<String> locationDepartmentNames) {
		this.locationDepartmentNames = locationDepartmentNames;
	}

	public List<String> getOrderDepartNames() {
		return orderDepartNames;
	}

	public void setOrderDepartNames(List<String> orderDepartNames) {
		this.orderDepartNames = orderDepartNames;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	@Override
	public String toString() {
		return "SalesRecordSearchCriteria [productNames=" + productNames + ", salesPersonNames=" + salesPersonNames
				+ ", hospitalNames=" + hospitalNames + ", locationDepartmentNames=" + locationDepartmentNames
				+ ", orderDepartNames=" + orderDepartNames + ", startAt=" + startAt + ", endAt=" + endAt + "]";
	}

}
