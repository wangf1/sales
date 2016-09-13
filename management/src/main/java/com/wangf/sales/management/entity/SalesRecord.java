package com.wangf.sales.management.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass())
				.add("installDepartment", installLocation.getDepartment().getName())
				.add("hospital", installLocation.getDepartment().getHospital().getName())
				.add("province", installLocation.getDepartment().getHospital().getProvince().getName())
				.add("product", installLocation.getProduct().getName())
				.add("orderDepartment", orderDepartment.getName().getName()).toString();
		return string;
	}
}
