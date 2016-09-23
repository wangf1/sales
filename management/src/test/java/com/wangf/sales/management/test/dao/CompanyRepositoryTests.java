package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.CompanyRepository;
import com.wangf.sales.management.entity.Company;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class CompanyRepositoryTests extends TestBase {

	@Autowired
	private CompanyRepository repository;

	@Test
	public void findsAllHospital() {
		Company result = this.repository.findByName("Thermo");
		System.out.println(result.getName());
		System.out.println(result.getProducts());
	}

}
