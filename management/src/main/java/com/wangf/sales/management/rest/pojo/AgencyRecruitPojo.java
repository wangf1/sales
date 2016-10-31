package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.AgencyRecruit;

public class AgencyRecruitPojo extends AgencyEventPojo {

	public static AgencyRecruitPojo from(AgencyRecruit entity) {
		AgencyRecruitPojo pojo = new AgencyRecruitPojo();
		pojo.setAgency(entity.getAgency().getName());
		pojo.setDate(entity.getDate());
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getAgency().getLevel());
		pojo.setProduct(entity.getProduct().getName());
		pojo.setProvince(entity.getAgency().getProvince().getName());
		pojo.setRegion(entity.getAgency().getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));

		return pojo;
	}
}
