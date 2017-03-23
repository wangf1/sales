package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.RegionMeeting;
import com.wangf.sales.management.entity.User;

@Repository
public interface RegionMeetingRepository extends PagingAndSortingRepository<RegionMeeting, Long> {
	String query_findBetweenDate = "select rm from RegionMeeting rm " + " where rm.date >= :startAt "
			+ " and rm.date < :endAt ";

	@Query(query_findBetweenDate)
	List<RegionMeeting> findBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	String findByUserAndBetweenDate = "select rm from RegionMeeting rm " + " where rm.date >= :startAt "
			+ " and rm.date < :endAt " + " and rm.salesPerson = :salesPerson ";

	@Query(findByUserAndBetweenDate)
	List<RegionMeeting> findByUserAndBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt,
			@Param("salesPerson") User salesPerson);

	String jpql_deleteById = "delete from RegionMeeting rm where rm.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);

	String query_getRegionMeetingStatuses = "select distinct rm.status from RegionMeeting rm ";

	@Query(query_getRegionMeetingStatuses)
	Set<String> getRegionMeetingStatuses();

	String query_getRegionMeetingTypes = "select distinct rm.type from RegionMeeting rm ";

	@Query(query_getRegionMeetingTypes)
	Set<String> getRegionMeetingTypes();

}
