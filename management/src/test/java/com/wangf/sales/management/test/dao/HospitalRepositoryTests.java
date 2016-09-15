package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class HospitalRepositoryTests extends TestBase {

	@Autowired
	private HospitalRepository repository;

	@Test
	public void findsHospitalByName() {
		List<Hospital> results = this.repository.findByName("长征");
		// Assert.assertTrue(results.size() >= 1);
		for (Hospital hospital : results) {
			System.out.println(hospital.getProvince().getName());
			System.out.println(hospital.getLevel().getName());
			System.out.println(hospital.getDepartments().size());
		}
	}

}
