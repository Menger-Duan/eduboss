package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ContractProductSubject;

public interface ContractProductSubjectService {
	
	/**
	 * 根据合同产品id查询科目排课列表
	 * @param contractProductId
	 * @return
	 */
	List<ContractProductSubject> findContractProductSubjectByCpId(String contractProductId);
	
	/**
	 * 删除ContractProductSubject
	 * @param contractProductSubject
	 */
	void deleteContractProductSubject(ContractProductSubject contractProductSubject);
	
	/**
	 * 保存或修改ContractProductSubject
	 * @param contractProductSubject
	 */
	void saveOrUpdateContractProductSubject(ContractProductSubject contractProductSubject);
	
	/**
	 * 根据合同产品id和科目id查找ContractProductSubject
	 * @param contractProductId
	 * @param subjectId
	 * @return
	 */
	ContractProductSubject findContractProductSubjectByCpIdAndSubjectId(String contractProductId, String subjectId);
	
	/**
	 * 修改合同产品，合同产品总金额小于已分配金额转移所有未消耗课时
	 * @param contractProductId
	 */
	void transferOutContractProductSubject(ContractProduct contractProduct);
	
	/**
	 * 根据学生、产品组(或产品)、科目计算剩余课时
	 * @param studentId
	 * @param productGroupId
	 * @param productId
	 * @param subjectId
	 * @return
	 */
	BigDecimal sumRemainHoursByStudentProductSubject(String studentId, String productGroupId, String productId, String subjectId);
	
	/**
	 * 根据id查找ContractProductSubject
	 * @param id
	 * @return
	 */
	ContractProductSubject findById(String id);
	
	/**
	 * 学生转校区，修改未消耗完的合同产品科目的校区
	 * @param studentId
	 * @param campusId
	 */
	void updateContractSubjectForTurnCampus(String studentId, String campusId);

}
