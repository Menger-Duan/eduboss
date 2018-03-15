package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.RoleCode;
import com.eduboss.service.ReportFormsService;
import com.eduboss.service.StaffBonusDayService;

/**
 * 报表
 * @author ndd
 */
@Controller
@RequestMapping(value = "/ReportFormsController")
public class ReportFormsController {
	
	@Autowired
	private ReportFormsService reportFormsService;
	
	@Autowired
	private StaffBonusDayService staffBonusDayService;
	
	@RequestMapping(value = "/getTop15Data")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15Data(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15Data(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15UserPerformance")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15UserPerformance(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15UserPerformance(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15ConsultorPerformance")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15ConsultorPerformance(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15ConsultorPerformance(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15StudyManagerPerformance")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15StudyManagerPerformance(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15StudyManagerPerformance(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15TeacherHours")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15TeacherHours(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15TeacherHours(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15StudyManagerHours")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15StudyManagerHours(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15StudyManagerHours(startDate, endDate ,isQueryAll);
	}
	
	@RequestMapping(value = "/getTop15DataFirst3")
	@ResponseBody
	public List<List<Map<String, Object>>> getTop15DataFirst3(String startDate,String endDate,Boolean isQueryAll){
		return reportFormsService.getTop15DataFirst3(startDate, endDate ,isQueryAll);
	}
	
	/** 
	 * top10
	* @param startDate
	* @param endDate
	* @param isQueryAll
	* @param roleCode
	* @return
	* @author  author :Yao 
	* @date  2016年7月21日 下午4:36:11 
	* @version 1.0 
	*/
	@RequestMapping(value = "/getTop10ByDateAndRole")
	@ResponseBody
	public List<Map<Object, Object>> getTop10ByDateAndRole(String startDate,String endDate,Boolean isQueryAll,RoleCode roleCode){
		return staffBonusDayService.getTop10ForPC(startDate, endDate, isQueryAll, roleCode);
	}
	
	@RequestMapping(value = "/getTop15DataLast2")
	@ResponseBody
	public List<Map<String, Object>> getTop15DataLast2(String startDate,String endDate,Boolean isQueryAll,String type){
		return reportFormsService.getTop15DataLast2(startDate, endDate ,isQueryAll,type);
	}
	
	@RequestMapping(value = "/getOutcomeIncomeRpData")
	@ResponseBody
	public List getOutcomeIncomeRpData(String startDate,String endDate, String orgId){
		
		return reportFormsService.getOutcomeIncomeRpData(startDate, endDate, orgId);
	}
	
	@RequestMapping(value = "/getStudentBySubjectRpData")
	@ResponseBody
	public List getStudentBySubjectRpData(String startDate,String endDate, String orgId){
		
		return reportFormsService.getStudentBySubjectRpData(startDate, endDate, orgId);
	}
	
	//新的欢迎页的课消统计 根据不同的产品类型 20170731 xiaojinwang
	@RequestMapping(value = "/getTop10CourseHoursByType")
	@ResponseBody
	public List<Map<String, Object>> getTop10CourseHoursByType(String startDate,String endDate,Boolean isQueryAll,String productType,String type){
		return reportFormsService.getTop10CourseHoursByType(startDate, endDate, isQueryAll, productType, type);
	}

}
