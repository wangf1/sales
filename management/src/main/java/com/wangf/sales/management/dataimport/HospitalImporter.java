package com.wangf.sales.management.dataimport;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.service.HospitalService;

@Service
public class HospitalImporter {

	@Autowired
	private HospitalService hospitalService;

	public void importHospital(InputStream inputXLS) throws IOException, SAXException, InvalidFormatException {
		List<HospitalPojo> hospitalPojos = readHospitalsFromExcel(inputXLS);
		hospitalService.insertOrUpdateHospitals(hospitalPojos);
	}

	private List<HospitalPojo> readHospitalsFromExcel(InputStream inputXLS)
			throws IOException, SAXException, InvalidFormatException {
		InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/import_hospital_config.xml"));
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		List<HospitalPojo> hospitals = new ArrayList<>();
		Map<String, Object> beans = new HashMap<>();
		beans.put("hospitals", hospitals);
		mainReader.read(inputXLS, beans);
		// No idea why the reader always read from the first row, should remove
		// the first row, since it is title rather than data
		hospitals.remove(0);

		return hospitals;
	}
}
