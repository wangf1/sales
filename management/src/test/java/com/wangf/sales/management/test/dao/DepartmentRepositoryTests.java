package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class DepartmentRepositoryTests extends TestBase {

	@Autowired
	private DepartmentRepository repository;

	@Test
	public void findsAllHospital() {
		List<Department> results = this.repository.findByName("ICU");
		Assert.assertTrue(results.size() >= 1);
		for (Department hospital : results) {
			System.out.println(hospital.getHospitals().size());
		}
	}

}
