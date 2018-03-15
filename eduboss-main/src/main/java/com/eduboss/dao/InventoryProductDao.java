package com.eduboss.dao;

import com.eduboss.domain.InventoryProduct;
import com.eduboss.domainVo.InventoryProductVo;
import com.eduboss.dto.DataPackage;

public interface InventoryProductDao extends GenericDAO<InventoryProduct,String> {
	
	/**
	 * 保存库存产品
	 * */
	public void saveInventoryProduct(InventoryProduct product);
	
	/**
	 * 分页查询库存产品 
	 * */
	public DataPackage getInventoryProductForGrid(InventoryProductVo inventoryProductVo,DataPackage dp);

}
