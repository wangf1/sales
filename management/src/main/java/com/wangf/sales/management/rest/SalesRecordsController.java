package com.wangf.sales.management.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		UserDetails principal = SecurityUtils.getCurrentUserDetails();
		logger.info(principal.getUsername());
		logger.info(principal.getAuthorities().toString());
		List<User> users = userRepository.findByUserName(principal.getUsername());
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
	public List<SalesRecord> advanceSearch(@RequestParam("product") String productName,
			@RequestParam("locationDepartmentName") String locationDepartmentName,
			@RequestParam("hospital") String hospitalName, @RequestParam("orderDepartName") String orderDepartName,
			@RequestParam("startFrom") Date startFrom) {
		UserDetails principal = SecurityUtils.getCurrentUserDetails();
		String salesPersonName = principal.getUsername();
		logger.info(salesPersonName);

		List<SalesRecord> result = salesRecordsService.advanceSearch(productName, salesPersonName, hospitalName,
				locationDepartmentName, orderDepartName, startFrom);
		return result;
	}

}
