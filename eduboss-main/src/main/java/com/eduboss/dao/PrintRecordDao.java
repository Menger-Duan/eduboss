package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PrintRecord;

@Repository
public interface PrintRecordDao extends GenericDAO<PrintRecord, String> {
	
	/**
	 * 统计合同打印次数
	 * */
	public int coutContractPrintedNumber(String contractId);

}
