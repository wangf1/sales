package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.AgencyTraining;

public class AgencyTrainingPojo extends AgencyEventPojo {

	public static AgencyTrainingPojo from(AgencyTraining entity) {
		AgencyTrainingPojo pojo = new AgencyTrainingPojo();
		pojo.setAgency(entity.getAgency().getName());
		pojo.setDate(entity.getDate());
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getAgency().getLevel());
		pojo.setProduct(entity.getProduct().getName());
		pojo.setProvince(entity.getAgency().getProvince().getName());
		pojo.setRegion(entity.getAgency().getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());

		return pojo;
	}

}
