package com.wangf.sales.management.test.dao;

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
		Product product = this.repository.findByName("PCT-Q");
		System.out.println(product.getName());
		System.out.println(product.getCompany().getName());
		Assert.assertTrue(product.getInstallLocations().size() >= 1);
		Assert.assertTrue(product.getInstallLocations().get(0).getSalesRecords().size() >= 1);
	}

}
