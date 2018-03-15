package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.InventoryProductDao;
import com.eduboss.dao.InventoryRecordDao;
import com.eduboss.domain.InventoryProduct;
import com.eduboss.domain.InventoryRecord;
import com.eduboss.domainVo.InventoryProductVo;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Repository
public class InventoryProductDaoImpl extends GenericDaoImpl<InventoryProduct,String> implements InventoryProductDao{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private InventoryRecordDao inventoryRecordDao;
	


	
	/**
	 * 保存库存产品
	 * */
	@Override
	public void saveInventoryProduct(InventoryProduct product) {
			product.setCreateTime(DateTools.getCurrentDateTime());
			product.setCreateUser(userService.getCurrentLoginUser());
			super.save(product);
		
		
	}


	@Override
	public DataPackage getInventoryProductForGrid(InventoryProductVo inventoryProductVo, DataPackage dp) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		
		hql.append(" from InventoryProduct where 1=1");
		if(StringUtils.isNotBlank(inventoryProductVo.getId())){
			hql.append(" and id = :id ");
			params.put("id", inventoryProductVo.getId());
		}
		if(StringUtils.isNotBlank(inventoryProductVo.getName())){
			hql.append(" and name like :name ");
			params.put("name", "%"+inventoryProductVo.getName()+"%");
		}
		hql.append(" order by createTime desc ");
		
		dp = super.findPageByHQL(hql.toString(), dp,true,params);
		
		List<InventoryProduct> list = (List<InventoryProduct>) dp.getDatas();
		List<InventoryProductVo> voList = new ArrayList<InventoryProductVo>();
		for(InventoryProduct ip : list){
			InventoryProductVo vo = HibernateUtils.voObjectMapping(ip, InventoryProductVo.class);
			DataPackage dataPackage = new DataPackage();
			InventoryRecordVo recordVo = new InventoryRecordVo();
			recordVo.setInventoryProductId(ip.getId());
			recordVo.setOrganizationId(userService.getCurrentLoginUser().getOrganizationId());
			dataPackage = inventoryRecordDao.getInventoryRecordForGrid(recordVo, dp);
			List<InventoryRecordVo> listInventory = (List<InventoryRecordVo>) dataPackage.getDatas();
			if(listInventory != null && listInventory.size()>0){
				InventoryRecordVo ir = listInventory.get(0);
				vo.setNewInventoryTime(ir.getNewInventoryTime());
				vo.setNumber(ir.getNumber());
			}
			voList.add(vo);
		}
		dp.setDatas(voList);
		
		return dp;
	}

}
