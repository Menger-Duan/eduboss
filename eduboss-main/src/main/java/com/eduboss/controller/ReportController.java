package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.eduboss.domainVo.*;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ReflushReportSubject;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.SummaryCycleType;
import com.eduboss.common.SummaryTableType;
import com.eduboss.domain.MiniClass;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.OrganizationDateReqVo;
import com.eduboss.dto.Response;
import com.eduboss.service.ReportService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.ExportExcel;

/**
 *
 * @author qin.jingkai 2014-10-17
 */

@Controller
@RequestMapping(value = "/ReportAction")
public class ReportController {

	private final static Logger log = Logger.getLogger(ReportController.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Autowired
	private ReportService reportService;

	@RequestMapping("/dowlOperatRpForStuMan")
	public ModelAndView dowlOperatRpForStuMan(
			@RequestParam String summaryTableStartDate,
			@RequestParam String summaryTableEndDate,
			@RequestParam SummaryTableType summaryTableType,
			@RequestParam SummaryCycleType summaryCycleType,
			@RequestParam String blCampusId, HttpServletRequest request,
			HttpServletResponse response) throws Exception { // http://a52071453.iteye.com/blog/1559005

		// summaryTableStartDate = "2014-10-01";
		// summaryTableEndDate = "2014-10-31";
		summaryTableStartDate = "2014-11-01";
		summaryTableEndDate = "2014-11-30";
		reportService.getExcelDataTypeOfCampusAndCycleOfMonthOrQuarterOrYear(
				SummaryCycleType.MONTH, summaryTableStartDate,
				summaryTableEndDate);
		return null;

		// Map<String,Object> paramMap=new HashMap<String, Object>();
		// HSSFWorkbook workbook = null;
		// if (SummaryCycleType.CAMPUS.equals(campusSummaryTableType)) {// 校区
		//
		// } else if (SummaryCycleType.CONSULTOR.equals(campusSummaryTableType))
		// {// 咨询师
		//
		// } else if
		// (SummaryCycleType.STUDY_MANAGER.equals(campusSummaryTableType)) {//
		// 学管
		// workbook=reportService.dowlOperatRpForStuMan(summaryTableStartDate,
		// summaryTableEndDate, paramMap);
		// }
		//
		// // 输出Excel到流
		// String fileName =dateFormat.format(new Date())+".xls";
		// response.setContentType("text/html;charset=utf-8");
		// request.setCharacterEncoding("UTF-8");
		// response.setContentType("application/x-msdownload;");
		// response.setHeader("Content-disposition", "attachment; filename="+
		// fileName);
		// OutputStream outputStream=response.getOutputStream();
		// workbook.write(outputStream);
		// outputStream.flush();
		// outputStream.close();
		// return null;
	}

	@RequestMapping(value = "/getGradeProportionRpData", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getGradeProportionRpData(
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String campusId) {
		DataPackage dp = reportService.getGradeProportionRpData(startDate,
				endDate, campusId);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getCampusEmployeesRpData", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCampusEmployeesRpData(
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String campusId) {
		DataPackage dp = reportService.getCampusEmployeesRpData(startDate,
				endDate, campusId);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getMiniClassEnrollmentAnalysis",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Integer> getMiniClassEnrollmentAnalysis(@RequestParam String miniClassId){
		return reportService.getMiniClassEnrollmentAnlysis(miniClassId);

	}

	@RequestMapping(value = "/getMiniClassFirstSchoolTimeStatistics",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Integer> getMiniClassFirstSchoolTimeStatistics(@RequestParam String miniClassId,@RequestParam String startDate,@RequestParam String endDate){
		return reportService.getMiniClassFirstSchoolTimeStatistics(miniClassId, startDate, endDate);
	}

	@RequestMapping(value = "/reflushReportData", method = RequestMethod.GET)
	@ResponseBody
	public Response reflushReportData(@RequestParam ReflushReportSubject procedureName, @RequestParam String countDate, String mappingDate, String orgId) throws SQLException {
		log.debug("reflushReportData start");
		reportService.reflushReportData(procedureName, countDate, mappingDate, orgId);
		return new Response();
	}

	@RequestMapping(value = "/getOooCourseConsumeTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOneOnOneCourseConsumeTeacherView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOneOnOneCourseConsumeTeacherView(courseConsumeTeacherVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@RequestMapping(value = "/getOooCourseConsumeTeacher", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOooCourseConsumeTeacher(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOooCourseConsumeTeacher(courseConsumeTeacherVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getOooCourseConsumeStudentView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOneOnOneCourseConsumeStudentView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOneOnOneCourseConsumeStudentView(
				courseConsumeTeacherVo.getStartDate(),
				courseConsumeTeacherVo.getEndDate(),
				courseConsumeTeacherVo.getOrganizationType(),
				courseConsumeTeacherVo.getBlCampusId(), dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getRpMiniClassSurplusMoney", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getReportMiniClassSurplusMoney(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute ReportMiniClassSurplusMoneyVo reportMiniClassSurplusMoneyVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassSurplusMoney(reportMiniClassSurplusMoneyVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getReportStudentSurplusFunding", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getReportStudentSurplusFunding(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute ReportStudentSurplusFundingVo reportStudentSurplusFundingVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getReportStudentSurplusFunding(reportStudentSurplusFundingVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getMiniClassStudentCount", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassStudentCount(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassStudentCount(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	/**
	 * 获取小班课消统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassConsumeAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassConsumeAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassConsumeAnalyze(
				basicOperationQueryVo.getStartDate(),
				basicOperationQueryVo.getEndDate(),
				basicOperationQueryVo.getBasicOperationQueryLevelType(),
				basicOperationQueryVo.getBlCampusId(), dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 现金流
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFinanceAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFinanceAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getFinanceAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 现金流（合同）
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFinanceContractAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFinanceContractAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getFinanceContractAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 现金流（公帐合同）
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPublicFinanceContractAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getPublicFinanceContractAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getPublicFinanceContractAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 校区签单合同
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCampusSignContract", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCampusSignContract(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getCampusSignContract(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 实收现金流计划完成情况
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFinanceContractAnalyzeRate", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFinanceContractAnalyzeRate(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getFinanceContractAnalyzeRate(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	/**
	 * 一对一学生统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOooStudentCountAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOooStudentCountAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOneOnOneStudentCountAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对一学生状态统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentOOOStatusCount", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getStudentOOOStatusCount(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentOOOStatusCount(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对一学生状态统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentOOOStatusDetail", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getStudentOOOStatusDetail(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String oooStatus)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentOOOStatusDetail(basicOperationQueryVo,oooStatus, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	
	/**
	 * 一对多学生状态统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentOTMStatusCount", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getStudentOTMStatusCount(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentOTMStatusCount(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对多学生状态统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentOTMStatusDetail", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getStudentOTMStatusDetail(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String oooStatus)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentOTMStatusDetail(basicOperationQueryVo,oooStatus, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 一对一学生状态统计
	 * @param countDate
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getStudentDetailStatu", method = RequestMethod.GET)
	@ResponseBody
	public Boolean getStudentDetailStatu(String countDate)
			throws Exception {
		return reportService.getStudentDetailStatu(countDate);
	}
	
	@RequestMapping(value = "/getOtmStudentDetailStatu", method = RequestMethod.GET)
	@ResponseBody
	public Boolean getOtmStudentDetailStatu(String countDate)
			throws Exception {
		return reportService.getOtmStudentDetailStatu(countDate);
	}

	/**
	 * 一对一剩余资金
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOooStudentRemainAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOooStudentRemainAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOneOnOneStudentRemainAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 课消
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCourseConsomeAnalyse", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCourseConsomeAnalyse(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getCourseConsomeAnalyse(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 退费
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRefundAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getRefundAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getRefundAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 收入
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getIncomingAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getIncomingAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getIncomingAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 小班产品统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassPeopleAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassPeopleAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassPeopleAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 学生待收款资金
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentPendingMoney")
	@ResponseBody
	public DataPackageForJqGrid getStudentPendingMoney(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentPendingMoney(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	/**
	 * 购买课时数
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPayClassHour")
	@ResponseBody
	public DataPackageForJqGrid getPayClassHour(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getPayClassHour(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 购买课时数 (每天)
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPayClassHourEveryDay")
	@ResponseBody
	public DataPackageForJqGrid getPayClassHourEveryDay(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getPayClassHourEveryDay(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 购买课时数 (校区维度)
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPayClassHourCampus")
	@ResponseBody
	public DataPackageForJqGrid getPayClassHourCampus(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getPayClassHourCampus(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 获取小班课消统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassKoufeiTongji", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassKoufeiTongji(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId,@RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassKoufeiTongji(basicOperationQueryVo,miniClassTypeId, dp,anshazhesuan);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}



	@RequestMapping(value = "/getMiniClassAttendsAnalyzeToExcel", method = RequestMethod.GET)
	@ResponseBody
	public void getMiniClassAttendsAnalyzeToExcel(@ModelAttribute GridRequest gridRequest, HttpServletResponse response, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId
			, @RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan, String organizationName, String teacherName
			, String studyManagerName) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);
		dataPackage = reportService.getMiniClassKoufeiTongji(basicOperationQueryVo,miniClassTypeId, dataPackage,anshazhesuan);

		List<Map<Object, Object>> datas = (List)dataPackage.getDatas();

		List list = new ArrayList<>();

		for (Map<Object, Object> map:datas){
			map.put("GROUNP", getName(map.get("GROUNP")));
			map.put("BRENCH", getName(map.get("BRENCH")));
			map.put("CAMPUS", getName(map.get("CAMPUS")));
			map.put("TEACHER", getName(map.get("TEACHER")));

			if (map.get("WORK_TYPE") == null || "".equals(map.get("WORK_TYPE"))){
				map.put("WORK_TYPE", null);
			}else {
				if ("FULL_TIME".equals(map.get("WORK_TYPE"))){
					map.put("WORK_TYPE", "全职");
				}else {
					map.put("WORK_TYPE", "兼职");
				}
			}

			map.put("MINI_CLASS_NAME", getName(map.get("MINI_CLASS_NAME")));
			map.put("STUDY_MANEGER", getName(map.get("STUDY_MANEGER")));

			BigDecimal peopleQuantity = (BigDecimal) map.get("PEOPLE_QUANTITY");

			if (peopleQuantity.compareTo(BigDecimal.ZERO)==0){
				map.put("ATTENDENT_RATE", "-");
				map.put("CHARGE_RATE", "-");
			}else {
				BigDecimal attendent = (BigDecimal) map.get("ATTENDENT_QUANTITY");
				BigDecimal late = (BigDecimal) map.get("LATE_QUANTITY");


				BigDecimal result = attendent.add(late).multiply(BigDecimal.valueOf(100)).divide(peopleQuantity, 2, BigDecimal.ROUND_HALF_UP);
				map.put("ATTENDENT_RATE", result+"%");

				BigDecimal charge = (BigDecimal) map.get("CHARGE_QUANTITY");
				BigDecimal result1 = charge.multiply(BigDecimal.valueOf(100)).divide(peopleQuantity, 2, BigDecimal.ROUND_HALF_UP);
				map.put("CHARGE_RATE", result1+"%");

			}

			if (map.get("CHARGE_AMOUNT") == null){
				map.put("CHARGE_AMOUNT", 0);
			}
			if (map.get("REAL_CHARGE_AMOUNT") == null){
				map.put("REAL_CHARGE_AMOUNT", 0);
			}

			list.add(map);
		}

		StringBuffer fileBuffer = new StringBuffer();
		//时间段
		if (StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())&&StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			fileBuffer.append(basicOperationQueryVo.getStartDate()+"-"+basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotEmpty(organizationName)){
			fileBuffer.append("_"+organizationName);
		}
		if (StringUtils.isNotEmpty(teacherName)){
			fileBuffer.append("_"+teacherName);
		}
		if (StringUtils.isNotEmpty(studyManagerName)){
			fileBuffer.append("_"+studyManagerName);
		}
		if ("ankeshi".equals(anshazhesuan)){
			fileBuffer.append("_小班课时消耗");
		}else {
			fileBuffer.append("_小班小时消耗");
		}
		fileBuffer.append("_导出时间"+DateTools.getCurrentDate()+".xls");

		String fileName =fileBuffer.toString() ;
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();
		String[] hearders = null;//表头数组
		String[] heardersId = null;//表头数组

		hearders = new String[]{"集团", "分公司", "课程校区", "老师", "老师所属校区", "全/兼职","小班名称", "教室", "班主任", "班主任归属部门", "年级", "科目", "班级类型", "计划人数", "课程日期","上课时间", "课时/小时","应到人数", "准时上课人数", "迟到人数", "缺勤人数","补课人数","出勤率", "计算工资人数","未扣费人数","扣费人数", "人头结算率","扣费金额","实际扣费金额"};
		heardersId = new String[]{"GROUNP","BRENCH","CAMPUS", "TEACHER", "TEACHER_BLCAMPUS_NAME", "WORK_TYPE", "MINI_CLASS_NAME", "CLASS_ROOM", "STUDY_MANEGER", "STUDY_MANEGER_DEPT_NAME", "GRADE", "SUBJECTS", "CLASS_TYPE_NAME", "PLAN_PEOPLE", "COURSE_DATE", "COURSE_TIME", "COURSE_HOURS", "PEOPLE_QUANTITY", "ATTENDENT_QUANTITY", "LATE_QUANTITY", "ABSENT_QUANTITY", "SUPPLEMENT_QUANTITY", "ATTENDENT_RATE", "SALARY_QUANTITY", "UNCHARGE_QUANTITY", "CHARGE_QUANTITY", "CHARGE_RATE" ,"CHARGE_AMOUNT", "REAL_CHARGE_AMOUNT"};

		try(OutputStream out = response.getOutputStream()){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}

	}

	private String getName(Object object) {
		if (object!=null){
			String str = (String) object;
			if (StringUtil.isBlank(str)){
				return "";
			}
			String[] array = str.split("_");
			if (array.length ==2){
				return array[1];
			}else {
				return "";
			}

		}
		return "";
	}


	/**
	 * 获取小班考勤统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassKaoqinTongji", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassKaoqinTongji(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId,@RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassKaoqinTongji(basicOperationQueryVo,miniClassTypeId, dp,anshazhesuan);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 每天分配课时数
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOsdMoneyArrangeRecord")
	@ResponseBody
	public DataPackageForJqGrid getOsdMoneyArrangeRecord(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOsdMoneyArrangeRecord(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 业绩(校区)
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getContractBonusOrganization", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getContractBonusOrganization(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getContractBonusOrganization(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 业绩(校区)
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getContractBonusStaff", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getContractBonusStaff(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, String flag)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getContractBonusStaff(basicOperationQueryVo, dp, flag);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 *小班产品分析（课程系列）
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassCourseSeries", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassCourseSeries(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassCourseSeries(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	/**
	 * 小班报读情况汇总
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @param catchStudentStatu 招生状态
	 * @param profitStatu 盈利状态
	 * @param lowNormalStudent 招生是否低于最低人数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassMemberTotal", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassMemberTotal(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String catchStudentStatu,String profitStatu,String lowNormalStudent)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassMemberTotal(basicOperationQueryVo, dp, catchStudentStatu, profitStatu,lowNormalStudent);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	/**
	 * 小班报读情况汇总（关键指标)
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassMemberTotalSum", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassMemberTotalSum(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassMemberTotalSum(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	/**
	 * 分公司校区绩效统计
	 * @param gridRequest
	 * @param year
	 * @param goalType
	 * @param targetType
	 * @return
     * @throws Exception
     */
	@RequestMapping(value = "/getPlanManagementTotal", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getPlanManagementTotal(
			@ModelAttribute GridRequest gridRequest,String year,String goalType,String targetType,@RequestParam(defaultValue = "month")String monthOrquarter)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getPlanManagementTotal(year,goalType,targetType, dp, monthOrquarter);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 小班报名人数统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassStudentRealCount", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassStudentRealCount(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String miniClassStatu)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassStudentRealCount(basicOperationQueryVo,miniClassStatu, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 小班报名人数统计（详细）
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassStudentRealCountDetail", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassStudentRealCountDetail(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String miniClassStatu,String Type)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassStudentRealCountDetail(basicOperationQueryVo,miniClassStatu, dp,Type);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 老师小班课时年级分布视图
	 * @param gridRequest
	 * @param courseConsumeTeacherVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniCourseConsumeTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniCourseConsumeTeacherView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo,@RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan)
			throws Exception {
		courseConsumeTeacherVo.setAnshazhesuan(anshazhesuan);//按课时（小时）折算
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniCourseConsumeTeacherView(courseConsumeTeacherVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 老师1对1+小班+一对多课时年级分布视图
	 * @param gridRequest
	 * @param courseConsumeTeacherVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOooMiniCourseConsumeTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOooMiniOtmCourseConsumeTeacherView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo,@RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan)
			throws Exception {
		courseConsumeTeacherVo.setAnshazhesuan(anshazhesuan);//按课时（小时）折算
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOooMiniOtmCourseConsumeTeacherView(courseConsumeTeacherVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/getExcelOooMiniOtmCourseConsumeTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public void getExcelOooMiniOtmCourseConsumeTeacherView(@ModelAttribute GridRequest gridRequest, HttpServletResponse response, @ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);
		dataPackage = reportService.getOooMiniOtmCourseConsumeTeacherView(courseConsumeTeacherVo, dataPackage);

		List<Map<Object, Object>> datas = (List)dataPackage.getDatas();

		List list = new ArrayList<>();

		for (Map<Object, Object> map:datas){
			map.put("GROUNP", getName(map.get("GROUNP")));
			map.put("BRENCH", getName(map.get("BRENCH")));
			map.put("CAMPUS", getName(map.get("CAMPUS")));
			map.put("TEACHER_NAME", getName(map.get("TEACHER_NAME")));
			list.add(map);
		}


		StringBuffer fileBuffer = new StringBuffer();
		//时间段
		if (StringUtils.isNotEmpty(courseConsumeTeacherVo.getStartDate())&&StringUtils.isNotEmpty(courseConsumeTeacherVo.getEndDate())){
			fileBuffer.append(courseConsumeTeacherVo.getStartDate()+"-"+courseConsumeTeacherVo.getEndDate());
		}

		if (StringUtils.isNotEmpty(courseConsumeTeacherVo.getProductQuarterSearchName())){
			fileBuffer.append("_"+courseConsumeTeacherVo.getProductQuarterSearchName());
		}

		if (StringUtils.isNotEmpty(courseConsumeTeacherVo.getBranchName())){
			fileBuffer.append("_"+courseConsumeTeacherVo.getBranchName());
		}

		if (StringUtils.isNotEmpty(courseConsumeTeacherVo.getBlCampusName())){
			fileBuffer.append("_"+courseConsumeTeacherVo.getBlCampusName());
		}

		if (StringUtils.isNotEmpty(courseConsumeTeacherVo.getTeacherTypeName())){
			fileBuffer.append("_"+courseConsumeTeacherVo.getTeacherTypeName());
		}

//		if (StringUtils.isNotEmpty(organizationName)){
//			fileBuffer.append("_"+organizationName);
//		}
//		if (StringUtils.isNotEmpty(teacherName)){
//			fileBuffer.append("_"+teacherName);
//		}
//		if (StringUtils.isNotEmpty(studyManagerName)){
//			fileBuffer.append("_"+studyManagerName);
//		}
		if ("ankeshi".equals(courseConsumeTeacherVo.getAnshazhesuan())){
			fileBuffer.append("_老师课时年级分布");
		}else {
			fileBuffer.append("_老师小时年级分布");
		}
		fileBuffer.append("_导出时间"+DateTools.getCurrentDate()+".xls");

		String fileName =fileBuffer.toString() ;
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();
		String[] hearders = null;//表头数组
		String[] heardersId = null;//表头数组

		hearders = new String[]{"集团", "分公司", "校区", "老师", "工号", "教师类型","教师等级", "课程校区", "全/兼", "总消耗", "一年级", "二年级", "三年级", "四年级", "五年级","六年级", "初一","初二", "初三", "高一", "高二","高三","其他"};
		heardersId = new String[]{"GROUNP","BRENCH","CAMPUS", "TEACHER_NAME", "EMPLOYEE_NO", "teacherTypeName", "teacherLevelName", "COURSE_CAMPUS_ID", "WORK_TYPE", "TOTAL_CONSUME_HOUR", "FIRST_GRADE", "SECOND_GRADE", "THIRD_GRADE", "FOURTH_GRADE", "FIFTH_GRADE", "SIXTH_GRADE", "MIDDLE_SCHOOL_FIRST_GRADE", "MIDDLE_SCHOOL_SECOND_GRADE", "MIDDLE_SCHOOL_THIRD_GRADE", "HIGH_SCHOOL_FIRST_GRADE", "HIGH_SCHOOL_SECOND_GRADE", "HIGH_SCHOOL_THIRD_GRADE", "OTHER_GRADE"};

		try(OutputStream out = response.getOutputStream()){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}

	}
	
	/**
	 * 老师小班人数年级分布（工资）
	 * @param gridRequest
	 * @param courseConsumeTeacherVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniChargedPeopleTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniChargedPeopleTeacherView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		
		dp = reportService.getMiniChargedPeopleTeacherView(
				courseConsumeTeacherVo.getStartDate(),
				courseConsumeTeacherVo.getEndDate(),
				courseConsumeTeacherVo.getOrganizationType(),
				courseConsumeTeacherVo.getBlCampusId(),
				courseConsumeTeacherVo.getProductQuarterSearch(),dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 小班教室使用率
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param searchType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getClassRoomUseRate", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getClassRoomUseRate(
	@ModelAttribute GridRequest gridRequest,String startDate,String endDate, OrganizationType organizationType,String blCampusId,String searchType,String weekDay)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getClassRoomUseRate(startDate, endDate, organizationType, blCampusId, dp,searchType,weekDay);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 一对一卡座使用率
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param searchType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getBoothNumUseRate", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getBoothNumUseRate(
	@ModelAttribute GridRequest gridRequest,String startDate,String endDate, OrganizationType organizationType,String blCampusId,String searchType,String weekDay)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getBoothNumUseRate(startDate, endDate, organizationType, blCampusId, dp,searchType,weekDay);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 小班未报读信息 
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getNotSignMiniProductInfo", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getNotSignMiniProductInfo(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, @ModelAttribute MiniClassProductSearchVo miniClassProductSearchVo) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getNotSignMiniProductInfo(basicOperationQueryVo, miniClassProductSearchVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	
	
	@RequestMapping(value = "/getConsultResourceUse", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getConsultResourceUse(@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			@ModelAttribute GridRequest gridRequest,String entranceIds)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getConsultResourceUse(basicOperationQueryVo, dp,entranceIds);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 小班指标添统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMiniClassIndexAnylize", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMiniClassIndexAnylize(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, @ModelAttribute MiniClassProductSearchVo miniClassProductSearchVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getMiniClassIndexAnylize(basicOperationQueryVo, miniClassProductSearchVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 获取一对多课消统计
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @param otmTypeStr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOtmClassAttendsAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOtmClassAttendsAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String otmTypeStr)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOtmClassAttendsAnalyze(basicOperationQueryVo,otmTypeStr, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 老师一对多课时年级分布视图
	 * @param gridRequest
	 * @param courseConsumeTeacherVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOtmCourseConsumeTeacherView", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOtmCourseConsumeTeacherView(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute CourseConsumeTeacherVo courseConsumeTeacherVo,String otmTypeStr,
			@RequestParam(required = false, defaultValue = "ankeshi") String anshazhesuan)
			throws Exception {
		courseConsumeTeacherVo.setAnshazhesuan(anshazhesuan);//按课时（小时）折算
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOtmCourseConsumeTeacherView(courseConsumeTeacherVo,otmTypeStr, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对多剩余资金
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOtmStudentRemainAnalyze", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOtmStudentRemainAnalyze(
			@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo)
			throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOtmStudentRemainAnalyze(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 财务审批进度表
	 * @param gridRequest
	 * @param stratDate
	 * @param endDate
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/fundsChangeHistoryAuditList")
	@ResponseBody
	public DataPackageForJqGrid fundsChangeHistoryAuditList(@ModelAttribute GridRequest gridRequest,
			String startDate,String endDate,String channel,String orgType,String groupById,String campusId){
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.fundsChangeHistoryAuditList(startDate, endDate, channel, dp,orgType,groupById,campusId);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对一科次统计报表
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param orgType
	 * @param groupById
	 * @return
	 */
	@RequestMapping(value="/courseSubjectList")
	@ResponseBody
	public DataPackageForJqGrid courseSubjectList(@ModelAttribute GridRequest gridRequest,
			String startDate,String endDate,String orgType,String groupById,String workType,String personnelType,
			String brenchId,String blSchool){
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.courseSubjectList(startDate, endDate,orgType,groupById,workType,personnelType,brenchId,blSchool,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对一科次科目分布报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @param workType
	 * @param roleType
	 * @param subject
	 * @return
	 */
	@RequestMapping(value="/getcourseSubjectTimesList")
	@ResponseBody
	public DataPackageForJqGrid getcourseSubjectTimesList(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, String workType, String roleType, String subject) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getcourseSubjectTimesList(basicOperationQueryVo, workType, roleType, subject, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	@RequestMapping(value="/courseConsumeRenJunList")
	@ResponseBody
	public DataPackageForJqGrid courseConsumeRenJunList(@ModelAttribute GridRequest gridRequest,
														@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,String groupById,String brenchId,String blSchool, String workType) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.courseConsumeRenJunList(basicOperationQueryVo,groupById, brenchId, blSchool,workType, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 一对一月结报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getOooMonthlyBalance")
	@ResponseBody
	public DataPackageForJqGrid getOooMonthlyBalance(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getOooMonthlyBalance(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 财务现金流凭证报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getFinanceMonthlyEvidence")
	@ResponseBody
	public DataPackageForJqGrid getFinanceMonthlyEvidence(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getFinanceMonthlyEvidence(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 财务扣费凭证报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getIncomeMonthlyEvidence")
	@ResponseBody
	public DataPackageForJqGrid getIncomeMonthlyEvidence(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getIncomeMonthlyEvidence(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@ResponseBody
	@RequestMapping(value="/getIncomeMonthlyEvidenceToExcel")
	public void getIncomeMonthlyEvidenceToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		List<IncomeMonthlyEvidenceVo> list = reportService.getIncomeMonthlyEvidenceForExcel(basicOperationQueryVo, dataPackage);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<IncomeMonthlyEvidenceVo> exporter = new ExportExcel<IncomeMonthlyEvidenceVo>();
		String[] hearders = new String[] {"集团","分公司","校区","1对1实际收入", "1对1优惠收入", "1对1历史扣费冲销", "1对1总营收", "一对多实际收入", "一对多优惠收入", 
				"一对多历史扣费冲销", "一对多总营收", "小班实际收入", "小班优惠收入", "小班历史扣费冲销", "小班总营收", "目标班实际收入", "目标班优惠收入", "目标班历史扣费冲销",
				"目标班总营收", "讲座实际收入", "讲座优惠收入", "讲座历史扣费冲销", "讲座总营收","其他实际收入", "其他优惠收入", "其他历史扣费冲销", "其他总营收", "划归实际收入",
				"划归优惠收入", "划归历史冲销", "划归总营收", "实际收入汇总", "优惠收入汇总", "历史扣费冲销汇总", "营收收入汇总", "实际营收比"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, list, out);
		}
	 }
	
	/**
	 * 营收凭证报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getIncomeMonthlyCampus")
	@ResponseBody
	public DataPackageForJqGrid getIncomeMonthlyCampus(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getIncomeMonthlyCampus(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@ResponseBody
	@RequestMapping(value="/getIncomeMonthlyCampusToExcel")
	public void getIncomeMonthlyCampusToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());

		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		if(PropertiesUtils.getStringValue("institution")!=null && "advance".equals(PropertiesUtils.getStringValue("institution"))) {
			List<OdsMonthIncomeCampusAdvanceVo> list = HibernateUtils.voListMapping(reportService.getIncomeMonthlyCampusForExcel(basicOperationQueryVo, dataPackage),OdsMonthIncomeCampusAdvanceVo.class);
			ExportExcel<OdsMonthIncomeCampusAdvanceVo> exporter = new ExportExcel<OdsMonthIncomeCampusAdvanceVo>();
			String[] hearders = new String[]{"集团", "分公司", "校区",
					"小班实收资金", "小班优惠资金", "小班实收历史冲销", "小班优惠历史冲销", "小班小计",
					"直播新签实际收入", "直播新签实际收入分成", "直播续费实际收入", "直播续费实际收入分成", "直播营收", "直播营收分成",
					"讲座实收资金", "讲座优惠资金", "讲座实收历史冲销", "讲座优惠历史冲销", "讲座小计", 
					"其他实收资金", "其他优惠资金", "其他实收历史冲销", "其他优惠历史冲销", "其他小计",
					"非经营划归收入", "非经营优惠对销", "非经营历史冲销", "非经营划归小计", 
					"实收资金汇总", "优惠资金汇总", "实收历史冲销汇总", "优惠历史冲销汇总", "历史冲销汇总", "小计汇总", "调整项", "调整后总计",
					"备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, list, out);
			}
		}else{
			List<OdsMonthIncomeCampusVo> list = reportService.getIncomeMonthlyCampusForExcel(basicOperationQueryVo, dataPackage);
			ExportExcel<OdsMonthIncomeCampusVo> exporter = new ExportExcel<OdsMonthIncomeCampusVo>();
			String[] hearders = new String[]{"集团", "分公司", "校区", "1对1实收资金", "1对1优惠资金",
					"1对1实收历史冲销", "1对1优惠历史冲销", "1对1小计", "1对多实收资金", "1对多优惠资金", "1对多实收历史冲销", "1对多优惠历史冲销", "1对多小计",
					"小组课程实收资金", "小组课程优惠资金", "小组课程实收历史冲销", "小组课程优惠历史冲销", "小组课程小计",
					"双师课程实收资金", "双师课程优惠资金", "双师课程实收历史冲销", "双师课程优惠历史冲销", "双师课程小计",
					//"直播实收资金", "直播优惠资金", "直播实收历史冲销", "直播历史历史冲销", "直播小计",
					"直播新签实际收入", "直播新签实际收入分成", "直播续费实际收入", "直播续费实际收入分成", "直播营收", "直播营收分成",
					"目标班实收资金", "目标班优惠资金", "目标班实收历史冲销", "目标班优惠历史冲销", "目标班小计",
					"讲座实收资金", "讲座优惠资金", "讲座实收历史冲销", "讲座优惠历史冲销", "讲座小计", "其他实收资金", "其他优惠资金", "其他实收历史冲销", "其他优惠历史冲销", "其他小计",
					"非经营划归收入", "非经营优惠对销", "非经营历史冲销", "非经营划归小计", "实收资金汇总", "优惠资金汇总", "实收历史冲销汇总", "优惠历史冲销汇总", "历史冲销汇总", "小计汇总", "调整项", "调整后总计",
					"备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, list, out);
			}
		}
	 }
	
	@ResponseBody
	@RequestMapping(value="/getIncomeMonthlyStudentToExcel")
	public void getIncomeMonthlyStudentToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());

		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		if(PropertiesUtils.getStringValue("institution")!=null && "advance".equals(PropertiesUtils.getStringValue("institution"))) {
			List<OdsMonthIncomeStudentAdvanceVo> list = HibernateUtils.voListMapping(reportService.getIncomeMonthlyStudentForExcel(basicOperationQueryVo, dataPackage),OdsMonthIncomeStudentAdvanceVo.class);
			ExportExcel<OdsMonthIncomeStudentAdvanceVo> exporter = new ExportExcel<OdsMonthIncomeStudentAdvanceVo>();
			String[] hearders = new String[] {"集团", "分公司", "校区", "学生",
					"小班实收资金", "小班优惠资金", "小班实收历史冲销", "小班优惠历史冲销", "小班小计",
					"直播新签实际收入", "直播新签实际收入分成", "直播续费实际收入", "直播续费实际收入分成", "直播营收", "直播营收分成",
					"讲座实收资金", "讲座优惠资金", "讲座实收历史冲销", "讲座优惠历史冲销", "讲座小计", 
					"其他实收资金", "其他优惠资金", "其他实收历史冲销", "其他优惠历史冲销", "其他小计",
					"非经营划归收入", "非经营优惠对销", "非经营历史冲销", "非经营划归小计",
					"实收资金汇总", "优惠资金汇总", "实收历史冲销汇总", "优惠历史冲销汇总", "历史冲销汇总", "小计汇总",
					"凭证月份", "凭证日期", "状态"};//表头数组
			try(OutputStream out = response.getOutputStream();){
				exporter.exportExcel(hearders, list, out);
			}
		}else{
			List<OdsMonthIncomeStudentVo> list = reportService.getIncomeMonthlyStudentForExcel(basicOperationQueryVo, dataPackage);
			ExportExcel<OdsMonthIncomeStudentVo> exporter = new ExportExcel<OdsMonthIncomeStudentVo>();
			/*String[] hearders = new String[] {"集团", "分公司", "校区", "学生", "1对1实收资金", "1对1优惠资金",
					"1对1实收历史冲销", "1对1优惠历史冲销", "1对1小计", "1对多实收资金", "1对多优惠资金", "1对多实收历史冲销", "1对多优惠历史冲销", "1对多小计",
					"小组课程实收资金", "小组课程优惠资金", "小组课程实收历史冲销", "小组课程优惠历史冲销", "小组课程小计", "目标班实收资金", "目标班优惠资金", "目标班实收历史冲销", "目标班优惠历史冲销", "目标班小计",
					"讲座实收资金", "讲座优惠资金", "讲座实收历史冲销", "讲座优惠历史冲销", "讲座小计", "其他实收资金", "其他优惠资金", "其他实收历史冲销", "其他优惠历史冲销", "其他小计",
					"非经营划归收入", "非经营优惠对销", "非经营历史冲销", "非经营划归小计", "实收资金汇总", "优惠资金汇总", "实收历史冲销汇总", "优惠历史冲销汇总", "历史冲销汇总", "小计汇总",
					"凭证月份", "凭证日期", "状态"};//表头数组
					*/	
			String[] hearders = new String[]{"集团", "分公司", "校区", "学生", "1对1实收资金", "1对1优惠资金",
					"1对1实收历史冲销", "1对1优惠历史冲销", "1对1小计", "1对多实收资金", "1对多优惠资金", "1对多实收历史冲销", "1对多优惠历史冲销", "1对多小计",
					"小组课程实收资金", "小组课程优惠资金", "小组课程实收历史冲销", "小组课程优惠历史冲销", "小组课程小计",
					"双师课程实收资金", "双师课程优惠资金", "双师课程实收历史冲销", "双师课程优惠历史冲销", "双师课程小计",
					//"直播实收资金", "直播优惠资金", "直播实收历史冲销", "直播历史历史冲销", "直播小计",
					"直播新签实际收入", "直播新签实际收入分成", "直播续费实际收入", "直播续费实际收入分成", "直播营收", "直播营收分成",
					"目标班实收资金", "目标班优惠资金", "目标班实收历史冲销", "目标班优惠历史冲销", "目标班小计",
					"讲座实收资金", "讲座优惠资金", "讲座实收历史冲销", "讲座优惠历史冲销", "讲座小计", "其他实收资金", "其他优惠资金", "其他实收历史冲销", "其他优惠历史冲销", "其他小计",
					"非经营划归收入", "非经营优惠对销", "非经营历史冲销", "非经营划归小计", "实收资金汇总", "优惠资金汇总", "实收历史冲销汇总", "优惠历史冲销汇总", "历史冲销汇总", "小计汇总",
					 "凭证月份", "凭证日期", "状态"};//表头数组
			try(OutputStream out = response.getOutputStream();){
				exporter.exportExcel(hearders, list, out);
			}
		}
	 }
	
	/**
	 * 剩余资金凭证报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getRemainMonthlyEvidence")
	@ResponseBody
	public DataPackageForJqGrid getRemainMonthlyEvidence(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getRemainMonthlyEvidence(basicOperationQueryVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@ResponseBody
	@RequestMapping(value="/getRemainMonthlyEvidenceToExcel")
	public void getRemainMonthlyEvidenceToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		List<OdsMonthRemainAmountCampusVo> list = reportService.getRemainMonthlyEvidenceToExcel(basicOperationQueryVo, dataPackage);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OdsMonthRemainAmountCampusVo> exporter = new ExportExcel<OdsMonthRemainAmountCampusVo>();
		String[] hearders = new String[] {"集团","分公司","校区","期初实收剩余（含电子账户）",
                "期间实际收款", "期间收款冲销", "期间历史收款冲销", "期间电子账户转入", "期间电子账户充值", "期间增加小计", "期间消耗实收", "期间实收划归收入", "期间实收退款",
                "期间电子账户转出", "期间实收冲销", "期间实收划归收入冲销", "期间历史实收补扣", "期间历史实收冲销", "期间历史实收划归收入冲销", "期间消耗小计", "期末实收剩余（含电子账户）", 
                "凭证月份", "凭证日期", "状态"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, list, out);
		}
	 }
	
	@ResponseBody
	@RequestMapping(value="/getRemainMonthlyStudentToExcel")
	public void getRemainMonthlyStudentToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute BasicOperationQueryVo basicOperationQueryVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		List<OdsMonthRemainAmountStudentVo> list = reportService.getRemainMonthlyStudentToExcel(basicOperationQueryVo, dataPackage);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OdsMonthRemainAmountStudentVo> exporter = new ExportExcel<OdsMonthRemainAmountStudentVo>();
		String[] hearders = new String[] {"集团","分公司","校区", "学生", "期初实收剩余（含电子账户）",
                "期间实际收款", "期间收款冲销", "期间历史收款冲销", "期间电子账户转入", "期间电子账户充值", "期间增加小计", "期间消耗实收", "期间实收划归收入", "期间实收退款",
                "期间电子账户转出", "期间实收冲销", "期间实收划归收入冲销", "期间历史实收补扣", "期间历史实收冲销", "期间历史实收划归收入冲销", "期间消耗小计", "期末实收剩余（含电子账户）", 
                "凭证月份", "凭证日期", "状态"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, list, out);
		}
	 }
	
	
	/**
	 * 学生科目状态报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value="/getStudentSubjectStatusReport")
	@ResponseBody
	public DataPackageForJqGrid getStudentSubjectStatusReport(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = reportService.getStudentSubjectStatusReport(basicOperationQueryVo, studentOooStatus, dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 学生科目状态报表到处
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/exportStudentSubjectStatusReport")
	@ResponseBody
	public void exportStudentSubjectStatusReport(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<OdsStudentSubjectStatusVo> list = reportService.exportStudentSubjectStatusReport(basicOperationQueryVo, studentOooStatus);
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OdsStudentSubjectStatusVo> exporter = new ExportExcel<OdsStudentSubjectStatusVo>();
		
		
		String[] hearders = new String[] {"分公司", "校区", "学管师", "学生", "年级", "一对一学生状态", "一对一总剩余课时", "语文(状态)", "语文(剩余课时)", "英语(状态)", "英语(剩余课时)", 
				"物理(状态)", "物理(剩余课时)", "化学(状态)", "化学(剩余课时)", "数学(状态)", "数学(剩余课时)", "生物(状态)", "生物(剩余课时)", "地理(状态)", "地理(剩余课时)", 
				"政治(状态)", "政治(剩余课时)", "历史(状态)", "历史(剩余课时)", "心理(状态)", "心理(剩余课时)", "科学(状态)", "科学(剩余课时)", "其他(状态)", "其他(剩余课时)", 
				"奥数(状态)", "奥数(剩余课时)", "奥英(状态)", "奥英(剩余课时)", "信息技术(状态)", "信息技术(剩余课时)", "成长助力(状态)", "成长助力(剩余课时)", 
				"日语(状态)", "日语(剩余课时)", "奥语(状态)", "奥语(剩余课时)"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, list, out);
		}
	}


	@RequestMapping(value="/getTwoTeacherCourseTotal")
	@ResponseBody
	public DataPackageForJqGrid getTwoTeacherCourseTotal(@ModelAttribute GridRequest gridRequest,ModelVo modelVo){
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage= reportService.getTwoTeacherCourseTotal(modelVo,dataPackage);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
     * 双师主班班次统计excel导出
     * @param modelVo
     * @return
     * @throws IOException 
     */
    @RequestMapping(value="/exportTwoTeacherMainClassesReport")
    @ResponseBody
    public void exportTwoTeacherMainClassesReport(ModelVo modelVo,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TwoTeacherMainClassesExcelVo> list = reportService.exportTwoTeacherMainClassesReport(modelVo);
        String filename = modelVo.getStartDate() + "~" + modelVo.getEndDate() + "主讲老师课时报表汇总.xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        ExportExcel<TwoTeacherMainClassesExcelVo> exporter = new ExportExcel<TwoTeacherMainClassesExcelVo>();
        String[] hearders = new String[] {"集团", "分公司", "主讲老师", "老师所属校区", "1-6个", "7个", "8个", "9个", "10个", "11个", "12个", "13个", "14个", 
                "15个", "16个", "17个", "18个", "19个", "20个"};//表头数组
        try(OutputStream out = response.getOutputStream();){
            exporter.exportExcel(hearders, list, out);
        }
    }
    
    /**
     * 双师辅班次统计excel导出
     * @param modelVo
     * @return
     * @throws IOException 
     */
    @RequestMapping(value="/exportTwoTeacherAuxiliaryClassesReport")
    @ResponseBody
    public void exportTwoTeacherAuxiliaryClassesReport(ModelVo modelVo,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TwoTeacherAuxiliaryClassesExcelVo> list = reportService.exportTwoTeacherAuxiliaryClassesReport(modelVo);
        String filename = modelVo.getStartDate() + "~" + modelVo.getEndDate() + "辅导老师课时报表汇总.xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        ExportExcel<TwoTeacherAuxiliaryClassesExcelVo> exporter = new ExportExcel<TwoTeacherAuxiliaryClassesExcelVo>();
        String[] hearders = new String[] {"集团", "分公司", "辅导老师", "老师所属校区", "1人", "2人", "3人", "4人", "5人", "6人", "7人", "8人", 
                "9人", "10人", "11人", "12人", "13人", "14人", "15人", "16人", "17人", "18人", "19人", "20人", "21人", "22人", "23人", "24人", "25人",};//表头数组
        try(OutputStream out = response.getOutputStream();){
            exporter.exportExcel(hearders, list, out);
        }
    }
    
    
    
    /**
     * 条件查询  小班满班率  老师维度和班级维度  
     */
    @RequestMapping(value="/getMiniClassFullClassRate")
    @ResponseBody
    public DataPackageForJqGrid getMiniClassFullClassRate(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClassFullRateVo miniClassRateVo,String type){
		DataPackage dataPackage=new DataPackage(gridRequest);
		
		if(type.equals("teacher")){
			//老师维度
			dataPackage = reportService.getMiniClassFullClassTeacherRate(dataPackage, miniClassRateVo);		
		}else{
			//班级维度
			dataPackage = reportService.getMiniClassFullClassClassRate(dataPackage, miniClassRateVo);		
			
		}
			
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
    }
    
    
    /**
     * 条件查询  小班退班率  老师维度和班级维度  
     */
    @RequestMapping(value="/getMiniClassQuitClassRate")
    @ResponseBody
    public DataPackageForJqGrid getMiniClassQuitClassRate(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClassQuitRateVo miniClassQuitRateVo,String type){
		DataPackage dataPackage=new DataPackage(gridRequest);
		
		if(type.equals("teacher")){
			//老师维度
			dataPackage = reportService.getMiniClassQuitClassTeacherRate(dataPackage, miniClassQuitRateVo);		
		}else{
			//产品线维度
			dataPackage = reportService.getMiniClassQuitClassProductRate(dataPackage, miniClassQuitRateVo);
			
		}
			
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
    }
    
	 @ResponseBody
	 @RequestMapping(value="/getMiniClassFullClassRateSize")
	 public Response getMiniClassFullClassRateSize(@ModelAttribute MiniClassFullRateVo miniClassRateVo,String type) {
		List<Map<Object, Object>> list = null;
		if(type.equals("teacher")){
			list = reportService.getMiniClassFullClassTeacherRateSize(miniClassRateVo);
		}else{
			list = reportService.getMiniClassFullClassClassRateSize(miniClassRateVo);
		}
	
		Response resp = new Response();
	    if(list==null || list.size()==0){
	    	resp.setResultCode(-1);    	
	    }
	    return resp;
	 }
	 
	 @ResponseBody
	 @RequestMapping(value="/getMiniClassQuitClassRateSize")
	 public Response getMiniClassFullClassRateSize(@ModelAttribute MiniClassQuitRateVo miniClassQuitRateVo,String type){
		
		List<Map<Object, Object>> list = null;
		if(type.equals("teacher")){
			list = reportService.getMiniClassQuitClassTeacherRateSize(miniClassQuitRateVo);
		}else{
			list = reportService.getMiniClassQuitClassProductRateSize(miniClassQuitRateVo);
		}
		
		Response resp = new Response();
	    if(list==null || list.size()==0){
	    	resp.setResultCode(-1);    	
	    }
	    return resp;
	 }
    
    /**
     * 分别的满班率和退班率导出Excel
     */		
	@ResponseBody
	@RequestMapping(value = "/getMCFullClassRateToExcel")
	public void getMCFullClassRateToExcel(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute MiniClassFullRateVo miniClassRateVo, String type, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());

		List<MiniClassFullRateTeacherExcelVo> fullRateTeacherExcelVos = null;
		List<MiniClassFullRateClassExcelVo> fullRateClassExcelVos = null;
		long time1 = System.currentTimeMillis();
		if (type.equals("teacher")) {
			fullRateTeacherExcelVos = reportService.getMCFullClassTeacheerRateToExcel(dataPackage, miniClassRateVo);
		} else {
			fullRateClassExcelVos = reportService.getMCFullClassClassRateToExcel(dataPackage, miniClassRateVo);
		}
		long time2 = System.currentTimeMillis();
		log.info("导出excel查询耗时:" + (time2 - time1) + "ms");
		// 导出的excel
		// 导出明细excel文件命名规则：年份+季度+分公司+校区（如有）+年级（如有）+科目（如有）+班级类型（如有）+老师姓名（如有）+满班率_导出时间yyyymmdd

		StringBuffer fileBuffer = new StringBuffer();

		// 年份
		if (StringUtil.isNotBlank(miniClassRateVo.getProductVersionName())) {
			fileBuffer.append(miniClassRateVo.getProductVersionName());
		}
		// 季度
		if (StringUtil.isNotBlank(miniClassRateVo.getProductQuarterName())) {
			fileBuffer.append(miniClassRateVo.getProductQuarterName());
		}
		// 分公司
		if (StringUtil.isNotBlank(miniClassRateVo.getBlBrenchName())) {
			fileBuffer.append(miniClassRateVo.getBlBrenchName());
		}
		// 校区
		if (StringUtil.isNotBlank(miniClassRateVo.getBlCampusName())) {
			fileBuffer.append(miniClassRateVo.getBlCampusName());
		}

		// 年级
		if (StringUtil.isNotEmpty(miniClassRateVo.getGradeName())) {
			fileBuffer.append(miniClassRateVo.getGradeName());
		}
		// 科目
		if (StringUtil.isNotBlank(miniClassRateVo.getSubjectName())) {
			fileBuffer.append(miniClassRateVo.getSubjectName());
		}
		// 班级类型
		if (StringUtil.isNotEmpty(miniClassRateVo.getClassTypeName())) {
			fileBuffer.append(miniClassRateVo.getClassTypeName());
		}
		// 老师
		if (StringUtil.isNotEmpty(miniClassRateVo.getTeacherName())) {
			fileBuffer.append(miniClassRateVo.getTeacherName());
		}
		String currentDate = DateTools.getCurrentDate();
		fileBuffer.append("满班率_导出时间").append(currentDate.replaceAll("-", "")).append(".xls");
		String fileName = fileBuffer.toString();

		response.setContentType("application/ms-excel;charset=UTF-8");
		// 设置Excel文件名字
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		if (type.equals("teacher")) {
			ExportExcel<MiniClassFullRateTeacherExcelVo> exporter = new ExportExcel<MiniClassFullRateTeacherExcelVo>();
			String[] hearders = new String[] { "年份", "季度", "分公司", "校区", "年级", "科目", "班型", "老师姓名", "已缴全款学生数", "计划招生学生数",
					"满班率" };// 表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, fullRateTeacherExcelVos, out);
			}
		} else {
			ExportExcel<MiniClassFullRateClassExcelVo> exporter = new ExportExcel<MiniClassFullRateClassExcelVo>();
			String[] hearders = new String[] { "年份", "季度", "分公司", "校区", "小班名称", "年级", "科目", "班型", "老师姓名", "已缴全款学生数",
					"计划招生学生数", "满班率" };// 表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, fullRateClassExcelVos, out);
			}
		}
		
		
		

	}
    
	@ResponseBody
	@RequestMapping(value = "/getMCQuitClassRateToExcel")
	public void getMCQuitClassRateToExcel(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute MiniClassQuitRateVo miniClassQuitRateVo, String type, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());

		List<MiniClassQuitRateTeacherExcelVo> quitRateTeacherExcelVos = null;
		List<MiniClassQuitRateProductExcelVo> quitRateProductExcelVos = null;
		long time1 = System.currentTimeMillis();
		if (type.equals("teacher")) {
			quitRateTeacherExcelVos = reportService.getMCQuitClassTeacheerRateToExcel(dataPackage, miniClassQuitRateVo);
		} else {
			quitRateProductExcelVos = reportService.getMCQuitClassProductRateToExcel(dataPackage, miniClassQuitRateVo);
		}
		long time2 = System.currentTimeMillis();
		log.info("导出excel查询耗时:" + (time2 - time1) + "ms");
		// 导出的excel
		// 导出明细excel文件命名规则：年份+季度+分公司+校区（如有）+年级（如有）+科目（如有）+班级类型（如有）+老师姓名（如有）+满班率_导出时间yyyymmdd

		StringBuffer fileBuffer = new StringBuffer();

		// 年份
		if (StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersionName())) {
			fileBuffer.append(miniClassQuitRateVo.getProductVersionName());
		}
		// 季度
		if (StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarterName())) {
			fileBuffer.append(miniClassQuitRateVo.getProductQuarterName());
		}
		// 分公司
		if (StringUtil.isNotBlank(miniClassQuitRateVo.getBlBrenchName())) {
			fileBuffer.append(miniClassQuitRateVo.getBlBrenchName());
		}
		// 校区
		if (StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusName())) {
			fileBuffer.append(miniClassQuitRateVo.getBlCampusName());
		}

		// 年级
		if (StringUtil.isNotEmpty(miniClassQuitRateVo.getGradeName())) {
			fileBuffer.append(miniClassQuitRateVo.getGradeName());
		}
		// 科目
		if (StringUtil.isNotBlank(miniClassQuitRateVo.getSubjectName())) {
			fileBuffer.append(miniClassQuitRateVo.getSubjectName());
		}
		// 班级类型
		if (StringUtil.isNotEmpty(miniClassQuitRateVo.getClassTypeName())) {
			fileBuffer.append(miniClassQuitRateVo.getClassTypeName());
		}
		// 老师
		if (StringUtil.isNotEmpty(miniClassQuitRateVo.getTeacherName())) {
			fileBuffer.append(miniClassQuitRateVo.getTeacherName());
		}
		String currentDate = DateTools.getCurrentDate();
		fileBuffer.append("满班率_导出时间").append(currentDate.replaceAll("-", "")).append(".xls");
		String fileName = fileBuffer.toString();

		response.setContentType("application/ms-excel;charset=UTF-8");
		// 设置Excel文件名字
		response.setHeader("Content-Disposition",
				"attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		if (type.equals("teacher")) {
			ExportExcel<MiniClassQuitRateTeacherExcelVo> exporter = new ExportExcel<MiniClassQuitRateTeacherExcelVo>();
			String[] hearders = new String[] { "年份", "季度", "分公司", "老师姓名", "已缴全款学生数", "无课消退费学生数",
					"课前退费率","已课消学生数","有课消退费学生数","课后退费率" };// 表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, quitRateTeacherExcelVos, out);
			}
		} else {
			ExportExcel<MiniClassQuitRateProductExcelVo> exporter = new ExportExcel<MiniClassQuitRateProductExcelVo>();
			String[] hearders = new String[] { "年份", "季度", "分公司", "所属校区","年级","科目","已缴全款学生数", "无课消退费学生数",
					"课前退费率","已课消学生数","有课消退费学生数","课后退费率" };// 表头数组
			try (OutputStream out = response.getOutputStream();) {
				exporter.exportExcel(hearders, quitRateProductExcelVos, out);
			}
		}	
	}
	
	/**
	 * 按pos机类型获取收款笔数
	 * @param reqVo
	 * @return
	 */
	@RequestMapping(value = "/getFundsCountGroupByPosType")
	@ResponseBody
	public List<Map<Object, Object>> getFundsCountGroupByPosType(@Valid OrganizationDateReqVo reqVo) {
	    return reportService.getFundsCountGroupByPosType(reqVo);
	}
	
	/**
     * 按pos机类型获取收款金额
     * @param reqVo
     * @return
     */
    @RequestMapping(value = "/getFundsAmountGroupByPosType")
    @ResponseBody
    public List<Map<Object, Object>> getFundsAmountGroupByPosType(@Valid OrganizationDateReqVo reqVo) {
        return reportService.getFundsAmountGroupByPosType(reqVo);
    }
	
}
