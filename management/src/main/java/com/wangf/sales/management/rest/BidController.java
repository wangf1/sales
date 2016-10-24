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
import com.wangf.sales.management.rest.pojo.BidPojo;
import com.wangf.sales.management.service.BidService;

@RestController
public class BidController {

	private static final Map<String, byte[]> BIDS_EXCEL_FILE_CACHE = new HashMap<>();

	@Autowired
	private BidService bidService;

	@Autowired
	private MiscDataExporter miscDataExporter;

	@RequestMapping(path = "/getBidsByCurrentUser", method = RequestMethod.POST)
	public List<BidPojo> getBidsByCurrentUser(@RequestBody SalesRecordSearchCriteria criteria) {
		List<BidPojo> pojos = bidService.getBidsByCurrentUser(criteria.getStartAt(), criteria.getEndAt());
		return pojos;
	}

	@RequestMapping(path = "/saveBids", method = RequestMethod.POST)
	public List<BidPojo> saveBids(@RequestBody List<BidPojo> pojos) {
		List<BidPojo> savedPojos = bidService.insertOrUpdateBids(pojos);
		return savedPojos;
	}

	@RequestMapping(path = "/deleteBids", method = RequestMethod.POST)
	public List<Long> deleteBids(@RequestBody List<Long> ids) {
		List<Long> deletedPojos = bidService.deleteBids(ids);
		return deletedPojos;
	}

	@RequestMapping(value = "/exportBids", method = RequestMethod.POST)
	public String getAgencyTrainingsFileDownloadUrl(@RequestBody SalesRecordSearchCriteria searchCriteria,
			HttpServletResponse response) throws FileNotFoundException, IOException {
		byte[] bytes = miscDataExporter.exportBids(searchCriteria.getStartAt(), searchCriteria.getEndAt());
		String key = searchCriteria.toString();
		String downloadUrl = "exportBids/" + key;
		BIDS_EXCEL_FILE_CACHE.put(key, bytes);
		return downloadUrl;
	}

	@RequestMapping(value = "/exportBids/{url}", method = RequestMethod.GET)
	public void getAgencyTrainingsFile(@PathVariable("url") String url, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		byte[] bytes = BIDS_EXCEL_FILE_CACHE.remove(url);
		InputStream in = new ByteArrayInputStream(bytes);
		IOUtils.copy(in, response.getOutputStream());
		response.setContentType(MediaType.OOXML_SHEET.toString());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Bids.xlsx\"");
		response.flushBuffer();
	}

}
