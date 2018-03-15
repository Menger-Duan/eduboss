package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.ContractRecord;

public interface ContractRecordDao extends GenericDAO<ContractRecord, String>{
	
	/**
	 * 根据合同Id 找修改记录列表
	 * @param contractId
	 * @return
	 */
	public List<ContractRecord> findLastRecordByContractId(String contractId);
}
