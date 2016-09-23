package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Company;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

	Company findByName(String name);

}
