package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wangf.sales.management.entity.Bid;
import com.wangf.sales.management.entity.User;

public interface BidRepository extends PagingAndSortingRepository<Bid, Long> {
	String query_findBetweenDate = "select b from Bid b " + " where b.date >= :startAt " + " and b.date < :endAt ";

	@Query(query_findBetweenDate)
	List<Bid> findBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	String findByUserAndBetweenDate = "select b from Bid b " + " where b.date >= :startAt " + " and b.date < :endAt "
			+ " and b.salesPerson = :salesPerson ";

	@Query(findByUserAndBetweenDate)
	List<Bid> findByUserAndBetweenDate(@Param("startAt") Date startAt, @Param("endAt") Date endAt,
			@Param("salesPerson") User salesPerson);

	String jpql_deleteById = "delete from Bid b where b.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);
}
