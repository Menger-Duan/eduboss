package com.eduboss.dao;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.*;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.OrganizationDateReqVo;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;



public interface ReportDao extends GenericDAO<Object, String>{

	/**
	 * 获取统计表中的数据  (营运部月结表（学管组）中的业绩与课时表格)
	 * @param beginDate
	 * @param endDate
	 */
	public List<OperatBonusAndClsHrInxVo> qryDataOperatBonusAndClsHrForStuMan(String beginDate,String endDate);



	/**
	 * 获取统计表中的数据  (营运部月结表（学管组）中的学员、班级务构成与服表格)
	 * @param beginDate
	 * @param endDate
	 */
	public List<OperatStuClsCompInxVo> qryDataOperatStuClsCompForStuMan(String beginDate,String endDate);

	/**
	 * 获取各角色员工的数量
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public CampusEmployeesVo qryEmployeeNumberByRole(String beginDate,String endDate,String[] roleCodes,String campusId);

	/**
	 * 获取校区其他角色员工人数
	 * @param beginDate
	 * @param endDate
	 * @param campusId
	 * @return
	 */
	public CampusEmployeesVo qryOtherEmpNumByRole(String beginDate,String endDate,String campusId);


	public CampusEmployeesVo qryPartTmAndFullTmEmpNumber(String beginDate,String endDate,String campusId);


	public CampusEmployeesVo qryBTypeEmpNumber(String beginDate,String endDate,String campusId);

	/**
	 * 获取校区离职人员
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public CampusEmployeesVo qryResignEmpNumber(String beginDate,String endDate,String campusId);

	/**
	 * 获取校区离职率
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public CampusEmployeesVo qryResignRatio(String beginDate,String endDate,String campusId);


	/**
	 * 获取校区离职率
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public CampusEmployeesVo qryTeacherResignRatio(String beginDate,String endDate,String campusId);


	public List<GradeProportiStatObj> getGradeProportionRpData(@RequestParam String startDate,@RequestParam String endDate,@RequestParam String campusId);




	public DataPackage getOneOnOneCourseConsumeTeacherView(CourseConsumeTeacherVo vo, DataPackage dp);
	
	
	/**
	 *通过小时折算
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	DataPackage getOneOnOneCourseConsumeTeacherViewHours(String startDate, String endDate, OrganizationType organizationType, String blCampusId, DataPackage dp, User currentLoginUser, Organization belongCampus);


	/**
	 * 一对一课消老师视图（学生维度）
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneCourseConsumeStudentView(String startDate,
														   String endDate, OrganizationType organizationType,
														   String blCampusId, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus);


	/**
	 * 小班剩余资金
	 * @param reportMiniClassSurplusMoneyVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getMiniClassSurplusMoney(ReportMiniClassSurplusMoneyVo reportMiniClassSurplusMoneyVo, DataPackage dp,
												User currentLoginUser, Organization currentLoginUserCampus);

	/**
	 * 学生剩余资金
	 */
	public DataPackage getReportStudentSurplusFunding(ReportStudentSurplusFundingVo reportStudentSurplusFundingVo, DataPackage dp,
												User currentLoginUser, Organization currentLoginUserCampus);

	/**
	 * 小班学生人数
	 * @param countDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentCount(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus);



	/**
	 * 小班课消统计
	 * @param countDate
	 * @param basicOperationQueryLevelType
	 * @param blCampusId
	 * @param dp
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	public DataPackage getMiniClassConsumeAnalyze(String startDate, String endDate,
												  BasicOperationQueryLevelType basicOperationQueryLevelType,
												  String blCampusId, DataPackage dp, User currentLoginUser,
												  Organization belongCampus);



	/**
	 * 现金流
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getFinanceAnalyze(BasicOperationQueryVo basicOperationQueryVo,
										 DataPackage dp, User currentLoginUser,
										 Organization currentLoginUserCampus);

	/**
	 * 现金流（根据合同校区）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo,
												 DataPackage dp, User currentLoginUser,
												 Organization currentLoginUserCampus);

	public DataPackage getFinanceContractAnalyzeRate(BasicOperationQueryVo basicOperationQueryVo,
													 DataPackage dp, User currentLoginUser,
													 Organization currentLoginUserCampus,String yearAndMonth,String targetType);

	/**
	 * 一对一学生统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getOneOnOneStudentCountAnalyze(BasicOperationQueryVo basicOperationQueryVo,
													  DataPackage dp, User currentLoginUser,
													  Organization currentLoginUserCampus);

	/**
	 * 一对一剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getOneOnOneStudentRemainAnalyze(BasicOperationQueryVo basicOperationQueryVo,
													   DataPackage dp, User currentLoginUser,
													   Organization currentLoginUserCampus);

	/**
	 * 课消
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getCourseConsomeAnalyse(BasicOperationQueryVo basicOperationQueryVo,
											   DataPackage dp, User currentLoginUser,
											   Organization currentLoginUserCampus);

	/**
	 * 学生待收款资金
	 */
	public DataPackage getStudentPendingMoney(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus);

	/**
	 * 购买课时数
	 */
	public DataPackage getPayClassHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 购买课时数
	 */
	public DataPackage getPayClassHourCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus);

	/**
	 * 退费
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getRefundAnalyze(BasicOperationQueryVo basicOperationQueryVo,
										DataPackage dp, User currentLoginUser,
										Organization currentLoginUserCampus);

	/**
	 * 收入
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getIncomingAnalyze(BasicOperationQueryVo basicOperationQueryVo,
										  DataPackage dp, User currentLoginUser,
										  Organization currentLoginUserCampus);

	/**
	 * 营收列表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomingAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	
	
	public DataPackage getIncomingBrenchForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	

	public DataPackage getIncomingCampusForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 小班产品统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getMiniClassPeopleAnalyze(BasicOperationQueryVo basicOperationQueryVo,
												 DataPackage dp, User currentLoginUser,
												 Organization currentLoginUserCampus);

	/**
	 * 查询小班扣费记录
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param miniClassTypeId
	 * @param currentLoginUser
	 * @param belongCampus
     * @return
     */
	public DataPackage getMiniClassKoufeiTongji(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String miniClassTypeId,
			User currentLoginUser, Organization belongCampus);

	/**
	 * 每天分配课时数
	 */
	public DataPackage getOsdMoneyArrangeRecord(BasicOperationQueryVo basicOperationQueryVo,
												DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus);

	/**
	 * 业绩（校区）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getContractBonusOrganization(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 * 业绩（签单人）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getContractBonusStaff(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	

	public DataPackage getContractBonusStaff2(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String flag);

	/**
	 * 记录刷新报表次数
	 */
	public void saveProcedureRefreshs(String procedureName);

	/**
	 * 查询小班学生学校分布
	 * @param miniClassId
	 * @return
	 */
	public Map<String,Integer> getMiniClassEnrollmentAnalysis(String miniClassId);

	public Map<String,Integer> getMiniClassFirstSchoolTimeStatistics(String miniClassId,String startDate,String endDate);

	/**
	 * 首页统计数据
	 * @param miniClassId
	 * @return
	 */
	public Map<String,Integer> getTodayAndMonthTotal();

	/**
	 * 首页统计数据营收
	 * @param miniClassId
	 * @return
	 */
	public List getTodayAndMonthTotalIncome();

	/**
	 * 首页统计数据课时
	 * @param miniClassId
	 * @return
	 */
	public List getTodayAndMonthTotalComsume();

	/**
	 * 手机查询现金流报表
	 * @param dp
	 * @param searchId
	 * @param Type
	 * @return
	 */
	public DataPackage getFinanceAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	
	
	
	public DataPackage getFinanceBrenchAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	public DataPackage getFinanceCampusAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	public DataPackage getFinanceUserAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	
	/**
	 * 现金流    图形报表数据
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public List<Map<Object, Object>> getFinanceAnalyzeForMobileLine(BasicOperationQueryVo basicOperationQueryVo);
	
	public List<Map<Object, Object>> getFinanceBrenchForMobileLine(BasicOperationQueryVo basicOperationQueryVo);
	
	/**
	 * 
	 * 现金流    图形报表数据  周比，月比
	 * @param basicOperationQueryVo
	 * @param preStartDate   上一周开始日期，或者上一月开始日期
	 * @param preEndDate     上一周结束日期，或者上一月结束日期
	 * @param nexStartDate   下一周开始日期，或者下一月开始日期
	 * @param nextEndDate    下一周结束日期，或者下一月结束日期
	 * @return
	 */
	public Map getFinanceAnalyzeForMobileTotal(BasicOperationQueryVo basicOperationQueryVo,String preStartDate,String preEndDate,String nexStartDate,String nextEndDate);
	
	public Map getFinanceBrenchForMobileTotal(BasicOperationQueryVo basicOperationQueryVo,String preStartDate,String preEndDate,String nexStartDate,String nextEndDate);
	
	/**
	 * 市场统计
	 * @param dp
	 * @param basicOperationQueryVo
	 * @return
	 */
	public DataPackage getCustomerTotalByCusType(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);
	
	/**
	 * 总览统计
	 * @param dp
	 * @param basicOperationQueryVo
	 * @return
	 */
	public DataPackage getAllTotalForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);



	public DataPackage getMiniClassCourseSeries(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);



	public DataPackage getMiniClassCourseSeries(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			String catchStudentStatu, String profitStatu,
			String lowNormalStudent);



	public DataPackage getMiniClassMemberTotalSum(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);



	public DataPackage getPlanManagementTotal(Integer preYear,String thisYear, String goalType,
			String targetType, DataPackage dp);



	public DataPackage getMiniClassStudentRealCount(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, User currentLoginUser, Organization belongCampus);



	public DataPackage getMiniClassStudentRealCountDetail(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, User currentLoginUser, Organization belongCampus,
			String type);
	
	/**
	 * 老师小班课时年级分布视图（按课时）
	 * @param courseConsumeTeacherVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp);


	/**
	 * 老师小班课时年级分布视图(按小时)
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param miniClassTypeId
     * @param dp
     * @return
     */
	DataPackage getMiniCourseConsumeTeacherViewHours(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp);
	
	/**
	 * 老师小班人数年级分布（工资）
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
	 * @param dp
	 * @return
	 */
	public DataPackage getNotSignMiniProductInfo(BasicOperationQueryVo basicOperationQueryVo, 
			MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp);
	
	/**
	 * 小班指标添统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassIndexAnylize(BasicOperationQueryVo basicOperationQueryVo, 
			MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp);



	/**
	 * 一对一学生状态统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	public DataPackage getStudentOOOStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			User currentLoginUser, Organization belongCampus);
	
	public DataPackage getStudentOTMStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			User currentLoginUser, Organization belongCampus);



	public List getTeacherCourseGrade(String teacherId, String startDate,
			String endDate);
	
	/**
	 * 手机端课程统计接口
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCoursesAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp);

	/**
	 * 咨询师资源利用情况
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getConsultResourceUse(BasicOperationQueryVo basicOperationQueryVo,DataPackage dp,String entranceIds);

	public DataPackage getStudentOOOStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp);
	
	public DataPackage getStudentOTMStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp);



	public Boolean getStudentDetailStatu(String countDate);
	public Boolean getOtmStudentDetailStatu(String countDate);



	public DataPackage getMiniClassKaoqinTongji(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String miniClassTypeId,
			User currentLoginUser, Organization belongCampus);
	
	/**
	 * 获取一对多课消统计
	 * @param basicOperationQueryVo
	 * @param otmTypeStr
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassAttendsAnalyze(BasicOperationQueryVo vo, String otmTypeStr, DataPackage dp);
	
	/**
	 *  老师一对多课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param otmTypeStr
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, String otmTypeStr, DataPackage dp);

	/**
	 * 老师一对多课时年级分布视图(小时)
	 * @param courseConsumeTeacherVo
	 * @param o
	 * @param dp
     * @return
     */
	DataPackage getOtmCourseConsumeTeacherViewHours(CourseConsumeTeacherVo courseConsumeTeacherVo, Object o, DataPackage dp);
	
	/**
	 * 一对多剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getOtmStudentRemainAnalyze(BasicOperationQueryVo basicOperationQueryVo,
													   DataPackage dp, User currentLoginUser,
													   Organization currentLoginUserCampus);
	
	/**
	 * 财务审批进度
	 * @param stratDate
	 * @param endDate
	 * @param channel
	 * @return
	 */
	public DataPackage fundsChangeHistoryAuditList(String startDate,String endDate,String channel,DataPackage dp,String orgType,String groupById,String campusId);


	/**
	 * 小时
	 * 小班考勤分析
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param miniClassTypeId
	 * @param currentLoginUser
	 * @param belongCampus
     * @return
     */
	DataPackage getMiniClassKaoqinTongjiHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, String miniClassTypeId, User currentLoginUser, Organization belongCampus);

	/**
	 * (小时)
	 * 查询小班扣费统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param miniClassTypeId
	 * @param currentLoginUser
	 * @param belongCampus
     * @return
     */
	DataPackage getMiniClassKoufeiTongjiHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, String miniClassTypeId, User currentLoginUser, Organization belongCampus);

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

	//老师一对一课消
	DataPackage getOneOnOneCourseConsumeTeacher(CourseConsumeTeacherVo vo, DataPackage dp);

	/**
	 * 一对一学生人均课消报表
	 *
	 * @param basicOperationQueryVo
	 * @param branchId
	 * @param blCampus
	 * @param workType 全/兼职
     * @param dp
     * @return
     */
	DataPackage courseConsumeRenJunList(BasicOperationQueryVo basicOperationQueryVo,String groupById, String branchId, String blCampus, String workType, DataPackage dp);



	/**
	 * 校区签单合同
	 * @param vo
	 * @param dp
	 * @return
	 */
	public DataPackage getCampusSignContract(BasicOperationQueryVo vo,
			DataPackage dp);
	
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
	 * 剩余资金凭证报表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getRemainMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	/** 
	 * 现金流凭证
	* @param yearMonth
	* @param type
	* @param campusId
	* @return
	* @author  author :Yao 
	* @date  2016年9月22日 下午7:43:26 
	* @version 1.0 
	*/
	List findInfoByMonth(BasicOperationQueryVo searchVo);

	/**
	 *
	 * @param preYear
	 * @param thisYear
	 * @param goalType
	 * @param targetType
	 * @param dp
	 * @author guo
	 * @return
	 */
	DataPackage getPlanManagementTotalForQUARTER(Integer preYear, String thisYear, String goalType, String targetType, DataPackage dp);



	/**
	 * 学生科目状态
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus, DataPackage dp);



	public List<Map<Object,Object>> exportStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus);

	/**
	 *
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
    DataPackage getContractBonusBranch(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);

	/**
	 *
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	DataPackage getContractBonusCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);
	
	
	
	/**
	 * 首页统计数据课时 根据不同的课程类型  获取课消和收款  20170731xiaojinwang 
	 * @param  小班  其他（一对多，双师） 总课消
	 * @return
	 */
	//小班类型
	public List getTodayMonthSmallConsume();
	//其他类型 包括一对多和双师
	public List getTodayMonthOtherConsume();
	//总课消 包括全部类型  一对一，小班，一对多，双师
	public List getTodayMonthTotalConsume();

	DataPackage getTwoTeacherCourseTotal(ModelVo modelVo,DataPackage dataPackage);

	List<Map<Object, Object>> exportTwoTeacherMainClassesReport(ModelVo modelVo);

	List<Map<Object, Object>> exportTwoTeacherAuxiliaryClassesReport(ModelVo modelVo);

    DataPackage getPublicFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization belongCampus);


    /**
     * 校区业绩统计
     * @author: duanmenrun
     * @Title: getContractBonusOrganization_1849 
     * @Description: TODO  
     * @param basicOperationQueryVo
     * @param dp
     * @return
     */
	public DataPackage getContractBonusOrganization_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp);


	/**
	 * 个人业绩统计
	 * @author: duanmenrun
	 * @Title: getContractBonusStaff_1849 
	 * @Description: TODO 
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param flag 
	 * @return
	 */
	public DataPackage getContractBonusStaff_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			String flag);


	/**
	 * 营业收入统计
	 * @author: duanmenrun
	 * @Title: getIncomingAnalyze_1849 
	 * @Description: TODO 
	 * @param basicOperationQueryVo
	 * @param dp 
	 * @param user
	 * @param belongCampus
	 * @return
	 */
	public DataPackage getIncomingAnalyze_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User user,
			Organization belongCampus);


	/**
	 * 校区业绩凭证统计
	 * @author: duanmenrun
	 * @Title: findCampusAchievementMainByMonth 
	 * @Description: TODO 
	 * @param searchVo
	 * @return
	 */
	public List findCampusAchievementMainByMonth(BasicOperationQueryVo searchVo);
	
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


