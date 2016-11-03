package com.wangf.sales.management.test.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.SalesRecordViewRepository;
import com.wangf.sales.management.entity.SalesRecordView;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class SalesRecordViewRepositoryTests extends TestBase {
	@Autowired
	private SalesRecordViewRepository repository;

	@Test
	public void findAll() throws Exception {
		Iterable<SalesRecordView> all = repository.findAll();
		System.out.println(all);
	}

	@Test
	public void findNewCustomer() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date thisMonthFirstDay = formatter.parse("2016-10-01");
		Date nextMonthFirstDay = formatter.parse("2016-11-01");
		Date previousMonthFirstday = formatter.parse("2016-09-01");
		Date dayAfterPreviousMonthLastday = formatter.parse("2016-10-01");
		List<String> provinces = new ArrayList<>();
		provinces.add("北京");
		List<Object[]> newC = repository.findNewCustomer(thisMonthFirstDay, nextMonthFirstDay, previousMonthFirstday,
				dayAfterPreviousMonthLastday, provinces);
		System.out.println(newC);
	}

	@Test
	public void findLostCustomer() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date thisMonthFirstDay = formatter.parse("2016-10-01");
		Date nextMonthFirstDay = formatter.parse("2016-11-01");
		Date previousMonthFirstday = formatter.parse("2016-09-01");
		Date dayAfterPreviousMonthLastday = formatter.parse("2016-10-01");
		List<String> provinces = new ArrayList<>();
		provinces.add("北京");
		List<Object[]> lost = repository.findLostCustomer(thisMonthFirstDay, nextMonthFirstDay, previousMonthFirstday,
				dayAfterPreviousMonthLastday, provinces);
		System.out.println(lost);
	}
}
