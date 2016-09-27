package com.wangf.sales.management.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.SalesRecordsService;
import com.wangf.sales.management.utils.SecurityUtils;

@RestController
public class SalesRecordsController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SalesRecordsService salesRecordsService;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(path = "/getSalesRecordsByCurrentUser", method = RequestMethod.GET)
	public List<SalesRecordPojo> getSalesRecords() {
		List<User> users = userRepository.findByUserName(SecurityUtils.getCurrentUserName());
		if (users.isEmpty()) {
			return null;
		}
		User currentUser = users.get(0);
		List<SalesRecord> records = currentUser.getSalesRecords();
		List<SalesRecordPojo> result = new ArrayList<>();
		for (SalesRecord record : records) {
			SalesRecordPojo pojo = SalesRecordPojo.from(record);
			result.add(pojo);
		}
		return result;
	}

	/**
	 * Example query string:
	 * 
	 * <pre>
	 * http://localhost:8090/management/salesRecordsAdvanceSearch?product=PCT-Q&locationDepartmentName=ICU&hospital=%E9%95%BF%E5%BE%81&orderDepartName=ICU&startFrom=2016-09-01
	 * </pre>
	 * 
	 * @param productName
	 * @param locationDepartmentName
	 * @param hospitalName
	 * @param orderDepartName
	 * @param startFrom
	 * @return
	 */
	@RequestMapping(path = "/salesRecordsAdvanceSearch", method = RequestMethod.GET)
	public List<SalesRecordPojo> advanceSearch(@RequestParam(name = "product", required = false) String productName,
			@RequestParam(name = "locationDepartmentName", required = false) String locationDepartmentName,
			@RequestParam(name = "hospital", required = false) String hospitalName,
			@RequestParam(name = "orderDepartName", required = false) String orderDepartName,
			@RequestParam(name = "startFrom", required = false) Date startFrom) {
		String salesPersonName = SecurityUtils.getCurrentUserName();
		logger.info(salesPersonName);

		List<SalesRecordPojo> records = salesRecordsService.searchAgainstSingleValues(productName, salesPersonName,
				hospitalName, locationDepartmentName, orderDepartName, startFrom);

		return records;
	}

	@RequestMapping(path = "/test", method = RequestMethod.GET)
	public SalesRecordSearchCriteria test() {

		SalesRecordSearchCriteria criteria = new SalesRecordSearchCriteria();
		criteria.setProductNames(Arrays.asList(new String[] { "PCT-Q" }));
		criteria.setSalesPersonNames(Arrays.asList(new String[] { "wangf" }));
		criteria.setHospitalNames(Arrays.asList(new String[] { "长征", "长海" }));
		criteria.setLocationDepartmentNames(Arrays.asList(new String[] { "ICU" }));
		criteria.setOrderDepartNames(Arrays.asList(new String[] { "ICU" }));
		criteria.setStartAt(new Date());
		criteria.setEndAt(new Date());
		criteria.setEndAt(new Date());
		return criteria;
	}

	/**
	 * POST http://localhost:8090/management/salesRecordsAdvanceSearch<br>
	 * Content-Type = application/json<br>
	 * Body:
	 * 
	 * <pre>
	{
		productNames: [
		"PCT-Q"
		],
		hospitalNames: [
		"长征",
		"长海"
		],
		locationDepartmentNames: [
		"ICU"
		],
		orderDepartNames: [
		"ICU"
		],
		startAt: "2016-08-16",
		endAt: "2016-09-17"
	}
	 * </pre>
	 * 
	 * @param criteria
	 * @return
	 */
	@RequestMapping(path = "/salesRecordsAdvanceSearch", method = RequestMethod.POST)
	public List<SalesRecordPojo> advanceSearch(@RequestBody SalesRecordSearchCriteria criteria) {
		if (!SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_ADMIN)) {
			// For non-admin user, only view sales records created by himself
			String salesPersonName = SecurityUtils.getCurrentUserName();
			List<String> salesPerson = new ArrayList<>();
			salesPerson.add(salesPersonName);
			criteria.setSalesPersonNames(salesPerson);
		}
		List<SalesRecordPojo> records = salesRecordsService.searchAgainstMultipleValues(criteria);
		return records;
	}

	/**
	 * POST http://localhost:8090/management/saveSalesRecord<br>
	 * Content-Type = application/json<br>
	 * Body:
	 * 
	 * <pre>
	{
	   "hospital":"长征",
	   "installDepartment":"ICU",
	   "orderDepartment":"ICU",
	   "product":"PCT-Q",
	   "quantity":"15"
	}
	 * </pre>
	 * 
	 * @param pojo
	 */
	@RequestMapping(path = "/saveSalesRecord", method = RequestMethod.POST)
	public SalesRecordPojo saveSalesRecord(@RequestBody SalesRecordPojo pojo) {
		SalesRecordPojo savedPojo = salesRecordsService.insertOrUpdate(pojo);
		return savedPojo;
	}

	@RequestMapping(path = "/saveSalesRecords", method = RequestMethod.POST)
	public List<Long> saveSalesRecords(@RequestBody List<SalesRecordPojo> pojos) {
		List<Long> savedPojoIds = salesRecordsService.insertOrUpdate(pojos);
		return savedPojoIds;
	}

	@RequestMapping(path = "/deleteSalesRecords", method = RequestMethod.POST)
	public List<Long> deleteSalesRecords(@RequestBody List<Long> salesRecordIds) {
		salesRecordsService.deleteSalesRecords(salesRecordIds);
		return salesRecordIds;
	}

}
