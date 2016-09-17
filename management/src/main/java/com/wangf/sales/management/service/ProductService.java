package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.rest.pojo.ProductPojo;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<ProductPojo> listAllProductNames() {
		Iterable<Product> products = productRepository.findAll();
		List<ProductPojo> pojos = new ArrayList<>();
		for (Product prod : products) {
			ProductPojo pojo = ProductPojo.from(prod);
			pojos.add(pojo);
		}
		return pojos;
	}
}
