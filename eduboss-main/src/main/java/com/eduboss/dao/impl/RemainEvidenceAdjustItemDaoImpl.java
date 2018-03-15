package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.RemainEvidenceAdjustItemDao;
import com.eduboss.domain.RemainEvidenceAdjustItem;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("RemainEvidenceAdjustItemDao")
public class RemainEvidenceAdjustItemDaoImpl extends GenericDaoImpl<RemainEvidenceAdjustItem, String> implements RemainEvidenceAdjustItemDao {

	/**
	 * 删除调整项
	 * @param summaryId
	 */
	@Override
	public void deleteItemsBySummaryId(String summaryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " delete RemainEvidenceAdjustItem where summary.id = :summaryId ";
		params.put("summaryId", summaryId);
		super.excuteHql(hql, params);
	}
	
}
