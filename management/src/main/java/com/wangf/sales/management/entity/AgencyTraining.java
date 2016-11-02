package com.wangf.sales.management.entity;

import javax.persistence.Entity;

@Entity
public class AgencyTraining extends AgencyEvent {
	private String trainingContent;

	public String getTrainingContent() {
		return trainingContent;
	}

	public void setTrainingContent(String trainingContent) {
		this.trainingContent = trainingContent;
	}

}
