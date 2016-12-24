package com.wangf.sales.management.rest.pojo;

public class StatusPojo {
	public static final String STATUS_KEY_ALREADY_CLONED_LAST_MONTH = "already_cloned_last_month";

	private int statusCode;
	private String statusKey;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

}
