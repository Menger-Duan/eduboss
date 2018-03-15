package com.eduboss.dao;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.Role;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface RoleDao extends GenericDAO<Role, String> {

	public List<Role> findRoleByCode(RoleCode roleCode);
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	public List<Role> findRoleByName(String roleName);

	List<Role> getUserRoleList(String userId);
	
}
