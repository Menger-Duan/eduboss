package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.ContractBonus;
import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.dto.DataPackage;

public interface ContractBonusDao extends GenericDAO<ContractBonus, String> {

	/**
	 *
	 * @param fundsChangeHistoryId
	 * @return
	 */
	List<ContractBonus> findByFundsChangeHistoryId(String fundsChangeHistoryId);

	/**
	 *
	 * @param fundsChangeHistoryId
	 */
	void deleteByFundsChangeHistoryId(String fundsChangeHistoryId);
	
	/**
	 * 根据收款ID和业绩分配类型删除业绩分配
	 * @param fundsChangeHistoryId
	 * @param type bonus：个人业绩， campus校区业绩
	 */
	void deleteByFundsChangeHistoryId(String fundsChangeHistoryId, String type);
	
	/**
	 * 合同业绩列表
	 * */
	public DataPackage getContractBonusList(DataPackage dp,ContractBonusVo contractBonusVo,Map params);

	/**
	 * 根据退费Id找到责任信息
	 * @param studentReturnId
	 * @return
	 */
	List<ContractBonus> findByStudentReturnId(String studentReturnId);
	
	
	/**
	 * 根据 退费Id 删除提成信息
	 * @param contractId
	 */
	void deleteByStudentReturnId(String StudentReturnId);
	
	

	/**
	 * 根据合同产品找出不在当前支付单号的其他业绩分配信息
	 * @param contractId
	 * @param fundId
	 * @return
	 */
	List<ContractBonus> findByContractIdExeptFundId(String contractId,String fundId,String productType);
	
}
