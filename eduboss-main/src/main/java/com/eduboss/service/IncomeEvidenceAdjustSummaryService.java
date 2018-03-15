package com.eduboss.service;

import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.eduboss.domain.IncomeEvidenceAdjustSummary;
import com.eduboss.domainVo.IncomeEvidenceAdjustItemVo;
import com.eduboss.domainVo.IncomeEvidenceAdjustSummaryVo;

/**
 * 
 * @author lixuejun
 *
 */
public interface IncomeEvidenceAdjustSummaryService {
	
	/**
	 * 保存调整项
	 * @param item
	 * @return
	 */
	IncomeEvidenceAdjustItem editIncomeAdjustItem(IncomeEvidenceAdjustItemVo itemVo); 
	
	/**
	 * 删除调整项
	 * @param itemId
	 */
	void deleteIncomeAdjustItem(String itemId);
	
	/**
	 * 保存调整概要
	 * @param summary
	 */
	void editIncomeAdjustSummary(IncomeEvidenceAdjustSummary summary);
	
	/**
	 * 查找调整概要
	 * @param summaryId
	 * @return
	 */
	IncomeEvidenceAdjustSummaryVo findIncomeAdjustSummaryById(String summaryId);

}
