package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.eduboss.domain.IncomeEvidenceAdjustSummary;
import com.eduboss.domainVo.IncomeEvidenceAdjustItemVo;
import com.eduboss.domainVo.IncomeEvidenceAdjustSummaryVo;
import com.eduboss.domainVo.OdsMonthIncomeCampusPrintVo;
import com.eduboss.dto.Response;
import com.eduboss.service.IncomeEvidenceAdjustSummaryService;
import com.eduboss.service.OdsMonthIncomeCampusService;

/**
 * 
 * @author lixuejun
 *
 */
@Controller
@RequestMapping(value="/IncomeEvidenceController")
public class IncomeEvidenceController {
	
	@Autowired
	private IncomeEvidenceAdjustSummaryService incomeEvidenceAdjustSummaryService;
	
	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;
	
	@RequestMapping(value="/editIncomeAdjustItem")
	@ResponseBody
	public IncomeEvidenceAdjustItem editIncomeAdjustItem(@RequestBody IncomeEvidenceAdjustItemVo itemVo) throws Exception{
		return incomeEvidenceAdjustSummaryService.editIncomeAdjustItem(itemVo);
	}
	
	@RequestMapping(value="/deleteIncomeAdjustItem")
	@ResponseBody
	public Response deleteIncomeAdjustItem(@RequestParam String itemId) throws Exception{
		incomeEvidenceAdjustSummaryService.deleteIncomeAdjustItem(itemId);
		return new Response();
	}

	@RequestMapping(value="/editIncomeAdjustSummary")
	@ResponseBody
	public Response editIncomeAdjustSummary(@RequestBody IncomeEvidenceAdjustSummary summary) throws Exception{
		incomeEvidenceAdjustSummaryService.editIncomeAdjustSummary(summary);
		return new Response();
	}
	
	@RequestMapping(value="/findIncomeAdjustSummaryById")
	@ResponseBody
	public IncomeEvidenceAdjustSummaryVo findIncomeAdjustSummaryById(@RequestParam String summaryId) throws Exception{
		return incomeEvidenceAdjustSummaryService.findIncomeAdjustSummaryById(summaryId);
	}
	
	@RequestMapping(value="/auditIncomeEvidence")
	@ResponseBody
	public Response auditIncomeEvidence(@RequestParam String evidenceId) throws Exception{
		odsMonthIncomeCampusService.auditIncomeEvidence(evidenceId);
		return new Response();
	}
	
	@RequestMapping(value="/rollbackIncomeEvidence")
	@ResponseBody
	public Response rollbackIncomeEvidence(@RequestParam String evidenceId) throws Exception{
		odsMonthIncomeCampusService.rollbackIncomeEvidence(evidenceId);
		return new Response();
	}
	
	@RequestMapping(value="/findOdsMonthIncomeCampusPrintById")
	@ResponseBody
	public OdsMonthIncomeCampusPrintVo findOdsMonthIncomeCampusPrintById(@RequestParam String evidenceId) throws Exception {
		return odsMonthIncomeCampusService.findOdsMonthIncomeCampusPrintById(evidenceId);
	}
	
	@RequestMapping(value="/findIncomeAuditRate")
	@ResponseBody
	public List<Map<String,String>> findIncomeAuditRate(@RequestParam String campusId,@RequestParam String receiptDate,@RequestParam String type) throws Exception {
		return odsMonthIncomeCampusService.findIncomeAuditRate(campusId, receiptDate,type);
	}
	
}
