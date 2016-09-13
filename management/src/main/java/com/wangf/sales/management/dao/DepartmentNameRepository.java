package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.DepartmentName;

@RepositoryRestResource(collectionResourceRel = "departmentName", path = "departmentName")
public interface DepartmentNameRepository extends PagingAndSortingRepository<DepartmentName, Long> {
	List<DepartmentName> findByName(@Param("name") String name);
}
