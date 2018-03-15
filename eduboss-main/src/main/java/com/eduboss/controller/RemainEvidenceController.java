package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.RemainEvidenceAdjustItem;
import com.eduboss.domain.RemainEvidenceAdjustSummary;
import com.eduboss.domainVo.OdsMonthRemainAmountCampusPrintVo;
import com.eduboss.domainVo.RemainEvidenceAdjustItemVo;
import com.eduboss.domainVo.RemainEvidenceAdjustSummaryVo;
import com.eduboss.dto.Response;
import com.eduboss.service.OdsMonthRemainAmountCampusService;
import com.eduboss.service.RemainEvidenceAdjustSummaryService;

/**
 * 
 * @author lixuejun
 *
 */
@Controller
@RequestMapping(value="/RemainEvidenceController")
public class RemainEvidenceController {
	
	@Autowired
	private RemainEvidenceAdjustSummaryService remainEvidenceAdjustSummaryService;
	
	@Autowired
	private OdsMonthRemainAmountCampusService odsMonthRemainAmountCampusService;
	
	@RequestMapping(value="/editRemainAdjustItem")
	@ResponseBody
	public RemainEvidenceAdjustItem editRemainAdjustItem(@RequestBody RemainEvidenceAdjustItemVo itemVo) throws Exception{
		return remainEvidenceAdjustSummaryService.editRemainAdjustItem(itemVo);
	}
	
	@RequestMapping(value="/deleteRemainAdjustItem")
	@ResponseBody
	public Response deleteRemainAdjustItem(@RequestParam String itemId) throws Exception{
		remainEvidenceAdjustSummaryService.deleteRemainAdjustItem(itemId);
		return new Response();
	}

	@RequestMapping(value="/editRemainAdjustSummary")
	@ResponseBody
	public Response editRemainAdjustSummary(@RequestBody RemainEvidenceAdjustSummary summary) throws Exception{
		remainEvidenceAdjustSummaryService.editRemainAdjustSummary(summary);
		return new Response();
	}
	
	@RequestMapping(value="/findRemainAdjustSummaryById")
	@ResponseBody
	public RemainEvidenceAdjustSummaryVo findRemainAdjustSummaryById(@RequestParam String summaryId) throws Exception{
		return remainEvidenceAdjustSummaryService.findRemainAdjustSummaryById(summaryId);
	}
	
	@RequestMapping(value="/auditRemainEvidence")
	@ResponseBody
	public Response auditRemainEvidence(@RequestParam String evidenceId) throws Exception{
		odsMonthRemainAmountCampusService.auditRemainEvidence(evidenceId);
		return new Response();
	}
	
	@RequestMapping(value="/rollbackRemainEvidence")
	@ResponseBody
	public Response rollbackRemainEvidence(@RequestParam String evidenceId) throws Exception{
		odsMonthRemainAmountCampusService.rollbackRemainEvidence(evidenceId);
		return new Response();
	}
	
	@RequestMapping(value="/findOdsMonthRemainAmountCampusPrintById")
	@ResponseBody
	public OdsMonthRemainAmountCampusPrintVo findOdsMonthRemainAmountCampusPrintById(@RequestParam String evidenceId) throws Exception {
		return odsMonthRemainAmountCampusService.findOdsMonthRemainAmountCampusPrintById(evidenceId);
	}
	
}
