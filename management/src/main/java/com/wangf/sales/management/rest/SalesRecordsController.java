package com.wangf.sales.management.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.utils.SecurityUtils;

@RestController
public class SalesRecordsController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(path = "/getSalesRecordsByCurrentUser", method = RequestMethod.GET)
	public List<SalesRecord> getSalesRecords() {
		UserDetails principal = SecurityUtils.getCurrentUserDetails();
		logger.info(principal.getUsername());
		logger.info(principal.getAuthorities().toString());
		List<User> users = userRepository.findByUserName(principal.getUsername());
		if (users.isEmpty()) {
			return null;
		}
		User currentUser = users.get(0);
		List<SalesRecord> result = currentUser.getSalesRecords();
		return result;
	}

	// public void

}
