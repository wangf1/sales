package com.wangf.sales.management.dataimport;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.service.AgencyService;

@Service
public class AgencyEventImporter {

	@Autowired
	private AgencyService agencyService;

	public void importAgencyRecruit(InputStream inputXLS) throws IOException, SAXException, InvalidFormatException {
		List<AgencyRecruitPojo> agencyRecruitPojos = readagencyRecruitsFromExcel(inputXLS);
		agencyService.insertOrUpdateAgencyRecruits(agencyRecruitPojos);
	}

	private List<AgencyRecruitPojo> readagencyRecruitsFromExcel(InputStream inputXLS)
			throws IOException, SAXException, InvalidFormatException {

		ConvertUtils.register(new CommaStringToListConverter(), List.class);

		InputStream inputXML = new BufferedInputStream(
				getClass().getResourceAsStream("/import_agencyRecruit_config.xml"));
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		List<AgencyRecruitPojo> agencyRecruits = new ArrayList<>();
		Map<String, Object> beans = new HashMap<>();
		beans.put("agencyRecruits", agencyRecruits);
		mainReader.read(inputXLS, beans);
		// No idea why the reader always read from the first row, should remove
		// the first row, since it is title rather than data
		agencyRecruits.remove(0);

		ConvertUtils.deregister(List.class);

		return agencyRecruits;
	}

	private static class CommaStringToListConverter extends AbstractConverter {

		@Override
		protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
			if (List.class.equals(type)) {
				String string = value.toString() == null ? "" : value.toString().trim();
				List<String> items = Arrays.asList(string.split("\\s*,\\s*"));
				return type.cast(items);
			}
			throw conversionException(type, value);
		}

		@Override
		protected Class<?> getDefaultType() {
			return List.class;
		}

	}
}
