package com.wangf.sales.management.rest.pojo;

import com.wangf.sales.management.entity.AgencyRecruit;

public class AgencyRecruitPojo extends AgencyEventPojo {

	public static AgencyRecruitPojo from(AgencyRecruit entity) {
		AgencyRecruitPojo pojo = new AgencyRecruitPojo();
		pojo.setAgency(entity.getAgency().getName());
		pojo.setDate(entity.getDate());
		pojo.setId(entity.getId());
		pojo.setLevel(entity.getAgency().getLevel());
		pojo.setProducts(AgencyEventPojo.getProductNames(entity.getProducts()));
		pojo.setProvince(entity.getAgency().getProvince().getName());
		pojo.setRegion(entity.getAgency().getProvince().getRegion());
		pojo.setSalesPerson(entity.getSalesPerson().getUserName());
		pojo.setSalesPersonFullName(PojoUtils.getFullName(entity.getSalesPerson()));
		if (entity.getLastModifyBy() != null) {
			pojo.setLastModifyBy(PojoUtils.getFullName(entity.getLastModifyBy()));
		}
		pojo.setLastModifyAt(entity.getLastModifyAt());

		return pojo;
	}
}
