package com.wangf.sales.management.dataimport;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.SalesRecordPojo;
import com.wangf.sales.management.service.HospitalService;
import com.wangf.sales.management.service.SalesRecordsService;

@Service
public class SalesRecordsImporter {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private SalesRecordsService salesRecordsService;

	public void importHospitalsAndSalesRecords(InputStream inputXLS)
			throws IOException, SAXException, InvalidFormatException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputXLS.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();

		InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
		InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

		Set<HospitalPojo> hospitals = readHospitalsFromExcel(is1);
		Set<SalesRecordPojo> salesRecords = readSalesRecordsFromExcel(is2);
		hospitalService.insertOrUpdateHospitals(hospitals);
		salesRecordsService.insertOrUpdate(salesRecords);
	}

	private Set<SalesRecordPojo> readSalesRecordsFromExcel(InputStream inputXLS)
			throws IOException, SAXException, InvalidFormatException {
		InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/import_salesOrder_config.xml"));
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		Map<String, Object> beans = new HashMap<>();
		List<SalesRecordPojo> salesRecords = new ArrayList<>();
		beans.put("salesRecords", salesRecords);
		mainReader.read(inputXLS, beans);

		// Remove the duplicate hospitals. Note use Set for Excel parse cannot
		// remove duplicate items, so must remove here.
		Set<SalesRecordPojo> result = new HashSet<>();
		for (SalesRecordPojo pojo : salesRecords) {
			result.add(pojo);
		}
		return result;
	}

	private Set<HospitalPojo> readHospitalsFromExcel(InputStream inputXLS)
			throws IOException, SAXException, InvalidFormatException {
		InputStream inputXML = new BufferedInputStream(
				getClass().getResourceAsStream("/import_hospital_from_salesRecords_config.xml"));
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		Map<String, Object> beans = new HashMap<>();
		List<HospitalPojo> hospitals = new ArrayList<>();
		beans.put("hospitals", hospitals);
		mainReader.read(inputXLS, beans);
		// Remove the duplicate hospitals. Note use Set for Excel parse cannot
		// remove duplicate items, so must remove here.
		Set<HospitalPojo> result = new HashSet<>();
		for (HospitalPojo pojo : hospitals) {
			result.add(pojo);
		}
		return result;
	}
}
