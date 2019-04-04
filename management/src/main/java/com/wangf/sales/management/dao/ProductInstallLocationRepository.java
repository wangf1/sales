package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.ProductInstallLocation;

@Repository
public interface ProductInstallLocationRepository extends BaseRepository<ProductInstallLocation, Long> {

	String query_findByProductDepartmentHospital = "select location from ProductInstallLocation location "
			+ " join location.product prod " + " join location.department dep "
			+ " join location.department.hospital hos " + " where  prod.name = :product "
			+ " and dep.name.name = :department " + " and hos.name = :hospital ";

	@Query(query_findByProductDepartmentHospital)
	ProductInstallLocation findByProductDepartmentHospital(@Param("product") String productName,
			@Param("department") String departName, @Param("hospital") String hospitalName);

	String jpql_deleteByIds = "delete from ProductInstallLocation l where l.id in :ids";

	@Modifying
	@Query(jpql_deleteByIds)
	void deleteByIds(@Param("ids") List<Long> ids);
}
