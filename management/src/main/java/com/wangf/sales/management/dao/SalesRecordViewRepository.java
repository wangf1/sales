package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.SalesRecordView;

@Repository
public interface SalesRecordViewRepository extends PagingAndSortingRepository<SalesRecordView, Long> {

	static final String jql_findNewCustomer = "select distinct thisMonth.hospital, thisMonth.product, thisMonth.region, thisMonth.province, thisMonth.sales_person from\n"
			+ "	(SELECT *\n" + "	FROM sales_record_view\n"
			+ "	where date >= :thisMonthFirstDay and date < :nextMonthFirstday) as thisMonth \n" + "left join \n"
			+ "	(SELECT *\n" + "	FROM sales_record_view\n"
			+ "	where date >= :previoudMonthFirstday and date < :dayAfterPrevioudMonthlastDay) as previoudMonth \n"
			+ "on thisMonth.hospital=previoudMonth.hospital \n" + "	and thisMonth.product=previoudMonth.product \n"
			+ "where previoudMonth.hospital is null\n" + "and thisMonth.province in :provinces\n"
			+ "group by thisMonth.hospital, thisMonth.product";

	@Query(value = jql_findNewCustomer, nativeQuery = true)
	List<Object[]> findNewCustomer(@Param("thisMonthFirstDay") Date thisMonthFirstDay,
			@Param("nextMonthFirstday") Date nextMonthFirstday,
			@Param("previoudMonthFirstday") Date previoudMonthFirstday,
			@Param("dayAfterPrevioudMonthlastDay") Date dayAfterPrevioudMonthlastDay,
			@Param("provinces") List<String> provinces);

	static final String jql_findLostCustomer = "select distinct previoudMonth.hospital, previoudMonth.product, previoudMonth.region, previoudMonth.province, previoudMonth.sales_person from\n"
			+ "	(SELECT *\n" + "	FROM sales_record_view\n"
			+ "	where date >= :thisMonthFirstDay and date < :nextMonthFirstday) as thisMonth \n" + "right join \n"
			+ "	(SELECT *\n" + "	FROM sales_record_view\n"
			+ "	where date >= :previoudMonthFirstday and date < :dayAfterPrevioudMonthlastDay) as previoudMonth \n"
			+ "on thisMonth.hospital=previoudMonth.hospital \n" + "	and thisMonth.product=previoudMonth.product \n"
			+ "where (thisMonth.hospital is null or thisMonth.quantity<=0)\n"
			+ "and previoudMonth.province in :provinces\n" + "group by previoudMonth.hospital, previoudMonth.product";

	@Query(value = jql_findLostCustomer, nativeQuery = true)
	List<Object[]> findLostCustomer(@Param("thisMonthFirstDay") Date thisMonthFirstDay,
			@Param("nextMonthFirstday") Date nextMonthFirstday,
			@Param("previoudMonthFirstday") Date previoudMonthFirstday,
			@Param("dayAfterPrevioudMonthlastDay") Date dayAfterPrevioudMonthlastDay,
			@Param("provinces") List<String> provinces);
}
