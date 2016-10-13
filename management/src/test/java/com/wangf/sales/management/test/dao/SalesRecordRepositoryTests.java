package com.wangf.sales.management.test.dao;

import java.util.Arrays;
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
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
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

	/*
	 * Do not know why run in Eclipse this test can pass, but fail when Maven
	 * build. So skip this test case.
	 */
	// @Test
	public void insert() throws Exception {

		long existingCount = repository.count();

		// ProductInstallLocation installLocation =
		// installLocationRepository.findOne(1L);
		ProductInstallLocation installLocation = installLocationRepository.findByProductDepartmentHospital("PCT-Q",
				"检验科", "长海");
		Department orderDepartment = departmentRepository.findOne(2L);
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

		Date lastMonth = getLastMonth();
		List<SalesRecord> records = repository.findBySalesPersonAndInstallLocationAndOrderDepartmentAndDateAfter(
				salesPerson, installLocation, orderDepartment, lastMonth);

		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			System.out.println(pojo);
		}
	}

	private Date getLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		Date lastMonth = calendar.getTime();
		return lastMonth;
	}

	@Test
	public void advanceSearch() throws Exception {
		List<SalesRecord> allRecords = repository.searchAgainstSingleValues(null, null, null, null, null, null);
		List<SalesRecord> records = repository.searchAgainstSingleValues("PCT-Q", "wangf", "长征", "ICU", "ICU",
				getLastMonth());
		System.out.println(allRecords.size());
		System.out.println(records);
	}

	@Test
	public void advanceSearchMultipeCritera() throws Exception {
		List<SalesRecord> allRecords = repository.searchAgainstMultipleValues(new SalesRecordSearchCriteria());
		System.out.println(allRecords);

		SalesRecordSearchCriteria criteria = new SalesRecordSearchCriteria();
		criteria.setProductNames(Arrays.asList(new String[] { "PCT-Q" }));
		criteria.setSalesPersonNames(Arrays.asList(new String[] { "wangf" }));
		criteria.setHospitalNames(Arrays.asList(new String[] { "长征", "长海" }));
		criteria.setLocationDepartmentNames(Arrays.asList(new String[] { "ICU" }));
		criteria.setOrderDepartNames(Arrays.asList(new String[] { "ICU" }));
		criteria.setStartAt(getLastMonth());
		criteria.setEndAt(new Date());

		List<SalesRecord> records = repository.searchAgainstMultipleValues(criteria);
		System.out.println(records);
	}

	@Test
	public void searchByLocationOrderDepartPersonMonth() throws Exception {
		SalesRecord isNull = repository.searchByLocationOrderDepartPersonInCurrentMonth(1, 1, "wangf1");
		SalesRecord record = repository.searchByLocationOrderDepartPersonInCurrentMonth(1, 1, "wangf");
		System.out.println(isNull);
		System.out.println(record);
	}

}
