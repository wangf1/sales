package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Province;

@Repository
public interface ProvinceRepository extends PagingAndSortingRepository<Province, Long> {

	Province findByName(String name);

	List<Province> findAllByOrderByNameAsc();

}
