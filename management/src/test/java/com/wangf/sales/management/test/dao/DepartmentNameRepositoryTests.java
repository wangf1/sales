package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.DepartmentNameRepository;
import com.wangf.sales.management.entity.DepartmentName;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class DepartmentNameRepositoryTests extends TestBase {

	@Autowired
	private DepartmentNameRepository repository;

	@Test
	public void findsAllHospital() {
		List<DepartmentName> results = this.repository.findByName("ICU");
		for (DepartmentName departName : results) {
			System.out.println(departName.getName());
			System.out.println(departName.getDepartments());
		}
	}

}
