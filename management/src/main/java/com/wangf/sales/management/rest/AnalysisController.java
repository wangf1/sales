package com.wangf.sales.management.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.AnalysisService;

@RestController
public class AnalysisController {
	@Autowired
	private AnalysisService analysisService;

	@RequestMapping(path = "/findNewCustomer", method = RequestMethod.POST)
	public List<SalesRecordPojo> findNewCustomer(@RequestBody SalesRecordSearchCriteria criteria) {
		List<SalesRecordPojo> records = analysisService.findNewCustomer(criteria.getEndAt(), criteria.getStartAt());
		return records;
	}

	@RequestMapping(path = "/findLostCustomer", method = RequestMethod.POST)
	public List<SalesRecordPojo> findLostCustomer(@RequestBody SalesRecordSearchCriteria criteria) {
		List<SalesRecordPojo> records = analysisService.findLostCustomer(criteria.getEndAt(), criteria.getStartAt());
		return records;
	}

}
