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
	public void findByName() {
		List<Product> results = this.repository.findByName("PCT-Q");
		Assert.assertTrue(results.size() >= 1);
		for (Product product : results) {
			System.out.println(product.getName());
			System.out.println(product.getCompany().getName());
			Assert.assertTrue(product.getInstallLocations().size() >= 1);
		}
	}

}
