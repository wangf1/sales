package com.wangf.sales.management.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.AgencyRecruitRepository;
import com.wangf.sales.management.dao.AgencyRepository;
import com.wangf.sales.management.dao.AgencyTrainingRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Agency;
import com.wangf.sales.management.entity.AgencyRecruit;
import com.wangf.sales.management.entity.AgencyTraining;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.AgencyEventPojo;
import com.wangf.sales.management.rest.pojo.AgencyPojo;
import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.rest.pojo.AgencyTrainingPojo;
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
	@Autowired
	private AgencyTrainingRepository agencyTrainingRepository;
	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	public List<AgencyRecruitPojo> listAgencyRecruitsByCurrentUser(Date startAt, Date endAt) {
		List<AgencyRecruit> entities;
		if (SecurityUtils.isCurrentUserAdmin()) {
			entities = agencyRecruitRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> employees = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User employee : employees) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(agencyRecruitRepository.findByUserAndBetweenDate(startAt, endAt, employee));
			}
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
			List<User> allUnderLineAndSelf = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User user : allUnderLineAndSelf) {
				List<AgencyRecruit> recruits = user.getAgencyRecruit();
				for (AgencyRecruit recruit : recruits) {
					Agency agency = recruit.getAgency();
					entities.add(agency);
				}
			}
		}
		List<AgencyPojo> result = new ArrayList<>();
		for (Agency entity : entities) {
			AgencyPojo pojo = AgencyPojo.from(entity);
			result.add(pojo);
		}

		Comparator<AgencyPojo> compareByName = (AgencyPojo a, AgencyPojo b) -> {
			Collator chineseCollator = Collator.getInstance(Locale.CHINESE);
			int compareResult = chineseCollator.compare(a.getName(), b.getName());
			return compareResult;
		};
		Collections.sort(result, compareByName);
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

	private AgencyRecruitPojo insertOrUpdateAgencyRecruit(AgencyRecruitPojo pojo) {
		Agency agency = createAgencyIfNotExist(pojo);
		AgencyRecruit entity = agencyRecruitRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new AgencyRecruit();
			isInsert = true;
		}
		entity.setAgency(agency);

		User currentUser = userService.getCurrentUser();
		User salesPerson;
		if (StringUtils.isNotBlank(pojo.getSalesPerson())) {
			// For the import case
			salesPerson = userRepository.findByUserName(pojo.getSalesPerson().trim());
		} else {
			salesPerson = currentUser;
		}
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			entity.setSalesPerson(salesPerson);
		} else {
			entity.setLastModifyBy(currentUser);
			entity.setLastModifyAt(new Date());
		}
		// For new created province, Must save and flush and CLEAR province
		// before save the user-province
		// relationship. Otherwise for new created province, the user-province
		// relationship will not be saved, root cause unknown.
		agencyRecruitRepository.save(entity);
		em.flush();
		em.clear();

		AgencyRecruit alreadySaved = agencyRecruitRepository.findOne(entity.getId());
		alreadySaved.getProducts().clear();
		for (String prodName : pojo.getProducts()) {
			Product product = productRepository.findByName(prodName);
			alreadySaved.getProducts().add(product);
		}
		agencyRecruitRepository.save(alreadySaved);

		AgencyRecruitPojo savedPojo = AgencyRecruitPojo.from(alreadySaved);
		return savedPojo;
	}

	private Agency createAgencyIfNotExist(AgencyEventPojo pojo) {
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

	public List<AgencyTrainingPojo> listAgencyTrainingsByCurrentUser(Date startAt, Date endAt) {
		List<AgencyTraining> entities;
		if (SecurityUtils.isCurrentUserAdmin()) {
			entities = agencyTrainingRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> employees = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User employee : employees) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(agencyTrainingRepository.findByUserAndBetweenDate(startAt, endAt, employee));
			}
		}
		List<AgencyTrainingPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (AgencyTraining entity : entities) {
			AgencyTrainingPojo pojo = AgencyTrainingPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	private AgencyTrainingPojo insertOrUpdateAgencyTraining(AgencyTrainingPojo pojo) {
		Agency agency = createAgencyIfNotExist(pojo);
		AgencyTraining entity = agencyTrainingRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new AgencyTraining();
			isInsert = true;
		}
		entity.setAgency(agency);
		User currentUser = userService.getCurrentUser();
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			entity.setSalesPerson(currentUser);
		} else {
			entity.setLastModifyBy(currentUser);
			entity.setLastModifyAt(new Date());
		}
		entity.setTrainingContent(pojo.getTrainingContent());

		// For new created province, Must save and flush and CLEAR province
		// before save the user-province
		// relationship. Otherwise for new created province, the user-province
		// relationship will not be saved, root cause unknown.
		agencyTrainingRepository.save(entity);
		em.flush();
		em.clear();

		AgencyTraining alreadySaved = agencyTrainingRepository.findOne(entity.getId());
		alreadySaved.getProducts().clear();
		for (String prodName : pojo.getProducts()) {
			Product product = productRepository.findByName(prodName);
			alreadySaved.getProducts().add(product);
		}
		agencyTrainingRepository.save(alreadySaved);

		AgencyTrainingPojo savedPojo = AgencyTrainingPojo.from(alreadySaved);
		return savedPojo;
	}

	public List<AgencyTrainingPojo> insertOrUpdateAgencyTrainings(List<AgencyTrainingPojo> pojos) {
		List<AgencyTrainingPojo> savedPojos = new ArrayList<>();
		for (AgencyTrainingPojo pojo : pojos) {
			AgencyTrainingPojo savedPojo = insertOrUpdateAgencyTraining(pojo);
			savedPojos.add(savedPojo);
		}
		return pojos;
	}

	public List<Long> deleteAgencyTrainings(List<Long> ids) {
		for (Long id : ids) {
			agencyTrainingRepository.deleteById(id);
		}
		return ids;
	}
}
