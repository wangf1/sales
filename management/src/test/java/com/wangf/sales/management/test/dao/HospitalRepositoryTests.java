package com.wangf.sales.management.test.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.ProductPriceRepository;
import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.ProductPrice;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class HospitalRepositoryTests extends TestBase {
	@Autowired
	private HospitalRepository repository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ProductInstallLocationRepository installLocationRepository;

	@Autowired
	private SalesRecordRepository salesRecordRepository;

	@Autowired
	private ProductPriceRepository priceRepository;

	@Test
	public void findsHospitalByName() {
		Hospital result = this.repository.findByName("长征");
		System.out.println(result);
	}

	@Test
	public void deleteByIds() throws Exception {
		Hospital hospital = repository.findOne(1L);
		System.out.println(hospital);
		List<Department> departments = hospital.getDepartments();
		List<Long> depIds = new ArrayList<>();
		if (departments != null) {
			for (Department dep : departments) {
				List<Long> locationIds = new ArrayList<>();
				List<ProductInstallLocation> installLocations = dep.getInstallLocations();
				for (ProductInstallLocation location : installLocations) {
					List<SalesRecord> salesRecords = location.getSalesRecords();
					List<Long> recordIds = new ArrayList<>();
					for (SalesRecord record : salesRecords) {
						recordIds.add(record.getId());
					}
					salesRecordRepository.deleteByIds(recordIds);
					locationIds.add(location.getId());
				}
				installLocationRepository.deleteByIds(locationIds);
				depIds.add(dep.getId());
			}
		}
		List<Long> priceIds = new ArrayList<>();
		for (ProductPrice price : hospital.getPrices()) {
			priceIds.add(price.getId());
		}
		priceRepository.deleteByIds(priceIds);

		departmentRepository.deleteByIds(depIds);
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		repository.deleteByIds(ids);

		Hospital shouldDeleted = repository.findOne(1L);
		System.out.println(shouldDeleted);
	}

}
