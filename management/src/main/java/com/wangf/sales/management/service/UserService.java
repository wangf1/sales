package com.wangf.sales.management.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
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
	@Autowired
	private ProvinceService provinceService;

	@PersistenceContext
	private EntityManager em;

	public List<String> listRegionsForUser(String userName) {
		Set<String> regionsSet = new HashSet<>();
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			Set<String> all = provinceService.listAllRegions();
			regionsSet.addAll(all);
		} else {
			User manager = userRepository.findOne(userName);
			List<User> allUsersNeedList = getAllUnderlineEmployeesIncludeSelf(manager);
			for (User user : allUsersNeedList) {
				// If the user is a manager, also show hospitals belongs to his
				// employees
				List<Province> provinces = user.getProvinces();
				for (Province province : provinces) {
					regionsSet.add(province.getRegion());
				}
			}
		}

		List<String> sortedResult = new ArrayList<>();
		sortedResult.addAll(regionsSet);
		Comparator<String> compareByName = (String a, String b) -> {
			Collator chineseCollator = Collator.getInstance(Locale.CHINESE);
			int result = chineseCollator.compare(a, b);
			return result;
		};
		Collections.sort(sortedResult, compareByName);
		return sortedResult;
	}

	public List<ProvincePojo> listProvincesForUser(String userName) {
		List<ProvincePojo> pojos = new ArrayList<>();
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			List<ProvincePojo> all = provinceService.listAll();
			pojos.addAll(all);
		} else {
			User manager = userRepository.findOne(userName);
			List<User> allUsersNeedList = getAllUnderlineEmployeesIncludeSelf(manager);
			for (User user : allUsersNeedList) {
				List<Province> provinces = user.getProvinces();
				for (Province province : provinces) {
					ProvincePojo pojo = ProvincePojo.from(province);
					if (pojos.contains(pojo)) {
						continue;
					}
					pojos.add(pojo);
				}
			}
			return pojos;
		}

		Comparator<ProvincePojo> compareByName = (ProvincePojo a, ProvincePojo b) -> {
			Collator chineseCollator = Collator.getInstance(Locale.CHINESE);
			int result = chineseCollator.compare(a.getName(), b.getName());
			return result;
		};
		Collections.sort(pojos, compareByName);
		return pojos;
	}

	public List<HospitalPojo> listHospitalsForUser(String userName) {
		List<Hospital> hospitals = new ArrayList<>();
		if (SecurityUtils.isCurrentUserAdminOrReadOnlyUser()) {
			Iterable<Hospital> all = hospitalRepository.findAll();
			for (Hospital hospital : all) {
				hospitals.add(hospital);
			}
		} else {
			User manager = userRepository.findOne(userName);
			List<User> allUsersNeedList = getAllUnderlineEmployeesIncludeSelf(manager);
			for (User user : allUsersNeedList) {
				// If the user is a manager, also show hospitals belongs to his
				// employees
				List<Province> provinces = user.getProvinces();
				for (Province province : provinces) {
					List<Hospital> hospitalEntities = province.getHospitals();
					for (Hospital hospital : hospitalEntities) {
						if (hospitals.contains(hospital)) {
							continue;
						}
						hospitals.add(hospital);
					}
				}
			}
		}
		List<HospitalPojo> pojos = new ArrayList<>();
		for (Hospital hospital : hospitals) {
			HospitalPojo pojo = HospitalPojo.from(hospital);
			pojos.add(pojo);
		}

		Comparator<HospitalPojo> compareByName = (HospitalPojo a, HospitalPojo b) -> {
			Collator chineseCollator = Collator.getInstance(Locale.CHINESE);
			int result = chineseCollator.compare(a.getName(), b.getName());
			return result;
		};
		Collections.sort(pojos, compareByName);
		return pojos;
	}

	public User getCurrentUser() {
		String currentUserName = SecurityUtils.getCurrentUserName();
		User currentUser = userRepository.findOne(currentUserName);
		return currentUser;
	}

	/**
	 * For Admin, list all users, for User, only list himself.
	 * 
	 * @return
	 */
	public List<UserPojo> listAllUsersByCurrentUserRole() {
		Iterable<User> users;
		if (SecurityUtils.isCurrentUserAdmin()) {
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
		String oldUserName = pojo.getId();

		String newUserName = pojo.getUserName();
		if (!oldUserName.equals(newUserName)) {
			userRepository.updateUserName(oldUserName, newUserName);
		}

		User toSave = userRepository.findOne(newUserName);
		toSave.setFirstName(pojo.getFirstName());
		toSave.setLastName(pojo.getLastName());
		toSave.setPassword(pojo.getPassword());
		String managerName = pojo.getManager();
		if (!StringUtils.equals(newUserName, managerName)) {
			User manager = userRepository.findByUserName(managerName);
			toSave.setManager(manager);
		} else {
			toSave.setManager(null);
		}
		userRepository.save(toSave);
		em.flush();
		// When set manager to null, must flush and save again, not sure why.
		userRepository.save(toSave);

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

	public List<User> getAllEmployeesIncludeSelfForCurrentUser() {
		User currentUser = getCurrentUser();
		List<User> allUnderLineAndSelf = getAllUnderlineEmployeesIncludeSelf(currentUser);
		return allUnderLineAndSelf;
	}

	public List<User> getAllUnderlineEmployeesIncludeSelf(User manager) {
		List<User> employees = getAllUnderlineEmployees(manager);
		employees.add(0, manager);
		return employees;
	}

	private List<User> getAllUnderlineEmployees(User manager) {
		List<User> allUser = new ArrayList<>();
		List<User> employees = manager.getEmployees();
		for (User user : employees) {
			allUser.add(user);
			List<User> indirectEmployees = getAllUnderlineEmployees(user);
			allUser.addAll(indirectEmployees);
		}
		return allUser;
	}

}
