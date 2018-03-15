package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsMonthIncomeCampus;

/**
 * 
 * @author lixuejun
 *
 */
public interface OdsMonthIncomeCampusDao extends GenericDAO<OdsMonthIncomeCampus, String> {

	/**
	 * 更新明细的审核状态
	 * @param campusId
	 * @param countDate
	 * @param auditStatus
	 */
	void updateOdsMonthIncomeStudentAuditStatus(String campusId, String countDate, EvidenceAuditStatus auditStatus);

	List<Map<String, String>> findOdsMonthIncomeCampusPrintByCountDate(
			String countDate, String status);

	List<Map<String, String>> findIncomeAuditRate(String campusId,
			String receitpDate,String type);

	List isFinishAudit(String campusId,String courseDate);
	
}
