package com.eduboss.dao;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Resource;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface ResourceDao extends GenericDAO<Resource, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	public List<Resource> findResourceByRoleIdArray(String[] roleIds);

	public List<Resource> findAllResourcesByUserId(String userId,String type);
	
	public List<Resource> findResourceByRoleIds(String roleIds);

	public List<Resource> getResourcesByEmployeeNo(String employeeNo, String type);
	
}
