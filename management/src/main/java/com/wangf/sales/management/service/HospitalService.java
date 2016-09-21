package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.HospitalLevel;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.HospitalPojo;

@Service
@Transactional
public class HospitalService {
	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private HospitalLevelService hospitalLevelService;
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private UserRepository userRepository;

	public HospitalPojo insertOrUpdateForUser(HospitalPojo pojo, String currentUser) {
		/*
		 * Firstly find by ID, if not exist, find by property. The purpose of
		 * search two times is: 1). Search by ID to avoid treat update case as
		 * insert. 2). Search by columns to avoid insert duplicate record
		 */
		Hospital entity = hospitalRepository.findOne(pojo.getId());
		if (entity == null) {
			entity = hospitalRepository.findByName(pojo.getName());
		}
		if (entity == null) {
			entity = new Hospital();
		}
		entity.setName(pojo.getName());
		HospitalLevel level = hospitalLevelService.findOrCreate(pojo.getLevel());
		entity.setLevel(level);
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);
		User user = userRepository.findOne(currentUser);
		if (!entity.getUsers().contains(user)) {
			entity.getUsers().add(user);
		}
		hospitalRepository.save(entity);

		HospitalPojo savedPojo = HospitalPojo.from(entity);
		return savedPojo;
	}

	public List<Long> insertOrUpdateForUser(List<HospitalPojo> pojos, String currentUser) {
		for (HospitalPojo pojo : pojos) {
			insertOrUpdateForUser(pojo, currentUser);
		}
		List<Long> allIds = new ArrayList<>();
		for (HospitalPojo pojo : pojos) {
			allIds.add(pojo.getId());
		}
		return allIds;
	}

	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			hospitalRepository.delete(id);
		}
	}
}
