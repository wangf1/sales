package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;

@RepositoryRestResource(collectionResourceRel = "salesRecord", path = "salesRecord")
public interface SalesRecordRepository extends PagingAndSortingRepository<SalesRecord, Long>, SalesRecordCustomQuery {

	List<SalesRecord> findBySalesPersonAndInstallLocationAndOrderDepartmentAndDateAfter(
			@Param("salesPerson") User salesPerson, @Param("installLocation") ProductInstallLocation installLocation,
			@Param("orderDepartment") Department orderDepartment, @Param("date") Date date);
}
