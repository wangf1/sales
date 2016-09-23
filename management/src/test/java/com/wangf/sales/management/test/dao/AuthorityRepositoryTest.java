package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.AuthorityRepository;
import com.wangf.sales.management.entity.Authority;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class AuthorityRepositoryTest extends TestBase {
	@Autowired
	private AuthorityRepository repository;

	@Test
	public void findsAllHospital() {
		Authority result = this.repository.findByUserNameAndAuthority("wangf", "Admin");
		System.out.println(result);
	}
}
