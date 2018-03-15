package com.eduboss.dao;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsMonthRemainAmountCampus;

/**
 * 
 * @author lixuejun
 *
 */
public interface OdsMonthRemainAmountCampusDao extends GenericDAO<OdsMonthRemainAmountCampus, String> {

	/**
	 * 更新明细的审核状态
	 * @param campusId
	 * @param countDate
	 * @param auditStatus
	 */
	void updateOdsMonthRemainStudentAuditStatus(String campusId, String countDate, EvidenceAuditStatus auditStatus);
	
}
