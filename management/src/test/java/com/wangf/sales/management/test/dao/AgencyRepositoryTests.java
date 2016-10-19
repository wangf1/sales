package com.wangf.sales.management.test.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.AgencyRepository;
import com.wangf.sales.management.entity.Agency;
import com.wangf.sales.management.test.TestBase;

public class AgencyRepositoryTests extends TestBase {
	@Autowired
	private AgencyRepository repository;

	@Test
	public void findAll() throws Exception {
		Iterable<Agency> all = repository.findAll();
		System.out.println(all);
	}
}
