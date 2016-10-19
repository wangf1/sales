package com.wangf.sales.management.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Agency {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private Province province;

	@Column(nullable = false)
	private String level;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "AGENCY_ID", referencedColumnName = "ID")
	private List<AgencyRecruit> agencyRecruit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<AgencyRecruit> getAgencyRecruit() {
		return agencyRecruit;
	}

	public void setAgencyRecruit(List<AgencyRecruit> agencyRecruit) {
		this.agencyRecruit = agencyRecruit;
	}

	@Override
	public String toString() {
		return "Agency [id=" + id + ", name=" + name + ", level=" + level + "]";
	}

}
