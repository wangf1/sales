package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.rest.pojo.ProvincePojo;

@Service
@Transactional
public class ProvinceService {
	@Autowired
	private ProvinceRepository provinceRepository;

	public ProvincePojo insertOrUpdate(ProvincePojo pojo) {

		/*
		 * Firstly find by ID, if not exist, find by (installLocation,
		 * orderDepartment, salesPerson, month). The purpose of search two times
		 * is: 1). Search by ID to avoid treat update case as insert. 2). Search
		 * by columns to avoid insert duplicate record
		 */
		Province province = provinceRepository.findOne(pojo.getId());
		if (province == null) {
			province = provinceRepository.findByName(pojo.getName());
		}
		if (province == null) {
			province = new Province();
		}
		province.setName(pojo.getName());
		province.setRegion(pojo.getRegion());
		provinceRepository.save(province);

		ProvincePojo savedPojo = ProvincePojo.from(province);
		return savedPojo;
	}

	public List<Long> insertOrUpdate(List<ProvincePojo> pojos) {
		for (ProvincePojo pojo : pojos) {
			insertOrUpdate(pojo);
		}
		List<Long> allIds = new ArrayList<>();
		for (ProvincePojo pojo : pojos) {
			allIds.add(pojo.getId());
		}
		return allIds;
	}

	public List<ProvincePojo> listAll() {
		Iterable<Province> provinces = provinceRepository.findAll();
		List<ProvincePojo> pojos = new ArrayList<>();
		for (Province province : provinces) {
			ProvincePojo pojo = ProvincePojo.from(province);
			pojos.add(pojo);
		}
		return pojos;
	}

	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			provinceRepository.delete(id);
		}
	}

}
