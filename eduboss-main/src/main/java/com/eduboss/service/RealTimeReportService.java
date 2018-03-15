package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domainVo.CampusIncomeVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.OneCampusIncomeVo;
import com.eduboss.dto.CourseTimeMonitorSearchInputVo;
import com.eduboss.dto.DataPackage;

public interface RealTimeReportService {

	/**
	 * 市场来源分析
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getMarkeyAnalysisList(DataPackage dataPackage,
			Map<String, Object> params);

	/**
	 * 市场类型分析
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getMarkeyTypeAnalysisList(DataPackage dataPackage,
			Map<String, Object> params);


	/**
	 * 来电量总数
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getCallList(DataPackage dataPackage, Map<String, Object> params);

	/**
	 * 学生年级分布
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getStudentDistributionList(DataPackage dataPackage,
			Map<String, Object> params);

	/**
	 * 学生学校分布
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getStudentSchoolDistributionList(DataPackage dataPackage,
			Map<String, Object> params);
	/**
	 * 获取每天的来电量
	 * @param params
	 * @return
	 */
	List<Map<Object, Object>> getCallEveryDayList(Map<String, Object> params);

	/**
	 * 每天新增学生
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	DataPackage getStudentAddEveryDayList(DataPackage dataPackage,
			Map<String, Object> params);

	/**
	 * 单独机构，每天学生新增
	 * @param params
	 * @return
	 */
	List<Map<Object, Object>> getStudentAddEveryDayDatas(
			Map<String, Object> params);

	/**
	 * 得到分校的收入
	 * @param params
	 * @return
	 */
	CampusIncomeVo getCampusIncome(Map<String, Object> params);
	
	/**
	 * 得到校区的收入新增学生以及课消
	 * @param params
	 * @return
	 */
	OneCampusIncomeVo getOneCampusTotalIncome(Map<String, Object> params);

    /**
     * 一对一课时汇总
     * @param params
     * @return
     */
    public List<Map<Object, Object>> totalOneOnOneCourse(Map<String, Object> params);
    
    /**
     * 一对一课程汇总已排课详情
     */
    public DataPackage getOneOneOneCourseInfo(DataPackage dp,String startDate, String endDate, CourseVo courseVo);
    
    /**
	 * 学管课时监控
	 * @param gridRequest
	 * @param courseTimeMonitorSearchInputVo
	 * @return
	 */
    DataPackage getMonthlyCourseTimeMonitor(DataPackage dataPackage, CourseTimeMonitorSearchInputVo courseTimeMonitorSearchInputVo);
    
    /**
	 * 产品消耗分析
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @return
	 */
    DataPackage getProductConsumeAnalyze(DataPackage dp, String startDate,String endDate,String organizationId);
    
    /**
	 * 产品销售分析
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @return
	 */
    DataPackage getProductMarketAnalyze(DataPackage dp, String startDate,String endDate,String organizationId);
    
    /**
	 * 产品销售分析(科目) 小班
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
    DataPackage getProductMarketSubjectAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType);
    
    /**
   	 * 产品销售分析(科目) 一对一
   	 * @param gridRequest
   	 * @param startDate
   	 * @param endDate
   	 * @param organizationId
   	 * @param productType
   	 * @return
   	 */
    DataPackage getProductMarketSubjectOneOnOneAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType);
       
    /**
	 * 产品销售分析(年级)
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
    DataPackage getProductMarketGradeAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType);
    
    /**
     * 网络资源利用情况
     * @param dp
     * @param startDate
     * @param endDate
     * @return
     */
    public DataPackage internetUseConditionList(DataPackage dp,String year,String month,String brenchId,String levelType,String followUserId);
    
    /**
     * TMK资源利用情况
     * @param dp
     * @param startDate
     * @param endDate
     * @return
     */
    public DataPackage tmkUseConditionList(DataPackage dp,String startDate,String endDate,String levelType,String brenchId);
    
    /**
     * 资源利用情况看客户详情
     * @param followUserId
     * @param startDate
     * @param endDate
     * @param numType
     * @return
     */
    public DataPackage resourceUseConditionInfo(DataPackage dataPackage,String followUserId,String year,String month,String numType,String brenchId);

	/**
	 *四种状态的课程列表"weishangke":未上课
	 * "yikaoqin": 已考勤
	 * "yiqueren":  已确认
	 * "unaudit": 未审
	 * @param params
	 * @return
     */
	DataPackage courseList(Map<String, Object> params ,DataPackage dp);

	/**
	 * 三种状态的小班课程列表"weishangke":未上课
	 * "yikaoqin": 已考勤
	 * "unaudit": 未审
	 * @param params
	 * @param dp
     * @return
     */
	DataPackage miniCourse(Map<String, Object> params, DataPackage dp);
	
	/**
	 * 三种状态的双师课程列表"weishangke":未上课
	 * "yikaoqin": 已考勤
	 * "unaudit": 未审
	 * @param params
	 * @param dp
     * @return
     */
	DataPackage twoTeacherCourse(Map<String, Object> params, DataPackage dp);

	/**
	 * 三种状态的小班课程列表"weishangke":未上课
	 * "yikaoqin": 已考勤
	 * "unaudit": 未审
	 * @param params
	 * @param dp
	 * @return
	 */
	DataPackage otmCourse(Map<String, Object> params, DataPackage dp);

	/**
	 *  "studentStatus"
	 *   "yingdaoStudent" 应到学生数
	 *
	 * @param params
	 * @param dp
     * @return
     */
	DataPackage otmStudent(Map<String, Object> params, DataPackage dp);

	DataPackage miniCourseStudentByStatus(Map<String, Object> params, DataPackage dataPackage);
	
	DataPackage twoCourseStudentByStatus(Map<String, Object> params, DataPackage dataPackage);
}
