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
public class DepartmentMeeting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Department department;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
	private Product product;

	private String purpose;

	private String subject;

	private double planCost;

	private String status;

	private double actualCost;

	private Date lastModifyAt;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "LAST_MODIFY_BY", referencedColumnName = "USERNAME")
	private User lastModifyBy;

	private int numberOfPeople;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public double getPlanCost() {
		return planCost;
	}

	public void setPlanCost(double planCost) {
		this.planCost = planCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
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

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id)
				.add("province", department.getHospital().getProvince().getName())
				.add("hospital", department.getHospital().getName()).add("department", department.getName().getName())
				.add("product", product.getName()).add("subject", subject).add("status", status).toString();
		return string;
	}
}
