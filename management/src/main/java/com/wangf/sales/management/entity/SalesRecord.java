package com.wangf.sales.management.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "INSTALL_LOCATION_ID", referencedColumnName = "ID")
	private ProductInstallLocation installLocation;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ORDER_DEPARTMENT_ID", referencedColumnName = "ID")
	private Department orderDepartment;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

	@Column
	private int quantity;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private Date lastModifyAt;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "LAST_MODIFY_BY", referencedColumnName = "USERNAME")
	private User lastModifyBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public Date getLastModifyAt() {
		return lastModifyAt;
	}

	public void setLastModifyAt(Date lastModifyAt) {
		this.lastModifyAt = lastModifyAt;
	}

	public User getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(User lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Override
	public String toString() {
		User manager = salesPerson.getManager();
		String managerName = manager != null ? manager.getUserName() : "";
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id)
				.add("region", installLocation.getDepartment().getHospital().getProvince().getRegion())
				.add("province", installLocation.getDepartment().getHospital().getProvince().getName())
				.add("manager", managerName).add("salesPerson", salesPerson.getUserName())
				.add("hospital", installLocation.getDepartment().getHospital().getName())
				.add("product", installLocation.getProduct().getName())
				.add("installDepartment", installLocation.getDepartment().getName().getName())
				.add("orderDepartment", orderDepartment.getName().getName()).add("quantity", quantity).add("date", date)
				.toString();
		return string;
	}
}
