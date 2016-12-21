package com.wangf.sales.management.rest.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.wangf.sales.management.entity.ProductPrice;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;

public class SalesRecordPojo extends PoJoBase {
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
	private double price;
	private String managerFullName;

	@Transient
	private boolean alreadyExisting;

	private Date date;

	private Date lastModifyAt;

	private String lastModifyBy;

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
		return "SalesRecordPojo [id=" + id + ", region=" + region + ", province=" + province + ", manager=" + manager
				+ ", salesPerson=" + salesPerson + ", hospital=" + hospital + ", product=" + product
				+ ", installDepartment=" + installDepartment + ", orderDepartment=" + orderDepartment + ", quantity="
				+ quantity + ", hospitalLevel=" + hospitalLevel + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hospital == null) ? 0 : hospital.hashCode());
		result = prime * result + ((installDepartment == null) ? 0 : installDepartment.hashCode());
		result = prime * result + ((orderDepartment == null) ? 0 : orderDepartment.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesRecordPojo other = (SalesRecordPojo) obj;
		if (hospital == null) {
			if (other.hospital != null)
				return false;
		} else if (!hospital.equals(other.hospital))
			return false;
		if (installDepartment == null) {
			if (other.installDepartment != null)
				return false;
		} else if (!installDepartment.equals(other.installDepartment))
			return false;
		if (orderDepartment == null) {
			if (other.orderDepartment != null)
				return false;
		} else if (!orderDepartment.equals(other.orderDepartment))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
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
		pojo.setManagerFullName(PojoUtils.getFullName(manager));

		pojo.setSalesPerson(record.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(record.getSalesPerson()));
		pojo.setHospital(record.getInstallLocation().getDepartment().getHospital().getName());
		pojo.setProduct(record.getInstallLocation().getProduct().getName());
		pojo.setInstallDepartment(record.getInstallLocation().getDepartment().getName().getName());
		pojo.setOrderDepartment(record.getOrderDepartment().getName().getName());
		pojo.setQuantity(record.getQuantity());
		pojo.setDate(record.getDate());
		pojo.setHospitalLevel(record.getInstallLocation().getDepartment().getHospital().getLevel().getName());
		pojo.setPrice(getProductPrice(record));
		if (record.getLastModifyBy() != null) {
			pojo.setLastModifyBy(PojoUtils.getFullName(record.getLastModifyBy()));
		}
		pojo.setLastModifyAt(record.getLastModifyAt());

		return pojo;
	}

	private static double getProductPrice(SalesRecord record) {
		List<ProductPrice> prices = record.getInstallLocation().getDepartment().getHospital().getPrices();
		if (prices == null) {
			return 0;
		}
		String productName = record.getInstallLocation().getProduct().getName();
		for (ProductPrice price : prices) {
			if (productName.equals(price.getProduct().getName())) {
				return price.getPrice();
			}
		}
		return 0;
	}

	public String getManagerFullName() {
		return managerFullName;
	}

	public void setManagerFullName(String managerFullName) {
		this.managerFullName = managerFullName;
	}

}
