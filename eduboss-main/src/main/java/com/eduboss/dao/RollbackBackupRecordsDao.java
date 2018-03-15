package com.eduboss.dao;

import java.util.List;

import com.eduboss.common.ProductType;
import com.eduboss.domain.RollbackBackupRecords;
import com.eduboss.dto.DataPackage;

public interface RollbackBackupRecordsDao extends GenericDAO<RollbackBackupRecords, String> {
	
	public DataPackage getRollbackBackupRecordsByTransactionId(String transactionId, DataPackage dp);
	

	public List<RollbackBackupRecords> getReCordByContractId(String contractId,ProductType productType);

}
