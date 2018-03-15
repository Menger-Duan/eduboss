package com.eduboss.dao;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Resource;
import com.eduboss.domain.Role;
import com.eduboss.domain.RoleResource;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface RoleResourceDao extends GenericDAO<RoleResource, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	public List<Resource> findResourceByRoleId(String roleId);
	
	public List<Resource> findResourceByRoleIdArray(String[] roleIds);
	
	public List<Role> findRoleByResId(String userId);
	
	public void marginRoleResourceList(String roleId, String selectResIds);
}
