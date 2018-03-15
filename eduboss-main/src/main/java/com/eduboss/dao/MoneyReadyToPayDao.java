package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.MoneyReadyToPay;

public interface MoneyReadyToPayDao extends GenericDAO<MoneyReadyToPay, String> {
	
	/**
	 * 根据支付ID跟状态找  pos机支付记录  。
	 * @param fundChangeHistoryId
	 * @param status
	 * @return
	 */
	public List<MoneyReadyToPay> getIsPayOkByFundChangeHistory(String fundChangeHistoryId,String [] status);
}
