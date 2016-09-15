package com.wangf.sales.management.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;

import com.wangf.sales.management.entity.SalesRecord;

public class SalesRecordRepositoryImpl implements SalesRecordCustomQuery {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<SalesRecord> advanceSearch(String productName, String salesPersonName, String hospitalName,
			String locationDepartmentName, String orderDepartName, Date startFrom) {
		String queryString = "select record from SalesRecord record " + " join record.installLocation location "
				+ " join record.orderDepartment orderDep " + " join record.salesPerson person " + " where 1=1 ";
		if (StringUtils.isNotBlank(productName)) {
			queryString = queryString + " and location.product.name = :product ";
		}
		if (StringUtils.isNotBlank(salesPersonName)) {
			queryString = queryString + " and record.salesPerson.userName = :salesPersonName ";
		}
		if (StringUtils.isNotBlank(hospitalName)) {
			queryString = queryString + " and location.department.hospital.name = :hospital ";
		}
		if (StringUtils.isNotBlank(locationDepartmentName)) {
			queryString = queryString + " and location.department.name.name = :locationDepartment ";
		}
		if (StringUtils.isNotBlank(orderDepartName)) {
			queryString = queryString + " and record.orderDepartment.name.name = :orderDepart ";
		}
		if (startFrom != null) {
			queryString = queryString + " and record.date >= :startFrom ";
		}

		TypedQuery<SalesRecord> query = em.createQuery(queryString, SalesRecord.class);

		if (StringUtils.isNotBlank(productName)) {
			query.setParameter("product", productName);
		}
		if (StringUtils.isNotBlank(salesPersonName)) {
			query.setParameter("salesPersonName", salesPersonName);
		}
		if (StringUtils.isNotBlank(hospitalName)) {
			query.setParameter("hospital", hospitalName);
		}
		if (StringUtils.isNotBlank(locationDepartmentName)) {
			query.setParameter("locationDepartment", locationDepartmentName);
		}
		if (StringUtils.isNotBlank(orderDepartName)) {
			query.setParameter("orderDepart", orderDepartName);
		}
		if (startFrom != null) {
			query.setParameter("startFrom", startFrom);
		}

		List<SalesRecord> records = query.getResultList();

		return records;
	}

}
