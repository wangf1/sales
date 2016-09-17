package com.wangf.sales.management.test.dao;

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
		Hospital result = this.repository.findByName("长征");
		System.out.println(result);
	}

}
