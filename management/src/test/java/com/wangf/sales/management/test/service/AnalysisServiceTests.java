package com.wangf.sales.management.test.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.AnalysisService;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class AnalysisServiceTests extends TestBase {
	@Autowired
	private AnalysisService service;

	@Test
	public void findNewCustomer() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date thisMonth = formatter.parse("2016-10-15");
		Date previousMonth = formatter.parse("2016-09-15");
		List<SalesRecordPojo> results = service.findNewCustomer(thisMonth, previousMonth);
		System.out.println(results);
	}

	@Test
	public void findLostCustomer() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date thisMonth = formatter.parse("2016-10-15");
		Date previousMonth = formatter.parse("2016-09-15");
		List<SalesRecordPojo> results = service.findLostCustomer(thisMonth, previousMonth);
		System.out.println(results);
	}
}
