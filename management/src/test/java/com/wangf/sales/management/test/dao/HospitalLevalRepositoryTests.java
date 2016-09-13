package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
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
		List<HospitalLevel> results = this.repository.findByName("三甲");
		Assert.assertTrue(results.size() >= 1);
		for (HospitalLevel level : results) {
			System.out.println(level.getHospitals());
		}
	}

}
