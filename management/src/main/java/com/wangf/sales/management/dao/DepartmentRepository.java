package com.wangf.sales.management.dao;

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
}
