package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;

@Service
public class SalesRecordsService {
	@Autowired
	private SalesRecordRepository salesRecordRepository;

	public List<SalesRecordPojo> searchAgainstSingleValues(String productName, String salesPersonName,
			String hospitalName, String locationDepartmentName, String orderDepartName, Date startFrom) {

		List<SalesRecord> records = salesRecordRepository.searchAgainstSingleValues(productName, salesPersonName,
				hospitalName, locationDepartmentName, orderDepartName, startFrom);

		List<SalesRecordPojo> result = new ArrayList<>();
		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			result.add(pojo);
		}
		return result;
	}

	public List<SalesRecordPojo> searchAgainstSingleValues(SalesRecordPojo criteria) {
		List<SalesRecordPojo> records = searchAgainstSingleValues(criteria.getProduct(), criteria.getSalesPerson(),
				criteria.getHospital(), criteria.getInstallDepartment(), criteria.getOrderDepartment(),
				criteria.getDate());
		return records;
	}

	public List<SalesRecordPojo> searchAgainstMultipleValues(SalesRecordSearchCriteria criteria) {
		List<SalesRecord> records = salesRecordRepository.searchAgainstMultipleValues(criteria);
		List<SalesRecordPojo> result = new ArrayList<>();
		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			result.add(pojo);
		}
		return result;
	}
}
