package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.Province;

@RepositoryRestResource(collectionResourceRel = "province", path = "province")
public interface ProvinceRepository extends PagingAndSortingRepository<Province, Long> {

	Province findByName(@Param("name") String name);

}
