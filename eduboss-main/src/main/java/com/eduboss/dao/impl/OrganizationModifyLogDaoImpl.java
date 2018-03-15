package com.eduboss.dao.impl;

import com.eduboss.dao.OrganizationModifyLogDao;
import com.eduboss.domain.OrganizationModifyLog;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("OrganizationModifyLogDao")
public class OrganizationModifyLogDaoImpl extends GenericDaoImpl<OrganizationModifyLog, String> implements OrganizationModifyLogDao {

	@Override
	public List<OrganizationModifyLog> findAllByOrgId(String orgId) {
		StringBuilder hql = new StringBuilder();
		Map param = new HashMap();
		hql.append(" from OrganizationModifyLog where organizationId =:orgId");
		param.put("orgId",orgId);
		return this.findAllByHQL(hql.toString(),param);
	}
}
