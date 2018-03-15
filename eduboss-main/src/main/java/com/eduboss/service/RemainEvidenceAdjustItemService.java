package com.eduboss.service;

import com.eduboss.domain.RemainEvidenceAdjustItem;
import com.eduboss.domainVo.RemainEvidenceAdjustItemVo;

/**
 * 
 * @author lixuejun
 *
 */
public interface RemainEvidenceAdjustItemService {
	
	/**
	 * 根据summaryId删除概要关联的所有调整项
	 * @param summaryId
	 */
	void deleteItemsBySummaryId(String summaryId);
	
	/**
	 * 删除调整项
	 * @param item
	 */
	void deleteItem(RemainEvidenceAdjustItem item);
	
	/**
	 * 保存调整项
	 * @param item
	 */
	void saveRemainEvidenceAdjustItem(RemainEvidenceAdjustItem item); 
	
	/**
	 * 根据id查找RemainEvidenceAdjustItemVo
	 * @param id
	 * @return
	 */
	RemainEvidenceAdjustItemVo findItemVoById(String id);
	
	/**
	 * 根据id查找RemainEvidenceAdjustItem
	 * @param id
	 * @return
	 */
	RemainEvidenceAdjustItem findItemById(String id);

}
