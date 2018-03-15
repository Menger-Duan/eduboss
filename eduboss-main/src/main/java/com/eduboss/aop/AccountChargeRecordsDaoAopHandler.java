package com.eduboss.aop;

import java.math.BigDecimal;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ProductType;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractService;
import com.eduboss.service.UserOperationLogService;
import com.eduboss.service.UserService;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 * @author Admin
 *
 */
public class AccountChargeRecordsDaoAopHandler {

	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
		Object[] oArray = joinPoint.getArgs();
		for (int i = 0; i < oArray.length; i++) {
			System.out.println(oArray[i]);
		}
		String methodName = joinPoint.getSignature().getName();
		System.out.println(methodName);
		if("save".equals(methodName)){
			this.saveTrigger(oArray);
		} 
	}

	private void saveTrigger(Object[] oArray) {
		AccountChargeRecords record =  (AccountChargeRecords)oArray[0];
		ContractProduct contractProduct = record.getContractProduct();
		BigDecimal consumeAmount = record.getAmount();
		BigDecimal auditHour = record.getQuality();
		
		// 更新 并且 检测合同
		updateContractTrigger(consumeAmount, contractProduct);
		// 更新 并且 检测合同产品
		updateContractProductTrigger(auditHour, consumeAmount, contractProduct);
	}
	
	
	/**
	 * 更新 并且检测 合同产品状态
	 * @param detal
	 * @param contractProduct
	 */
	private void updateContractProductTrigger(BigDecimal consumeQuality,
			BigDecimal detal, ContractProduct contractProduct) {
		// 更新合同产品的值 已付金额 已消费的数量
		contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().add(detal));
		contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().add(consumeQuality));
		// 检测合同产品状态 是否完结
		if(contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID && contractProduct.getPaidAmount().subtract(contractProduct.getConsumeAmount()).compareTo(BigDecimal.ZERO) == 0 ) {
			contractProduct.setStatus(ContractProductStatus.ENDED);
		}
	}

	/**
	 * 更新 并且检测 合同状态
	 * @param detal
	 * @param contractProduct
	 */
	private void updateContractTrigger(BigDecimal detal,
			ContractProduct contractProduct) {
		Contract contract =  contractProduct.getContract();
		// 更新合同消费总金额,剩余资金, 剩余课时
		contract.setConsumeAmount(contract.getConsumeAmount().add(detal));
		contract.setRemainingAmount(contract.getPaidAmount().subtract((contract.getConsumeAmount())));
		
		// 检测合同状态 是否完结
		if(contract.getPaidStatus() == ContractPaidStatus.PAID && contract.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 ) {
			contract.setContractStatus(ContractStatus.FINISHED);
		}
	}
	
	
}
