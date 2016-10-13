package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Department;

@Repository
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {
	String query_findByDepartmentNameHospitalName = "select dep from Department dep " + " join dep.hospital hos "
			+ " where dep.name.name = :department " + " and hos.name = :hospital ";

	@Query(query_findByDepartmentNameHospitalName)
	Department findByDepartmentNameHospitalName(@Param("department") String departName,
			@Param("hospital") String hospitalName);

	String jpql_deleteByIds = "delete from Department d where d.id in :ids";

	@Modifying
	@Query(jpql_deleteByIds)
	void deleteByIds(@Param("ids") List<Long> ids);

}
