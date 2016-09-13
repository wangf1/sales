package com.wangf.sales.management.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class ProductRepositoryTests extends TestBase {

	@Autowired
	private ProductRepository repository;

	@Test
	public void findsAllHospital() {
		List<Product> results = this.repository.findByName("PCT-Q");
		Assert.assertTrue(results.size() >= 1);
		for (Product hospital : results) {
			System.out.println(hospital.getName());
			System.out.println(hospital.getCompany().getName());
		}
	}

}
