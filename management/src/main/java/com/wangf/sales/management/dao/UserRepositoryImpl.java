package com.wangf.sales.management.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class UserRepositoryImpl implements UserCustomQuery {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void updateUserName(String oldUserName, String newUserName) {
		enableForeignKeyCheck(false);

		updateSalesPersonForTable("AgencyRecruit", oldUserName, newUserName);
		updateSalesPersonForTable("AgencyTraining", oldUserName, newUserName);
		updateSalesPersonForTable("Bid", oldUserName, newUserName);
		updateSalesPersonForTable("DepartmentMeeting", oldUserName, newUserName);
		updateSalesPersonForTable("RegionMeeting", oldUserName, newUserName);
		updateSalesPersonForTable("SalesRecord", oldUserName, newUserName);
		updateSalesPersonForTable("Speaker", oldUserName, newUserName);
		updateUserProvinceForChangedUser(oldUserName, newUserName);
		updateManagerForChangedUser(oldUserName, newUserName);
		updatePreferenceForChangedUser(oldUserName, newUserName);
		updateAuthorityForChangedUser(oldUserName, newUserName);

		enableForeignKeyCheck(true);

		updateUserNameForUser(oldUserName, newUserName);
	}

	private void enableForeignKeyCheck(boolean enabled) {
		String enableValue = enabled ? "1" : "0";
		String disableForeignKeyCheck = "SET FOREIGN_KEY_CHECKS=" + enableValue;
		Query queryDiableFKCheck = em.createNativeQuery(disableForeignKeyCheck);
		queryDiableFKCheck.executeUpdate();
	}

	private void updateSalesPersonForTable(String entityName, String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update " + entityName
				+ " e set e.salesPerson.userName = :newUserName where e.salesPerson.userName = :oldUserName";
		Query queryUpdateSalesPerson = em.createQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();

		String updateLastModifyBy = "update " + entityName
				+ " e set e.lastModifyBy.userName = :newUserName where e.lastModifyBy.userName = :oldUserName";
		Query queryUpdateLastModifyBy = em.createQuery(updateLastModifyBy);
		queryUpdateLastModifyBy.setParameter("oldUserName", oldUserName);
		queryUpdateLastModifyBy.setParameter("newUserName", newUserName);
		queryUpdateLastModifyBy.executeUpdate();
	}

	private void updateUserProvinceForChangedUser(String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update USER_PROVINCE set username = :newUserName where username = :oldUserName";
		Query queryUpdateSalesPerson = em.createNativeQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();
	}

	private void updateManagerForChangedUser(String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update User e set e.manager.userName = :newUserName where e.manager.userName = :oldUserName";
		Query queryUpdateSalesPerson = em.createQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();
	}

	private void updatePreferenceForChangedUser(String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update UserPreference e set e.user.userName = :newUserName where e.user.userName = :oldUserName";
		Query queryUpdateSalesPerson = em.createQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();
	}

	private void updateAuthorityForChangedUser(String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update Authority e set e.user.userName = :newUserName where e.user.userName = :oldUserName";
		Query queryUpdateSalesPerson = em.createQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();
	}

	private void updateUserNameForUser(String oldUserName, String newUserName) {
		String updateSalesPersonForTable = "update User e set e.userName = :newUserName where e.userName = :oldUserName";
		Query queryUpdateSalesPerson = em.createQuery(updateSalesPersonForTable);
		queryUpdateSalesPerson.setParameter("oldUserName", oldUserName);
		queryUpdateSalesPerson.setParameter("newUserName", newUserName);
		queryUpdateSalesPerson.executeUpdate();
	}

}
