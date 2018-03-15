package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.MoneyRollbackRecords;
import com.eduboss.domainVo.MoneyRollbackRecordsVo;
import com.eduboss.domainVo.RollbackBackupRecordsVo;
import com.eduboss.dto.DataPackage;

public interface MoneyRollbackRecordsService {

	/**
	 * 资金回滚记录列表
	 * @param dataPackage
	 * @param moneyRollbackRecordsVo
	 * @return
	 */
	public DataPackage getMoneyRollbackRecordsList(DataPackage dataPackage,
			MoneyRollbackRecordsVo moneyRollbackRecordsVo);
	
	/**
	 * 根据id查询资金回滚记录
	 * @param id
	 * @return
	 */
	public MoneyRollbackRecordsVo findMoneyRollbackRecordsById(String id);
	
	/**
	 * 根据transactionId获取备份的扣费记录
	 * @param transactionId
	 * @return
	 */
	public DataPackage getRollbackBackupRecordsByTransactionId(String transactionId, DataPackage dataPackage);

	/**
	 * 新增信息
	 * @param moneyRollbackRecordsVo
	 */
	public void editMoneyRollbackRecords(MoneyRollbackRecords moneyRollbackRecords);

}
