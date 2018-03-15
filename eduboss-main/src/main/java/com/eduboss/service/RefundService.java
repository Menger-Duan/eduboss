package com.eduboss.service;

import java.util.List;
import java.util.Set;

import com.eduboss.domain.Contract;
import com.eduboss.domain.RefundContract;
import com.eduboss.domain.RefundContractProduct;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.RefundContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;


public interface RefundService {


	/**
	 * 生成一个退费合同。 （ 生成一个退费合同， 退费的产品关联， 第一步的退费流程  ）
	 * @param refundContract 退费合同
	 */
	void saveNewRefundContract(RefundContract refundContract);
	
	/**
	 * 获取 待退费的 合同信息
	 * @param stuId
	 * @return
	 */
	List<ContractVo> getPendingRefundContractsByStuId(String stuId);
	
	/**
	 * 获取 退费合同列表 信息 用于列表显示
	 * @return
	 */
	DataPackage getPageRefundContracts(DataPackage dp, RefundContractVo refundContractVo, TimeVo timeVo);
	
	/**
	 * 获取退费合同的详情
	 * @param refundContractId
	 * @return
	 */
	RefundContractVo getRedundContractById(String refundContractId) ;
	
	/**
	 * 审批 某个 退费合同 (可以审批不通过吗? 后面的流程呢？)
     * @param refundContractVo 审批的合同Vo，读取id，approve，remark
	 * @return
	 */
	void approveRefundContract(RefundContractVo refundContractVo);
	
	
	
	
}
