package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domainVo.RefundIncomeDistributeVo;
import com.eduboss.jedis.Message;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.dto.DataPackage;


public interface ContractBonusService {
	public DataPackage getContractBonusVoList(DataPackage dp,ContractBonusVo contractBonusVo,Map params);

	public RefundIncomeDistributeVo getContractBonusByStudentReturnId(
			String studentReturnId);
	
	public void saveContractBonus(String fundChangeHistoryId,
								  String bonusType, String studentReturnId, String returnType, String returnReason, MultipartFile certificateImageFile, String accountName, String account, String productType, RefundIncomeDistributeVo refundVo);
	
	/**
	 * 退费详情删除图片
	 * @param fundsChangeId
	 */
	public void  deleteImage(String fundsChangeId);


	/**
	 * 修改合同后如果每种产品的实付金额少于已分配的业绩,对应的业绩要删除
	 * @param contractId
     */
	public List<Message> afterEditContractDeleteContractBonus(String contractId);
	

}
