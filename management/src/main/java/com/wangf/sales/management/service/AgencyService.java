package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.AgencyRecruitRepository;
import com.wangf.sales.management.dao.AgencyRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.entity.Agency;
import com.wangf.sales.management.entity.AgencyRecruit;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.AgencyPojo;
import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class AgencyService {

	@Autowired
	private AgencyRecruitRepository agencyRecruitRepository;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserService userService;

	public List<AgencyRecruitPojo> listAgencyRecruitsByCurrentUser(Date startAt, Date endAt) {
		List<AgencyRecruit> entities;
		if (SecurityUtils.isCurrentUserAdmin()) {
			entities = agencyRecruitRepository.findBetweenDate(startAt, endAt);
		} else {
			User currentUser = userService.getCurrentUser();
			entities = agencyRecruitRepository.findByUserAndBetweenDate(startAt, endAt, currentUser);
		}
		List<AgencyRecruitPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (AgencyRecruit entity : entities) {
			AgencyRecruitPojo pojo = AgencyRecruitPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<AgencyPojo> getAgenciesByCurrentUser() {
		List<Agency> entities = new ArrayList<>();
		if (SecurityUtils.isCurrentUserAdmin()) {
			Iterable<Agency> iterable = agencyRepository.findAll();
			for (Agency agency : iterable) {
				entities.add(agency);
			}
		} else {
			User currentUser = userService.getCurrentUser();
			List<AgencyRecruit> recruits = currentUser.getAgencyRecruit();
			for (AgencyRecruit recruit : recruits) {
				Agency agency = recruit.getAgency();
				entities.add(agency);
			}
		}
		List<AgencyPojo> result = new ArrayList<>();
		for (Agency entity : entities) {
			AgencyPojo pojo = AgencyPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<String> getAgencyLevels() {
		List<String> allLevels = agencyRepository.listAllAgencyLevels();
		return allLevels;
	}

	private Agency insertOrUpdateAgency(AgencyPojo pojo) {
		Agency entity = agencyRepository.findOne(pojo.getId());
		if (entity == null) {
			entity = agencyRepository.findByName(pojo.getName());
		}
		if (entity == null) {
			entity = new Agency();
		}
		entity.setName(pojo.getName());
		entity.setLevel(pojo.getLevel());
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);
		agencyRepository.save(entity);

		return entity;
	}

	public AgencyRecruitPojo insertOrUpdateAgencyRecruit(AgencyRecruitPojo pojo) {
		Agency agency = createAgencyIfNotExist(pojo);
		AgencyRecruit entity = agencyRecruitRepository.findOne(pojo.getId());
		if (entity == null) {
			entity = agencyRecruitRepository.findByAgencyNameAndProductName(pojo.getAgency(), pojo.getProduct());
		}
		if (entity == null) {
			entity = new AgencyRecruit();
		}
		entity.setAgency(agency);
		Product product = productRepository.findByName(pojo.getProduct());
		entity.setProduct(product);
		User salesPerson = userService.getCurrentUser();
		entity.setSalesPerson(salesPerson);

		agencyRecruitRepository.save(entity);
		AgencyRecruitPojo savedPojo = AgencyRecruitPojo.from(entity);
		return savedPojo;
	}

	private Agency createAgencyIfNotExist(AgencyRecruitPojo pojo) {
		AgencyPojo agencyPojo = new AgencyPojo();
		agencyPojo.setLevel(pojo.getLevel());
		agencyPojo.setName(pojo.getAgency());
		agencyPojo.setProvince(pojo.getProvince());
		Agency agency = insertOrUpdateAgency(agencyPojo);
		return agency;
	}

	public List<AgencyRecruitPojo> insertOrUpdateAgencyRecruits(List<AgencyRecruitPojo> pojos) {
		List<AgencyRecruitPojo> savedPojos = new ArrayList<>();
		for (AgencyRecruitPojo pojo : pojos) {
			AgencyRecruitPojo savedPojo = insertOrUpdateAgencyRecruit(pojo);
			savedPojos.add(savedPojo);
		}
		return savedPojos;
	}

	public List<Long> deleteAgencyRecruits(List<Long> ids) {
		for (Long id : ids) {
			agencyRecruitRepository.deleteById(id);
		}
		return ids;
	}
}
