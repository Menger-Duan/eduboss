package com.eduboss.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.ReflushReportSubject;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.SummaryCycleType;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CourseConsumeTeacherVo;
import com.eduboss.domainVo.IncomeMonthlyEvidenceVo;
import com.eduboss.domainVo.MiniClassFullRateClassExcelVo;
import com.eduboss.domainVo.MiniClassFullRateTeacherExcelVo;
import com.eduboss.domainVo.MiniClassFullRateVo;
import com.eduboss.domainVo.MiniClassQuitRateProductExcelVo;
import com.eduboss.domainVo.MiniClassQuitRateTeacherExcelVo;
import com.eduboss.domainVo.MiniClassQuitRateVo;
import com.eduboss.domainVo.OdsMonthIncomeCampusVo;
import com.eduboss.domainVo.OdsMonthIncomeStudentVo;
import com.eduboss.domainVo.OdsMonthRemainAmountCampusVo;
import com.eduboss.domainVo.OdsMonthRemainAmountStudentVo;
import com.eduboss.domainVo.OdsStudentSubjectStatusVo;
import com.eduboss.domainVo.ReportMiniClassSurplusMoneyVo;
import com.eduboss.domainVo.ReportStudentSurplusFundingVo;
import com.eduboss.domainVo.TwoTeacherAuxiliaryClassesExcelVo;
import com.eduboss.domainVo.TwoTeacherMainClassesExcelVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.OrganizationDateReqVo;


public interface ReportService {

	public List getTeacherCourseGrade(String teacherId,String startDate,String endDate);

	public  HSSFWorkbook dowlOperatRpForStuMan(String beiginDate,String endDate,Map<String,Object>paramMap);


	DataPackage getCampusEmployeesRpData(String beiginDate,String endDate,String campusId);


	DataPackage getGradeProportionRpData(@RequestParam String startDate,@RequestParam String endDate,@RequestParam String campusId);


	/**
	 * 获取校区总结表excel数据
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws IOException
	 */
	public HSSFWorkbook getExcelDataTypeOfCampusAndCycleOfMonthOrQuarterOrYear(SummaryCycleType summaryCycleType,
																			   String startDate, String endDate) throws IOException;

	/**
	 * 根据sql查询
	 * @param sql
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getReportDataBySql(String sql, DataPackage dataPackage);


	/**
	 *  一对一课消老师视图
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneCourseConsumeTeacherView(CourseConsumeTeacherVo vo, DataPackage dp);

	/**
	 *  一对一课消老师视图（学生维度）
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneCourseConsumeStudentView(String startDate,
														   String endDate, OrganizationType organizationType,
														   String blCampusId, DataPackage dp);


	/**
	 * 小班剩余资金
	 * @param reportMiniClassSurplusMoneyVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassSurplusMoney(ReportMiniClassSurplusMoneyVo reportMiniClassSurplusMoneyVo, DataPackage dp);

	/**
	 * 学生剩余资金
	 */
	public DataPackage getReportStudentSurplusFunding(ReportStudentSurplusFundingVo reportStudentSurplusFundingVo, DataPackage dp);

	/**
	 *  小班学生人数
	 * @param countDate
	 * @param string
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentCount(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);



	/**
	 * 小班课消统计
	 * @param countDate
	 * @param endDate
	 * @param basicOperationQueryLevelType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassConsumeAnalyze(String startDate,
												  String endDate, BasicOperationQueryLevelType basicOperationQueryLevelType,
												  String blCampusId, DataPackage dp);


	/**
	 * 现金流
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getFinanceAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 现金流（合同）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getFinanceContractAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	public DataPackage getFinanceContractAnalyzeRate(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 一对一学生统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneStudentCountAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 一对一剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneStudentRemainAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 课消
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseConsomeAnalyse(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);


	/**
	 * 刷新报表数据
	 * @param procedureName
	 * @throws SQLException
	 */
	public void reflushReportData(ReflushReportSubject procedureName, String targetDay, String mappingDate, String orgId) throws SQLException;
	/**
	 * 学生待收款资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentPendingMoney(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 购买课时数
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getPayClassHour(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 购买课时数(校区维度)
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getPayClassHourCampus(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 购买课时数(每天)
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getPayClassHourEveryDay(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);

	/**
	 * 退费
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getRefundAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 收入
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomingAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 营收列表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomingAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 小班产品统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassPeopleAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 小班扣费统计
	 * @param basicOperationQueryVo
	 * @param miniClassTypeId
	 * @param dp
	 * @param anshazhesuan
     * @return
     */
	public DataPackage getMiniClassKoufeiTongji(BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId, DataPackage dp,String anshazhesuan);

	/**
	 * 每天分配课时数
	 */
	public DataPackage getOsdMoneyArrangeRecord(BasicOperationQueryVo basicOperationQueryVo,
												DataPackage dp);


	public DataPackage getContractBonusOrganization(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	public DataPackage getContractBonusStaff(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, String flag);

	public Map<String,Integer> getMiniClassEnrollmentAnlysis(String miniClassId);

	public Map<String,Integer> getMiniClassFirstSchoolTimeStatistics(String miniClassId, String startDate, String endDate);


	/**
	 * 手机现金流
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getFinanceAnalyzeForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);


	public DataPackage getMiniClassCourseSeries(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);


	/**
	 * 小班报读情况汇总
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param catchStudentStatu
	 * @param profitStatu
	 * @param lowNormalStudent
	 * @return
	 */
	public DataPackage getMiniClassMemberTotal(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			String catchStudentStatu, String profitStatu,
			String lowNormalStudent);


	/**
	 * 小班报读情况汇总（关键指标）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassMemberTotalSum(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);


	public DataPackage getPlanManagementTotal(String year, String goalType,
			String targetType, DataPackage dp, String monthOrquarter);


	/**
	 * 小班报读人数统计
	 * @param basicOperationQueryVo
	 * @param miniClassStatu
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentRealCount(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp);


	/**
	 * 小班报读人数统计（详细）
	 * @param basicOperationQueryVo
	 * @param miniClassStatu
	 * @param dp
	 * @param type
	 * @return
	 */
	public DataPackage getMiniClassStudentRealCountDetail(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, String type);
	
	/**
	 *  老师小班课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp);

	/**
	 * 老师1对1+小班+一对多课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param dp
     * @return
     */
	public DataPackage getOooMiniOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp);
	
	/**
	 *  老师小班人数年级分布（工资）
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniChargedPeopleTeacherView(String startDate,
														   String endDate, OrganizationType organizationType,
														   String blCampusId,String productQuarterSearch, DataPackage dp);


	/**
	 * 客户市场来源统计
	 * @param basicOperationQueryVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getCustomerTotalByCusType(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dataPackage);
	
	/**
	 * 总览统计
	 * @param dp
	 * @param basicOperationQueryVo
	 * @return
	 */
	public DataPackage getAllTotalForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	
	/**
	 * 教室使用率
	 * @param startDate 
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @param searchType  维度查询
	 * @return
	 */
	public DataPackage getClassRoomUseRate(String startDate,
														   String endDate, OrganizationType organizationType,
														   String blCampusId, DataPackage dp,String searchType,String weekDay);
	
	/**
	 * 卡座使用率
	 * @param startDate 
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @param searchType  维度查询
	 * @return
	 */
	public DataPackage getBoothNumUseRate(String startDate,
														   String endDate, OrganizationType organizationType,
														   String blCampusId, DataPackage dp,String searchType,String weekDay);
	
	/**
	 * 小班未报读信息
	 * @param basicOperationQueryVo
	 * @param miniClassProductSearchVo
	 * @param dp
	 * @return
	 */
	public DataPackage getNotSignMiniProductInfo(
			BasicOperationQueryVo basicOperationQueryVo,
			MiniClassProductSearchVo miniClassProductSearchVo,
			DataPackage dp);
	
	/**
	 * 小班指标添统计
	 * @param basicOperationQueryVo
	 * @param miniClassProductSearchVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassIndexAnylize(
			BasicOperationQueryVo basicOperationQueryVo,
			MiniClassProductSearchVo miniClassProductSearchVo,
			DataPackage dp);

	/**
	 * 一对一学生状态统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentOOOStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	public DataPackage getStudentOTMStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 手机端课程统计接口
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCoursesAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 一对一学生状态详情
	 * @param basicOperationQueryVo
	 * @param oooStatus
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentOOOStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp);
	
	public DataPackage getStudentOTMStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp);

	public Boolean getStudentDetailStatu(String countDate);
	

	public Boolean getOtmStudentDetailStatu(String countDate);

	public DataPackage getMiniClassKaoqinTongji(BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId, DataPackage dp,String anshazhesuan);

	public DataPackage getConsultResourceUse(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String entranceIds);
	
	/**
	 * 获取一对多课消统计
	 * @param basicOperationQueryVo
	 * @param otmTypeStr
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassAttendsAnalyze(BasicOperationQueryVo basicOperationQueryVo, String otmTypeStr, DataPackage dp);
	
	/**
	 *  老师一对多课时年级分布视图
	 * @param basicOperationQueryVo
	 * @param otmTypeStr
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, String otmTypeStr, DataPackage dp);
	
	/**
	 * 一对多剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmStudentRemainAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp);
	
	/**
	 * 财务审批进度
	 * @param stratDate
	 * @param endDate
	 * @param channel
	 * @return
	 */
	public DataPackage fundsChangeHistoryAuditList(String startDate,String endDate,String channel,DataPackage dp,String orgType,String groupById,String campusId);
	
	
	/**
	 * 一对一科次统计报表
	 * @param startDate
	 * @param endDate
	 * @param orgType
	 * @param groupById
	 * @param dp
	 * @return
	 */
	public DataPackage courseSubjectList(String startDate,String endDate,String orgType,String groupById,String workType,String personnelType,String brenchId,String blSchool,DataPackage dp);
	
	/**
	 * 一对一科次科目分布报表
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @param workType
	 * @param roleType
	 * @param subject
	 * @param dp
	 * @return
	 */
	public DataPackage getcourseSubjectTimesList(BasicOperationQueryVo basicOperationQueryVo, String workType, String roleType, String subject, DataPackage dp);
	

	public DataPackage getOooCourseConsumeTeacher(
			CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp);

	/**
	 * 一对一学生人均课消报表
	 * @param basicOperationQueryVo
	 * @param branchId
	 * @param blCampus
	 * @param workType
     * @param dp
     * @return
     */
	DataPackage courseConsumeRenJunList(BasicOperationQueryVo basicOperationQueryVo,String groupById, String branchId, String blCampus, String workType, DataPackage dp);

	/**
	 * 校区签单合同
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCampusSignContract(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 一对一月结报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOooMonthlyBalance(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 财务现金流凭证报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getFinanceMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 财务扣费凭证报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomeMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 营收凭证报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomeMonthlyCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 营收凭证报表forexcel
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<OdsMonthIncomeCampusVo> getIncomeMonthlyCampusForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 营收凭证报表forexcel 明细（学生）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<OdsMonthIncomeStudentVo> getIncomeMonthlyStudentForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	
	/**
	 * 剩余资金凭证报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getRemainMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 剩余资金凭证报表forexcel
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<OdsMonthRemainAmountCampusVo> getRemainMonthlyEvidenceToExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/**
	 * 剩余资金凭证报表forexcel 明细（学生）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<OdsMonthRemainAmountStudentVo> getRemainMonthlyStudentToExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	
	/**
	 * 财务扣费凭证报表forexcel
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<IncomeMonthlyEvidenceVo> getIncomeMonthlyEvidenceForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	public Map getFinanceAnalyzeForMobileLine(
			BasicOperationQueryVo basicOperationQueryVo,String type);

	/**
	 * 学生科目状态报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus, DataPackage dp);
	
	public List<OdsStudentSubjectStatusVo> exportStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus);

	DataPackage getTwoTeacherCourseTotal(ModelVo modelVo,DataPackage dataPackage);

	List<TwoTeacherMainClassesExcelVo> exportTwoTeacherMainClassesReport(ModelVo modelVo);

	List<TwoTeacherAuxiliaryClassesExcelVo> exportTwoTeacherAuxiliaryClassesReport(ModelVo modelVo);

    DataPackage getPublicFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
    
    DataPackage getMiniClassFullClassTeacherRate(DataPackage dataPackage,MiniClassFullRateVo miniClassRateVo);
    DataPackage getMiniClassFullClassClassRate(DataPackage dataPackage,MiniClassFullRateVo miniClassRateVo);
    
    DataPackage getMiniClassQuitClassTeacherRate(DataPackage dataPackage,MiniClassQuitRateVo miniClassQuitRateVo);
    DataPackage getMiniClassQuitClassProductRate(DataPackage dataPackage,MiniClassQuitRateVo miniClassQuitRateVo);
    
    List<Map<Object, Object>> getMiniClassFullClassTeacherRateSize(MiniClassFullRateVo miniClassRateVo);
    List<Map<Object, Object>> getMiniClassFullClassClassRateSize(MiniClassFullRateVo miniClassRateVo);    
    List<Map<Object, Object>> getMiniClassQuitClassTeacherRateSize(MiniClassQuitRateVo miniClassQuitRateVo);
    List<Map<Object, Object>> getMiniClassQuitClassProductRateSize(MiniClassQuitRateVo miniClassQuitRateVo);
    
    List<MiniClassFullRateTeacherExcelVo> getMCFullClassTeacheerRateToExcel(DataPackage dataPackage,MiniClassFullRateVo miniClassRateVo);
    List<MiniClassFullRateClassExcelVo> getMCFullClassClassRateToExcel(DataPackage dataPackage,MiniClassFullRateVo miniClassRateVo);    
    List<MiniClassQuitRateTeacherExcelVo> getMCQuitClassTeacheerRateToExcel(DataPackage dataPackage,MiniClassQuitRateVo miniClassQuitRateVo);
    List<MiniClassQuitRateProductExcelVo> getMCQuitClassProductRateToExcel(DataPackage dataPackage,MiniClassQuitRateVo miniClassQuitRateVo);
    
    /**
     * 按pos机类型获取收款笔数
     * @param reqVo
     * @return
     */
    List<Map<Object, Object>> getFundsCountGroupByPosType(OrganizationDateReqVo reqVo);
    
    /**
     * 按pos机类型获取收款金额
     * @param reqVo
     * @return
     */
    List<Map<Object, Object>> getFundsAmountGroupByPosType(OrganizationDateReqVo reqVo);
    
}
