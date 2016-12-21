package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.utils.DateUtils;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class SalesRecordsService {
	@Autowired
	private SalesRecordRepository salesRecordRepository;
	@Autowired
	private ProductInstallLocationService installLocationService;
	@Autowired
	private DepartmentService departmentServcie;

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	public List<SalesRecordPojo> searchAgainstSingleValues(String productName, String salesPersonName,
			String hospitalName, String locationDepartmentName, String orderDepartName, Date startFrom) {

		List<SalesRecord> records = salesRecordRepository.searchAgainstSingleValues(productName, salesPersonName,
				hospitalName, locationDepartmentName, orderDepartName, startFrom);

		List<SalesRecordPojo> result = new ArrayList<>();
		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			result.add(pojo);
		}
		return result;
	}

	public List<SalesRecordPojo> searchAgainstSingleValues(SalesRecordPojo criteria) {
		List<SalesRecordPojo> records = searchAgainstSingleValues(criteria.getProduct(), criteria.getSalesPerson(),
				criteria.getHospital(), criteria.getInstallDepartment(), criteria.getOrderDepartment(),
				criteria.getDate());
		return records;
	}

	public List<SalesRecordPojo> searchAgainstMultipleValues(SalesRecordSearchCriteria criteria) {
		if (!SecurityUtils.isCurrentUserAdmin()) {
			// For non-admin user, only view sales records created by himself
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
		List<SalesRecord> records = salesRecordRepository.searchAgainstMultipleValues(criteria);
		List<SalesRecordPojo> result = new ArrayList<>();
		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			result.add(pojo);
		}
		return result;
	}

	public SalesRecordPojo insertOrUpdate(SalesRecordPojo pojo) {
		ProductInstallLocation installLocation = installLocationService.findOrCreateByProductDepartmentHospital(
				pojo.getProduct(), pojo.getInstallDepartment(), pojo.getHospital());
		Department orderDepartment = departmentServcie
				.findOrCreateByDepartNameAndHospitalName(pojo.getOrderDepartment(), pojo.getHospital());
		User salesPerson;
		if (StringUtils.isNotBlank(pojo.getSalesPerson())) {
			salesPerson = userRepository.findByUserName(pojo.getSalesPerson().trim());
		} else {
			salesPerson = userService.getCurrentUser();
		}
		/*
		 * Firstly find by ID, if not exist, find by (installLocation,
		 * orderDepartment, salesPerson, month). The purpose of search two times
		 * is: 1). Search by ID to avoid treat update case as insert. 2). Search
		 * by columns to avoid insert duplicate record
		 */
		SalesRecord record = salesRecordRepository.findOne(pojo.getId());
		if (record == null) {
			record = salesRecordRepository.searchByLocationOrderDepartPersonInCurrentMonth(installLocation.getId(),
					orderDepartment.getId(), salesPerson.getUserName());
		}
		boolean alreadyExisting = true;
		if (record == null) {
			record = new SalesRecord();
			alreadyExisting = false;
		}
		record.setInstallLocation(installLocation);
		record.setOrderDepartment(orderDepartment);
		if (!alreadyExisting) {
			// For existing sales record, do not change the sales person, since
			// manager or admin may modify sales record for a user, should not
			// change the sales person of a sales record
			record.setSalesPerson(salesPerson);
		} else {
			User currentUser = userService.getCurrentUser();
			record.setLastModifyBy(currentUser);
			record.setLastModifyAt(new Date());
		}
		record.setQuantity(pojo.getQuantity());
		if (alreadyExisting) {
			// salesRecordRepository.save(record) method cannot update, possibly
			// because the record is a detached one from other entity manager
			// context. So user "merge" to resolve the issue.
			salesRecordRepository.getEntityManager().merge(record);
		} else {
			// For new entity, merge method will throw exception, should use
			// save
			salesRecordRepository.save(record);
			// Refresh record in order to get the date of a new created record
			salesRecordRepository.getEntityManager().refresh(record);
		}

		SalesRecordPojo savedPojo = SalesRecordPojo.from(record);
		savedPojo.setAlreadyExisting(alreadyExisting);
		return savedPojo;
	}

	public List<Long> insertOrUpdate(Iterable<SalesRecordPojo> pojos) {
		for (SalesRecordPojo pojo : pojos) {
			insertOrUpdate(pojo);
		}
		List<Long> allIds = new ArrayList<>();
		for (SalesRecordPojo pojo : pojos) {
			allIds.add(pojo.getId());
		}
		return allIds;
	}

	public void deleteSalesRecords(List<Long> salesRecordIds) {
		for (Long id : salesRecordIds) {
			salesRecordRepository.delete(id);
		}
	}

	public void cloneLastMonthData() {
		List<SalesRecord> salesRecordsOfLastMonth = getSalesRrecordOfLastMonthForCurrentUser();
		for (SalesRecord record : salesRecordsOfLastMonth) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			// Set id = 0 in order to perform insert if not exist
			pojo.setId(0);
			insertOrUpdate(pojo);
		}
	}

	private List<SalesRecord> getSalesRrecordOfLastMonthForCurrentUser() {
		SalesRecordSearchCriteria criteria = new SalesRecordSearchCriteria();
		Date startAt = DateUtils.getFirstDayOfLastMonth();
		criteria.setStartAt(startAt);
		Date endAt = DateUtils.getFirstDayOfCurrentMonth();
		criteria.setEndAt(endAt);
		criteria.setIncludeEndAt(false);

		String salesPersonName = SecurityUtils.getCurrentUserName();
		List<String> salesPerson = new ArrayList<>();
		salesPerson.add(salesPersonName);
		criteria.setSalesPersonNames(salesPerson);

		List<SalesRecord> salesRecordsOfLastMonth = salesRecordRepository.searchAgainstMultipleValues(criteria);
		return salesRecordsOfLastMonth;
	}
}
