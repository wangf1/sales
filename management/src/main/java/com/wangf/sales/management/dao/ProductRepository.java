package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.Product;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	Product findByName(@Param("name") String name);

}
