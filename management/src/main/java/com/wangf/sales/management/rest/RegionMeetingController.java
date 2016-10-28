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
import com.wangf.sales.management.rest.pojo.RegionMeetingPojo;
import com.wangf.sales.management.service.RegionMeetingService;

@RestController
public class RegionMeetingController {

	private static final Map<String, byte[]> EXCEL_FILE_CACHE = new HashMap<>();

	@Autowired
	private RegionMeetingService regionMeetingService;

	@Autowired
	private MiscDataExporter miscDataExporter;

	@RequestMapping(path = "/getRegionMeetingsByCurrentUser", method = RequestMethod.POST)
	public List<RegionMeetingPojo> getRegionMeetingsByCurrentUser(@RequestBody SalesRecordSearchCriteria criteria) {
		List<RegionMeetingPojo> pojos = regionMeetingService.getRegionMeetingsByCurrentUser(criteria.getStartAt(),
				criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveRegionMeetings", method = RequestMethod.POST)
	public List<RegionMeetingPojo> saveRegionMeetings(@RequestBody List<RegionMeetingPojo> pojos) {
		List<RegionMeetingPojo> savedPojos = regionMeetingService.insertOrUpdateRegionMeetings(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteRegionMeetings", method = RequestMethod.POST)
	public List<Long> deleteRegionMeetings(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = regionMeetingService.deleteRegionMeetings(ids);
		return deletedPojos;
	}

	@RequestMapping(value = "/exportRegionMeetings", method = RequestMethod.POST)
	public String getFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportRegionMeetings(searchCriteria.getStartAt(), searchCriteria.getEndAt());
		String key = searchCriteria.toString();
		String downloadUrl = "exportRegionMeetings/" + key;
		EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportRegionMeetings/{url}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RegionMeetings.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		in.close();
	}

	@RequestMapping(value = "/getRegionMeetingStatuses", method = RequestMethod.GET)
	public Set<String> getRegionMeetingStatuses() {
		Set<String> types = regionMeetingService.getRegionMeetingStatuses();
		return types;
	}

}
