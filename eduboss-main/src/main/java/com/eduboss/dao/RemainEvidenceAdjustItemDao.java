package com.eduboss.dao;

import com.eduboss.domain.RemainEvidenceAdjustItem;

/**
 * 
 * @author lixuejun
 *
 */
public interface RemainEvidenceAdjustItemDao extends GenericDAO<RemainEvidenceAdjustItem, String> {

	/**
	 * 删除调整项
	 * @param summaryId
	 */
	void deleteItemsBySummaryId(String summaryId);
	
}
