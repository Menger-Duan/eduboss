package com.eduboss.service;

import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.eduboss.domainVo.IncomeEvidenceAdjustItemVo;

/**
 * 
 * @author lixuejun
 *
 */
public interface IncomeEvidenceAdjustItemService {
	
	/**
	 * 根据summaryId删除概要关联的所有调整项
	 * @param summaryId
	 */
	void deleteItemsBySummaryId(String summaryId);
	
	/**
	 * 删除调整项
	 * @param item
	 */
	void deleteItem(IncomeEvidenceAdjustItem item);
	
	/**
	 * 保存调整项
	 * @param item
	 */
	void saveIncomeEvidenceAdjustItem(IncomeEvidenceAdjustItem item); 
	
	/**
	 * 根据id查找IncomeEvidenceAdjustItemVo
	 * @param id
	 * @return
	 */
	IncomeEvidenceAdjustItemVo findItemVoById(String id);
	
	/**
	 * 根据id查找IncomeEvidenceAdjustItem
	 * @param id
	 * @return
	 */
	IncomeEvidenceAdjustItem findItemById(String id);

}
