package com.wangf.sales.management.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.dao.AuthorityRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Authority;
import com.wangf.sales.management.entity.User;

@Service
@Transactional
public class AuthorityServcie {

	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private UserRepository userRepository;

	public Authority findOrCreateByUserNameRoleName(String userName, String roleName) {
		Authority auth = authorityRepository.findByUserNameAndAuthority(userName, roleName);
		if (auth != null) {
			return auth;
		}
		auth = new Authority();
		User user = userRepository.findOne(userName);
		auth.setUser(user);
		auth.setAuthority(roleName);
		authorityRepository.save(auth);
		return auth;
	}

	public void deleteByUserName(String userName) {
		List<Authority> authorities = authorityRepository.findByUserName(userName);
		for (Authority authority : authorities) {
			authority.setUser(null);
			authorityRepository.delete(authority);
		}
	}

	public void delete(String userName, String roleName) {
		Authority auth = authorityRepository.findByUserNameAndAuthority(userName, roleName);
		if (auth == null) {
			return;
		}
		auth.setUser(null);
		authorityRepository.delete(auth);
	}

	public void delete(Authority auth) {
		if (auth == null) {
			return;
		}
		auth.setUser(null);
		authorityRepository.delete(auth);
	}

	public List<Authority> findByUserName(String userName) {
		List<Authority> authorities = authorityRepository.findByUserName(userName);
		return authorities;
	}
}
