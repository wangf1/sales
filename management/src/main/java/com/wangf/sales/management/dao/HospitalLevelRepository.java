package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.HospitalLevel;

@RepositoryRestResource(collectionResourceRel = "hospitalLevel", path = "hospitalLevel")
public interface HospitalLevelRepository extends PagingAndSortingRepository<HospitalLevel, Long> {

	List<HospitalLevel> findByName(@Param("name") String name);

}
