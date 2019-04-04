
package com.wangf.sales.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
	default T findOne(ID id) {
		return findById(id).orElse(null);
	}
}
