package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.FundsAuditRecord;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
@Repository
public interface FundsAuditRecordDao extends GenericDAO<FundsAuditRecord, Integer> {

	/**
	 * 根据收款id查找收款审核流水
	 * @param fundsId
	 * @return
	 */
	DataPackage findFundsAuditRecordByFundsId(String fundsId, DataPackage dp);
	
}
