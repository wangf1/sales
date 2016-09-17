package com.wangf.sales.management.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.ProductInstallLocation;

@Service
@Transactional
public class ProductInstallLocationService {
	@Autowired
	private ProductInstallLocationRepository locationRepository;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ProductRepository productRepository;

	public ProductInstallLocation findOrCreateByProductDepartmentHospital(String productName, String installDepartment,
			String hospital) {
		ProductInstallLocation location = locationRepository.findByProductDepartmentHospital(productName,
				installDepartment, hospital);
		if (location != null) {
			return location;
		}

		Department department = departmentService.findOrCreateByDepartNameAndHospitalName(installDepartment, hospital);
		List<Product> products = productRepository.findByName(productName);
		location = new ProductInstallLocation();
		location.setDepartment(department);
		location.setProduct(products.get(0));
		locationRepository.save(location);
		return location;
	}
}
