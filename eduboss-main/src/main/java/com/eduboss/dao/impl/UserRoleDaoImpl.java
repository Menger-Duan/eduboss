package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserRoleDao;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.domain.UserRole;
	
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
@Repository("UserRoleDao")
public class UserRoleDaoImpl extends GenericDaoImpl<UserRole, String> implements UserRoleDao {

	@Override
	public List<User> findUserByRoleId(String roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserRole where id.roleId = :roleId ";
		params.put("roleId", roleId);
		List<UserRole> userRoles = super.findAllByHQL(hql, params);
		List<User> userList = new ArrayList<User>();
		for (UserRole urRole : userRoles) {
			userList.add(super.getHibernateTemplate().get(User.class, urRole.getUserId()));
		}
		return userList;
	}

	@Override
	public List<Role> findRoleByUserId(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserRole where userId = :userId ";
		params.put("userId", userId);
		List<UserRole> userRoles = super.findAllByHQL(hql, params);
		List<Role> roleList = new ArrayList<Role>();
		for (UserRole urRole : userRoles) {
			if (urRole != null) {
				Role role = super.getHibernateTemplate().get(Role.class, urRole.getRoleId());
				if (role != null) roleList.add(role);
			}
		}
		return roleList;
	}
	
	@Override
	public void marginUserRoleList(String userId, String selectRoleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserRole where userId = :userId ";
		params.put("userId", userId);
		List<UserRole> userRoles = super.findAllByHQL(hql, params);
		List<String> roleIdArray = stringToList(selectRoleIds);
		Boolean flag=false;
		for (UserRole userOrg : userRoles) {
			if(!flag && roleIdArray.contains("ROL0000000146")){
				flag=true;
			}

			if (roleIdArray.contains(userOrg.getRoleId())) {
				roleIdArray.remove(userOrg.getRoleId());
			} else {
				super.delete(userOrg);//delete if not in the selectRoleIds
			}
		}
		if (roleIdArray.size() > 0) {
			for (String orgId : roleIdArray) {

				super.save(new UserRole(userId, orgId));
			}
			//添加本校区老师，默认加上系统基本权限
			if(roleIdArray.size()==1 && roleIdArray.get(0).equals("ROL00000002") && !flag){
				super.save(new UserRole(userId, "ROL0000000146"));
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

	@Override
	public List<Role> loadRoleByUserId(String userId){	
		if (SecurityContextHolder.getContext()!=null && SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user= ((UserDetailsImpl)principal).getUser();
			return user.getRole();
		}else{
			Map<String, Object> params = new HashMap<String, Object>();
			String hql = " from UserRole where userId = :userId ";
			params.put("userId", userId);
			List<UserRole> userRoles = super.findAllByHQL(hql, params);
			List<Role> roleList = new ArrayList<Role>();
			for (UserRole urRole : userRoles) {
				if (urRole != null) {
					Role role = super.getHibernateTemplate().get(Role.class, urRole.getRoleId());
					if (role != null) roleList.add(role);
				}
			}
			return roleList;
		}
	}
	
}
