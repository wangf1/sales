package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.ProvincePojo;

@Service
@Transactional
public class ProvinceService {
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	public ProvincePojo insertOrUpdate(ProvincePojo pojo) {

		/*
		 * Firstly find by ID, if not exist, find by property. The purpose of
		 * search two times is: 1). Search by ID to avoid treat update case as
		 * insert. 2). Search by columns to avoid insert duplicate record
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
		// For new created province, Must save and flush and CLEAR province
		// before save the user-province
		// relationship. Otherwise for new created province, the user-province
		// relationship will not be saved, root cause unknown.
		provinceRepository.save(province);
		em.flush();
		em.clear();

		Province alreadySaved = provinceRepository.findOne(province.getId());
		alreadySaved.getUsers().clear();
		for (String userName : pojo.getSalesPersons()) {
			User user = userRepository.findByUserName(userName);
			alreadySaved.getUsers().add(user);
		}
		provinceRepository.save(alreadySaved);

		ProvincePojo savedPojo = ProvincePojo.from(alreadySaved);
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
		Iterable<Province> provinces = provinceRepository.findAllByOrderByNameAsc();
		List<ProvincePojo> pojos = new ArrayList<>();
		for (Province province : provinces) {
			ProvincePojo pojo = ProvincePojo.from(province);
			pojos.add(pojo);
		}
		return pojos;
	}

	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			// If there is hospitals belong to this province, delete will not be
			// allowed.
			provinceRepository.delete(id);
		}
	}

	public Set<String> listAllRegions() {
		Set<String> resions = provinceRepository.getAllRegions();
		return resions;
	}

}
