package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.SalesRecord;

@RepositoryRestResource(collectionResourceRel = "salesRecord", path = "salesRecord")
public interface SalesRecordRepository extends PagingAndSortingRepository<SalesRecord, Long> {

}
