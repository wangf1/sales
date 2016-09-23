package com.wangf.sales.management.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.ProductInstallLocation;

@Repository
public interface ProductInstallLocationRepository extends PagingAndSortingRepository<ProductInstallLocation, Long> {

	String query_findByProductDepartmentHospital = "select location from ProductInstallLocation location "
			+ " join location.product prod " + " join location.department dep "
			+ " join location.department.hospital hos " + " where  prod.name = :product "
			+ " and dep.name.name = :department " + " and hos.name = :hospital ";

	@Query(query_findByProductDepartmentHospital)
	ProductInstallLocation findByProductDepartmentHospital(@Param("product") String productName,
			@Param("department") String departName, @Param("hospital") String hospitalName);
}
