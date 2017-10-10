package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentMeetingRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.DepartmentMeeting;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.DepartmentMeetingPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class DepartmentMeetingService {
	@Autowired
	private DepartmentMeetingRepository departmentMeetingRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;

	public List<DepartmentMeetingPojo> getDepartmentMeetingsByCurrentUser(Date startAt, Date endAt) {
		List<DepartmentMeeting> entities;
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			entities = departmentMeetingRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> allUsersToSearch = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User user : allUsersToSearch) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(departmentMeetingRepository.findByUserAndBetweenDate(startAt, endAt, user));
			}
		}
		List<DepartmentMeetingPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (DepartmentMeeting entity : entities) {
			DepartmentMeetingPojo pojo = DepartmentMeetingPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<DepartmentMeetingPojo> insertOrUpdateDepartmentMeetings(List<DepartmentMeetingPojo> pojos) {
		List<DepartmentMeetingPojo> savedPojos = new ArrayList<>();
		for (DepartmentMeetingPojo pojo : pojos) {
			DepartmentMeetingPojo savedPojo = insertOrUpdateDepartmentMeeting(pojo);
			savedPojos.add(savedPojo);
		}
		return savedPojos;
	}

	private DepartmentMeetingPojo insertOrUpdateDepartmentMeeting(DepartmentMeetingPojo pojo) {
		DepartmentMeeting entity = departmentMeetingRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new DepartmentMeeting();
			isInsert = true;
		}
		Department department = departmentService.findOrCreateByDepartNameAndHospitalName(pojo.getDepartment(),
				pojo.getHospital());
		entity.setDepartment(department);
		Product product = productRepository.findByName(pojo.getProduct());
		entity.setProduct(product);
		entity.setActualCost(pojo.getActualCost());
		entity.setDate(pojo.getDate());
		entity.setPlanCost(pojo.getPlanCost());
		entity.setPurpose(pojo.getPurpose());
		entity.setStatus(pojo.getStatus());
		entity.setSubject(pojo.getSubject());
		entity.setNumberOfPeople(pojo.getNumberOfPeople());
		User salesPerson = userService.getCurrentUser();
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			entity.setSalesPerson(salesPerson);
		} else {
			entity.setLastModifyBy(salesPerson);
			entity.setLastModifyAt(new Date());
		}
		entity.setPlanDate(pojo.getPlanDate());

		departmentMeetingRepository.save(entity);
		DepartmentMeetingPojo savedPojo = DepartmentMeetingPojo.from(entity);
		return savedPojo;
	}

	public List<Long> deleteDepartmentMeetings(List<Long> ids) {
		for (Long id : ids) {
			departmentMeetingRepository.deleteById(id);
		}
		return ids;
	}

	public Set<String> getDepartmentMeetingStatuses() {
		Set<String> types = departmentMeetingRepository.getDepartmentMeetingStatuses();
		return types;
	}

	public List<DepartmentMeetingPojo> searchAgainstMultipleValues(SalesRecordSearchCriteria criteria) {
		if (!SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			// For non-admin user, only view data created by himself
			String salesPersonName = SecurityUtils.getCurrentUserName();
			List<String> salesPerson = new ArrayList<>();
			salesPerson.add(salesPersonName);
			criteria.setSalesPersonNames(salesPerson);
		}
		List<String> allEmployeesIncludeSelf = new ArrayList<>();
		for (String managerName : criteria.getSalesPersonNames()) {
			// If the user is a manager, also get all records of his employees
			User manager = userRepository.findOne(managerName);
			List<User> employeesIncludeSelf = userService.getAllUnderlineEmployeesIncludeSelf(manager);
			for (User employee : employeesIncludeSelf) {
				allEmployeesIncludeSelf.add(employee.getUserName());
			}
		}
		criteria.setSalesPersonNames(allEmployeesIncludeSelf);
		List<DepartmentMeeting> records = departmentMeetingRepository
				.searchFinishedMeetingAgainstMultipleValues(criteria);
		List<DepartmentMeetingPojo> result = new ArrayList<>();
		for (DepartmentMeeting record : records) {
			DepartmentMeetingPojo pojo = DepartmentMeetingPojo.from(record);
			result.add(pojo);
		}
		return result;
	}

}
