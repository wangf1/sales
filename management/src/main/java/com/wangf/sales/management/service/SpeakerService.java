package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.SpeakerRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.Speaker;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SpeakerPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class SpeakerService {

	@Autowired
	private SpeakerRepository speakerRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	public List<SpeakerPojo> getSpeakersByCurrentUser(Date startAt, Date endAt) {
		List<Speaker> entities;
		if (SecurityUtils.isCurrentUserAdmin()) {
			entities = speakerRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> employees = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User employee : employees) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(speakerRepository.findByUserAndBetweenDate(startAt, endAt, employee));
			}
		}
		List<SpeakerPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (Speaker entity : entities) {
			SpeakerPojo pojo = SpeakerPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<SpeakerPojo> insertOrUpdateSpeakers(List<SpeakerPojo> pojos) {
		List<SpeakerPojo> savedPojos = new ArrayList<>();
		for (SpeakerPojo pojo : pojos) {
			SpeakerPojo savedPojo = insertOrUpdateSpeaker(pojo);
			savedPojos.add(savedPojo);
		}
		return savedPojos;
	}

	private SpeakerPojo insertOrUpdateSpeaker(SpeakerPojo pojo) {
		Speaker entity = speakerRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new Speaker();
			isInsert = true;
		}
		Hospital hospital = hospitalRepository.findByName(pojo.getHospital());
		entity.setHospital(hospital);
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);
		entity.setSpeakerName(pojo.getSpeakerName());
		entity.setType(pojo.getType());
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			User salesPerson = userService.getCurrentUser();
			entity.setSalesPerson(salesPerson);
		}

		speakerRepository.save(entity);
		SpeakerPojo savedPojo = SpeakerPojo.from(entity);
		return savedPojo;
	}

	public List<Long> deleteSpeakers(List<Long> ids) {
		for (Long id : ids) {
			speakerRepository.deleteById(id);
		}
		return ids;
	}

	public Set<String> getSpeakerTypes() {
		Set<String> types = speakerRepository.getSpeakerTypes();
		return types;
	}
}
