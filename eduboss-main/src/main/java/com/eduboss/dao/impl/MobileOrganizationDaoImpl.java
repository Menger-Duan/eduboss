package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.MobileOrganizationDao;
import com.eduboss.domain.MobileOrganization;
import com.google.common.collect.Maps;

@Repository("MobileOrganizationDao")
public class MobileOrganizationDaoImpl extends GenericDaoImpl<MobileOrganization, String> implements MobileOrganizationDao {

	@Override
	public List<MobileOrganization> getOrganizationBoy(String parentId,Integer level) {
		Map<String, Object> params = Maps.newHashMap();
		String hql="from MobileOrganization  where 1=1 ";
		if (StringUtils.isNotBlank(parentId)) {
			hql += " and  parentId = :parentId ";
			params.put("parentId", parentId);
		}
		if (level!=null) {
			hql += " and level = :level ";
			params.put("level", level);
		}
		hql+=" order by length(orgLevel),orgOrder";
		List<MobileOrganization> list=this.findAllByHQL(hql,params) ;
		return list;
	}
	
	@Override
	public MobileOrganization getOrganizationByParentId(String parentId, Integer level) {
		String hql="from MobileOrganization  where 1=1 ";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(parentId)) {
			hql += " and  id = :parentId ";
			params.put("parentId", parentId);
		}
		if (level!=null) {
			hql += " and level = :level ";
			params.put("level", level);
		}
		hql+=" order by length(orgLevel),orgOrder";
		List<MobileOrganization> list=this.findAllByHQL(hql,params) ;
		return list.get(0);
	}
	
}
