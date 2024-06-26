package com.wangf.sales.management.auth;

public class ResourcePermission {
	private Permission salesRecord = new Permission();
	private Permission province = new Permission();
	private Permission hospital = new Permission();
	private Permission department = new Permission();
	private Permission departmentName = new Permission();
	private Permission product = new Permission();
	private Permission user = new Permission();
	private Permission showSalesPersonForSalesRecord = new Permission();
	private Permission productPrice = new Permission();
	private Permission dataCollect = new Permission();
	private Permission analysis = new Permission();

	public Permission getSalesRecord() {
		return salesRecord;
	}

	public void setSalesRecord(Permission salesRecord) {
		this.salesRecord = salesRecord;
	}

	public Permission getProvince() {
		return province;
	}

	public void setProvince(Permission province) {
		this.province = province;
	}

	public Permission getHospital() {
		return hospital;
	}

	public void setHospital(Permission hospital) {
		this.hospital = hospital;
	}

	public Permission getDepartment() {
		return department;
	}

	public void setDepartment(Permission department) {
		this.department = department;
	}

	public Permission getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(Permission departmentName) {
		this.departmentName = departmentName;
	}

	public Permission getProduct() {
		return product;
	}

	public void setProduct(Permission product) {
		this.product = product;
	}

	public Permission getUser() {
		return user;
	}

	public void setUser(Permission user) {
		this.user = user;
	}

	public Permission getShowSalesPersonForSalesRecord() {
		return showSalesPersonForSalesRecord;
	}

	public void setShowSalesPersonForSalesRecord(Permission showSalesPersonForSalesRecord) {
		this.showSalesPersonForSalesRecord = showSalesPersonForSalesRecord;
	}

	public Permission getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Permission productPrice) {
		this.productPrice = productPrice;
	}

	public Permission getDataCollect() {
		return dataCollect;
	}

	public void setDataCollect(Permission dataCollect) {
		this.dataCollect = dataCollect;
	}

	public Permission getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Permission analysis) {
		this.analysis = analysis;
	}

}
