package com.wangf.sales.management.entity;

import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

@Entity
public class Province {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	private String region;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID")
	private List<Hospital> hospitals;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID")
	private List<Bid> bids;

	/**
	 * Which users response for this province.
	 */
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_PROVINCE", joinColumns = {
			@JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME") }, uniqueConstraints = {
							@UniqueConstraint(columnNames = { "USERNAME", "PROVINCE_ID" }) })
	private List<User> users;

	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID")
	private List<RegionMeeting> regionMeetings;

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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<Hospital> getHospitals() {
		if (hospitals == null) {
			hospitals = new ArrayList<>();
		}
		return hospitals;
	}

	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public List<User> getUsers() {
		if (users == null) {
			return new ArrayList<>();
		}
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<RegionMeeting> getRegionMeetings() {
		return regionMeetings;
	}

	public void setRegionMeetings(List<RegionMeeting> regionMeetings) {
		this.regionMeetings = regionMeetings;
	}

	@Override
	public String toString() {
		String string = MoreObjects.toStringHelper(this.getClass()).add("id", id).add("name", name)
				.add("region", region).toString();
		return string;
	}
}
