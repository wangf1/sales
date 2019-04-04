package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.DepartmentMeeting;
import com.wangf.sales.management.entity.User;

@Repository
public interface DepartmentMeetingRepository
		extends BaseRepository<DepartmentMeeting, Long>, DepartmentMeetingCustomQuery {
	String query_findBetweenDate = "select dm from DepartmentMeeting dm " + " where dm.date >= :startAt "
			+ " and dm.date < :endAt ";

	@Query(query_findBetweenDate)
	List<DepartmentMeeting> findBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	String findByUserAndBetweenDate = "select dm from DepartmentMeeting dm " + " where dm.date >= :startAt "
			+ " and dm.date < :endAt " + " and dm.salesPerson = :salesPerson ";

	@Query(findByUserAndBetweenDate)
	List<DepartmentMeeting> findByUserAndBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt,
			@Param("salesPerson") User salesPerson);

	String jpql_deleteById = "delete from DepartmentMeeting dm where dm.id = :id";

	@Override
	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);

	String query_getDepartmentMeetingStatuses = "select distinct dm.status from DepartmentMeeting dm ";

	@Query(query_getDepartmentMeetingStatuses)
	Set<String> getDepartmentMeetingStatuses();

}
