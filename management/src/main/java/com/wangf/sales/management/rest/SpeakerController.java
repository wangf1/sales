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
import com.wangf.sales.management.rest.pojo.SpeakerPojo;
import com.wangf.sales.management.service.SpeakerService;

@RestController
public class SpeakerController {

	private static final Map<String, byte[]> SPEAKERS_EXCEL_FILE_CACHE = new HashMap<>();

	@Autowired
	private SpeakerService speakerService;

	@Autowired
	private MiscDataExporter miscDataExporter;

	@RequestMapping(path = "/getSpeakersByCurrentUser", method = RequestMethod.POST)
	public List<SpeakerPojo> getSpeakersByCurrentUser(@RequestBody SalesRecordSearchCriteria criteria) {
		List<SpeakerPojo> pojos = speakerService.getSpeakersByCurrentUser(criteria.getStartAt(), criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveSpeakers", method = RequestMethod.POST)
	public List<SpeakerPojo> saveSpeakers(@RequestBody List<SpeakerPojo> pojos) {
		List<SpeakerPojo> savedPojos = speakerService.insertOrUpdateSpeakers(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteSpeakers", method = RequestMethod.POST)
	public List<Long> deleteSpeakers(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = speakerService.deleteSpeakers(ids);
		return deletedPojos;
	}

	@RequestMapping(value = "/exportSpeakers", method = RequestMethod.POST)
	public String getAgencyTrainingsFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportSpeakers(searchCriteria.getStartAt(), searchCriteria.getEndAt());
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportSpeakers/" + key;
		SPEAKERS_EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportSpeakers/{url}", method = RequestMethod.GET)
	public void getAgencyTrainingsFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = SPEAKERS_EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Speakers.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(path = "/getSpeakerTypes", method = RequestMethod.GET)
	public Set<String> getSpeakerTypes() {
		Set<String> types = speakerService.getSpeakerTypes();
		return types;
	}

}
