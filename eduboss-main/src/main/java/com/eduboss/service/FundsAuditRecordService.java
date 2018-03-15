package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.FundsAuditRecord;
import com.eduboss.domainVo.FundsAuditRecordVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
public interface FundsAuditRecordService {

	/**
	 * 保存收款记录审批流水
	 * @param record
	 */
	void saveFundsAuditRecord(FundsAuditRecord record);
	
	/**
	 * 根据收款id查找收款审核流水
	 * @param fundsId
	 * @return
	 */
	DataPackage findFundsAuditRecordByFundsId(String fundsId, DataPackage dp);
	
}
