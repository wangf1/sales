package com.wangf.sales.management.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesRecordSearchCriteria {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> productNames;
	private List<String> salesPersonNames;
	private List<String> hospitalNames;
	private List<String> locationDepartmentNames;
	private List<String> orderDepartNames;
	private Date startAt;
	private Date endAt;
	private boolean includeStartAt = true;
	private boolean includeEndAt = true;

	public List<String> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}

	public List<String> getSalesPersonNames() {
		if (salesPersonNames == null) {
			salesPersonNames = new ArrayList<>();
		}
		return salesPersonNames;
	}

	public void setSalesPersonNames(List<String> salesPersonNames) {
		this.salesPersonNames = salesPersonNames;
	}

	public List<String> getHospitalNames() {
		return hospitalNames;
	}

	public void setHospitalNames(List<String> hospitalNames) {
		this.hospitalNames = hospitalNames;
	}

	public List<String> getLocationDepartmentNames() {
		return locationDepartmentNames;
	}

	public void setLocationDepartmentNames(List<String> locationDepartmentNames) {
		this.locationDepartmentNames = locationDepartmentNames;
	}

	public List<String> getOrderDepartNames() {
		return orderDepartNames;
	}

	public void setOrderDepartNames(List<String> orderDepartNames) {
		this.orderDepartNames = orderDepartNames;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public boolean isIncludeStartAt() {
		return includeStartAt;
	}

	public void setIncludeStartAt(boolean includeStartAt) {
		this.includeStartAt = includeStartAt;
	}

	public boolean isIncludeEndAt() {
		return includeEndAt;
	}

	public void setIncludeEndAt(boolean includeEndAt) {
		this.includeEndAt = includeEndAt;
	}

	@Override
	public String toString() {
		return "SalesRecordSearchCriteria [productNames=" + productNames + ", salesPersonNames=" + salesPersonNames
				+ ", hospitalNames=" + hospitalNames + ", locationDepartmentNames=" + locationDepartmentNames
				+ ", orderDepartNames=" + orderDepartNames + ", startAt=" + startAt + ", endAt=" + endAt + "]";
	}

	/**
	 * Get MD5 then Base64 string of toString, used as unique download URL,
	 * since special character "." (maybe other characters also) can lead Spring
	 * Controller cannot get the full URL.
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String getMD5Base64String() {
		String string = toString();
		String md5base64 = "static_download_url";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = digest.digest(string.getBytes());
			byte[] md5base64Bytes = Base64.getEncoder().encode(md5Bytes);
			md5base64 = new String(md5base64Bytes);
			// Must remove all non-word character in order to make it a valid
			// URL
			md5base64 = md5base64.replaceAll("\\W", "");
		} catch (NoSuchAlgorithmException e) {
			logger.warn("Cannot get MD5 MessageDigest", e);
		}
		return md5base64;
	}

}
