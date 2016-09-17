package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.Hospital;

@RepositoryRestResource(collectionResourceRel = "hospital", path = "hospital")
public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long> {

	Hospital findByName(@Param("name") String name);

}
