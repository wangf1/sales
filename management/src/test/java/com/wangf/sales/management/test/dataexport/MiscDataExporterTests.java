package com.wangf.sales.management.test.dataexport;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dataexport.MiscDataExporter;
import com.wangf.sales.management.test.TestBase;
import com.wangf.sales.management.utils.DateUtils;

@Transactional
public class MiscDataExporterTests extends TestBase {
	@Autowired
	private MiscDataExporter exporter;

	// Not yet figure out how to inject mock Spring component bean when unit
	// test, the following two test case will fail.

	@Test
	public void exportAgencyRecruit() throws Exception {
		Date startAt = DateUtils.getFirstDayOfCurrentMonth();
		Date endAt = DateUtils.getFirstDayOfNextMonth();
		// byte[] bytes = exporter.exportAgencyRecruit(startAt, endAt);
		// OutputStream out = new FileOutputStream("target/AgencyRecruit.xlsx");
		// out.write(bytes);
		// out.close();
	}

	@Test
	public void exportBids() throws Exception {
		Date startAt = DateUtils.getFirstDayOfCurrentMonth();
		Date endAt = DateUtils.getFirstDayOfNextMonth();
		// byte[] bytes = exporter.exportBids(startAt, endAt);
		// OutputStream out = new FileOutputStream("target/Bids.xlsx");
		// out.write(bytes);
		// out.close();
	}
}
