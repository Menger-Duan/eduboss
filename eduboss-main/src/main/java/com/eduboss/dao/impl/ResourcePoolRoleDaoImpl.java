package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ResourcePoolRoleDao;
import com.eduboss.domain.ResourcePoolRole;
import com.eduboss.domainVo.ResourcePoolRoleVo;
import com.google.common.collect.Maps;

@Repository("ResourcePoolRoleDao")     //标识为bean
public class ResourcePoolRoleDaoImpl extends GenericDaoImpl<ResourcePoolRole, String> implements ResourcePoolRoleDao{

	@Override
	public List<ResourcePoolRole> getResourcePoolRoleList(ResourcePoolRoleVo resourcePoolroleVo) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder();
		Map param = new HashMap();
		hql.append(" from ResourcePoolRole where organizationId =:organizationId");
		if (StringUtils.isNotBlank(resourcePoolroleVo.getOrganizationId())) {
			param.put("organizationId",resourcePoolroleVo.getOrganizationId());
		}
		hql.append(" order by createTime desc ");
		return this.findAllByHQL(hql.toString(),param);
	}

	@Override
	public void deleteResourcePoolRoleById(String organizationId) {
		// TODO Auto-generated method stub
		String hql = " delete from ResourcePoolRole where ORGANIZATION_ID = :id  ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", organizationId);
		super.excuteHql(hql, params);
	}

}
