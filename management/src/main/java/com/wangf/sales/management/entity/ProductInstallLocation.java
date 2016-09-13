package com.wangf.sales.management.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class ProductInstallLocation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private Product product;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private Department department;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
