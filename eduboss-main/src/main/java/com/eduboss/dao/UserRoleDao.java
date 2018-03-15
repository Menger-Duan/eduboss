package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserRole;
import com.eduboss.dto.DataPackage;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface UserRoleDao extends GenericDAO<UserRole, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	public List<User> findUserByRoleId(String roleId);
	
	public List<Role> findRoleByUserId(String userId);

	/**
	 * 新用户同步，不需要该方法了。
	 * @param userId
	 * @param selectRoleIds
	 */
	@Deprecated
	public void marginUserRoleList(String userId, String selectRoleIds);
	
	public List<Role> loadRoleByUserId(String userId);

	
}
