package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
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
		List<Company> results = this.repository.findByName("Thermo");
		Assert.assertTrue(results.size() >= 1);
		for (Company hospital : results) {
			System.out.println(hospital.getName());
			System.out.println(hospital.getProducts());
		}
	}

}
