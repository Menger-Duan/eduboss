package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domain.UserOrganization;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface UserOrganizationDao extends GenericDAO<UserOrganization, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
//	public List<Organization> findOrganizationByUserId(String userId);

	@Deprecated
	public List<User> findUserByOrgId(String orgId);
	
	public void marginUserOrgList(String userId, String selectOrgIds);
}
