package com.wangf.sales.management.test.dao;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangf.sales.management.dao.BidRepository;
import com.wangf.sales.management.entity.Bid;
import com.wangf.sales.management.test.TestBase;

@Transactional
public class BidRepositoryTests extends TestBase {
	@Autowired
	private BidRepository repository;

	@Test
	public void findAll() throws Exception {
		Iterable<Bid> all = repository.findAll();
		System.out.println(all);
	}
}
