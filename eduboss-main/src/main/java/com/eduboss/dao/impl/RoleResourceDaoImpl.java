package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.RoleResourceDao;
import com.eduboss.domain.Resource;
import com.eduboss.domain.Role;
import com.eduboss.domain.RoleResource;
import com.eduboss.domain.RoleResourceId;

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
@Repository("RoleResourceDao")
public class RoleResourceDaoImpl extends GenericDaoImpl<RoleResource, String> implements RoleResourceDao {

	private static final Logger log = LoggerFactory.getLogger(RoleResourceDaoImpl.class);
	// property constants

	@Override
	public List<Resource> findResourceByRoleId(String roleId) {
		List<RoleResource> userRoles = super.findByCriteria(Expression.eq("roleId", roleId));
		List<Resource> resList = new ArrayList<Resource>();
		for (RoleResource roleResource : userRoles) {
			resList.add(super.getHibernateTemplate().get(Resource.class, roleResource.getResourceId()));
		}
		return resList;
	}
	
	public List<Resource> findResourceByRoleIdArray(String[] roleIds) {
		List<RoleResource> userRoles = super.findByCriteria(Expression.in("roleId", roleIds));
		List<Resource> resList = new ArrayList<Resource>();
		for (RoleResource roleResource : userRoles) {
			resList.add(super.getHibernateTemplate().get(Resource.class, roleResource.getResourceId()));
		}
		return resList;
	}

	@Override
	public List<Role> findRoleByResId(String userId) {
		List<RoleResource> roleResourcess = super.findByCriteria(Expression.eq("resourceId", userId));
		List<Role> roleList = new ArrayList<Role>();
		for (RoleResource roleResource : roleResourcess) {
			roleList.add(super.getHibernateTemplate().get(Role.class, roleResource.getRoleId()));
		}
		return roleList;
	}

	@Override
	public void marginRoleResourceList(String roleId, String selectResIds) {
		List<RoleResource> roleResources = super.findByCriteria(Expression.eq("roleId", roleId));
		List<String> resIdArray = stringToList(selectResIds);
		for (RoleResource roleRes : roleResources) {
			if (resIdArray.contains(roleRes.getResourceId())) {
				resIdArray.remove(roleRes.getResourceId());
			} else {
				super.delete(roleRes);//delete if not in the selectResIds
			}
		}
		if (resIdArray.size() > 0) {
			for (String resId : resIdArray) {
				RoleResource rr=new RoleResource();
				rr.setResourceId(resId);
				rr.setRoleId(roleId);
				super.save(rr);
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
