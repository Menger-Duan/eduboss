package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.PayType;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ElectronicAccountChangeLog;
import com.eduboss.domain.MoneyArrangeLog;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;


/**
 * @classname	MoneyArrangeLogDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface MoneyArrangeLogDao extends GenericDAO<MoneyArrangeLog, String> {

	
	/**
	 * 保存分配资金
	 * @param contractProduct
	 * @param student 
	 * @param assignAmount
	 * @param currentLoginUser
	 * @param remark 
	 */
	void saveOneRecord(ContractProduct contractProduct,
			Student student, BigDecimal assignAmount, User currentLoginUser, String remark, PayType payType);

	/**
	 * 获取分配资金的记录
	 * @param contractProductId
	 * @return
	 */
	int getNumberOfRecords(String contractProductId);

	/**
	 * 获取资金分配的logs
	 * @param contractProductId
	 * @return
	 */
	List<MoneyArrangeLog> findAllRecordsByContractProductId(
			String contractProductId);
	
}
