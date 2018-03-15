package com.eduboss.service;

import com.eduboss.domain.RemainEvidenceAdjustItem;
import com.eduboss.domain.RemainEvidenceAdjustSummary;
import com.eduboss.domainVo.RemainEvidenceAdjustItemVo;
import com.eduboss.domainVo.RemainEvidenceAdjustSummaryVo;

/**
 * 
 * @author lixuejun
 *
 */
public interface RemainEvidenceAdjustSummaryService {
	
	/**
	 * 保存调整项
	 * @param item
	 * @return
	 */
	RemainEvidenceAdjustItem editRemainAdjustItem(RemainEvidenceAdjustItemVo itemVo); 
	
	/**
	 * 删除调整项
	 * @param itemId
	 */
	void deleteRemainAdjustItem(String itemId);
	
	/**
	 * 保存调整概要
	 * @param summary
	 */
	void editRemainAdjustSummary(RemainEvidenceAdjustSummary summary);
	
	/**
	 * 查找调整概要
	 * @param summaryId
	 * @return
	 */
	RemainEvidenceAdjustSummaryVo findRemainAdjustSummaryById(String summaryId);

}
