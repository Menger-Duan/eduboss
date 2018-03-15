package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.FundsAuditRecordDao;
import com.eduboss.domain.FundsAuditRecord;
import com.eduboss.domainVo.FundsAuditRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.FundsAuditRecordService;
import com.eduboss.utils.HibernateUtils;

@Service
public class FundsAuditRecordServiceImpl implements FundsAuditRecordService {
	
	@Autowired
	private FundsAuditRecordDao fundsAuditRecordDao;

	/**
	 * 保存收款记录审批流水
	 * @param record
	 */
	@Override
	public void saveFundsAuditRecord(FundsAuditRecord record) {
		fundsAuditRecordDao.save(record);
	}
	
	/**
	 * 根据收款id查找收款审核流水
	 */
	@Override
	public DataPackage findFundsAuditRecordByFundsId(String fundsId, DataPackage dp) {
		dp = fundsAuditRecordDao.findFundsAuditRecordByFundsId(fundsId, dp);
		List<FundsAuditRecordVo> list = HibernateUtils.voListMapping((List<FundsAuditRecord>)dp.getDatas(), FundsAuditRecordVo.class);
		dp.setDatas(list);
		return dp;
	}
	
}
