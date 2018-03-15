package com.eduboss.dao;

import com.eduboss.domain.YeePayInfo;

import java.util.List;

public interface YeePayInfoDao extends GenericDAO<YeePayInfo, String> {
	
	/**
	 * 根据支付ID跟状态找  pos机支付记录  。
	 * @param fundChangeHistoryId
	 * @param status
	 * @return
	 */
	public List<YeePayInfo> getIsPayOkByFundChangeHistory(String fundChangeHistoryId, String[] status);
}
