package com.eduboss.dao.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.InventoryManagerDao;
import com.eduboss.domain.InventoryManager;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.InventoryManagerVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.google.common.collect.Maps;

@Repository
public class InventoryManagerDaoImpl extends GenericDaoImpl<InventoryManager,String> implements InventoryManagerDao{
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Override
	public DataPackage getInventoryManagerForGrid(
			InventoryManagerVo inventoryManagerVo, DataPackage dp) {
		Organization org = userService.getCurrentLoginUser().getOrganization().get(0);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		
		hql.append(" from InventoryManager where 1=1");
		if(StringUtils.isNotBlank(inventoryManagerVo.getStartDate())){
			hql.append(" and createTime >= :startDate ");
			params.put("startDate", inventoryManagerVo.getStartDate()+" 00:00:00 " );
		}
		if(StringUtils.isNotBlank(inventoryManagerVo.getEndDate())){
			hql.append(" and createTime <= :endDate");
			params.put("endDate", inventoryManagerVo.getEndDate()+" 23:59:59 " );
		}
		if(inventoryManagerVo.getNumber()>0){
			hql.append(" and number = :number ");
			params.put("number", inventoryManagerVo.getNumber());
		}
		
		if(StringUtils.isNotBlank(inventoryManagerVo.getCreateUserName())){
			hql.append(" and createUser.name like :createUserName %");
			params.put("createUserName", "%"+inventoryManagerVo.getCreateUserName()+"%");
		}
		
		if(StringUtils.isNotBlank(inventoryManagerVo.getResourceInventoryId())){
			hql.append(" and resourceInventory.id = :resourceInventoryId '")
			.append(" or targetInventory.id = :targetInventoryId ");
			params.put("resourceInventoryId", inventoryManagerVo.getResourceInventoryId());
			params.put("targetInventoryId", inventoryManagerVo.getResourceInventoryId());
		}
		if(StringUtils.isNotBlank(inventoryManagerVo.getInventoryProductName())){
			hql.append(" and( resourceInventory.inventoryProduct.name like :resourceInventoryName ")
			.append(" or targetInventory.inventoryProduct.name like :targetInventoryName ");
			params.put("resourceInventoryName", "%"+inventoryManagerVo.getInventoryProductName()+"%");
			params.put("targetInventoryName", "%"+inventoryManagerVo.getInventoryProductName()+"%");
		}
		if(StringUtils.isNotBlank(inventoryManagerVo.getResourceInventoryOrgId())){
			hql.append(" and resourceInventory.organization.id = :resourceOrgId ");
			params.put("resourceOrgId", inventoryManagerVo.getResourceInventoryOrgId());
		}
		if(StringUtils.isNotBlank(inventoryManagerVo.getTargetInventoryOrgId())){
			hql.append(" and targetInventory.organization.id = :targetOrgId ");
			params.put("targetOrgId", inventoryManagerVo.getTargetInventoryOrgId());
		}
//		hql.append(" and (resourceInventory.organization.orgLevel like '").append(org.getOrgLevel())
//		.append("%'  or targetInventory.organization.orgLevel like '").append(org.getOrgLevel()).append("%')");
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("库存操作管理","hql","resourceInventory.organization.id"));
		dp = super.findPageByHQL(hql.toString(), dp,true,params);
		return dp;
	}


}
