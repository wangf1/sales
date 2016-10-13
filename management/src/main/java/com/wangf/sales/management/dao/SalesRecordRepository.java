package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;

@Repository
public interface SalesRecordRepository extends PagingAndSortingRepository<SalesRecord, Long>, SalesRecordCustomQuery {

	List<SalesRecord> findBySalesPersonAndInstallLocationAndOrderDepartmentAndDateAfter(User salesPerson,
			ProductInstallLocation installLocation, Department orderDepartment, Date date);

	String jpql_deleteByIds = "delete from SalesRecord s where s.id in :ids";

	@Modifying
	@Query(jpql_deleteByIds)
	void deleteByIds(@Param("ids") List<Long> ids);
}
