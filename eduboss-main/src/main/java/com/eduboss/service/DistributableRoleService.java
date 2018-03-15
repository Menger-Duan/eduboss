package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.DistributableRole;

public interface DistributableRoleService {
	/**
	 * 查找可分配角色
	 * @author: duanmenrun
	 * @Title: findDistributableRolesByRoleId 
	 * @Description: TODO 
	 * @param roleId
	 * @return
	 */
	List<DistributableRole> findDistributableRolesByRoleId(String roleId);
	/**
	 * 更新可分配角色
	 * @author: duanmenrun
	 * @Title: updateDistributableRoleList 
	 * @Description: TODO 
	 * @param roleId
	 * @param list
	 */
	void updateDistributableRoleList(String roleId, List<String> list);

}
