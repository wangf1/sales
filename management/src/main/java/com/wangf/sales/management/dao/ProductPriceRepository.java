package com.wangf.sales.management.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wangf.sales.management.entity.ProductPrice;

public interface ProductPriceRepository extends PagingAndSortingRepository<ProductPrice, Long> {

	String query_findByProductNameAndHospitalName = "select price from ProductPrice price " + " join price.product prod"
			+ " join price.hospital hos " + " where prod.name = :product " + " and hos.name = :hospital ";

	@Query(query_findByProductNameAndHospitalName)
	ProductPrice findByProductNameAndHospitalName(@Param("product") String productName,
			@Param("hospital") String hospitalName);
}
