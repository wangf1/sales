package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public Set<String> listRegionsForUser(String userName) {
		User user = userRepository.findOne(userName);
		List<Hospital> hospitals = user.getHospitals();
		Set<String> regions = new HashSet<>();
		for (Hospital hospital : hospitals) {
			Province province = hospital.getProvince();
			regions.add(province.getRegion());
		}
		return regions;
	}

	public List<ProvincePojo> listProvincesForUser(String userName) {
		User user = userRepository.findOne(userName);
		List<Hospital> hospitals = user.getHospitals();
		List<ProvincePojo> pojos = new ArrayList<>();
		for (Hospital hospital : hospitals) {
			Province province = hospital.getProvince();
			ProvincePojo pojo = ProvincePojo.from(province);
			if (pojos.contains(pojo)) {
				continue;
			}
			pojos.add(pojo);
		}
		return pojos;
	}

	public List<HospitalPojo> listHospitalsForUser(String userName) {
		User user = userRepository.findOne(userName);
		List<Hospital> hospitals = user.getHospitals();
		List<HospitalPojo> pojos = new ArrayList<>();
		for (Hospital hospital : hospitals) {
			HospitalPojo pojo = HospitalPojo.from(hospital);
			pojos.add(pojo);
		}
		return pojos;
	}
}
