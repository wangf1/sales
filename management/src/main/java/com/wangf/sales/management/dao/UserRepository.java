package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

	List<User> findByUserName(String name);

}
