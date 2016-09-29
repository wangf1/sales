package com.wangf.sales.management.test.dataexport;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dataexport.SalesRecordsExcelExporter;
import com.wangf.sales.management.test.TestBase;
import com.wangf.sales.management.utils.DateUtils;

@Transactional
public class SalesRecordsExcelExporterTest extends TestBase {
	@Autowired
	private SalesRecordsExcelExporter exporter;

	@Test
	public void testExport() throws Exception {
		byte[] bytes = exporter.export(DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getFirstDayOfNextMonth());
		OutputStream out = new FileOutputStream("target/sales_records.xlsx");
		out.write(bytes);
		out.close();
	}
}
