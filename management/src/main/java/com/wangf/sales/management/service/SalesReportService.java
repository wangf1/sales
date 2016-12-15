package com.wangf.sales.management.service;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.rest.pojo.DepartmentMeetingPojo;
import com.wangf.sales.management.rest.pojo.SalesQuantityReportData;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;

@Service
@Transactional
public class SalesReportService {

	@Autowired
	private SalesRecordsService salesRecordsService;

	@Autowired
	private DepartmentMeetingService departmentMeetingService;

	public List<SalesQuantityReportData> getSalesQuantityReportData(SalesRecordSearchCriteria criteria) {
		Map<String, SalesQuantityReportData> dateReportDataMap = new HashMap<>();

		putSalesQuantityIntoMap(criteria, dateReportDataMap);
		putDepartmentMeetingQuantityIntoMap(criteria, dateReportDataMap);

		List<SalesQuantityReportData> reportDatas = new ArrayList<>();
		reportDatas.addAll(dateReportDataMap.values());
		sortReportDataByDate(reportDatas);
		return reportDatas;
	}

	private void putSalesQuantityIntoMap(SalesRecordSearchCriteria criteria,
			Map<String, SalesQuantityReportData> dateReportDataMap) {
		List<SalesRecordPojo> salesRecords = salesRecordsService.searchAgainstMultipleValues(criteria);
		for (SalesRecordPojo record : salesRecords) {
			String key = getDateStringAsYYYYMM(record.getDate());
			SalesQuantityReportData reportData = dateReportDataMap.get(key);
			if (reportData == null) {
				reportData = new SalesQuantityReportData();
				reportData.setDate(key);
				reportData.setSalesQuantity(record.getQuantity());
				dateReportDataMap.put(key, reportData);
			} else {
				int salesQuantitySum = reportData.getSalesQuantity() + record.getQuantity();
				reportData.setSalesQuantity(salesQuantitySum);
			}
		}
	}

	private void putDepartmentMeetingQuantityIntoMap(SalesRecordSearchCriteria criteria,
			Map<String, SalesQuantityReportData> dateReportDataMap) {
		List<DepartmentMeetingPojo> meetings = departmentMeetingService.searchAgainstMultipleValues(criteria);
		for (DepartmentMeetingPojo meeting : meetings) {
			String key = getDateStringAsYYYYMM(meeting.getDate());
			SalesQuantityReportData reportData = dateReportDataMap.get(key);
			if (reportData == null) {
				reportData = new SalesQuantityReportData();
				reportData.setDate(key);
				reportData.setDepartmentMeetingQuantity(1);
				dateReportDataMap.put(key, reportData);
			} else {
				int meetingQuantitySum = reportData.getDepartmentMeetingQuantity() + 1;
				reportData.setDepartmentMeetingQuantity(meetingQuantitySum);
			}
		}
	}

	private String getDateStringAsYYYYMM(Date date) {
		String pattern = "yyyy-MM";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String key = format.format(date);
		return key;
	}

	private void sortReportDataByDate(List<SalesQuantityReportData> reportDatas) {
		Comparator<SalesQuantityReportData> compareByName = (SalesQuantityReportData a, SalesQuantityReportData b) -> {
			Collator collator = Collator.getInstance();
			int compareResult = collator.compare(a.getDate(), b.getDate());
			return compareResult;
		};
		Collections.sort(reportDatas, compareByName);
	}

}
