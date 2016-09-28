package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

	@PersistenceContext
	private EntityManager em;

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

		HospitalLevel origionalLevel = entity.getLevel();

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
		// Must flush otherwise delete origionalLevel will fail
		em.flush();

		if (origionalLevel != null) {
			// Must explicitly remove
			origionalLevel.getHospitals().remove(entity);
			hospitalLevelService.deleteIfNoChildHospital(origionalLevel);
		}

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

	/**
	 * Delete the entity.
	 * 
	 * @param ids
	 */
	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			Hospital hospital = hospitalRepository.findOne(id);
			HospitalLevel level = hospital.getLevel();

			hospitalRepository.deleteById(id);

			hospitalLevelService.deleteIfNoChildHospital(level);
		}
	}

}
