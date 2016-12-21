package com.wangf.sales.management.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AgencyEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected long id;

	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date date;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	protected Agency agency;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "AGENCYEVENT_PRODUCT", joinColumns = {
			@JoinColumn(name = "AGENCYEVENT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "AGENCYEVENT_ID", "PRODUCT_ID" }) })
	protected List<Product> products;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "SALES_PERSON", referencedColumnName = "USERNAME")
	protected User salesPerson;

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

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public User getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(User salesPerson) {
		this.salesPerson = salesPerson;
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
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("agency", agency.getName())
				.toString();
		return string;
	}
}
