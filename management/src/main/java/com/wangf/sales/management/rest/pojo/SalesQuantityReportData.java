package com.wangf.sales.management.rest.pojo;

public class SalesQuantityReportData {
	/**
	 * The format should be "yyyy-MM", e.g. 2016-12
	 */
	private String date;
	private Float salesQuantity = Float.valueOf(0);
	private Integer departmentMeetingQuantity = Integer.valueOf(0);

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Float getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(Float salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

	public Integer getDepartmentMeetingQuantity() {
		return departmentMeetingQuantity;
	}

	public void setDepartmentMeetingQuantity(Integer departmentMeetingQuantity) {
		this.departmentMeetingQuantity = departmentMeetingQuantity;
	}

	@Override
	public String toString() {
		return "SalesQuantityReportData [date=" + date + ", salesQuantity=" + salesQuantity
				+ ", departmentMeetingQuantity=" + departmentMeetingQuantity + "]";
	}

}
