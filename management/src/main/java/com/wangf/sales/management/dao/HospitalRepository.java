package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.Province;

@Repository
public interface HospitalRepository extends BaseRepository<Hospital, Long> {

	Hospital findByName(String name);

	String jpql_deleteByIds = "delete from Hospital h where h.id in :ids";

	@Modifying
	@Query(jpql_deleteByIds)
	void deleteByIds(@Param("ids") List<Long> ids);

	String jpql_deleteById = "delete from Hospital h where h.id = :id";

	@Modifying
	@Query(jpql_deleteById)
	void deleteById(@Param("id") Long id);

	List<Hospital> findByProvinceIn(List<Province> provinces);

}
