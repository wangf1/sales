package com.wangf.sales.management.rest.pojo;

import java.util.Date;

import javax.persistence.Transient;

import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;

public class SalesRecordPojo {
	private long id;
	private String region;
	private String province;
	private String manager;
	private String salesPerson;
	private String hospital;
	private String product;
	private String installDepartment;
	private String orderDepartment;
	private int quantity;
	private String hospitalLevel;

	@Transient
	private boolean alreadyExisting;

	private Date date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getInstallDepartment() {
		return installDepartment;
	}

	public void setInstallDepartment(String installDepartment) {
		this.installDepartment = installDepartment;
	}

	public String getOrderDepartment() {
		return orderDepartment;
	}

	public void setOrderDepartment(String orderDepartment) {
		this.orderDepartment = orderDepartment;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(String hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}

	public boolean isAlreadyExisting() {
		return alreadyExisting;
	}

	public void setAlreadyExisting(boolean alreadyExisting) {
		this.alreadyExisting = alreadyExisting;
	}

	@Override
	public String toString() {
		return "SalesRecordPojo [id=" + id + ", region=" + region + ", province=" + province + ", manager=" + manager
				+ ", salesPerson=" + salesPerson + ", hospital=" + hospital + ", product=" + product
				+ ", installDepartment=" + installDepartment + ", orderDepartment=" + orderDepartment + ", quantity="
				+ quantity + ", hospitalLevel=" + hospitalLevel + ", date=" + date + "]";
	}

	public static SalesRecordPojo from(SalesRecord record) {
		SalesRecordPojo pojo = new SalesRecordPojo();
		pojo.setId(record.getId());
		pojo.setRegion(record.getInstallLocation().getDepartment().getHospital().getProvince().getRegion());
		pojo.setProvince(record.getInstallLocation().getDepartment().getHospital().getProvince().getName());

		// If no manager, then assume him/her self is the manager
		User manager = record.getSalesPerson().getManager();
		String managerName = manager != null ? manager.getUserName() : record.getSalesPerson().getUserName();
		pojo.setManager(managerName);

		pojo.setSalesPerson(record.getSalesPerson().getUserName());
		pojo.setHospital(record.getInstallLocation().getDepartment().getHospital().getName());
		pojo.setProduct(record.getInstallLocation().getProduct().getName());
		pojo.setInstallDepartment(record.getInstallLocation().getDepartment().getName().getName());
		pojo.setOrderDepartment(record.getOrderDepartment().getName().getName());
		pojo.setQuantity(record.getQuantity());
		pojo.setDate(record.getDate());
		pojo.setHospitalLevel(record.getInstallLocation().getDepartment().getHospital().getLevel().getName());

		return pojo;
	}

}
