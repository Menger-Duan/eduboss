package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.InventoryRecord;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;

public interface InventoryRecordDao extends GenericDAO<InventoryRecord,String>{
	
	/**
	 * 库存列表
	 * */
	public DataPackage getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo,DataPackage dp);
	public List<InventoryRecord> getInventoryRecordForList(InventoryRecordVo inventoryRecordVo,DataPackage dp);
	
	
	/**
	 * 新增库存
	 * */
	public InventoryRecord saveInventoryRecord(InventoryRecord inventoryRecord);
	
	

}
