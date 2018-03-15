package com.eduboss.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Course;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

public interface MoneyArrangeLogService {

	/**
	 * 看是否有分配资金的记录
	 * @param contractProduct
	 * @return
	 */
	public boolean hasAssignRecordOfContractProduct(
			ContractProduct contractProduct);

	/**
	 * 根据合同产品ID 删除资金分配记录
	 * @param id
	 */
	public void deleteByContractProductId(String contractProductId); 
}
