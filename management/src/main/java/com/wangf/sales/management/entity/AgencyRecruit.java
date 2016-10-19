package com.wangf.sales.management.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "agency_id", "product_id" }) })
public class AgencyRecruit extends AgencyEvent {

}
