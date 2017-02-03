package com.wangf.sales.management.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.wangf.sales.management.dao.SalesRecordSearchCriteria;
import com.wangf.sales.management.dataexport.MiscDataExporter;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.AnalysisService;

@RestController
public class AnalysisController {

	private static final Map<String, byte[]> EXCEL_FILE_CACHE = new HashMap<>();

	@Autowired
	private AnalysisService analysisService;

	@Autowired
	private MiscDataExporter miscDataExporter;

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

	@RequestMapping(value = "/exportNewCustomers", method = RequestMethod.POST)
	public String getExportNewCustomersDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportNewCustomers(searchCriteria.getEndAt(), searchCriteria.getStartAt());
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportNewCustomers/" + key;
		EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportNewCustomers/{url}", method = RequestMethod.GET)
	public void downloadNewCustomers(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"NewCustomers.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		in.close();
	}

	@RequestMapping(value = "/exportLostCustomers", method = RequestMethod.POST)
	public String getExportLostCustomersDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportLostCustomers(searchCriteria.getEndAt(), searchCriteria.getStartAt());
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportLostCustomers/" + key;
		EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportLostCustomers/{url}", method = RequestMethod.GET)
	public void downloadLostCustomers(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"LostCustomers.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		in.close();
	}
}
