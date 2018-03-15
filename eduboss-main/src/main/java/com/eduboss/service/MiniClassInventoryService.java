package com.eduboss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.domain.MiniClassInventory;

@Service
public interface MiniClassInventoryService {

	List<MiniClassInventory> findMiniClassInventoryByMiniClassIds(String miniClassIds);
	
	void saveMiniClassInventory(MiniClassInventory inventory);
	
	/**
	 * 减库存
	 * @param miniClassId
	 * @return
	 */
	boolean reduceInventory(String miniClassId, int num, boolean isAllowedExcess);
	
	/**
	 * 扣库存，特殊流程，允许库存是负数
	 * @param miniClassId
	 * @param num
	 * @param allowedExcess
	 * @return
	 */
    boolean reduceInventoryAllowedExcess(String miniClassId, int num);
	
	/**
     * 回滚库存
     * @param miniClassId
     * @return
     */
    boolean revertInventory(String miniClassId);
	
	/**
	 * 回滚库存
	 * @param miniClassId
	 * @param num
	 * @return
	 */
	boolean revertInventory(String miniClassId, int num);
	
	MiniClassInventory findMiniClassInventoryByMiniClassId(String miniClassId);
	
	/**
	 * 更新库存总数
	 * @param miniClassId
	 * @param maxQuantity
	 * @param normalQuantity
	 */
	void updateInventory(String miniClassId, int maxQuantity, int normalQuantity);
	
}
