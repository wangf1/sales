package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wangf.sales.management.entity.AgencyTraining;
import com.wangf.sales.management.entity.User;

public interface AgencyTrainingRepository extends PagingAndSortingRepository<AgencyTraining, Long> {
	String query_findBetweenDate = "select ar from AgencyTraining ar " + " where ar.date >= :startAt "
			+ " and ar.date < :endAt ";

	@Query(query_findBetweenDate)
	List<AgencyTraining> findBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	String findByUserAndBetweenDate = "select ar from AgencyTraining ar " + " where ar.date >= :startAt "
			+ " and ar.date < :endAt " + " and ar.salesPerson = :salesPerson ";

	@Query(findByUserAndBetweenDate)
	List<AgencyTraining> findByUserAndBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt,
			@Param("salesPerson") User salesPerson);

	String jpql_deleteById = "delete from AgencyTraining a where a.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);
}
