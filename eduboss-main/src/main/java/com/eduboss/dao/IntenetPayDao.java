package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.IntenetPay;

@Repository
public interface IntenetPayDao extends GenericDAO<IntenetPay, String> {
	
	
	/**
	 * 按照订单流水号查询支付信息
	 * @param trxid
	 * @return
	 */
	public IntenetPay findIntenetPayByTrxid(String trxid);
	
	
	/**
	 * 根据收款记录ID查询订单信息
	 * @param fundId
	 * @return
	 */
	public IntenetPay findIntenetPayByFundId(String fundId);

	public IntenetPay findIntenetPayByreqsn(String reqsn);
	
}
