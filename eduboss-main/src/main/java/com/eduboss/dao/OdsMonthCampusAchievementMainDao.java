package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsMonthCampusAchievementMain;

public interface OdsMonthCampusAchievementMainDao extends GenericDAO<OdsMonthCampusAchievementMain, String> {
	
	List findInfoByMonth(String yearMonth,String type,String campusId);

	List<Map<String,String>> findPaymentRecieptMainByMonth(String yearMonth,
			String evidenceAuditStatus);

	List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth);
	/**
	 * 更新现金流明细审核状态
	 * @author: duanmenrun
	 * @Title: updateRecieptMainStudentAuditStatus 
	 * @Description: TODO 
	 * @param receiptMonth
	 * @param campusId
	 * @param nextStatus
	 */
	void updateRecieptMainStudentAuditStatus(String receiptMonth, String campusId, EvidenceAuditStatus nextStatus);

}
