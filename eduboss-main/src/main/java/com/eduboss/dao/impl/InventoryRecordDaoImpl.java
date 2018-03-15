package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.InventoryRecordDao;
import com.eduboss.domain.InventoryRecord;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Repository
public class InventoryRecordDaoImpl extends GenericDaoImpl<InventoryRecord,String> implements InventoryRecordDao{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	
	/**
	 * 库存管理列表
	 * */
	@Override
	public DataPackage getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo, DataPackage dp) {
		Organization org = userService.getCurrentLoginUser().getOrganization().get(0);
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		
		hql.append(" from InventoryRecord where 1=1");
		if(StringUtils.isNotBlank(inventoryRecordVo.getStartDate())){
			hql.append(" and newInventoryTime >= :startDate ");
			params.put("startDate", inventoryRecordVo.getStartDate());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getEndDate())){
			hql.append(" and newInventoryTime <= :endDate ");
			params.put("endDate", inventoryRecordVo.getEndDate()+" 23:59:59 ");
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getInventoryProductName())){
			hql.append(" and inventoryProduct.name like :inventoryProductName ");
			params.put("inventoryProductName", "%"+inventoryRecordVo.getInventoryProductName()+"%");
		}
		if(inventoryRecordVo.getNumber()>0){
			hql.append(" and number = :number ");
			params.put("number", inventoryRecordVo.getNumber());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getInventoryProductId())){
			hql.append(" and inventoryProduct.id = :inventoryProductId ");
			params.put("inventoryProductId", inventoryRecordVo.getInventoryProductId());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getOrganizationId())){
			hql.append(" and organization.id = :orgId ");
			params.put("orgId", inventoryRecordVo.getOrganizationId());
		}
		//hql.append(" and organization.orgLevel like '").append(org.getOrgLevel()).append("%'");

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","organization.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("库存管理","nsql","hql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		dp = super.findPageByHQL(hql.toString(), dp,true,params);
		List<InventoryRecord> list = (List<InventoryRecord>) dp.getDatas();
		List<InventoryRecordVo> voList = new ArrayList<InventoryRecordVo>();
		for(InventoryRecord inventory : list){
			InventoryRecordVo vo = HibernateUtils.voObjectMapping(inventory, InventoryRecordVo.class);
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	
	/**
	 * 新增库存
	 * */
	@Override
	public InventoryRecord saveInventoryRecord(InventoryRecord inventoryRecord) {
		DataPackage dp = new DataPackage();
		InventoryRecord oldRecord = null;
		if(StringUtils.isNotBlank(inventoryRecord.getId())){
			oldRecord = super.findById(inventoryRecord.getId());
		}else{
			InventoryRecordVo inventoryRecordVo = new InventoryRecordVo();
			inventoryRecordVo.setOrganizationId(inventoryRecord.getOrganization().getId());
			inventoryRecordVo.setInventoryProductId(inventoryRecord.getInventoryProduct().getId());
			List<InventoryRecord> list = this.getInventoryRecordForList(inventoryRecordVo, dp);
			//List<InventoryRecord> list = (List<InventoryRecord>) dp.getDatas();
			if(list!=null && list.size()>0){
				oldRecord = list.get(0);
			}
		}
		
		if(oldRecord != null){
			double oldNumber = oldRecord.getNumber();
			double oldPrice =  oldRecord.getPrice();
			oldRecord.setNewInventoryTime(DateTools.getCurrentDateTime());
			oldRecord.setNumber(oldNumber+inventoryRecord.getNumber());
			//重新计算单价
			double newPrice = Math.round((oldNumber*oldPrice+inventoryRecord.getNumber()*inventoryRecord.getPrice())/(oldNumber+inventoryRecord.getNumber())*100);
			oldRecord.setPrice(newPrice/100);
			oldRecord.setModifyTime(DateTools.getCurrentDateTime());
			oldRecord.setModifyUser(userService.getCurrentLoginUser());
			super.save(oldRecord);
			return oldRecord;
		}else{
			inventoryRecord.setNewInventoryTime(DateTools.getCurrentDateTime());
			inventoryRecord.setCreateTime(DateTools.getCurrentDateTime());
			inventoryRecord.setCreateUser(userService.getCurrentLoginUser());
			super.save(inventoryRecord);
			return inventoryRecord;
		}
		 
		//super.save(inventoryRecord);
	}


	@Override
	public List<InventoryRecord> getInventoryRecordForList(InventoryRecordVo inventoryRecordVo,
			DataPackage dp) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from InventoryRecord where 1=1");
		if(StringUtils.isNotBlank(inventoryRecordVo.getStartDate())){
			hql.append(" and newInventoryTime >= :startDate ");
			params.put("startDate", inventoryRecordVo.getStartDate());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getEndDate())){
			hql.append(" and newInventoryTime <= :endDate ");
			params.put("endDate", inventoryRecordVo.getEndDate()+" 23:59:59 ");
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getInventoryProductName())){
			hql.append(" and inventoryProduct.name like :inventoryProductName ");
			params.put("inventoryProductName", "%"+inventoryRecordVo.getInventoryProductName()+"%");
		}
		if(inventoryRecordVo.getNumber()>0){
			hql.append(" and number = :number ");
			params.put("number", inventoryRecordVo.getNumber());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getInventoryProductId())){
			hql.append(" and inventoryProduct.id = :inventoryProductId ");
			params.put("inventoryProductId", inventoryRecordVo.getInventoryProductId());
		}
		if(StringUtils.isNotBlank(inventoryRecordVo.getOrganizationId())){
			hql.append(" and organization.id = :orgId ");
			params.put("orgId", inventoryRecordVo.getOrganizationId());
		}
		dp = super.findPageByHQL(hql.toString(), dp,true,params);
		List<InventoryRecord> list = (List<InventoryRecord>) dp.getDatas();
		return list;
	}
	
	

}
