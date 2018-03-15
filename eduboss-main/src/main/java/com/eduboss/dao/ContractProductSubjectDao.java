package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.ContractProductSubject;


@Repository
public interface ContractProductSubjectDao extends GenericDAO<ContractProductSubject, String> {

	/**
	 * 根据合同产品id查询科目排课列表
	 * @param contractProductId
	 * @return
	 */
	List<ContractProductSubject> findContractProductSubjectByCpId(String contractProductId);
	
	/**
	 * 根据合同产品id和科目id查找ContractProductSubject
	 * @param contractProductId
	 * @param subjectId
	 * @return
	 */
	ContractProductSubject findContractProductSubjectByCpIdAndSubjectId(String contractProductId, String subjectId);
	
	/**
	 *  根据学生、产品组(或产品)、科目组计算剩余课时
	 * @param studentId
	 * @param productGroupId
	 * @param productId
	 * @param subjectId
	 * @return
	 */
	BigDecimal sumRemainHoursByStudentProductSubject(String studentId, String productGroupId, String productId, String subjectId);
	
	/**
	 * 学生转校区，修改未消耗完的合同产品科目的校区
	 * @param studentId
	 * @param campusId
	 */
	void updateContractSubjectForTurnCampus(String studentId, String campusId);
	
}
