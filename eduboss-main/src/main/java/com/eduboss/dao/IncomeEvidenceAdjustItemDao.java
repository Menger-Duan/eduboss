package com.eduboss.dao;

import com.eduboss.domain.IncomeEvidenceAdjustItem;

/**
 * 
 * @author lixuejun
 *
 */
public interface IncomeEvidenceAdjustItemDao extends GenericDAO<IncomeEvidenceAdjustItem, String> {

	/**
	 * 删除调整项
	 * @param summaryId
	 */
	void deleteItemsBySummaryId(String summaryId);
	
}
