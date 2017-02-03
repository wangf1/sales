package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

	@PersistenceContext
	private EntityManager em;

	public List<BidPojo> getBidsByCurrentUser(Date startAt, Date endAt) {
		List<Bid> entities;
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			entities = bidRepository.findBetweenDate(startAt, endAt);
		} else {
			entities = new ArrayList<>();
			List<User> employees = userService.getAllEmployeesIncludeSelfForCurrentUser();
			for (User employee : employees) {
				// If the user is a manager, also show data belongs to his
				// employees
				entities.addAll(bidRepository.findByUserAndBetweenDate(startAt, endAt, employee));
			}
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
		entity.setPrice(pojo.getBiddingPrice());
		User currentUser = userService.getCurrentUser();
		if (isInsert) {
			// Only set salesPerson for new created entity, since manager or
			// admin may update the entity, should not change the entity's owner
			entity.setSalesPerson(currentUser);
		} else {
			entity.setLastModifyBy(currentUser);
			entity.setLastModifyAt(new Date());
		}
		Province province = provinceRepository.findByName(pojo.getProvince());
		entity.setProvince(province);
		entity.setStatus(pojo.getBidStatus());

		// For new created province, Must save and flush and CLEAR province
		// before save the user-province
		// relationship. Otherwise for new created province, the user-province
		// relationship will not be saved, root cause unknown.
		bidRepository.save(entity);
		em.flush();
		em.clear();

		Bid alreadySaved = bidRepository.findOne(entity.getId());
		alreadySaved.getProducts().clear();
		for (String prodName : pojo.getProducts()) {
			Product product = productRepository.findByName(prodName);
			alreadySaved.getProducts().add(product);
		}
		bidRepository.save(alreadySaved);

		BidPojo savedPojo = BidPojo.from(alreadySaved);
		return savedPojo;
	}

	public List<Long> deleteBids(List<Long> ids) {
		for (Long id : ids) {
			bidRepository.deleteById(id);
		}
		return ids;
	}

}
