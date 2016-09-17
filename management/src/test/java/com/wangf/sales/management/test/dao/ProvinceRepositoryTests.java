package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class ProvinceRepositoryTests extends TestBase {

	@Autowired
	private ProvinceRepository repository;

	@Test
	public void findByName() throws Exception {
		Province province = repository.findByName("上海");
		System.out.println(province);
	}
}
