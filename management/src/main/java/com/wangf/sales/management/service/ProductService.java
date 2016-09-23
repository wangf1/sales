package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.entity.Company;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.rest.pojo.ProductPojo;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CompanyService companyService;

	public List<ProductPojo> listAllProductNames() {
		Iterable<Product> products = productRepository.findAll();
		List<ProductPojo> pojos = new ArrayList<>();
		for (Product prod : products) {
			ProductPojo pojo = ProductPojo.from(prod);
			pojos.add(pojo);
		}
		return pojos;
	}

	public ProductPojo insertOrUpdate(ProductPojo pojo) {

		/*
		 * Firstly find by ID, if not exist, find by property. The purpose of
		 * search two times is: 1). Search by ID to avoid treat update case as
		 * insert. 2). Search by columns to avoid insert duplicate record
		 */
		Product product = productRepository.findOne(pojo.getId());
		if (product == null) {
			product = productRepository.findByName(pojo.getName());
		}
		if (product == null) {
			product = new Product();
		}
		product.setName(pojo.getName());
		Company company = companyService.findOrCreateByName(pojo.getCompany());
		product.setCompany(company);
		productRepository.save(product);

		ProductPojo savedPojo = ProductPojo.from(product);
		return savedPojo;
	}

	public List<ProductPojo> insertOrUpdate(List<ProductPojo> pojos) {
		List<ProductPojo> allSaved = new ArrayList<>();
		for (ProductPojo pojo : pojos) {
			ProductPojo saved = insertOrUpdate(pojo);
			allSaved.add(saved);
		}
		return allSaved;
	}

	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			Product toDelete = productRepository.findOne(id);
			toDelete.setCompany(null);
			productRepository.delete(toDelete);
		}
	}
}
