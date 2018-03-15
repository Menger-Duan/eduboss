package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ContractProductSubjectDao;
import com.eduboss.domain.ContractProductSubject;

@Repository("ContractProductSubjectDao")
public class ContractProductSubjectDaoImpl extends GenericDaoImpl<ContractProductSubject, String> implements
		ContractProductSubjectDao {

	/**
	 * 根据合同产品id查询科目排课列表
	 */
	@Override
	public List<ContractProductSubject> findContractProductSubjectByCpId(String contractProductId) {
		Map<String, Object> params = Maps.newHashMap();
		String hql = " from ContractProductSubject where contractProduct.id = :contractProductId ";
		params.put("contractProductId", contractProductId);
		return super.findAllByHQL(hql, params);
	}
	
	/**
	 * 根据合同产品id和科目id查找ContractProductSubject
	 */
	@Override
	public ContractProductSubject findContractProductSubjectByCpIdAndSubjectId(String contractProductId, String subjectId) {
		ContractProductSubject returnCpSubject = null;
		Map<String, Object> params = Maps.newHashMap();
		String hql = " from ContractProductSubject where contractProduct.id = :contractProductId and subject.id = :subjectId ";
		params.put("contractProductId", contractProductId);
		params.put("subjectId", subjectId);
		List<ContractProductSubject> list = super.findAllByHQL(hql, params);
		if (list != null && list.size() > 0) {
			returnCpSubject = list.get(0);
		}
		return returnCpSubject;
	}

	/**
	 * 根据学生、产品、科目组计算剩余课时数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal sumRemainHoursByStudentProductSubject(String studentId,
			String productGroupId, String productId, String subjectId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT sum(cps.QUANTITY - cps.CONSUME_HOURS) remainHours FROM "
				+ " contract_product_subject cps LEFT JOIN contract_product cp ON cps.CONTRACT_PRODUCT_ID = cp.ID "
				+ " LEFT JOIN contract c ON cp.CONTRACT_ID = c.ID LEFT JOIN product p on cp.PRODUCT_ID = p.ID "
				+ " WHERE 1=1 ";
		sql += " AND cp.STATUS = 'NORMAL' ";
		if (StringUtils.isNotBlank(studentId)) {
			sql += " AND c.STUDENT_ID = :studentId ";
			params.put("studentId", studentId);
		}
		if (StringUtils.isNotBlank(productGroupId)) {
			sql += " AND p.PRODUCT_GROUP_ID = :productGroupId ";
			params.put("productGroupId", productGroupId);
		} else {
			sql += " AND p.ID = :productId ";
			params.put("productId", productId);
		}
		if (StringUtils.isNotBlank(subjectId)) {
			sql += " AND cps.SUBJECT_ID = :subjectId ";
			params.put("subjectId", subjectId);
		}
		List<Map<Object, Object>> list = super.findMapBySql(sql, params);
		if (list.size() > 0 && list.get(0).get("remainHours") != null) {
			return (BigDecimal) list.get(0).get("remainHours");
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 学生转校区，修改未消耗完的合同产品科目的校区
	 */
	@Override
	public void updateContractSubjectForTurnCampus(String studentId, String campusId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " UPDATE contract_product_subject cps LEFT JOIN contract_product cp ON cps.CONTRACT_PRODUCT_ID = cp.ID LEFT JOIN contract c ON cp.CONTRACT_ID = c.ID ";
		sql += " SET cps.BL_CAMPUS_ID = :campusId ";
		sql += " WHERE cps.QUANTITY > cps.CONSUME_HOURS AND c.STUDENT_ID = :studentId ";
		params.put("campusId", campusId);
		params.put("studentId", studentId);
		super.excuteSql(sql, params);
	}
	
}
