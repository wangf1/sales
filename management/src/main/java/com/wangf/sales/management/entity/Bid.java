package com.wangf.sales.management.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Province province;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

	private String description;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "BID_PRODUCT", joinColumns = {
			@JoinColumn(name = "BID_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "BID_ID", "PRODUCT_ID" }) })
	private List<Product> products;

	private double price;

	private String status;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Product> getProducts() {
		if (products == null) {
			products = new ArrayList<>();
		}
		return products;
	}

	public void setProduct(List<Product> products) {
		this.products = products;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("description", description)
				.add("status", status).add("province", province.getName()).add("price", price)
				.add("salesPerson", salesPerson.getUserName()).toString();
		return string;
	}
}
