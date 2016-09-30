package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.ProductPriceRepository;
import com.wangf.sales.management.dao.ProductRepository;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.Product;
import com.wangf.sales.management.entity.ProductPrice;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.ProductPricePojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Transactional
@Service
public class ProductPriceService {
	@Autowired
	private UserService userService;

	@Autowired
	private ProductPriceRepository priceRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private ProductRepository productRepository;

	public Iterable<ProductPricePojo> listProductPricesByCurrentUser() {
		List<ProductPricePojo> result = new ArrayList<>();
		if (SecurityUtils.isCurrentUserAdmin()) {
			Iterable<ProductPrice> allPrices = priceRepository.findAll();
			for (ProductPrice price : allPrices) {
				ProductPricePojo pojo = ProductPricePojo.from(price);
				result.add(pojo);
			}
		} else {
			User currentUser = userService.getCurrentUser();
			List<Hospital> hospitals = currentUser.getHospitals();
			for (Hospital hospital : hospitals) {
				List<ProductPrice> prices = hospital.getPrices();
				for (ProductPrice price : prices) {
					ProductPricePojo pojo = ProductPricePojo.from(price);
					result.add(pojo);
				}
			}
		}
		return result;
	}

	public ProductPricePojo insertOrUpdate(ProductPricePojo pojo) {

		ProductPrice entity = priceRepository.findOne(pojo.getId());
		if (entity == null) {
			entity = priceRepository.findByProductNameAndHospitalName(pojo.getProduct(), pojo.getHospital());
		}
		if (entity == null) {
			entity = new ProductPrice();
		}
		Hospital hospital = hospitalRepository.findByName(pojo.getHospital());
		entity.setHospital(hospital);
		Product product = productRepository.findByName(pojo.getProduct());
		entity.setProduct(product);
		entity.setPrice(pojo.getPrice());
		priceRepository.save(entity);

		ProductPricePojo savedPojo = ProductPricePojo.from(entity);
		return savedPojo;
	}

	public List<ProductPricePojo> insertOrUpdate(List<ProductPricePojo> pojos) {
		for (ProductPricePojo pojo : pojos) {
			insertOrUpdate(pojo);
		}
		List<ProductPricePojo> allSaved = new ArrayList<>();
		for (ProductPricePojo pojo : pojos) {
			allSaved.add(pojo);
		}
		return allSaved;
	}

	public void deleteByIds(List<Long> ids) {
		for (Long id : ids) {
			priceRepository.delete(id);
		}
	}
}
