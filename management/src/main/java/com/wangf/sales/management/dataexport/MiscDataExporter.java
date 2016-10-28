package com.wangf.sales.management.dataexport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.rest.pojo.AgencyRecruitPojo;
import com.wangf.sales.management.rest.pojo.AgencyTrainingPojo;
import com.wangf.sales.management.rest.pojo.BidPojo;
import com.wangf.sales.management.rest.pojo.DepartmentMeetingPojo;
import com.wangf.sales.management.rest.pojo.RegionMeetingPojo;
import com.wangf.sales.management.rest.pojo.SpeakerPojo;
import com.wangf.sales.management.service.AgencyService;
import com.wangf.sales.management.service.BidService;
import com.wangf.sales.management.service.DepartmentMeetingService;
import com.wangf.sales.management.service.RegionMeetingService;
import com.wangf.sales.management.service.SpeakerService;

@Service
public class MiscDataExporter {
	@Autowired
	private AgencyService agencyService;

	@Autowired
	private BidService bidService;

	@Autowired
	private SpeakerService speakerService;

	@Autowired
	private DepartmentMeetingService departmentMeetingService;

	@Autowired
	private RegionMeetingService regionMeetingService;

	public byte[] exportAgencyRecruit(Date startAt, Date endAt) throws IOException {
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

	public byte[] exportAgencyTrainings(Date startAt, Date endAt) throws IOException {
		List<AgencyTrainingPojo> data = agencyService.listAgencyTrainingsByCurrentUser(startAt, endAt);
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

	public byte[] exportBids(Date startAt, Date endAt) throws IOException {
		List<BidPojo> data = bidService.getBidsByCurrentUser(startAt, endAt);
		try (InputStream template = getClass().getResourceAsStream("/Bids_template.xlsx")) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Context context = new Context();
				context.putVar("data", data);
				JxlsHelper.getInstance().processTemplate(template, out, context);
				byte[] result = out.toByteArray();
				return result;
			}
		}
	}

	public byte[] exportSpeakers(Date startAt, Date endAt) throws IOException {
		List<SpeakerPojo> data = speakerService.getSpeakersByCurrentUser(startAt, endAt);
		try (InputStream template = getClass().getResourceAsStream("/Speakers_template.xlsx")) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Context context = new Context();
				context.putVar("data", data);
				JxlsHelper.getInstance().processTemplate(template, out, context);
				byte[] result = out.toByteArray();
				return result;
			}
		}
	}

	public byte[] exportDepartmentMeetings(Date startAt, Date endAt) throws IOException {
		List<DepartmentMeetingPojo> data = departmentMeetingService.getDepartmentMeetingsByCurrentUser(startAt, endAt);
		try (InputStream template = getClass().getResourceAsStream("/DepartmentMeetings_template.xlsx")) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Context context = new Context();
				context.putVar("data", data);
				JxlsHelper.getInstance().processTemplate(template, out, context);
				byte[] result = out.toByteArray();
				return result;
			}
		}
	}

	public byte[] exportRegionMeetings(Date startAt, Date endAt) throws IOException {
		List<RegionMeetingPojo> data = regionMeetingService.getRegionMeetingsByCurrentUser(startAt, endAt);
		try (InputStream template = getClass().getResourceAsStream("/RegionMeetings_template.xlsx")) {
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
