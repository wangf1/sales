package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

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
		Iterable<Department> results = this.repository.findAll();
		for (Department depart : results) {
			System.out.println(depart.getName().getName());
			System.out.println(depart.getHospital().getName());
		}
	}

	@Test
	public void findByDepartmentNameHospitalName() throws Exception {
		Department department = repository.findByDepartmentNameHospitalName("ICU", "长征");
		System.out.println(department);
	}

}
