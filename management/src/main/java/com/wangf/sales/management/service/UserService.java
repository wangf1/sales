package com.wangf.sales.management.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangf.sales.management.auth.ResourcePermission;
import com.wangf.sales.management.auth.RoleResourcePermissions;
import com.wangf.sales.management.dao.HospitalRepository;
import com.wangf.sales.management.dao.UserRepository;
import com.wangf.sales.management.entity.Authority;
import com.wangf.sales.management.entity.Hospital;
import com.wangf.sales.management.entity.Province;
import com.wangf.sales.management.entity.User;
import com.wangf.sales.management.rest.pojo.HospitalPojo;
import com.wangf.sales.management.rest.pojo.ProvincePojo;
import com.wangf.sales.management.rest.pojo.UserPojo;
import com.wangf.sales.management.utils.SecurityUtils;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private AuthorityServcie authorityServcie;

	@PersistenceContext
	private EntityManager em;

	public Set<String> listRegionsForUser(String userName) {
		User user = userRepository.findOne(userName);
		List<Hospital> hospitals = user.getHospitals();
		Set<String> regions = new HashSet<>();
		for (Hospital hospital : hospitals) {
			Province province = hospital.getProvince();
			regions.add(province.getRegion());
		}
		return regions;
	}

	public List<ProvincePojo> listProvincesForUser(String userName) {
		User user = userRepository.findOne(userName);
		List<Hospital> hospitals = user.getHospitals();
		List<ProvincePojo> pojos = new ArrayList<>();
		for (Hospital hospital : hospitals) {
			Province province = hospital.getProvince();
			ProvincePojo pojo = ProvincePojo.from(province);
			if (pojos.contains(pojo)) {
				continue;
			}
			pojos.add(pojo);
		}
		return pojos;
	}

	public List<HospitalPojo> listHospitalsForUser(String userName) {
		List<Hospital> hospitals;
		if (SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_ADMIN)) {
			Iterable<Hospital> all = hospitalRepository.findAll();
			hospitals = new ArrayList<>();
			for (Hospital hospital : all) {
				hospitals.add(hospital);
			}
		} else {
			User user = userRepository.findOne(userName);
			hospitals = user.getHospitals();
		}
		List<HospitalPojo> pojos = new ArrayList<>();
		for (Hospital hospital : hospitals) {
			HospitalPojo pojo = HospitalPojo.from(hospital);
			pojos.add(pojo);
		}
		return pojos;
	}

	public User getCurrentUser() {
		String currentUserName = SecurityUtils.getCurrentUserName();
		User currentUser = userRepository.findOne(currentUserName);
		return currentUser;
	}

	/**
	 * Only delete the relationship.
	 * 
	 * @param ids
	 */
	public void deleteUserHospitalRelationship(List<Long> hostpitalIds) {
		for (Long id : hostpitalIds) {
			Hospital hospital = hospitalRepository.findOne(id);
			User user = getCurrentUser();
			user.getHospitals().remove(hospital);
			userRepository.save(user);
		}
	}

	/**
	 * For Admin, list all users, for User, only list himself.
	 * 
	 * @return
	 */
	public List<UserPojo> listAllUsersByCurrentUserRole() {
		Iterable<User> users;
		if (SecurityUtils.getCurrentUserRoles().contains(SecurityUtils.ROLE_ADMIN)) {
			users = userRepository.findAll();
		} else {
			List<User> userList = new ArrayList<>();
			User user = getCurrentUser();
			userList.add(user);
			users = userList;
		}
		List<UserPojo> pojos = new ArrayList<>();
		for (User user : users) {
			pojos.add(UserPojo.from(user));
		}
		return pojos;
	}

	public UserPojo insertOrUpdate(UserPojo pojo) {
		User toSave = userRepository.findOne(pojo.getUserName());
		if (toSave == null) {
			toSave = new User();
		}
		toSave.setUserName(pojo.getUserName());
		toSave.setFirstName(pojo.getFirstName());
		toSave.setLastName(pojo.getLastName());
		toSave.setPassword(pojo.getPassword());
		userRepository.save(toSave);
		em.flush();
		// em.detach(toSave);

		updateRoles(pojo);

		UserPojo result = UserPojo.from(toSave);
		return result;
	}

	private void updateRoles(UserPojo pojo) {
		String[] rolesArray = pojo.getRoles().split(",");
		List<String> newRoles = new ArrayList<>();
		for (String role : rolesArray) {
			newRoles.add(role.trim());
		}
		List<Authority> authorities = authorityServcie.findByUserName(pojo.getUserName());
		if (authorities != null) {
			for (Authority auth : authorities) {
				if (newRoles.contains(auth.getAuthority())) {
					continue;
				}
				authorityServcie.delete(auth);
			}
		}
		for (String role : newRoles) {
			authorityServcie.findOrCreateByUserNameRoleName(pojo.getUserName(), role);
		}
	}

	public List<UserPojo> insertOrUpdate(List<UserPojo> pojos) {
		List<UserPojo> allSaved = new ArrayList<>();
		for (UserPojo pojo : pojos) {
			UserPojo saved = insertOrUpdate(pojo);
			allSaved.add(saved);
		}
		return allSaved;
	}

	public void deleteUsers(List<String> userNames) {
		for (String id : userNames) {
			// If there are relationships, such as hospital, salesRecord, etc,
			// delete will fail.
			authorityServcie.deleteByUserName(id);
			em.flush();
			User user = userRepository.findOne(id);
			em.refresh(user);
			user.getAuthorities().clear();
			userRepository.delete(user);
		}
	}

	public ResourcePermission getResourcePermissionForCurrentUser() {
		List<String> currentUserRoles = SecurityUtils.getCurrentUserRoles();
		ResourcePermission permission = RoleResourcePermissions.getResourcePermissionForRoles(currentUserRoles);
		return permission;
	}

}
