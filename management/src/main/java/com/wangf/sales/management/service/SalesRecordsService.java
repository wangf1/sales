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
import com.wangf.sales.management.dao.UserPreferenceRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.entity.UserPreference;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.rest.pojo.StatusPojo;
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
	@Autowired
	private UserPreferenceRepository userPreferenceRepository;

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
		if (!SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
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
		 * Firstly find by ID, if not exist, find by (installLocation, orderDepartment,
		 * salesPerson, month). The purpose of search two times is: 1). Search by ID to
		 * avoid treat update case as insert. 2). Search by columns to avoid insert
		 * duplicate record
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
			salesRecordRepository.saveAndFlush(record);
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
		salesRecordRepository.deleteByIds(salesRecordIds);
	}

	public StatusPojo cloneLastMonthData(Date whichMonthToClone) {
		StatusPojo statusPojo = new StatusPojo();
		if (isAlreadyCloned()) {
			statusPojo.setStatusKey(StatusPojo.STATUS_KEY_ALREADY_CLONED_LAST_MONTH);
			return statusPojo;
		}
		List<SalesRecord> salesRecordsOfLastMonth = getSalesRrecordOfLastMonthForCurrentUser(whichMonthToClone);
		for (SalesRecord record : salesRecordsOfLastMonth) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			// Set id = 0 in order to perform insert if not exist
			pojo.setId(0);
			insertOrUpdate(pojo);
		}
		updateLastCloneMonth();
		return statusPojo;
	}

	private boolean isAlreadyCloned() {
		User currentUser = userService.getCurrentUser();
		UserPreference lastCloneMonthProperty = userPreferenceRepository.findByUserAndPropertyName(currentUser,
				UserPreference.PROPERTY_LAST_CLONE_MONTH);
		String lastCloneMonth = "";
		if (lastCloneMonthProperty != null) {
			lastCloneMonth = lastCloneMonthProperty.getPropertyValue();
		}
		Date lastMonth = DateUtils.getFirstDayOfLastMonth();
		String lastMonthString = DateUtils.getDateStringAsYYYYMM(lastMonth);
		boolean alreadyCloned = lastMonthString.equals(lastCloneMonth);
		return alreadyCloned;
	}

	private void updateLastCloneMonth() {
		User currentUser = userService.getCurrentUser();
		UserPreference lastCloneMonthProperty = userPreferenceRepository.findByUserAndPropertyName(currentUser,
				UserPreference.PROPERTY_LAST_CLONE_MONTH);
		if (lastCloneMonthProperty == null) {
			lastCloneMonthProperty = new UserPreference();
		}
		lastCloneMonthProperty.setUser(currentUser);
		lastCloneMonthProperty.setPropertyName(UserPreference.PROPERTY_LAST_CLONE_MONTH);
		Date lastMonth = DateUtils.getFirstDayOfLastMonth();
		String lastMonthString = DateUtils.getDateStringAsYYYYMM(lastMonth);
		lastCloneMonthProperty.setPropertyValue(lastMonthString);
		userPreferenceRepository.save(lastCloneMonthProperty);
	}

	private List<SalesRecord> getSalesRrecordOfLastMonthForCurrentUser(Date whichMonthToClone) {
		SalesRecordSearchCriteria criteria = new SalesRecordSearchCriteria();
		Date startAt, endAt;
		if (whichMonthToClone == null) {
			startAt = DateUtils.getFirstDayOfLastMonth();
			endAt = DateUtils.getFirstDayOfCurrentMonth();
		} else {
			startAt = DateUtils.getFirstDayOfMonth(whichMonthToClone);
			endAt = DateUtils.getFirstDayOfNextMonth(whichMonthToClone);
		}
		criteria.setStartAt(startAt);
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
