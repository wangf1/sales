package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import com.wangf.sales.management.entity.SalesRecord;

public interface SalesRecordCustomQuery {
	List<SalesRecord> advanceSearch(String productName, String salesPersonName, String hospitalName,
			String locationDepartmentName, String orderDepartName, Date startFrom);
}
