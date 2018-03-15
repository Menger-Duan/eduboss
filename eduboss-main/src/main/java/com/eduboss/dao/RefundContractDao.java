package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundContract;
import com.eduboss.domainVo.RefundContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;


/**
 * @classname	PointialStudentDao.java 
 * @Description
 * @author	Zhang YiHeng
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	Zhang YiHeng
 * @Version	1.0
 */

@Repository
public interface RefundContractDao extends GenericDAO<RefundContract, String> {

	/**
	 * 获取退费合同列表信息
	 * @param dp
	 * @param refundContractVo
	 * @param timeVo
	 * @return
	 */
	DataPackage getPageRefundContracts(DataPackage dp,
			RefundContractVo refundContractVo, TimeVo timeVo);

	
}
