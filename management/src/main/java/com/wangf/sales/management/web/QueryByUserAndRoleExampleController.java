package com.wangf.sales.management.web;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.entity.Hospital;

@RestController
public class QueryByUserAndRoleExampleController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HospitalRepository repositry;

	@RequestMapping("/getHospital")
	public List<Hospital> getHospital() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails principal = (UserDetails) securityContext.getAuthentication().getPrincipal();
		Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
		logger.info(principal.getUsername());
		logger.info(authorities.toString());
		List<Hospital> hospitals = repositry.findByName(principal.getUsername());
		return hospitals;
	}
}