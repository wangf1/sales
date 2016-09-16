package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;

@Service
public class SalesRecordsService {
	@Autowired
	private SalesRecordRepository salesRecordRepository;
	@Autowired
	private ProductInstallLocationRepository installLocationRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

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

	@Transactional
	public SalesRecordPojo save(SalesRecordPojo pojo) {
		// FIXME!!! in one month, should not insert duplicate record for same
		// (hospital,installDepart, orderDepart, product)
		ProductInstallLocation installLocation = installLocationRepository
				.findByProductDepartmentHospital(pojo.getProduct(), pojo.getInstallDepartment(), pojo.getHospital());
		Department orderDepartment = departmentRepository.findByDepartmentNameHospitalName(pojo.getOrderDepartment(),
				pojo.getHospital());
		User salesPerson = userService.getCurrentUser();

		SalesRecord record = new SalesRecord();
		record.setInstallLocation(installLocation);
		record.setOrderDepartment(orderDepartment);
		record.setSalesPerson(salesPerson);
		record.setQuantity(pojo.getQuantity());
		SalesRecord saved = salesRecordRepository.save(record);
		// Refresh in order to get the date property
		salesRecordRepository.getEntityManager().refresh(saved);

		SalesRecordPojo savedPojo = SalesRecordPojo.from(saved);
		return savedPojo;
	}
}
