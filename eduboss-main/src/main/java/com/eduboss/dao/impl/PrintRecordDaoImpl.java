package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.PrintRecordDao;
import com.eduboss.domain.PrintRecord;


@Repository("PrintRecord")
public class PrintRecordDaoImpl extends GenericDaoImpl<PrintRecord, String> implements PrintRecordDao {
	
	/**
	 * 统计合同打印次数
	 * */
	@Override
	public int coutContractPrintedNumber(String contractId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder countHql = new StringBuilder();
		countHql.append("select count(*) from PrintRecord where businessId = :contractId ");
		params.put("contractId", contractId);
		return this.findCountHql(countHql.toString(), params);
	}
}
