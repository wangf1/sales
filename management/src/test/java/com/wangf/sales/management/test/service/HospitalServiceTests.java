package com.wangf.sales.management.test.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.service.HospitalService;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class HospitalServiceTests extends TestBase {

	@Autowired
	private HospitalService service;
	@Autowired
	private HospitalRepository hospitalRepository;

	@Test
	public void testDelete() throws Exception {
		Hospital hospital = hospitalRepository.findOne(1L);
		System.out.println(hospital.getName());
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		service.deleteHospitalAndAllRelatedData(ids);
		Hospital shouldNotexist = hospitalRepository.findOne(1L);
		System.out.println(shouldNotexist);
	}

}
