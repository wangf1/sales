package com.wangf.sales.management.test.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.AgencyRecruitRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.AgencyRecruit;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class AgencyRecruitRepositoryTests extends TestBase {

	@Autowired
	private AgencyRecruitRepository recruitRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void findAll() throws Exception {
		Iterable<AgencyRecruit> all = recruitRepository.findAll();
		System.out.println(all);
	}

	@Test
	public void findBetweenDate() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date previousMonth = formatter.parse("2016-09-15");
		List<AgencyRecruit> results = recruitRepository.findBetweenDate(previousMonth, new Date());
		System.out.println(results);
	}

	@Test
	public void findByUserAndBetweenDate() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date previousMonth = formatter.parse("2016-09-15");
		User user = userRepository.findByUserName("wangf");
		List<AgencyRecruit> results = recruitRepository.findByUserAndBetweenDate(previousMonth, new Date(), user);
		System.out.println(results);
		User noSuchUser = userRepository.findByUserName("noSuchUser");
		List<AgencyRecruit> results2 = recruitRepository.findByUserAndBetweenDate(previousMonth, new Date(),
				noSuchUser);
		Assert.assertTrue(results2.isEmpty());
	}

	@Test
	public void deleteById() throws Exception {
		recruitRepository.deleteById(1L);
		Iterable<AgencyRecruit> all = recruitRepository.findAll();
		System.out.println(all);
	}
}
