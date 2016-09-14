package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class ProductInstallLocationRepositoryTests extends TestBase {

	@Autowired
	private ProductInstallLocationRepository installLocationRepository;

	@Test
	public void findsAll() {
		Iterable<ProductInstallLocation> result = installLocationRepository.findAll();
		// installLocationRepository.findByProductDepartmentHospital("PCT-Q", );
		System.out.println(result);
	}

	@Test
	public void findByProductDepartmentHospital() throws Exception {
		ProductInstallLocation installLocation = installLocationRepository.findByProductDepartmentHospital("PCT-Q",
				"ICU", "长征");
		System.out.println(installLocation);
	}

}
