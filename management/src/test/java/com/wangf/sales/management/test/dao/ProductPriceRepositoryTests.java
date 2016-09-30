package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.ProductPriceRepository;
import com.wangf.sales.management.entity.ProductPrice;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class ProductPriceRepositoryTests extends TestBase {
	@Autowired
	private ProductPriceRepository repository;

	@Test
	public void testFindByProductNameAndHospitalName() throws Exception {
		ProductPrice price = repository.findByProductNameAndHospitalName("PCT-Q", "长征");
		System.out.println(price);
	}

}
