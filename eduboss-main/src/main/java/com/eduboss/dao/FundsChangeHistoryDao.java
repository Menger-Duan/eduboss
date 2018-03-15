package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.PayWay;
import com.eduboss.domain.Contract;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.FundsChangeSearchVo;
import com.eduboss.dto.TimeVo;

public interface FundsChangeHistoryDao extends GenericDAO<FundsChangeHistory, String> {
	
	/**
	 * 用HQL 列出所有收款的Fund
	 * @param dp
	 * @return
	 */
	DataPackage listFundsChangeHistory(DataPackage dp, FundsChangeSearchVo vo);
	
	void save(FundsChangeHistory fundsChangeHistory);

	Double historySumFundsChange(String contractId);
	
	/**
	 * 根据事务id计算冲销总计
	 * @param transactionId
	 * @return
	 */
	BigDecimal getWashSumFundsByTransactionId(String transactionId);

	/**
	 * 根据不同的 类型 生成收入记录， 大部分使用在退费下
	 * @param refundMoney
	 * @param payWay
	 * @param student
	 */
	void saveOneFundRecord(BigDecimal refundMoney, PayWay payWay, Student student);

	/**
	 * 根据合同ID 删除优惠收款记录
	 * @param contract
	 */
	void deletePromotionRecord(Contract contract);
	
	/**
	 * 根据合同ID查找收入记录
	 * @param contractId
	 * @return
	 */
	List<FundsChangeHistory> getFundsChangeHistoryListByContractId(String contractId);
	
	/**
	 * 根据学生ID查找收入记录
	 * @param studentId
	 * @return
	 */
	List<FundsChangeHistory> getFundsChangeHistoryListByStudentId(String studentId);

	/**
	 * 检查收款记录posId和posNumber是否唯一
	 * @param posId
	 * @param posNumber
	 * @return
	 */
	boolean isPosIAndPosNumberdUnique(String id, String posId, String posNumber);

	/**
	 * 根据收款记录计算有多少不能分配到合同的金额
	 * @param contractId
	 * @return
	 */
    BigDecimal sumCanNotAssignAmount(String contractId);
}
