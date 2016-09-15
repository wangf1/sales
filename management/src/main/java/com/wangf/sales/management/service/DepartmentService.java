package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentNameRepository;
import com.wangf.sales.management.entity.DepartmentName;
import com.wangf.sales.management.rest.pojo.DepartmentNamePojo;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentNameRepository departmentNameRepository;

	public List<DepartmentNamePojo> listAllDepartmentNames() {
		Iterable<DepartmentName> departments = departmentNameRepository.findAll();
		List<DepartmentNamePojo> pojos = new ArrayList<>();
		for (DepartmentName department : departments) {
			DepartmentNamePojo pojo = DepartmentNamePojo.from(department);
			pojos.add(pojo);
		}
		return pojos;
	}
}
