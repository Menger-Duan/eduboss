package com.eduboss.dao.impl;

import com.eduboss.dao.UserModifyLogDao;
import com.eduboss.dao.UserOrganizationRoleDao;
import com.eduboss.domain.UserModifyLog;
import com.eduboss.domain.UserOrganizationRole;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("UserModifyLogDao")
public class UserModifyLogDaoImpl extends GenericDaoImpl<UserModifyLog, String> implements UserModifyLogDao {

	@Override
	public List<UserModifyLog> findAllByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		Map param = new HashMap();
		hql.append(" from UserModifyLog where userId =:userId");
		param.put("userId",userId);
		hql.append(" order by createTime desc ");
		return this.findAllByHQL(hql.toString(),param);
	}
}
