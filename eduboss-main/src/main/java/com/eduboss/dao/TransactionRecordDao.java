package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.TransactionRecord;

@Repository
public interface TransactionRecordDao extends GenericDAO<TransactionRecord, String> {

	public void saveTransactionRecord(String transactionUuid);
	
}
