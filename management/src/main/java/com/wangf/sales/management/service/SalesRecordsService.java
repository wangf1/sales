package com.wangf.sales.management.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;

@Service
public class SalesRecordsService {
	@Autowired
	private SalesRecordRepository salesRecordRepository;
	@Autowired
	private ProductInstallLocationRepository installLocationRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;

	public List<SalesRecord> advanceSearch(String productName, String salesPersonName, String hospitalName,
			String locationDepartmentName, String orderDepartName, Date startFrom) {

		User salesPerson = userRepository.findOne(salesPersonName);
		ProductInstallLocation installLocation = installLocationRepository.findByProductDepartmentHospital(productName,
				locationDepartmentName, hospitalName);
		Department orderDepartment = departmentRepository.findByDepartmentNameHospitalName(orderDepartName,
				hospitalName);

		List<SalesRecord> records = salesRecordRepository
				.findBySalesPersonAndInstallLocationAndOrderDepartmentAndDateAfter(salesPerson, installLocation,
						orderDepartment, startFrom);
		return records;
	}
}
