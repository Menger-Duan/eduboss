package com.eduboss.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OdsMonthRemainAmountCampusDao;
import com.eduboss.domain.OdsMonthRemainAmountCampus;
import com.google.common.collect.Maps;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("OdsMonthRemainAmountCampusDao")  
public class OdsMonthRemainAmountCampusDaoImpl extends GenericDaoImpl<OdsMonthRemainAmountCampus, String> implements OdsMonthRemainAmountCampusDao {

	
	/**
	 * 更新明细的审核状态
	 */
	public void updateOdsMonthRemainStudentAuditStatus(String campusId, String countDate, EvidenceAuditStatus auditStatus) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("auditStatus", auditStatus.getValue());
		params.put("campusId", campusId);
		params.put("countDate", countDate);
		String sql = " UPDATE ods_month_remain_amount_student SET EVIDENCE_AUDIT_STATUS = :auditStatus  WHERE CAMPUS_ID = :campusId AND COUNT_DATE = :countDate ";
		super.excuteSql(sql,params);
	}
	
}
