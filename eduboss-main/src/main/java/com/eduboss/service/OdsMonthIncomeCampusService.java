package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.OdsMonthIncomeCampus;
import com.eduboss.domainVo.OdsMonthIncomeCampusPrintVo;


/**
 * 
 * @author lixuejun
 *
 */
public interface OdsMonthIncomeCampusService {

	/**
	 * 审批营收凭证
	 * @param evidenceId
	 */
	void auditIncomeEvidence(String evidenceId);
	
	/**
	 * 审批不通过营收凭证
	 * @param evidenceId
	 */
	void rollbackIncomeEvidence(String evidenceId);
	
	/**
	 * 检查营收凭证能否审批或刷新
	 * @param evidenceId
	 */
	boolean checkCanAuditOrFlush(String evidenceId);
	
	/**
	 * 根据id查找OdsMonthIncomeCampus
	 * @param evidenceId
	 * @return
	 */
	OdsMonthIncomeCampus findOdsMonthIncomeCampusById(String evidenceId);
	
	/**
	 * 根据id查找OdsMonthIncomeCampusPrintVo
	 * @param evidenceId
	 * @return
	 */
	OdsMonthIncomeCampusPrintVo findOdsMonthIncomeCampusPrintById(String evidenceId);
	

	List<Map<String,String>> findOdsMonthIncomeCampusPrintByCountDate(String countDate,String status);

	List<Map<String, String>> findIncomeAuditRate(String campusId,
			String receitpDate,String type);
	
	boolean isFinishAudit(String campusId,String courseDate);
}
