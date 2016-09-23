package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	Product findByName(String name);

}
