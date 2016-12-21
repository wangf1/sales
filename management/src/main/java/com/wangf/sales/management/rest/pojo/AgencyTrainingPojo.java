package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.AgencyTraining;

public class AgencyTrainingPojo extends AgencyEventPojo {

	private String trainingContent;

	public String getTrainingContent() {
		return trainingContent;
	}

	public void setTrainingContent(String trainingContent) {
		this.trainingContent = trainingContent;
	}

	public static AgencyTrainingPojo from(AgencyTraining entity) {
		AgencyTrainingPojo pojo = new AgencyTrainingPojo();
		pojo.setAgency(entity.getAgency().getName());
		pojo.setDate(entity.getDate());
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getAgency().getLevel());
		pojo.setProducts(AgencyEventPojo.getProductNames(entity.getProducts()));
		pojo.setProvince(entity.getAgency().getProvince().getName());
		pojo.setRegion(entity.getAgency().getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));
		pojo.setTrainingContent(entity.getTrainingContent());
		if (entity.getLastModifyBy() != null) {
			pojo.setLastModifyBy(PojoUtils.getFullName(entity.getLastModifyBy()));
		}
		pojo.setLastModifyAt(entity.getLastModifyAt());
		return pojo;
	}

}
