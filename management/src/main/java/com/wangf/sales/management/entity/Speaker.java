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
public class Speaker {
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

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Hospital hospital;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
	private Department department;

	private String speakerName;

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

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
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
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("speakerName", speakerName)
				.add("department", getDepartment().getName().getName()).add("province", province.getName())
				.add("hospital", hospital.getName()).add("salesPerson", salesPerson.getUserName()).toString();
		return string;
	}
}
