package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Hospital;

@Repository
public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long> {

	Hospital findByName(String name);

}
