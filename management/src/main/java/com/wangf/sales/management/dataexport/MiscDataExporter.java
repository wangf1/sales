package com.wangf.sales.management.dataexport;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.service.AgencyService;

@Service
public class MiscDataExporter {
	@Autowired
	private AgencyService agencyService;

	public byte[] exportAgencyRecruit(Date startAt, Date endAt) throws FileNotFoundException, IOException {
		List<AgencyRecruitPojo> data = agencyService.listAgencyRecruitsByCurrentUser(startAt, endAt);
		try (InputStream template = getClass().getResourceAsStream("/Agency_Recruits_template.xlsx")) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Context context = new Context();
				context.putVar("data", data);
				JxlsHelper.getInstance().processTemplate(template, out, context);
				byte[] result = out.toByteArray();
				return result;
			}
		}
	}
}
