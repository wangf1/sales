package com.wangf.sales.management.test.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class SalesRecordRepositoryTests extends TestBase {

	@Autowired
	private SalesRecordRepository repository;
	@Autowired
	private ProductInstallLocationRepository installLocationRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void findsAll() {
		Iterable<SalesRecord> results = this.repository.findAll();
		// Iterable<User> results = this.repository.findAll();
		// Assert.assertTrue(results.size() >= 1);
		for (SalesRecord record : results) {
			System.out.println(record);
		}
	}

	@Test
	public void insert() throws Exception {

		long existingCount = repository.count();

		// ProductInstallLocation installLocation =
		// installLocationRepository.findOne(1L);
		ProductInstallLocation installLocation = installLocationRepository.findByProductDepartmentHospital("PCT-Q",
				"ICU", "长征");
		Department orderDepartment = departmentRepository.findOne(1L);
		User salesPerson = userRepository.findOne("wangf");

		SalesRecord record = new SalesRecord();
		record.setInstallLocation(installLocation);
		record.setOrderDepartment(orderDepartment);
		record.setSalesPerson(salesPerson);
		record.setQuantity(200);

		repository.save(record);

		Assert.assertEquals(repository.count(), existingCount + 1);
	}

	@Test
	public void findBySalesPersonAndInstallLocationAndOrderDepartment() throws Exception {
		User salesPerson = userRepository.findOne("wangf");
		ProductInstallLocation installLocation = installLocationRepository.findByProductDepartmentHospital("PCT-Q",
				"ICU", "长征");
		Department orderDepartment = departmentRepository.findByDepartmentNameHospitalName("ICU", "长征");

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		Date lastMonth = calendar.getTime();
		List<SalesRecord> records = repository.findBySalesPersonAndInstallLocationAndOrderDepartmentAndDateAfter(
				salesPerson, installLocation, orderDepartment, lastMonth);
		System.out.println(records);
	}

}
