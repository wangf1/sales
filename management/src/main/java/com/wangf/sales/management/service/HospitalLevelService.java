package com.wangf.sales.management.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.HospitalLevelRepository;
import com.wangf.sales.management.entity.HospitalLevel;

@Service
@Transactional
public class HospitalLevelService {

	@Autowired
	private HospitalLevelRepository hospitalLevelRepository;

	public HospitalLevel findOrCreate(String name) {
		HospitalLevel entity = hospitalLevelRepository.findByName(name);
		if (entity == null) {
			entity = new HospitalLevel();
		}
		entity.setName(name);
		;
		hospitalLevelRepository.save(entity);
		return entity;
	}
}
