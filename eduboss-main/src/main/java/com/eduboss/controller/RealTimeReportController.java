package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.CourseInfoExcelVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.dto.CourseTimeMonitorSearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.service.RealTimeReportService;
import com.eduboss.service.UserService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/RealTimeReportController")
public class RealTimeReportController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RealTimeReportService realTimeReportService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	/**
	 * 市场来源分析查找表单
	 * @param request
	 * @param gridRequest
	 * @param summaryLogVo
	 * @return
	 */
	@RequestMapping(value = "/getMarkeyAnalysisList")
	@ResponseBody
	public DataPackageForJqGrid getMarkeyAnalysisList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}else{
			organizationId = userService.getCurrentLoginUserOrganization().getId();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
	
		dataPackage = realTimeReportService.getMarkeyAnalysisList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	
	/**
	 * 市场类型分析查找表单
	 * @param request
	 * @param gridRequest
	 * @param summaryLogVo
	 * @return
	 */
	@RequestMapping(value = "/getMarkeyTypeAnalysisList")
	@ResponseBody
	public DataPackageForJqGrid getMarkeyTypeAnalysisList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
	
		dataPackage = realTimeReportService.getMarkeyTypeAnalysisList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 来电量总数
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	@RequestMapping(value = "/getCallList")
	@ResponseBody
	public DataPackageForJqGrid getCallList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
	
		dataPackage = realTimeReportService.getCallList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}

	/**
	 * 学生年级分布
	 * @param request
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getStudentDistributionList")
	@ResponseBody
	public DataPackageForJqGrid getStudentDistributionList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
//		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
//			startDate =request.getParameter("startDate");
//		}else{
//			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
//			return dataPackageForJqGrid;
//		}
//		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
//			endDate = request.getParameter("endDate");
//		}else{
//			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
//			return dataPackageForJqGrid;
//		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
	
		dataPackage = realTimeReportService.getStudentDistributionList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	/**
	 * 学生学校分布 2015-04-13
	 * @author Yao
	 * @param request
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getStudentSchoolDistributionList")
	@ResponseBody
	public DataPackageForJqGrid getStudentSchoolDistributionList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
		dataPackage = realTimeReportService.getStudentSchoolDistributionList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	/**
	 * 获取每天的来电量
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @return
	 */
	@RequestMapping(value = "/getCallingEveryDayDatas")
	@ResponseBody
	public List<Map<Object, Object>> getCallingEveryDayDatas(String startDate,String endDate,String organizationId,String organizationName){
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
		params.put("organizationName", organizationName);
	
		List<Map<Object, Object>>  dataList = realTimeReportService.getCallEveryDayList(params);
		return dataList;
	}	
	/**
	 * 每天新增学生
	 * @param request
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getStudentAddEveryDayList")
	@ResponseBody
	public DataPackageForJqGrid getStudentAddEveryDayList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}else{
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
	
		dataPackage = realTimeReportService.getStudentAddEveryDayList(dataPackage, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 单独机构，每天学生新增
	 * @param request
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getStudentAddEveryDayDatas")
	@ResponseBody
	public List<Map<Object, Object>> getStudentAddEveryDayDatas(String startDate,String endDate,String organizationId,String organizationName){
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationId", organizationId);
		params.put("organizationName", organizationName);
	
		List<Map<Object, Object>>  dataList = realTimeReportService.getStudentAddEveryDayDatas(params);
		return dataList;
	}

    /**
     * 一对一课时汇总
     * @param request
     * @return
     */
    @RequestMapping(value = "/totalOneOnOneCourse")
    @ResponseBody
    public List<Map<Object, Object>> totalOneOnOneCourse(HttpServletRequest request){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("startDate", request.getParameter("startDate"));
        params.put("endDate", request.getParameter("endDate"));
        params.put("organizationId", request.getParameter("organizationIdFinder"));
        params.put("queryConflict", request.getParameter("queryConflict"));
        List<Map<Object, Object>>  dataList = realTimeReportService.totalOneOnOneCourse(params);
        return dataList;
    }
    
    /**
     * 一对一课程汇总已排课详情
     * @param gridRequest
     * @return
     */
    @RequestMapping(value = "/getoneOneOneCourseInfo")
	@ResponseBody
    public DataPackageForJqGrid getoneOneOneCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate
    		,@ModelAttribute CourseVo courseVo){
    	DataPackage dp = new DataPackage(gridRequest);
    	dp=realTimeReportService.getOneOneOneCourseInfo(dp, startDate, endDate, courseVo);
    	DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
    }
    
    /**
     * 导出一对一课程汇总已排课详情
     * @param gridRequest
     * @return
     */
    @RequestMapping(value = "/excelOneOneCourseInfo")
	@ResponseBody
    public void excelOneOneCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate
    		,@ModelAttribute CourseVo courseVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
    	
    	DataPackage dp = new DataPackage(gridRequest);    	
    	dp.setPageSize(gridRequest.getNumOfRecordsLimitation());
    	dp=realTimeReportService.getOneOneOneCourseInfo(dp, startDate, endDate, courseVo);
				
    	String filename = "一对一查勤表.xls";
    	String orgName="";
		if(StringUtils.isNotBlank(courseVo.getCampusId())){
			Organization campus=organizationDao.findById(courseVo.getCampusId());
			if (campus!=null){
				orgName=campus.getName();
				filename = campus.getName()+"一对一查勤表.xls";
			}
		}	
    	if(StringUtil.isNotBlank(courseVo.getCourseDate())){
    		filename = orgName+courseVo.getCourseDate()+"一对一查勤表.xls";
    	}else if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
    		filename = orgName+startDate+"至"+endDate+"一对一查勤表.xls";
    	}		
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<CourseInfoExcelVo> exporter = new ExportExcel<CourseInfoExcelVo>();
		String[] hearders = new String[] {"上课日期","上课时间","学管师","学生","年级","科目","上课星期","计划课时","老师","实际查勤","查勤人"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			List<CourseInfoExcelVo> datas = HibernateUtils.voListMapping((List)dp.getDatas(), CourseInfoExcelVo.class);
			exporter.exportExcel(hearders, datas, out, 1);
		}
    }
    
    
	@RequestMapping(value = "/oneOnOneCourse")
	@ResponseBody
	public DataPackageForJqGrid  oneOnOneCourse(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("courseStatus", request.getParameter("courseStatus"));
		dp = realTimeReportService.courseList(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/miniCourse")
	@ResponseBody
	public DataPackageForJqGrid  miniCourse(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("courseStatus", request.getParameter("courseStatus"));
		params.put("miniclasstypes", request.getParameter("miniclasstypes"));
		dp = realTimeReportService.miniCourse(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@RequestMapping(value = "/twoTeacherCourse")
	@ResponseBody
	public DataPackageForJqGrid  twoTeacherCourse(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("courseStatus", request.getParameter("courseStatus"));
		params.put("twoTeacherclasstypes", request.getParameter("twoTeacherclasstypes"));
		dp = realTimeReportService.twoTeacherCourse(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}


	@RequestMapping(value = "/otmCourse")
	@ResponseBody
	public DataPackageForJqGrid  otmCourse(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("courseStatus", request.getParameter("courseStatus"));
		params.put("otmclasstypes", request.getParameter("otmclasstypes"));
		dp = realTimeReportService.otmCourse(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}

	@RequestMapping(value = "/otmStudent")
	@ResponseBody
	public DataPackageForJqGrid otmStudent(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("studentStatus", request.getParameter("studentStatus"));
		params.put("otmclasstypes", request.getParameter("otmclasstypes"));
		dp = realTimeReportService.otmStudent(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	@RequestMapping(value = "/miniCourseStudentByStatus")
	@ResponseBody
	public DataPackageForJqGrid miniCourseStudentByStatus(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("studentStatus", request.getParameter("studentStatus"));
		params.put("miniclasstypes", request.getParameter("miniclasstypes"));
		dp = realTimeReportService.miniCourseStudentByStatus(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
    
	@RequestMapping(value = "/twoCourseStudentByStatus")
	@ResponseBody
	public DataPackageForJqGrid twoCourseStudentByStatus(@ModelAttribute GridRequest gridRequest,HttpServletRequest request){
		DataPackage dp = new DataPackage(gridRequest);
		Map<String,Object> params = new HashMap<>();
		params.put("startDate",request.getParameter("startDate"));
		params.put("endDate",request.getParameter("endDate"));
		params.put("organizationId", request.getParameter("organizationId"));
		params.put("studentStatus", request.getParameter("studentStatus"));
		params.put("twoclasstypes", request.getParameter("twoclasstypes"));
		dp = realTimeReportService.twoCourseStudentByStatus(params,dp);
		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
    /**
	 * 学管课时监控
	 * @param gridRequest
	 * @param courseTimeMonitorSearchInputVo
	 * @return
	 */
	@RequestMapping(value = "/getMonthlyCourseTimeMonitor")
	@ResponseBody
	public DataPackageForJqGrid getMonthlyCourseTimeMonitor(@ModelAttribute GridRequest gridRequest, @ModelAttribute CourseTimeMonitorSearchInputVo courseTimeMonitorSearchInputVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getMonthlyCourseTimeMonitor(dataPackage, courseTimeMonitorSearchInputVo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 产品消耗分析
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @return
	 */
	@RequestMapping(value = "/getProductConsumeAnalyze")
	@ResponseBody
	public DataPackageForJqGrid getProductConsumeAnalyze(@ModelAttribute GridRequest gridRequest, String startDate,String endDate,String organizationId){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getProductConsumeAnalyze(dataPackage, startDate, endDate, organizationId);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 产品销售分析
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @return
	 */
	@RequestMapping(value = "/getProductMarketAnalyze")
	@ResponseBody
	public DataPackageForJqGrid getProductMarketAnalyze(@ModelAttribute GridRequest gridRequest, String startDate,String endDate,String organizationId){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getProductMarketAnalyze(dataPackage, startDate, endDate, organizationId);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 产品销售分析(科目) 小班
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
	@RequestMapping(value = "/getProductMarketSubjectAnalyze")
	@ResponseBody
	public DataPackageForJqGrid getProductMarketSubjectAnalyze(@ModelAttribute GridRequest gridRequest, 
			String startDate,String endDate,String organizationId, String productType){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getProductMarketSubjectAnalyze(dataPackage, startDate, endDate, organizationId, productType);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 产品销售分析(科目) 一对一
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
	@RequestMapping(value = "/getProductMarketSubOneAnalyze")
	@ResponseBody
	public DataPackageForJqGrid getProductMarketSubjectOneOnOneAnalyze(@ModelAttribute GridRequest gridRequest, 
			String startDate,String endDate,String organizationId, String productType){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getProductMarketSubjectOneOnOneAnalyze(dataPackage, startDate, endDate, organizationId, productType);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 产品销售分析(年级)
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
	@RequestMapping(value = "/getProductMarketGradeAnalyze")
	@ResponseBody
	public DataPackageForJqGrid getProductMarketGradeAnalyze(@ModelAttribute GridRequest gridRequest, 
			String startDate,String endDate,String organizationId, String productType){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.getProductMarketGradeAnalyze(dataPackage, startDate, endDate, organizationId, productType);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;

	}
	
	/**
	 * 网络资源利用情况
	 */
	@RequestMapping(value="/internetUseConditionList")
	@ResponseBody
	public DataPackageForJqGrid internetUseConditionList(@ModelAttribute GridRequest gridRequest, 
			String year,String month,String brenchId,String levelType,String followUserId){
		if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			brenchId=null;
		}
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.internetUseConditionList(dataPackage, year, month,brenchId,levelType,followUserId);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
	 * TMK资源利用情况
	 */
	@RequestMapping(value="/tmkUseConditionList")
	@ResponseBody
	public DataPackageForJqGrid tmkUseConditionList(@ModelAttribute GridRequest gridRequest, 
			String startDate,String endDate,String levelType,String brenchId){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.tmkUseConditionList(dataPackage, startDate, endDate,levelType,brenchId);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	//根据留电量，未跟进，签单量，查看对应的客户详情
	@RequestMapping(value="/resourceUseConditionInfo")
	@ResponseBody
	public DataPackageForJqGrid resourceUseConditionInfo(@ModelAttribute GridRequest gridRequest,
			@RequestParam String followUserId,String year,String month,String numType,String brenchId){		
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = realTimeReportService.resourceUseConditionInfo(dataPackage, followUserId, year, month, numType,brenchId);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	
}
