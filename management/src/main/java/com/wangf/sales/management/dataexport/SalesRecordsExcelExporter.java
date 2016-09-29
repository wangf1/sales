package com.wangf.sales.management.dataexport;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.SalesRecordsService;

@Service
public class SalesRecordsExcelExporter {
	@Autowired
	private SalesRecordsService salesRecordsService;

	public byte[] export(Date start, Date end) throws FileNotFoundException, IOException {
		List<SalesRecordPojo> records = getSalesRecordsByDate(start, end);
		try (InputStream template = getClass().getResourceAsStream("/sales_records_template.xlsx")) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Context context = new Context();
				context.putVar("records", records);
				JxlsHelper.getInstance().processTemplate(template, out, context);
				byte[] result = out.toByteArray();
				return result;
			}
		}
	}

	private List<SalesRecordPojo> getSalesRecordsByDate(Date start, Date end) {
		SalesRecordSearchCriteria criteria = new SalesRecordSearchCriteria();
		criteria.setStartAt(start);
		criteria.setEndAt(end);
		List<SalesRecordPojo> records = salesRecordsService.searchAgainstMultipleValues(criteria);
		return records;
	}

}
