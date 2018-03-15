package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.DistributableRoleDao;
import com.eduboss.domain.DistributableRole;
import com.google.common.collect.Maps;

@Repository("DistributableRoleDao")
public class DistributableRoleDaoImpl extends GenericDaoImpl<DistributableRole, String> implements DistributableRoleDao{

	@Override
	public List<DistributableRole> findDistributableRolesByRoleId(String roleId) {
		// TODO Auto-generated method stub
		String hql = " from DistributableRole where roleId = :roleId  ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("roleId", roleId);
		return super.findAllByHQL(hql, params);
	}

}
