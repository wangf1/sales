package com.wangf.sales.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wangf.sales.management.entity.Authority;

@Repository
public interface AuthorityRepository extends BaseRepository<Authority, Long> {

	String query_findByUserNameAndAuthority = "select auth from Authority auth " + " join auth.user u "
			+ " where u.userName = :userName " + " and auth.authority = :role ";

	@Query(query_findByUserNameAndAuthority)
	Authority findByUserNameAndAuthority(@Param("userName") String userName, @Param("role") String role);

	String query_findByUserName = "select auth from Authority auth " + " join auth.user u "
			+ " where u.userName = :userName ";

	@Query(query_findByUserName)
	List<Authority> findByUserName(@Param("userName") String userName);

	String query_listAllRoles = "select distinct auth.authority from Authority auth";

	@Query(query_listAllRoles)
	List<String> listAllRoles();

}
