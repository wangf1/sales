package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.HospitalLevel;

@Repository
public interface HospitalLevelRepository extends PagingAndSortingRepository<HospitalLevel, Long> {

	HospitalLevel findByName(String name);

}
