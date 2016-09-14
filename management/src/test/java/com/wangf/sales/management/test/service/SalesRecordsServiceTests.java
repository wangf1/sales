package com.wangf.sales.management.test.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.entity.SalesRecord;
import com.wangf.sales.management.service.SalesRecordsService;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class SalesRecordsServiceTests extends TestBase {

	@Autowired
	private SalesRecordsService service;

	@Test
	public void advanceSearch() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		Date lastMonth = calendar.getTime();

		List<SalesRecord> records = service.advanceSearch("PCT-Q", "wangf", "长征", "ICU", "ICU", lastMonth);
		System.out.println(records);
	}

}
