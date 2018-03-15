package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.DistributableRole;

public interface DistributableRoleDao  extends GenericDAO<DistributableRole,String>{
	/**
	 * 查找可分配角色
	 * @author: duanmenrun
	 * @Title: findDistributableRolesByRoleId 
	 * @Description: TODO 
	 * @param roleId
	 * @return
	 */
	List<DistributableRole> findDistributableRolesByRoleId(String roleId);

}
