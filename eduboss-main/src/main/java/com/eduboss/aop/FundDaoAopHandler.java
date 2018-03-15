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
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.FundsChangeHistory;
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
public class FundDaoAopHandler {

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

	/**
	 * 对交款后的一个trigger， 包括合同的已交款，合同的剩余款，合同的可分配资金
	 * @param oArray
	 */
	private void saveTrigger(Object[] oArray) {
		FundsChangeHistory fund = (FundsChangeHistory) oArray[0];
		
		Contract contract = fund.getContract();
		
		// 更改合同的 待付款，已交款，可分配资金
		contract.setPaidAmount(contract.getPaidAmount().add(fund.getTransactionAmount()));
		contract.setPendingAmount(contract.getTotalAmount().subtract(contract.getPaidAmount()));
		contract.setAvailableAmount(contract.getAvailableAmount().add(fund.getTransactionAmount()));
		
		// 检查 合同缴费状态
		int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
		if(flag == 0) {
			contract.setPaidStatus(ContractPaidStatus.PAID);
		} else if (flag >0) {
			contract.setPaidStatus(ContractPaidStatus.PAYING);
		} else {
			throw new ApplicationException(ErrorCode.MONEY_ERROR);
		}
		
	}
	
}
