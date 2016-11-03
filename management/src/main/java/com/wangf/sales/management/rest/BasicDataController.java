package com.wangf.sales.management.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.wangf.sales.management.auth.ResourcePermission;
import com.wangf.sales.management.dao.CompanyRepository;
import com.wangf.sales.management.dao.HospitalLevelRepository;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dataexport.SalesRecordsExcelExporter;
import com.wangf.sales.management.entity.Company;
import com.wangf.sales.management.entity.HospitalLevel;
import com.wangf.sales.management.rest.pojo.DepartmentNamePojo;
import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProductPojo;
import com.wangf.sales.management.rest.pojo.ProductPricePojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.rest.pojo.UserPojo;
import com.wangf.sales.management.service.AuthorityServcie;
import com.wangf.sales.management.service.DepartmentService;
import com.wangf.sales.management.service.HospitalService;
import com.wangf.sales.management.service.ProductPriceService;
import com.wangf.sales.management.service.ProductService;
import com.wangf.sales.management.service.ProvinceService;
import com.wangf.sales.management.service.UserService;

@RestController
public class BasicDataController {
	private Logger logger = LoggerFactory.getLogger(getClass());
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
	private SalesRecordsExcelExporter salesRecordsExcelExporter;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private AuthorityServcie authorityServcie;
	@Autowired
	private ProductPriceService priceService;

	private Map<String, byte[]> excelFileCache = new HashMap<>();

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
		List<Long> ids = hospitalService.insertOrUpdateHospitals(pojos);
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

	@RequestMapping(path = "/listAllUsers", method = RequestMethod.GET)
	public Iterable<UserPojo> listAllUsers() {
		Iterable<UserPojo> users = userService.listAllUsersByCurrentUserRole();
		return users;
	}

	@RequestMapping(path = "/saveUsers", method = RequestMethod.POST)
	public List<UserPojo> saveUsers(@RequestBody List<UserPojo> users) {
		userService.insertOrUpdate(users);
		return users;
	}

	@RequestMapping(path = "/deleteUsers", method = RequestMethod.POST)
	public List<String> deleteUsers(@RequestBody List<String> userNames) {
		userService.deleteUsers(userNames);
		return userNames;
	}

	@RequestMapping(path = "/listAllRoles", method = RequestMethod.GET)
	public List<String> listAllRoles() {
		List<String> roles = authorityServcie.listAllRoles();
		return roles;
	}

	@RequestMapping(path = "/getRoleResourcePermissionMapping", method = RequestMethod.GET)
	public ResourcePermission getRoleResourcePermissionMapping() {
		ResourcePermission result = userService.getResourcePermissionForCurrentUser();
		return result;
	}

	@RequestMapping(value = "/exportSalesRecords", method = RequestMethod.POST)
	public String getSalesRecordsFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = salesRecordsExcelExporter.export(searchCriteria);
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportSalesRecords/" + key;
		excelFileCache.put(key, bytes);
		logger.debug("Sales records export URL is: {}", downloadUrl);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportSalesRecords/{url}", method = RequestMethod.GET)
	public void getFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		logger.debug("Sales records export URL is: {}", url);
		byte[] bytes = excelFileCache.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SalesRecords.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(path = "/listProductPricesByCurrentUser", method = RequestMethod.GET)
	public Iterable<ProductPricePojo> listProductPricesByCurrentUser() {
		Iterable<ProductPricePojo> pojos = priceService.listProductPricesByCurrentUser();
		return pojos;
	}

	@RequestMapping(path = "/saveProductPrices", method = RequestMethod.POST)
	public List<ProductPricePojo> saveProductPrices(@RequestBody List<ProductPricePojo> pojos) {
		List<ProductPricePojo> saved = priceService.insertOrUpdate(pojos);
		return saved;
	}

	@RequestMapping(path = "/deleteProductPrices", method = RequestMethod.POST)
	public List<Long> deleteProductPrices(@RequestBody List<Long> ids) {
		priceService.deleteByIds(ids);
		return ids;
	}

	@RequestMapping(path = "/deleteHospitals", method = RequestMethod.POST)
	public List<Long> deleteHospitals(@RequestBody List<Long> ids) {
		hospitalService.deleteByIds(ids);
		return ids;
	}

}
