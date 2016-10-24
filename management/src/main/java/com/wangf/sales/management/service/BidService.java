package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.BidRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.dao.ProvinceRepository;
import com.wangf.sales.management.entity.Bid;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.BidPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class BidService {

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	public List<BidPojo> getBidsByCurrentUser(Date startAt, Date endAt) {
		List<Bid> entities;
		if (SecurityUtils.isCurrentUserAdmin()) {
			entities = bidRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			User manager = userService.getCurrentUser();
			List<User> employees = manager.getEmployees();
			for (User employee : employees) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(bidRepository.findByUserAndBetweenDate(startAt, endAt, employee));
			}
			entities.addAll(bidRepository.findByUserAndBetweenDate(startAt, endAt, manager));
		}
		List<BidPojo> result = new ArrayList<>();
		if (entities == null) {
			return result;
		}
		for (Bid entity : entities) {
			BidPojo pojo = BidPojo.from(entity);
			result.add(pojo);
		}
		return result;
	}

	public List<BidPojo> insertOrUpdateBids(List<BidPojo> pojos) {
		List<BidPojo> savedPojos = new ArrayList<>();
		for (BidPojo pojo : pojos) {
			BidPojo savedPojo = insertOrUpdateBid(pojo);
			savedPojos.add(savedPojo);
		}
		return savedPojos;
	}

	private BidPojo insertOrUpdateBid(BidPojo pojo) {
		Bid entity = bidRepository.findOne(pojo.getId());
		boolean isInsert = false;
		if (entity == null) {
			entity = new Bid();
			isInsert = true;
		}
		entity.setDescription(pojo.getDescription());
		entity.setPrice(pojo.getPrice());
		Product product = productRepository.findByName(pojo.getProduct());
		entity.setProduct(product);
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			User salesPerson = userService.getCurrentUser();
			entity.setSalesPerson(salesPerson);
		}
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);

		bidRepository.save(entity);
		BidPojo savedPojo = BidPojo.from(entity);
		return savedPojo;
	}

	public List<Long> deleteBids(List<Long> ids) {
		for (Long id : ids) {
			bidRepository.deleteById(id);
		}
		return ids;
	}

}
