package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentNameRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.RegionMeetingRepository;
import com.wangf.sales.management.entity.DepartmentName;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.RegionMeeting;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.RegionMeetingPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class RegionMeetingService {
	@Autowired
	private RegionMeetingRepository regionMeetingRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private DepartmentNameRepository departmentNameRepository;

	public List<RegionMeetingPojo> getRegionMeetingsByCurrentUser(Date startAt, Date endAt) {
		List<RegionMeeting> entities;
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			entities = regionMeetingRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> allUsersToSearch = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User user : allUsersToSearch) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(regionMeetingRepository.findByUserAndBetweenDate(startAt, endAt, user));
			}
		}
		List<RegionMeetingPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (RegionMeeting entity : entities) {
			RegionMeetingPojo pojo = RegionMeetingPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<RegionMeetingPojo> insertOrUpdateRegionMeetings(List<RegionMeetingPojo> pojos) {
		List<RegionMeetingPojo> savedPojos = new ArrayList<>();
		for (RegionMeetingPojo pojo : pojos) {
			RegionMeetingPojo savedPojo = insertOrUpdateRegionMeeting(pojo);
			savedPojos.add(savedPojo);
		}
		return savedPojos;
	}

	private RegionMeetingPojo insertOrUpdateRegionMeeting(RegionMeetingPojo pojo) {
		RegionMeeting entity = regionMeetingRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new RegionMeeting();
			isInsert = true;
		}
		entity.setExhibitionCost(pojo.getExhibitionCost());
		entity.setForm(pojo.getForm());
		entity.setName(pojo.getName());
		entity.setNumberOfPeople(pojo.getNumberOfPeople());
		entity.setOtherCost(pojo.getOtherTAndE());
		entity.setOtherTAndE(pojo.getOtherTAndE());
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);
		entity.setSatelliteMeetingCost(pojo.getSatelliteMeetingCost());
		entity.setSpeakerCost(pojo.getSpeakerCost());
		entity.setStatus(pojo.getStatus());
		entity.setType(pojo.getType());
		DepartmentName departmentName = departmentNameRepository.findByName(pojo.getDepartment());
		entity.setDepartmentName(departmentName);
		User currentUser = userService.getCurrentUser();
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			entity.setSalesPerson(currentUser);
		} else {
			entity.setLastModifyBy(currentUser);
			entity.setLastModifyAt(new Date());
		}

		regionMeetingRepository.save(entity);
		RegionMeetingPojo savedPojo = RegionMeetingPojo.from(entity);
		return savedPojo;
	}

	public List<Long> deleteRegionMeetings(List<Long> ids) {
		for (Long id : ids) {
			regionMeetingRepository.deleteById(id);
		}
		return ids;
	}

	public Set<String> getRegionMeetingStatuses() {
		Set<String> types = regionMeetingRepository.getRegionMeetingStatuses();
		return types;
	}

	public Set<String> getRegionMeetingTypes() {
		Set<String> types = regionMeetingRepository.getRegionMeetingTypes();
		return types;
	}

}
