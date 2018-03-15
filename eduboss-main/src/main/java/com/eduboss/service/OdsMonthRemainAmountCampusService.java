package com.eduboss.service;

import com.eduboss.domain.OdsMonthRemainAmountCampus;
import com.eduboss.domainVo.OdsMonthIncomeCampusPrintVo;
import com.eduboss.domainVo.OdsMonthRemainAmountCampusPrintVo;


/**
 * 
 * @author lixuejun
 *
 */
public interface OdsMonthRemainAmountCampusService {

	/**
	 * 审批剩余资金凭证
	 * @param evidenceId
	 */
	void auditRemainEvidence(String evidenceId);
	
	/**
	 * 审批不通过剩余资金凭证
	 * @param evidenceId
	 */
	void rollbackRemainEvidence(String evidenceId);
	
	/**
	 * 检查营收凭证能否审批或刷新
	 * @param evidenceId
	 */
	boolean checkCanAuditOrFlush(String evidenceId);
	
	/**
	 * 根据id查找OdsMonthRemainAmountCampus
	 * @param evidenceId
	 * @return
	 */
	OdsMonthRemainAmountCampus findOdsMonthRemainAmountCampusById(String evidenceId);
	
	/**
	 * 根据id查找OdsMonthRemainAmountCampusPrintVo
	 * @param evidenceId
	 * @return
	 */
	OdsMonthRemainAmountCampusPrintVo findOdsMonthRemainAmountCampusPrintById(String evidenceId);
	
}
