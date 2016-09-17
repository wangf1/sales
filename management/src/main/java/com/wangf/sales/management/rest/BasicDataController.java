package com.wangf.sales.management.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.rest.pojo.DepartmentNamePojo;
import com.wangf.sales.management.rest.pojo.ProductPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.service.DepartmentService;
import com.wangf.sales.management.service.ProductService;
import com.wangf.sales.management.service.ProvinceService;

@RestController
public class BasicDataController {
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProvinceService provinceServcie;

	@RequestMapping(path = "/listAllDepartments", method = RequestMethod.GET)
	public List<DepartmentNamePojo> listAllDepartments() {
		List<DepartmentNamePojo> departNames = departmentService.listAllDepartmentNames();
		return departNames;
	}

	@RequestMapping(path = "/listAllProducts", method = RequestMethod.GET)
	public List<ProductPojo> listAllProducts() {
		List<ProductPojo> departNames = productService.listAllProductNames();
		return departNames;
	}

	@RequestMapping(path = "/saveProvinces", method = RequestMethod.POST)
	public List<Long> saveProvinces(@RequestBody List<ProvincePojo> pojos) {
		List<Long> ids = provinceServcie.insertOrUpdate(pojos);
		return ids;
	}
}
