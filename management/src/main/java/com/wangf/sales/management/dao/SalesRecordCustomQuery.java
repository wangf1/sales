package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.wangf.sales.management.entity.SalesRecord;

public interface SalesRecordCustomQuery {
	List<SalesRecord> searchAgainstSingleValues(String productName, String salesPersonName, String hospitalName,
			String locationDepartmentName, String orderDepartName, Date startFrom);

	List<SalesRecord> searchAgainstMultipleValues(SalesRecordSearchCriteria criteria);

	EntityManager getEntityManager();
}
