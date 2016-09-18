package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;

@Service
@Transactional
public class SalesRecordsService {
	@Autowired
	private SalesRecordRepository salesRecordRepository;
	@Autowired
	private ProductInstallLocationService installLocationService;
	@Autowired
	private DepartmentService departmentServcie;

	@Autowired
	private UserService userService;

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

	public SalesRecordPojo insertOrUpdate(SalesRecordPojo pojo) {
		ProductInstallLocation installLocation = installLocationService.findOrCreateByProductDepartmentHospital(
				pojo.getProduct(), pojo.getInstallDepartment(), pojo.getHospital());
		Department orderDepartment = departmentServcie
				.findOrCreateByDepartNameAndHospitalName(pojo.getOrderDepartment(), pojo.getHospital());
		User salesPerson = userService.getCurrentUser();
		/*
		 * Firstly find by ID, if not exist, find by (installLocation,
		 * orderDepartment, salesPerson, month). The purpose of search two times
		 * is: 1). Search by ID to avoid treat update case as insert. 2). Search
		 * by columns to avoid insert duplicate record
		 */
		SalesRecord record = salesRecordRepository.findOne(pojo.getId());
		if (record == null) {
			record = salesRecordRepository.searchByLocationOrderDepartPersonMonth(installLocation.getId(),
					orderDepartment.getId(), salesPerson.getUserName(), new Date());
		}
		boolean alreadyExisting = true;
		if (record == null) {
			record = new SalesRecord();
			alreadyExisting = false;
		}
		record.setInstallLocation(installLocation);
		record.setOrderDepartment(orderDepartment);
		record.setSalesPerson(salesPerson);
		record.setQuantity(pojo.getQuantity());
		if (alreadyExisting) {
			// salesRecordRepository.save(record) method cannot update, possibly
			// because the record is a detached one from other entity manager
			// context. So user "merge" to resolve the issue.
			salesRecordRepository.getEntityManager().merge(record);
		} else {
			// For new entity, merge method will throw exception, should use
			// save
			salesRecordRepository.save(record);
			// Refresh record in order to get the date of a new created record
			salesRecordRepository.getEntityManager().refresh(record);
		}

		SalesRecordPojo savedPojo = SalesRecordPojo.from(record);
		savedPojo.setAlreadyExisting(alreadyExisting);
		return savedPojo;
	}

	public List<Long> insertOrUpdate(List<SalesRecordPojo> pojos) {
		for (SalesRecordPojo pojo : pojos) {
			insertOrUpdate(pojo);
		}
		List<Long> allIds = new ArrayList<>();
		for (SalesRecordPojo pojo : pojos) {
			allIds.add(pojo.getId());
		}
		return allIds;
	}

	public void deleteSalesRecords(List<Long> salesRecordIds) {
		for (Long id : salesRecordIds) {
			salesRecordRepository.delete(id);
		}
	}
}
