package com.eduboss.dao;

import java.util.List;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsMonthCampusAchievementMainStudent;

public interface OdsMonthCampusAchievementMainStudentDao extends GenericDAO<OdsMonthCampusAchievementMainStudent, String> {
	
	List<OdsMonthCampusAchievementMainStudent> findInfoByMonth(String yearMonth,String campusId);

	/**
	 * 更新校区业绩-学生明细审批状态
	 * @author: duanmenrun
	 * @Title: updateRecieptMainStudentAuditStatus 
	 * @Description: TODO 
	 * @param receiptMonth
	 * @param campusId
	 * @param nextStatus
	 */
	void updateRecieptMainStudentAuditStatus(String receiptMonth, String campusId, EvidenceAuditStatus nextStatus);
}
