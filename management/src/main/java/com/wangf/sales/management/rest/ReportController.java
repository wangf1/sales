package com.wangf.sales.management.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.rest.pojo.SalesQuantityReportData;
import com.wangf.sales.management.service.SalesReportService;

@RestController
@RequestMapping(path = "/report")
public class ReportController {

	@Autowired
	private SalesReportService salesReportService;

	@RequestMapping(path = "/getSalesQuantityReport", method = RequestMethod.POST)
	public List<SalesQuantityReportData> getSalesQuantityReport(@RequestBody SalesRecordSearchCriteria criteria) {
		List<SalesQuantityReportData> datas = salesReportService.getSalesQuantityReportData(criteria);
		return datas;
	}
}
