package com.eduboss.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.IntenetPayDao;
import com.eduboss.domain.IntenetPay;
import com.google.common.collect.Maps;

@Repository("IntenetPayDao")
public class IntenetPayDaoImpl   extends GenericDaoImpl<IntenetPay, String> implements IntenetPayDao {

	@Override
	public IntenetPay findIntenetPayByTrxid(String trxid) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("trxid", trxid);
		String hql="from IntenetPay where trxid= :trxid ";
		return this.findOneByHQL(hql,params);
	}

	@Override
	public IntenetPay findIntenetPayByFundId(String fundId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundId", fundId);
		String hql="from IntenetPay where fundsChange.id= :fundId ";
		return this.findOneByHQL(hql,params);
	}


	@Override
	public IntenetPay findIntenetPayByreqsn(String reqsn) {
		Map<String, Object> params = Maps.newHashMap();
		String hql="from IntenetPay where reqsn= :reqsn ";
		params.put("reqsn", reqsn);
		return this.findOneByHQL(hql, params);
	}
	
}
