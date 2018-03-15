package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserOrganizationDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domain.UserOrganization;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("UserOrganizationDao")
public class UserOrganizationDaoImpl extends GenericDaoImpl<UserOrganization, String> implements UserOrganizationDao {

//	private static final Logger log = LoggerFactory.getLogger(UserOrganizationDaoImpl.class);
	// property constants

	/*@Override
	public List<Organization> findOrganizationByUserId(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserOrganization where user.userId = :userId ";
		params.put("userId", userId);
		List<UserOrganization> userOrganizations = super.findAllByHQL(hql, params);
		List<Organization> organizationList = new ArrayList<Organization>();
		for (UserOrganization userOrganization : userOrganizations) {
			Organization org = super.getHibernateTemplate().get(Organization.class, userOrganization.getOrganization().getId());
			if (org != null) {
				this.getHibernateTemplate().initialize(org);
				organizationList.add(org);
			}
		}
		return organizationList;
	}*/

	@Override
	public List<User> findUserByOrgId(String orgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserOrganization where organizationId = :organizationId ";
		params.put("organizationId", orgId);
		List<UserOrganization> userOrgs = super.findAllByHQL(hql, params);
		List<User> userList = new ArrayList<User>();
		for (UserOrganization userOrganization : userOrgs) {
			userList.add(super.getHibernateTemplate().get(User.class, userOrganization.getOrganization().getId()));
		}
		return userList;
	}

	@Override
	public void marginUserOrgList(String userId, String selectOrgIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserOrganization where userId = :userId ";
		params.put("userId", userId);
		List<UserOrganization> UserOrganizations = super.findAllByHQL(hql, params);
		List<String> orgIdArray = stringToList(selectOrgIds);
		for (UserOrganization userOrganization : UserOrganizations) {
			if (orgIdArray.contains(userOrganization.getOrganization().getId())) {
				orgIdArray.remove(userOrganization.getOrganization().getId());
			} else {
				super.delete(userOrganization);//delete if not in the selectOrgIds
			}
		}
		if (orgIdArray.size() > 0) {
			for (String orgId : orgIdArray) {
				UserOrganization uo = new UserOrganization();
				uo.setOrganization(new Organization(orgId));
				uo.setUser(new User(userId));
				super.save(uo);
			}
		}
	}
	
	private List<String> stringToList(String listString) {
		List<String> returnList = new ArrayList<String> ();
		for (String itemString : listString.split(",")) {
			returnList.add(itemString);
		}
		return returnList;
	}
	
}
