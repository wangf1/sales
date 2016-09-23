package com.wangf.sales.management.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.CompanyRepository;
import com.wangf.sales.management.entity.Company;

@Service
@Transactional
public class CompanyService {
	@Autowired
	private CompanyRepository companyRepository;

	public Company findOrCreateByName(String companyName) {
		Company company = companyRepository.findByName(companyName);
		if (company != null) {
			return company;
		}
		company = new Company();
		company.setName(companyName);
		companyRepository.save(company);
		return company;
	}
}
