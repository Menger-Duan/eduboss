package com.eduboss.service.impl;

import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.service.MoneyArrangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("com.eduboss.service.MoneyArrangeLogService")
public class MoneyArrangeLogServiceImpl implements MoneyArrangeLogService {
	
	@Autowired
	private MoneyArrangeLogDao moneyArrangeLogDao;

	@Override
	public boolean hasAssignRecordOfContractProduct(
			ContractProduct contractProduct) {
		int countOfArrangeRecord =  moneyArrangeLogDao.getNumberOfRecords(contractProduct.getId());		
		return countOfArrangeRecord==0?false:true; 
		
	}

	@Override
	public void deleteByContractProductId(String contractProductId) {
		
		List<MoneyArrangeLog>logs = moneyArrangeLogDao.findAllRecordsByContractProductId(contractProductId);
		moneyArrangeLogDao.deleteAll(logs);
		moneyArrangeLogDao.flush();
	}
}
