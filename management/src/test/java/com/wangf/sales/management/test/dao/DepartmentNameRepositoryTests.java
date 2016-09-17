package com.wangf.sales.management.test.dao;

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
	public void findByName() {
		DepartmentName result = this.repository.findByName("ICU");
		System.out.println(result);
	}

}
