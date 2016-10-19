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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.google.common.base.MoreObjects;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "agency_id", "product_id" }) })
public class AgencyRecruit {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Agency agency;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Product product;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	private User salesPerson;

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

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("agency", agency.getName())
				.add("product", product.getName()).toString();
		return string;
	}
}
