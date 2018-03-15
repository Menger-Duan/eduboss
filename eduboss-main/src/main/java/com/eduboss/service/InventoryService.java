package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.InventoryManager;
import com.eduboss.domain.InventoryProduct;
import com.eduboss.domain.InventoryRecord;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.InventoryManagerVo;
import com.eduboss.domainVo.InventoryProductVo;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;

public interface InventoryService {
	
	/*********************************库存管理开始*******************************************/
	
	/**
	 * 库存管理列表
	 * */
	public DataPackage getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo, DataPackage dp);
	
	/**
	 * 保存库存产品
	 * */
	public void saveInventoryProduct(InventoryProduct product);
	
	/**
	 * 分页查询库存产品
	 * */
	public DataPackage getInventoryProductForGrid(InventoryProductVo inventoryProductVo,DataPackage dp);
	
	
	/**
	 * 新增库存记录
	 * */
	public void saveInventoryRecord(InventoryRecord inventoryRecord);
	
	/**
	 * 移库
	 * */
	public void moveInventoryRecord(InventoryManager inventoryManager);
	
	/**
	 * 报损
	 * */
	public void destroyInventoryRecord(InventoryManager inventoryManager);
	
	/**
	 * 销售
	 * */
	public void saleInventoryRecord(InventoryManager inventoryManager);
	
	/**
	 * 领用
	 * */
	public void consumeInventoryRecord(InventoryManager inventoryManager);
	
	/**
	 * 退回
	 * */
	public void backInventoryRecord(InventoryManager inventoryManager);
	
	/**
	 * 库存管理列表
	 * */
	public DataPackage getInventoryManagerForGrid(InventoryManagerVo inventoryManagerVo, DataPackage dp);
	
	public List<AutoCompleteOptionVo> getStudentForPerformance(String input);
	public List<Organization> getOrganizationForInventory(Organization org);
	
	/*********************************库存管理结束*******************************************/

}
