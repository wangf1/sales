package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Speaker;
import com.wangf.sales.management.entity.User;

@Repository
public interface SpeakerRepository extends BaseRepository<Speaker, Long> {
	String query_findBetweenDate = "select s from Speaker s " + " where s.date >= :startAt " + " and s.date < :endAt ";

	@Query(query_findBetweenDate)
	List<Speaker> findBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	String findByUserAndBetweenDate = "select s from Speaker s " + " where s.date >= :startAt "
			+ " and s.date < :endAt " + " and s.salesPerson = :salesPerson ";

	@Query(findByUserAndBetweenDate)
	List<Speaker> findByUserAndBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt,
			@Param("salesPerson") User salesPerson);

	String jpql_deleteById = "delete from Speaker s where s.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);

	// String query_getSpeakerTypes = "select distinct s.type from Speaker s ";
	//
	// @Query(query_getSpeakerTypes)
	// Set<String> getSpeakerTypes();
}
