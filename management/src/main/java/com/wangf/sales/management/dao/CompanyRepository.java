package com.wangf.sales.management.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Company;

@Repository
public interface CompanyRepository extends BaseRepository<Company, Long> {

	Company findByName(String name);

	String jpql_deleteById = "delete from Company c where c.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);
}
