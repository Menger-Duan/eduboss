package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsPaymentReceiptModify;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainPrintVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptVo;
import com.eduboss.domainVo.OdsPaymentRecieptModifyVo;
import com.eduboss.dto.Response;


public interface OdsMonthPaymentRecieptService {

	List<OdsMonthPaymentRecieptVo> findPaymentRecieptByMonth(String yearMonth,String campusId);
	
	
	List findPaymentRecieptMainByMonth(BasicOperationQueryVo searchVo);


	List<Map<String,String>> findPaymentRecieptMainByMonth(String yearMonth,String evidenceAuditStatus);


	List<OdsPaymentRecieptModifyVo> findInfoByMainId(String mainId);


	Response saveModifyInfo(OdsPaymentReceiptModify odsPaymentReceiptModify);


	Response deleteModifyInfo(String id);


	OdsMonthPaymentRecieptMainVo findMainInfoById(String id);


	void auditPaymentReciept(String id, EvidenceAuditStatus status);
	
	void rollbackPaymentReciept(String id);


	void flushInfoById(String id);


	Response updatePaymentMainInfo(String id, String remark);
	
	/**
	 * 根据id查找OdsMonthPaymentRecieptMainPrintVo
	 * @param evidenceId
	 * @return
	 */
	OdsMonthPaymentRecieptMainPrintVo findOdsMonthPaymentRecieptMainPrintById(String evidenceId);


	/**
	 * 得到某个校区的审核比例
	 * @param campusId
	 * @param channel
	 * @param receiptMonth
	 * @return
	 */
	List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth);

	List<Map<Object,Object>> getPersonPaymentFinaceBonus(BasicOperationQueryVo searchVo);

	List getPaymentRecieptReportExcel(BasicOperationQueryVo searchVo);
}

