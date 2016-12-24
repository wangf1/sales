package com.wangf.sales.management.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.entity.UserPreference;

@Repository
public interface UserPreferenceRepository extends PagingAndSortingRepository<UserPreference, Long> {
	UserPreference findByUserAndPropertyName(User user, String propertyName);
}
