package com.eduboss.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.eduboss.domain.Contract;

public interface ContractDao extends GenericDAO<Contract, String>{
	
	public void save(Contract contract);
	
	/**
	 * 根据学生id查询剩余课时>0的合同
	 * @param studentId
	 * @return
	 */
	public List<Contract> getNewContractByStudentId(String studentId);

	/**
	 * 根据学生的ID 获取 他之前的已经付款的，而且没有绑定合同的定金合同
	 * @param stuId
	 * @return 定金合同s
	 */
	public List<Contract> getPaidNormalDeposits(String stuId);

	public void updateRefundContract(String id, String contractIds,String userId);

	/**
	 * 根据学生的ID 来获取 最新的 最老的 正常的 有剩余资金的 且有 剩余赠送课时 合同
	 * @param stuId
	 * @param orderFlag
	 * @return
	 */
	public List<Contract> getOrderContracts(String id, Order order);

	/**
	 * 根据学生的ID 和 小班产品编号 返回 正常的 新增的 or 续费合同 已经交完费用的 合同
	 * @param studentId
	 * @param productId
	 * @return
	 */
	public List<Contract> findSpecificMiniClassContractsByStudentId(String studentId, String productId);

	/**
	 * 根据student 取出最新的一份合同
	 * @param studentId
	 * @return
	 */
	public Contract getLastedContracts(String studentId);

	/**
	 * 根据学生ID 和 产品ID， 查找相应的有others Class 的 合同
	 * @param studentId
	 * @param contractId
	 * @return
	 */
//	public List<Contract> findSpecificOtherClassContractsByStudentId(
//			String studentId, String contractId);
	
	
	/**
	 * 根据客户ID查询合同
	 * */
	public List<Contract> findContractByCustomer(String customerId, String paidStatus, String contractStatus);
	
	/**
	 * 检查有没有冻结的合同产品
	 * @param contract
	 * @return
	 */
	public boolean checkHasFrozenCp(Contract contract);
	
	public int countContractByCustomer(String customerId);
	
}
