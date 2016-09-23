package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.DepartmentName;

@Repository
public interface DepartmentNameRepository extends PagingAndSortingRepository<DepartmentName, Long> {
	DepartmentName findByName(String name);
}
