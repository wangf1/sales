package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wangf.sales.management.entity.Agency;

public interface AgencyRepository extends PagingAndSortingRepository<Agency, Long> {

	Agency findByName(String name);

	String query_listAllAgencyLevels = "select distinct a.level from Agency a";

	@Query(query_listAllAgencyLevels)
	List<String> listAllAgencyLevels();
}
