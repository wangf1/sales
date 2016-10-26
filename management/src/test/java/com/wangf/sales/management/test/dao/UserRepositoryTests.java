package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class UserRepositoryTests extends TestBase {

	@Autowired
	private UserRepository repository;

	@Test
	public void findsAllHospital() {
		User user = this.repository.findByUserName("Stella");
		System.out.println(user.getUserName());
		System.out.println(user.getLastName());
		System.out.println(user.getPassword());
		System.out.println(user.getManager());
		System.out.println(user.getEmployees());
		System.out.println(user.getAuthorities());
		System.out.println(user.getSalesRecords());
	}

}
