package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordViewRepository;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.utils.DateUtils;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class AnalysisService {
	@Autowired
	private SalesRecordViewRepository salesRecordViewRepository;
	@Autowired
	private UserService userService;

	public List<SalesRecordPojo> findNewCustomer(Date thisMonth, Date previousMonth) {

		Date thisMonthFirstDay = DateUtils.getFirstDayOfMonth(thisMonth);
		Date nextMonthFirstDay = DateUtils.getFirstDayOfNextMonth(thisMonth);
		Date previousMonthFirstday = DateUtils.getFirstDayOfMonth(previousMonth);
		Date dayAfterPreviousMonthLastday = DateUtils.getFirstDayOfNextMonth(previousMonth);

		DateUtils.getFirstDayOfMonth(thisMonth);

		List<SalesRecordPojo> newCustomers = new ArrayList<>();
		List<String> provinces = getProvinceNamesForCurrentUser();
		List<Object[]> queryResults = salesRecordViewRepository.findNewCustomer(thisMonthFirstDay, nextMonthFirstDay,
				previousMonthFirstday, dayAfterPreviousMonthLastday, provinces);
		for (Object[] result : queryResults) {
			SalesRecordPojo pojo = new SalesRecordPojo();
			pojo.setHospital((String) result[0]);
			pojo.setProduct((String) result[1]);
			newCustomers.add(pojo);
		}
		return newCustomers;
	}

	public List<SalesRecordPojo> findLostCustomer(Date thisMonth, Date previousMonth) {

		Date thisMonthFirstDay = DateUtils.getFirstDayOfMonth(thisMonth);
		Date nextMonthFirstDay = DateUtils.getFirstDayOfNextMonth(thisMonth);
		Date previousMonthFirstday = DateUtils.getFirstDayOfMonth(previousMonth);
		Date dayAfterPreviousMonthLastday = DateUtils.getFirstDayOfNextMonth(previousMonth);

		DateUtils.getFirstDayOfMonth(thisMonth);

		List<SalesRecordPojo> newCustomers = new ArrayList<>();
		List<String> provinces = getProvinceNamesForCurrentUser();
		List<Object[]> queryResults = salesRecordViewRepository.findLostCustomer(thisMonthFirstDay, nextMonthFirstDay,
				previousMonthFirstday, dayAfterPreviousMonthLastday, provinces);
		for (Object[] result : queryResults) {
			SalesRecordPojo pojo = new SalesRecordPojo();
			pojo.setHospital((String) result[0]);
			pojo.setProduct((String) result[1]);
			newCustomers.add(pojo);
		}
		return newCustomers;
	}

	private List<String> getProvinceNamesForCurrentUser() {
		List<String> provinces = new ArrayList<>();
		String userName = SecurityUtils.getCurrentUserName();
		List<ProvincePojo> pojos = userService.listProvincesForUser(userName);
		for (ProvincePojo pojo : pojos) {
			provinces.add(pojo.getName());
		}
		if (provinces.isEmpty()) {
			// Add a emtpy province to avoid SQL syntax exception
			provinces.add("");
		}
		return provinces;
	}
}
