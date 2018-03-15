package com.eduboss.dao;

import java.math.BigDecimal;

import com.eduboss.domain.Contract;
import com.eduboss.domain.StudnetAccMv;


public interface StudnetAccMvDao extends GenericDAO<StudnetAccMv, String>{

	/**
	 * 获取学生课时和金钱统计信息
	 * @param studentId
	 * @return
	 */
	public StudnetAccMv getStudnetAccMvByStudentId(String studentId);
	
	/**
	 * 转移资金 到学生账户
	 * @param studentId
	 * @param amountForTransfer
	 */
	public void transferAmountToElectronicAcc(String studentId, BigDecimal amountForTransfer);
	
	
}
