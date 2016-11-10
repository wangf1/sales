package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.DepartmentRepository;
import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.ProductInstallLocationRepository;
import com.wangf.sales.management.dao.ProductPriceRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.dao.SalesRecordRepository;
import com.wangf.sales.management.entity.Department;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.HospitalLevel;
import com.wangf.sales.management.entity.ProductInstallLocation;
import com.wangf.sales.management.entity.ProductPrice;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.rest.pojo.HospitalPojo;

@Service
@Transactional
public class HospitalService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private HospitalLevelService hospitalLevelService;
	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ProductInstallLocationRepository installLocationRepository;

	@Autowired
	private SalesRecordRepository salesRecordRepository;

	@Autowired
	private ProductPriceRepository priceRepository;

	@PersistenceContext
	private EntityManager em;

	private HospitalPojo insertOrUpdateHospital(HospitalPojo pojo) {
		/*
		 * Firstly find by ID, if not exist, find by property. The purpose of
		 * search two times is: 1). Search by ID to avoid treat update case as
		 * insert. 2). Search by columns to avoid insert duplicate record
		 */
		Hospital entity = hospitalRepository.findOne(pojo.getId());
		if (entity == null) {
			entity = hospitalRepository.findByName(pojo.getName());
		}
		if (entity == null) {
			entity = new Hospital();
		}

		HospitalLevel origionalLevel = entity.getLevel();

		entity.setName(pojo.getName());
		HospitalLevel level = hospitalLevelService.findOrCreate(pojo.getLevel());
		entity.setLevel(level);
		Province province = provinceRepository.findByName(pojo.getProvince());
		if (province == null) {
			logger.error(
					"Province {} does not exist. Please change province name to existing one or create this province",
					pojo.getProvince());
		}
		entity.setProvince(province);

		hospitalRepository.save(entity);
		// Must flush otherwise delete origionalLevel will fail
		em.flush();

		if (origionalLevel != null && origionalLevel.getId() != level.getId()) {
			// Must explicitly remove
			origionalLevel.getHospitals().remove(entity);
			hospitalLevelService.deleteIfNoChildHospital(origionalLevel);
		}

		HospitalPojo savedPojo = HospitalPojo.from(entity);
		return savedPojo;
	}

	public List<Long> insertOrUpdateHospitals(Iterable<HospitalPojo> pojos) {
		for (HospitalPojo pojo : pojos) {
			insertOrUpdateHospital(pojo);
		}
		List<Long> allIds = new ArrayList<>();
		for (HospitalPojo pojo : pojos) {
			allIds.add(pojo.getId());
		}
		return allIds;
	}

	/**
	 * Delete the entity.
	 * 
	 * @param ids
	 */
	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			Hospital hospital = hospitalRepository.findOne(id);
			HospitalLevel level = hospital.getLevel();

			hospitalRepository.deleteById(id);

			hospitalLevelService.deleteIfNoChildHospital(level);
		}
	}

	public void deleteHospitalAndAllRelatedData(List<Long> hospitalIds) {
		for (Long id : hospitalIds) {
			deleteHospitalAndAllRelatedData(id);
		}
	}

	private void deleteHospitalAndAllRelatedData(Long hospitalId) {
		Hospital hospital = hospitalRepository.findOne(hospitalId);
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

		List<ProductPrice> prices = hospital.getPrices();
		if (prices != null) {
			List<Long> priceIds = new ArrayList<>();

			for (ProductPrice price : prices) {
				priceIds.add(price.getId());
			}
			priceRepository.deleteByIds(priceIds);
		}

		departmentRepository.deleteByIds(depIds);
		List<Long> ids = new ArrayList<>();
		ids.add(hospitalId);
		deleteByIds(ids);
	}
}
