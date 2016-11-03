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
import com.wangf.sales.management.rest.pojo.AgencyPojo;
import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.rest.pojo.AgencyTrainingPojo;
import com.wangf.sales.management.service.AgencyService;
import com.wangf.sales.management.service.UserService;

@RestController
public class AgencyController {

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private UserService userServcie;

	@Autowired
	private MiscDataExporter miscDataExporter;

	private static Map<String, byte[]> AGENCY_RECRUITS_EXCEL_FILE_CACHE = new HashMap<>();
	private static Map<String, byte[]> AGENCY_TRAININGS_EXCEL_FILE_CACHE = new HashMap<>();

	@RequestMapping(path = "/listAgencyRecruitsByCurrentUser", method = RequestMethod.POST)
	public List<AgencyRecruitPojo> listAgencyRecruitsByCurrentUser(@RequestBody SalesRecordSearchCriteria criteria) {
		List<AgencyRecruitPojo> pojos = agencyService.listAgencyRecruitsByCurrentUser(criteria.getStartAt(),
				criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveAgencyRecruits", method = RequestMethod.POST)
	public List<AgencyRecruitPojo> saveAgencyRecruits(@RequestBody List<AgencyRecruitPojo> pojos) {
		List<AgencyRecruitPojo> savedPojos = agencyService.insertOrUpdateAgencyRecruits(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteAgencyRecruits", method = RequestMethod.POST)
	public List<Long> deleteAgencyRecruits(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = agencyService.deleteAgencyRecruits(ids);
		return deletedPojos;
	}

	@RequestMapping(path = "/getAgenciesByCurrentUser", method = RequestMethod.GET)
	public List<AgencyPojo> listAgenciesByCurrentUser() {
		userServcie.getCurrentUser();
		List<AgencyPojo> pojos = agencyService.getAgenciesByCurrentUser();
		return pojos;
	}

	@RequestMapping(path = "/getAgencyLevels", method = RequestMethod.GET)
	public List<String> getAgencyLevels() {
		userServcie.getCurrentUser();
		List<String> result = agencyService.getAgencyLevels();
		return result;
	}

	@RequestMapping(value = "/exportAgencyRecruits", method = RequestMethod.POST)
	public String getAgencyRecruitsFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportAgencyRecruit(searchCriteria.getStartAt(), searchCriteria.getEndAt());
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportAgencyRecruits/" + key;
		AGENCY_RECRUITS_EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportAgencyRecruits/{url}", method = RequestMethod.GET)
	public void getAgencyRecruitsFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = AGENCY_RECRUITS_EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"AgencyRecruits.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(path = "/listAgencyTrainingsByCurrentUser", method = RequestMethod.POST)
	public List<AgencyTrainingPojo> listAgencyTrainingsByCurrentUser(@RequestBody SalesRecordSearchCriteria criteria) {
		List<AgencyTrainingPojo> pojos = agencyService.listAgencyTrainingsByCurrentUser(criteria.getStartAt(),
				criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveAgencyTrainings", method = RequestMethod.POST)
	public List<AgencyTrainingPojo> saveAgencyTrainings(@RequestBody List<AgencyTrainingPojo> pojos) {
		List<AgencyTrainingPojo> savedPojos = agencyService.insertOrUpdateAgencyTrainings(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteAgencyTrainings", method = RequestMethod.POST)
	public List<Long> deleteAgencyTrainings(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = agencyService.deleteAgencyTrainings(ids);
		return deletedPojos;
	}

	@RequestMapping(value = "/exportAgencyTrainings", method = RequestMethod.POST)
	public String getAgencyTrainingsFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportAgencyTrainings(searchCriteria.getStartAt(), searchCriteria.getEndAt());
		String key = searchCriteria.getMD5Base64String();
		String downloadUrl = "exportAgencyTrainings/" + key;
		AGENCY_TRAININGS_EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportAgencyTrainings/{url}", method = RequestMethod.GET)
	public void getAgencyTrainingsFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = AGENCY_TRAININGS_EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"AgencyTrainings.xlsx\"");
		IOUtils.copy(in, response.getOutputStream());
		response.flushBuffer();
	}
}
