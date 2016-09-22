package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.HospitalLevelRepository;
import com.wangf.sales.management.entity.HospitalLevel;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class HospitalLevalRepositoryTests extends TestBase {

	@Autowired
	private HospitalLevelRepository repository;

	@Test
	public void findsHospitalByName() {
		HospitalLevel result = this.repository.findByName("三甲");
		System.out.println(result);
	}

}
