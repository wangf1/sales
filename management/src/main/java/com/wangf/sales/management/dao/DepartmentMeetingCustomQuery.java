package com.wangf.sales.management.dao;

import java.util.List;

import com.wangf.sales.management.entity.DepartmentMeeting;

public interface DepartmentMeetingCustomQuery {
	List<DepartmentMeeting> searchAgainstMultipleValues(SalesRecordSearchCriteria criteria);
}
