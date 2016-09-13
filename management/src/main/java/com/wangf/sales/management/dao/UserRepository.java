package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wangf.sales.management.entity.User;

@RepositoryRestResource(collectionResourceRel = "province", path = "province")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	List<User> findByUserName(@Param("userName") String name);

}
