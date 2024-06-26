package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class ProductInstallLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private Product product;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private Department department;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "INSTALL_LOCATION_ID", referencedColumnName = "ID")
	private List<SalesRecord> salesRecords;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<SalesRecord> getSalesRecords() {
		return salesRecords;
	}

	public void setSalesRecords(List<SalesRecord> salesRecords) {
		this.salesRecords = salesRecords;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("product", product.getName())
				.add("department", department.getName()).add("hospital", department.getHospital().getName()).toString();
		return string;
	}
}
