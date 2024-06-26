package com.wangf.sales.management.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Province;

@Repository
public interface ProvinceRepository extends BaseRepository<Province, Long> {

	Province findByName(String name);

	List<Province> findAllByOrderByNameAsc();

	String query_listAllRegions = "select distinct p.region from Province p order by p.region asc";

	@Query(query_listAllRegions)
	Set<String> getAllRegions();

	String jpql_deleteById = "delete from Province p where p.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);

}
