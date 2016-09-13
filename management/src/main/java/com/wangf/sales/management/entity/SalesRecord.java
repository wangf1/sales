package com.wangf.sales.management.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.MoreObjects;

@Entity
public class SalesRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "INSTALL_LOCATION_ID", referencedColumnName = "ID")
	private ProductInstallLocation installLocation;

	@ManyToOne
	@JoinColumn(name = "ORDER_DEPARTMENT_ID", referencedColumnName = "ID")
	private Department orderDepartment;

	@ManyToOne
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

	@Column
	private int quantity;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public ProductInstallLocation getInstallLocation() {
		return installLocation;
	}

	public void setInstallLocation(ProductInstallLocation installLocation) {
		this.installLocation = installLocation;
	}

	public Department getOrderDepartment() {
		return orderDepartment;
	}

	public void setOrderDepartment(Department orderDepartment) {
		this.orderDepartment = orderDepartment;
	}

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
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

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass())
				.add("region", installLocation.getDepartment().getHospital().getProvince().getRegion())
				.add("province", installLocation.getDepartment().getHospital().getProvince().getName())
				.add("manager", salesPerson.getManager().getUserName()).add("salesPerson", salesPerson.getUserName())
				.add("hospital", installLocation.getDepartment().getHospital().getName())
				.add("product", installLocation.getProduct().getName())
				.add("installDepartment", installLocation.getDepartment().getName())
				.add("orderDepartment", orderDepartment.getName().getName()).add("quantity", quantity).add("date", date)
				.toString();
		return string;
	}
}
