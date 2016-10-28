package com.wangf.sales.management.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.wangf.sales.management.rest.pojo.DepartmentMeetingPojo;
import com.wangf.sales.management.service.DepartmentMeetingService;

@RestController
public class DepartmentMeetingController {

	private static final Map<String, byte[]> DEPARTMENTMEETINGS_EXCEL_FILE_CACHE = new HashMap<>();

	@Autowired
	private DepartmentMeetingService departmentMeetingService;

	@Autowired
	private MiscDataExporter miscDataExporter;

	@RequestMapping(path = "/getDepartmentMeetingsByCurrentUser", method = RequestMethod.POST)
	public List<DepartmentMeetingPojo> getDepartmentMeetingsByCurrentUser(
			@RequestBody SalesRecordSearchCriteria criteria) {
		List<DepartmentMeetingPojo> pojos = departmentMeetingService
				.getDepartmentMeetingsByCurrentUser(criteria.getStartAt(), criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveDepartmentMeetings", method = RequestMethod.POST)
	public List<DepartmentMeetingPojo> saveDepartmentMeetings(@RequestBody List<DepartmentMeetingPojo> pojos) {
		List<DepartmentMeetingPojo> savedPojos = departmentMeetingService.insertOrUpdateDepartmentMeetings(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteDepartmentMeetings", method = RequestMethod.POST)
	public List<Long> deleteDepartmentMeetings(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = departmentMeetingService.deleteDepartmentMeetings(ids);
		return deletedPojos;
	}

	@RequestMapping(value = "/exportDepartmentMeetings", method = RequestMethod.POST)
	public String getFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportDepartmentMeetings(searchCriteria.getStartAt(),
				searchCriteria.getEndAt());
		String key = searchCriteria.toString();
		String downloadUrl = "exportDepartmentMeetings/" + key;
		DEPARTMENTMEETINGS_EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportDepartmentMeetings/{url}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = DEPARTMENTMEETINGS_EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"DepartmentMeetings.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		in.close();
	}

	@RequestMapping(value = "/getDepartmentMeetingStatuses", method = RequestMethod.GET)
	public Set<String> getDepartmentMeetingStatuses() {
		Set<String> types = departmentMeetingService.getDepartmentMeetingStatuses();
		return types;
	}

}
