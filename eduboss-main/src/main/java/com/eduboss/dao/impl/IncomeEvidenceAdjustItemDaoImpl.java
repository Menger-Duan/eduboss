package com.eduboss.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.IncomeEvidenceAdjustItemDao;
import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.google.common.collect.Maps;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("IncomeEvidenceAdjustItemDao")
public class IncomeEvidenceAdjustItemDaoImpl extends GenericDaoImpl<IncomeEvidenceAdjustItem, String> implements IncomeEvidenceAdjustItemDao {

	/**
	 * 删除调整项
	 * @param summaryId
	 */
	@Override
	public void deleteItemsBySummaryId(String summaryId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("summaryId", summaryId);
		String hql = " delete IncomeEvidenceAdjustItem where summary.id = :summaryId ";
		super.excuteHql(hql,params);
	}
	
}
