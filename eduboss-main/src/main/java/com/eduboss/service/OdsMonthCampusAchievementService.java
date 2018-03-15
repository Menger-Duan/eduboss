package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsCampusAchievementModify;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainPrintVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainStudentVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementModifyVo;
import com.eduboss.dto.Response;


public interface OdsMonthCampusAchievementService {

	List<OdsMonthCampusAchievementMainStudentVo> findAchievementMainStudentByMonth(String yearMonth,String campusId);
	
	
	List findCampusAchievementMainByMonth(BasicOperationQueryVo searchVo);

	List<OdsMonthCampusAchievementModifyVo> findModifyInfoByMainId(String mainId);


	Response saveAchievementModifyInfo(OdsCampusAchievementModify odsCampusAchievementModify);


	Response deleteAchievementModifyInfo(String id);


	OdsMonthCampusAchievementMainVo findAchievementMainInfoById(String id);


	void auditCampusAchievement(String id, EvidenceAuditStatus status);
	
	void rollbackPaymentReciept(String id);


	void flushCampusAchievementById(String id);


	Response updateAchievementMainInfo(String id, String remark);
	
	/**
	 * 根据id 获取审核记录
	 * @param evidenceId
	 * @return
	 */
	OdsMonthCampusAchievementMainPrintVo findOdsMonthCampusAchievementMainPrintById(String evidenceId);


	/**
	 * 得到某个校区的审核比例
	 * @param campusId
	 * @param channel
	 * @param receiptMonth
	 * @return
	 */
	List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth);

}

