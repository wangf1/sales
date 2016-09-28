package com.wangf.sales.management.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	Product findByName(String name);

	String jpql_deleteById = "delete from Product p where p.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);
}
