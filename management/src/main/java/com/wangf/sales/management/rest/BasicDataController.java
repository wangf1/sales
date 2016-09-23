package com.wangf.sales.management.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.CompanyRepository;
import com.wangf.sales.management.dao.HospitalLevelRepository;
import com.wangf.sales.management.entity.Company;
import com.wangf.sales.management.entity.HospitalLevel;
import com.wangf.sales.management.rest.pojo.DepartmentNamePojo;
import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProductPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.service.DepartmentService;
import com.wangf.sales.management.service.HospitalService;
import com.wangf.sales.management.service.ProductService;
import com.wangf.sales.management.service.ProvinceService;
import com.wangf.sales.management.utils.SecurityUtils;

@RestController
public class BasicDataController {
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProvinceService provinceServcie;
	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private HospitalLevelRepository hospitalLevelRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@RequestMapping(path = "/listAllDepartments", method = RequestMethod.GET)
	public List<DepartmentNamePojo> listAllDepartments() {
		List<DepartmentNamePojo> departNames = departmentService.listAllDepartmentNames();
		return departNames;
	}

	@RequestMapping(path = "/saveDepartmentNames", method = RequestMethod.POST)
	public List<DepartmentNamePojo> saveDepartmentNams(@RequestBody List<DepartmentNamePojo> pojos) {
		List<DepartmentNamePojo> results = departmentService.insertOrUpdateDepartmentName(pojos);
		return results;
	}

	@RequestMapping(path = "/deleteDepartmentNams", method = RequestMethod.POST)
	public List<Long> deleteDepartmentNams(@RequestBody List<Long> ids) {
		departmentService.deleteDepartmentNameByIds(ids);
		return ids;
	}

	@RequestMapping(path = "/listAllProducts", method = RequestMethod.GET)
	public List<ProductPojo> listAllProducts() {
		List<ProductPojo> departNames = productService.listAllProductNames();
		return departNames;
	}

	@RequestMapping(path = "/saveProducts", method = RequestMethod.POST)
	public List<ProductPojo> saveProducts(@RequestBody List<ProductPojo> pojos) {
		List<ProductPojo> allSaved = productService.insertOrUpdate(pojos);
		return allSaved;
	}

	@RequestMapping(path = "/deleteProducts", method = RequestMethod.POST)
	public List<Long> deleteProducts(@RequestBody List<Long> ids) {
		productService.deleteByIds(ids);
		return ids;
	}

	@RequestMapping(path = "/listAllProvinces", method = RequestMethod.GET)
	public List<ProvincePojo> listAllProvinces() {
		List<ProvincePojo> provincePojos = provinceServcie.listAll();
		return provincePojos;
	}

	@RequestMapping(path = "/saveProvinces", method = RequestMethod.POST)
	public List<Long> saveProvinces(@RequestBody List<ProvincePojo> pojos) {
		List<Long> ids = provinceServcie.insertOrUpdate(pojos);
		return ids;
	}

	@RequestMapping(path = "/deleteProvinces", method = RequestMethod.POST)
	public List<Long> deleteProvinces(@RequestBody List<Long> ids) {
		provinceServcie.deleteByIds(ids);
		return ids;
	}

	@RequestMapping(path = "/saveHospitals", method = RequestMethod.POST)
	public List<Long> saveHospitals(@RequestBody List<HospitalPojo> pojos) {
		String currentUser = SecurityUtils.getCurrentUserName();
		List<Long> ids = hospitalService.insertOrUpdateForUser(pojos, currentUser);
		return ids;
	}

	@RequestMapping(path = "/listAllHospitalLevels", method = RequestMethod.GET)
	public Iterable<HospitalLevel> listAllHospitalLevels() {
		Iterable<HospitalLevel> levels = hospitalLevelRepository.findAll();
		return levels;
	}

	@RequestMapping(path = "/listAllConpanies", method = RequestMethod.GET)
	public Iterable<Company> listAllConpanies() {
		Iterable<Company> companies = companyRepository.findAll();
		return companies;
	}

}
