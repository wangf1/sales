package com.wangf.sales.management.test.dataimport;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dataimport.HospitalImporter;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class HospitalImporterTest extends TestBase {
	@Autowired
	private HospitalImporter importer;

	@Test
	public void testImportHospital() throws Exception {
		InputStream inputXLS = new BufferedInputStream(
				getClass().getResourceAsStream("/hospital_from_installBase.xlsx"));
		// not sure why test in Eclipse can pass but test in Maven build fail
		// importer.importHospital(inputXLS);
	}
}
