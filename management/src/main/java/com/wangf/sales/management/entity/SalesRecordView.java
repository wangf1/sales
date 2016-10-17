package com.wangf.sales.management.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Subselect;

import com.wangf.sales.management.rest.pojo.SalesRecordPojo;

@Entity
@Table(name = "sales_record_view")
// @Subselect annotation make hibernate not create table for this entity, see
// http://stackoverflow.com/a/33689357/1029242
@Subselect("select * from sales_record_view")
public class SalesRecordView {
	@Id
	private long id;
	private String region;
	private String province;
	private String manager;
	private String salesPerson;
	private String hospital;
	private String hospitalLevel;
	private String product;
	private String installDepartment;
	private String orderDepartment;
	private int quantity;
	private double price;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "SalesRecordPojo [id=" + id + ", region=" + region + ", province=" + province + ", manager=" + manager
				+ ", salesPerson=" + salesPerson + ", hospital=" + hospital + ", product=" + product
				+ ", installDepartment=" + installDepartment + ", orderDepartment=" + orderDepartment + ", quantity="
				+ quantity + ", hospitalLevel=" + hospitalLevel + ", date=" + date + "]";
	}

	public static SalesRecordPojo from(SalesRecordView record) {
		SalesRecordPojo pojo = new SalesRecordPojo();
		pojo.setId(record.getId());
		pojo.setRegion(record.getRegion());
		pojo.setProvince(record.getProvince());
		pojo.setManager(record.getManager());
		pojo.setSalesPerson(record.getSalesPerson());
		pojo.setHospital(record.getHospital());
		pojo.setProduct(record.getProduct());
		pojo.setInstallDepartment(record.getInstallDepartment());
		pojo.setOrderDepartment(record.getOrderDepartment());
		pojo.setQuantity(record.getQuantity());
		pojo.setDate(record.getDate());
		pojo.setHospitalLevel(record.getHospitalLevel());
		pojo.setPrice(record.getPrice());

		return pojo;
	}

}
