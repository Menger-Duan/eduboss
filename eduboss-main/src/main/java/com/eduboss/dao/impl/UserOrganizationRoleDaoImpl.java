package com.eduboss.dao.impl;

import com.eduboss.dao.UserOrganizationRoleDao;
import com.eduboss.domain.UserOrganizationRole;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("UserOrganizationRoleDao")
public class UserOrganizationRoleDaoImpl extends GenericDaoImpl<UserOrganizationRole, String> implements UserOrganizationRoleDao {

	@Override
	public List<UserOrganizationRole> findAllOrgRoleByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		Map param = new HashMap();
		hql.append(" from UserOrganizationRole where user.userId =:userId");
		param.put("userId",userId);
		hql.append(" order by createTime desc ");
		return this.findAllByHQL(hql.toString(),param);
	}
}
