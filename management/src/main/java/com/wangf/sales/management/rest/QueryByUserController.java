package com.wangf.sales.management.rest;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.auth.ResourcePermission;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.rest.pojo.UserPojo;
import com.wangf.sales.management.service.UserService;
import com.wangf.sales.management.utils.SecurityUtils;

@RestController
public class QueryByUserController {

	// private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/getHospitalsByCurrentUser", method = RequestMethod.GET)
	public List<HospitalPojo> getHospitalsByCurrentUser() {
		List<HospitalPojo> hospitals = userService.listHospitalsForUser(SecurityUtils.getCurrentUserName());
		return hospitals;
	}

	@RequestMapping(path = "/getRegionsByCurrentUser", method = RequestMethod.GET)
	public Set<String> getRegionsByCurrentUser() {
		Set<String> result = userService.listRegionsForUser(SecurityUtils.getCurrentUserName());
		return result;
	}

	@RequestMapping(path = "/getProvincesByCurrentUser", method = RequestMethod.GET)
	public List<ProvincePojo> getProvincesByCurrentUser() {
		List<ProvincePojo> result = userService.listProvincesForUser(SecurityUtils.getCurrentUserName());
		return result;
	}

	@RequestMapping(path = "/getCurrentUser", method = RequestMethod.GET)
	public UserPojo getCurrentUserName() {
		User user = userService.getCurrentUser();
		UserPojo pojo = UserPojo.from(user);
		return pojo;
	}

	@RequestMapping(path = "/getResourcePermissionForCurrentUser", method = RequestMethod.GET)
	public ResourcePermission getResourcePermissionForCurrentUser() {
		ResourcePermission permission = userService.getResourcePermissionForCurrentUser();
		return permission;
	}

}