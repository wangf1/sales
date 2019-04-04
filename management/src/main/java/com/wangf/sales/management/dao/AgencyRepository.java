package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wangf.sales.management.entity.Agency;

public interface AgencyRepository extends BaseRepository<Agency, Long> {

	Agency findByName(String name);

	String query_listAllAgencyLevels = "select distinct a.level from Agency a";

	@Query(query_listAllAgencyLevels)
	List<String> listAllAgencyLevels();

	String jpql_deleteById = "delete from Agency a where a.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);
}
