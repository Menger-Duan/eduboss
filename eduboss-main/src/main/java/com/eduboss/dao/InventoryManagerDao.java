package com.eduboss.dao;

import com.eduboss.domain.InventoryManager;
import com.eduboss.domainVo.InventoryManagerVo;
import com.eduboss.dto.DataPackage;

public interface InventoryManagerDao extends GenericDAO<InventoryManager,String>{
	
	/**
	 * 记录列表
	 * */
	public DataPackage getInventoryManagerForGrid(InventoryManagerVo inventoryManagerVo,DataPackage dp);

}
