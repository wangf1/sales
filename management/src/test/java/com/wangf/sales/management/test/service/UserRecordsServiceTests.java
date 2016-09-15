package com.wangf.sales.management.test.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.service.UserService;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class UserRecordsServiceTests extends TestBase {

	@Autowired
	private UserService service;

	@Test
	public void listRegionsAndProvinces() throws Exception {
		String salesPersonName = "wangf";
		Set<String> regions = service.listRegionsForUser(salesPersonName);
		System.out.println(regions);
		List<ProvincePojo> provinces = service.listProvincesForUser(salesPersonName);
		System.out.println(provinces);
	}

	@Test
	public void listHospitalByUser() throws Exception {
		List<HospitalPojo> hospitals = service.listHospitalsForUser("wangf");
		System.out.println(hospitals);
	}

}
