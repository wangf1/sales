package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.Company;

@RepositoryRestResource(collectionResourceRel = "company", path = "company")
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

	Company findByName(@Param("name") String name);

}
