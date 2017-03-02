package com.wangf.sales.management.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

import com.wangf.sales.management.entity.DepartmentMeeting;

public class DepartmentMeetingRepositoryImpl implements DepartmentMeetingCustomQuery {

	public static final String STATUS_PLAN = "预申请";

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<DepartmentMeeting> searchFinishedMeetingAgainstMultipleValues(SalesRecordSearchCriteria criteria) {
		String queryString = "select record from DepartmentMeeting record " + " join record.department department ";
		if (CollectionUtils.isNotEmpty(criteria.getSalesPersonNames())) {
			queryString = queryString + " join record.salesPerson person ";
		}
		queryString = queryString + " where 1=1 ";
		if (CollectionUtils.isNotEmpty(criteria.getSalesPersonNames())) {
			queryString = queryString + " and record.salesPerson.userName in :salesPersonNames ";
		}
		if (CollectionUtils.isNotEmpty(criteria.getHospitalNames())) {
			queryString = queryString + " and department.hospital.name in :hospitals ";
		}
		if (CollectionUtils.isNotEmpty(criteria.getOrderDepartNames())) {
			queryString = queryString + " and department.name.name in :orderDeparts ";
		}
		if (criteria.getStartAt() != null) {
			String compareOperater = criteria.isIncludeStartAt() ? ">=" : ">";
			queryString = queryString + " and record.date " + compareOperater + " :startAt ";
		}
		if (criteria.getEndAt() != null) {
			String compareOperater = criteria.isIncludeEndAt() ? "<=" : "<";
			queryString = queryString + " and record.date " + compareOperater + " :endAt ";
		}
		// We only want query finished meetings, so the planed meeting should
		// not include
		queryString = queryString + " and record.status !='" + STATUS_PLAN + "' ";

		TypedQuery<DepartmentMeeting> query = em.createQuery(queryString, DepartmentMeeting.class);

		if (CollectionUtils.isNotEmpty(criteria.getSalesPersonNames())) {
			query.setParameter("salesPersonNames", criteria.getSalesPersonNames());
		}
		if (CollectionUtils.isNotEmpty(criteria.getHospitalNames())) {
			query.setParameter("hospitals", criteria.getHospitalNames());
		}
		if (CollectionUtils.isNotEmpty(criteria.getOrderDepartNames())) {
			query.setParameter("orderDeparts", criteria.getOrderDepartNames());
		}
		if (criteria.getStartAt() != null) {
			query.setParameter("startAt", criteria.getStartAt());
		}
		if (criteria.getEndAt() != null) {
			query.setParameter("endAt", criteria.getEndAt());
		}

		List<DepartmentMeeting> records = query.getResultList();

		return records;
	}

}
