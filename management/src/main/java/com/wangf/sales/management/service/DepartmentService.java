package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentNameRepository;
import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.DepartmentName;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.rest.pojo.DepartmentNamePojo;

@Service
@Transactional
public class DepartmentService {
	@Autowired
	private DepartmentNameRepository departmentNameRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	public List<DepartmentNamePojo> listAllDepartmentNames() {
		Iterable<DepartmentName> departments = departmentNameRepository.findAll();
		List<DepartmentNamePojo> pojos = new ArrayList<>();
		for (DepartmentName department : departments) {
			DepartmentNamePojo pojo = DepartmentNamePojo.from(department);
			pojos.add(pojo);
		}
		return pojos;
	}

	public Department findOrCreateByDepartNameAndHospitalName(String departmentName, String hospitalName) {
		Department department = departmentRepository.findByDepartmentNameHospitalName(departmentName, hospitalName);
		if (department != null) {
			return department;
		}
		department = new Department();
		Hospital hospital = hospitalRepository.findByName(hospitalName);
		department.setHospitals(hospital);
		DepartmentName name = departmentNameRepository.findByName(departmentName);
		department.setName(name);
		departmentRepository.save(department);
		return department;
	}
}
