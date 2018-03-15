package com.eduboss.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.ContractType;
import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.common.MonthType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.ReflushReportSubject;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.SummaryClassType;
import com.eduboss.common.SummaryCycleType;
import com.eduboss.common.TeacherLevel;
import com.eduboss.common.TeacherType;
import com.eduboss.dao.IncomeDistributionDao;
import com.eduboss.dao.MiniClassDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.dao.SummaryCampusDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.SummaryCampus;
import com.eduboss.domain.SummaryCampusTotalExtend;
import com.eduboss.domain.User;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CampusEmployeesVo;
import com.eduboss.domainVo.CourseConsumeTeacherVo;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.domainVo.GradeProportiStatObj;
import com.eduboss.domainVo.GradeProportionVo;
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
import com.eduboss.domainVo.SummaryCampusVo;
import com.eduboss.domainVo.TwoTeacherAuxiliaryClassesExcelVo;
import com.eduboss.domainVo.TwoTeacherMainClassesExcelVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.OrganizationDateReqVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.OtmClassService;
import com.eduboss.service.ReportService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.ReportUtil;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;



@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private SummaryCampusDao summaryCampusDao;

	@Autowired
	private UserService userService;

	@Autowired
	private OperationCountService operationCountService;

	@Autowired
	private DataDictService dataDictService;
	
	@Autowired
	private OtmClassService otmClassService;

	@Autowired
	private IncomeDistributionDao incomeDistributionDao;

	public  Vector<ReflushReportSubject> syncReflushReport =new Vector<ReflushReportSubject>();

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private SmallClassService smallClassService;
	
	@Autowired
	private MiniClassDao miniClassDao;
	
	@Autowired
	private OrganizationService organizationService;

	/**
	 * 获取校区总结表excel数据
	 */
	@Override
	public HSSFWorkbook getExcelDataTypeOfCampusAndCycleOfMonthOrQuarterOrYear(SummaryCycleType summaryCycleType, String summaryStartDate, String summaryEndDate) throws IOException {
		// 总结表对象
		SummaryCampus summaryCampusOneonone = genSummaryObjectByClassType(SummaryClassType.ONE_ON_ONE, summaryCycleType, summaryStartDate, summaryEndDate);
		SummaryCampus summaryCampusMiniClass = genSummaryObjectByClassType(SummaryClassType.MINI_CLASS, summaryCycleType, summaryStartDate, summaryEndDate);
		SummaryCampus summaryCampusPromise = genSummaryObjectByClassType(SummaryClassType.PROMISE_CLASS, summaryCycleType, summaryStartDate, summaryEndDate);
		SummaryCampus summaryCampusCampus = genSummaryObjectByClassType(SummaryClassType.CAMPUS, summaryCycleType, summaryStartDate, summaryEndDate);
		// 校区总，数据延伸表
		SummaryCampusTotalExtend summaryCampusTotalExtend = null;
		if (summaryCampusCampus != null) {
			Set<SummaryCampusTotalExtend> summaryCampusTotalExtends = summaryCampusCampus.getSummaryCampusTotalExtends();
			for (SummaryCampusTotalExtend one:summaryCampusTotalExtends) {
				summaryCampusTotalExtend = one;
				break;
			}
		}

		HSSFWorkbook workbook= new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		// 第1行
		genTitle(1, new String[]{"星火教育    校区（月、季、年）总结表"}, sheet);

		// 第2行
		genTitle(2, new String[]{"时间区间：2014年 月  日-  月  日            总结人： "}, sheet);

		// 第3-4行	校区员工情况
		genTitle(3, new String[]{"校区员工情况","咨询","学管","全职老师","兼职老师","其它","B类老师","全兼比例","离职人数","校区离职率","教师离职率"}, sheet);
		genCampusCycleOfCampusStaffCondition(4, "校区总", summaryCampusTotalExtend, sheet);

		// 第5-9行	现金流与收入	
		genTitle(5, new String[]{"现金流与收入","咨询预收","学管预收","退费额","校区总预收","退费占比","预收环比","预收同比","课耗收入","课耗环比","课耗同比","续费占比","转介绍占新签","营运占比"}, sheet);
		genCampusCycleOfCashFlowAndRevenue(6, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfCashFlowAndRevenue(7, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfCashFlowAndRevenue(8, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfCashFlowAndRevenue(9, "校区总", summaryCampusCampus, sheet);

		// 第10-14行	目标达成率
		genTitle(10, new String[]{"目标达成率","预收目标","预收达成率","累计季度达成率","累计年度达成率","课耗目标","课耗达成率","累计季度达成率","累计年度达成率","咨询达成率","学管达成率"}, sheet);
		genCampusCycleOfTargetReachRate(11, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfTargetReachRate(12, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfTargetReachRate(13, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfTargetReachRate(14, "校区总", summaryCampusCampus, sheet);

		// 第15-16行	营运支出
		genTitle(15, new String[]{"营运支出","校区水费","校区电费","校区打印费","校区通讯费"}, sheet);
		HSSFRow row16 = sheet.createRow(15);
		HSSFCell cell16_0 = row16.createCell(0);
		cell16_0.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell16_0.setCellValue(new HSSFRichTextString("校区总"));

		// 第17-18行	资源利用率
		genTitle(17, new String[]{"资源利用率","直访上门数量","来电量","有效口推数量","外呼部资源","线上资源","其它资源","总资源量","总上门数","直访签单率","来电邀约率","总上门签单率"}, sheet);
		genCampusCycleOfResUtilizationRate(18, "校区总", summaryCampusTotalExtend, sheet);

		// 第19-23行	校区课时
		genTitle(19, new String[]{"校区课时","计划课时数","校区实际总课时","课时达成率","全职老师平均课时","B类老师平均课时","全兼职课时比例","一对一赠送比例","一对一课时均价","总剩余课时数","一对一生均课耗","科组全职平均课时"}, sheet);
		genCampusCycleOfCampusCourseHout(20, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfCampusCourseHout(21, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfCampusCourseHout(22, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfCampusCourseHout(23, "校区总", summaryCampusCampus, sheet);

		// 第24-28行	学员与班级1
		genTitle(24, new String[]{"学员与班级","新签学生","续费学生","退费学生","停课学生","结课学生","总在读学生","停课学生比例","续生率","月总转介绍人数","低于20H学生数","低于10H学生数","续读科次","新签科次"}, sheet);
		genCampusCycleOfStudentAndClass1st(25, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfStudentAndClass1st(26, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfStudentAndClass1st(27, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfStudentAndClass1st(28, "校区总", summaryCampusCampus, sheet);

		// 第29-33行	学员与班级2
		genTitle(29, new String[]{"学员与班级","退费科次","目标科次","科次达成率","人均科次","总开班数","班均人数","新生数量","旧生数量","语文科学生数","数学科学生数","英语科学生数","物理科学生数","化学科学生数"}, sheet);
		genCampusCycleOfStudentAndClass2nd(30, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfStudentAndClass2nd(31, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfStudentAndClass2nd(32, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfStudentAndClass2nd(33, "校区总", summaryCampusCampus, sheet);

		// 第34-38行	学员与班级3
		genTitle(34, new String[]{"学员与班级","政治科学生数","历史科学生数","地理科学生数","生物科学生数","其它科学生数","小一至五占比","小六占比","初一占比","初二占比","初三占比","高一占比","高二占比","高三占比"}, sheet);
		genCampusCycleOfStudentAndClass3rd(35, "一对一", summaryCampusOneonone, sheet);
		genCampusCycleOfStudentAndClass3rd(36, "小班", summaryCampusMiniClass, sheet);
		genCampusCycleOfStudentAndClass3rd(37, "目标班", summaryCampusPromise, sheet);
		genCampusCycleOfStudentAndClass3rd(38, "校区总", summaryCampusCampus, sheet);

		// 第39-43行	服务与满意度
		genTitle(39, new String[]{"服务与满意度","家长会次数","均家长会次数","回访次数","均回访次数","教管听课次数","首课换老师数","点评次数","均点评次数","校区满意度","学管满意度","教师满意度","成绩进步率","电访D类数"}, sheet);
		genTitle(40, new String[]{"一对一"}, sheet);
		genTitle(41, new String[]{"小班"}, sheet);
		genTitle(42, new String[]{"目标班"}, sheet);
		genTitle(43, new String[]{"校区总"}, sheet);

		// 第44-45行	校区营运管理
		genTitle(44, new String[]{"校区营运管理","校区培训","校区常规会议","教研日"}, sheet);
		genTitle(45, new String[]{"校区总"}, sheet);

		// 第46-47行	校区运营总结
		genTitle(46, new String[]{"校区运营总结：（校区整体运营中的亮点、存在或潜在问题、需要的支持等。可从以下几个方面：业绩达成、资源利用率、团队管理、教学质量、电访结果、退费与投诉、成本管控、校区可持续发展等等）"}, sheet);
		genTitle(47, new String[]{""}, sheet);

		// 第48行	备注
		genTitle(48, new String[]{"备注\n1.本报表填写人为校区主任，审核人为总监或总经理。每月初5号前上交至总监。\n2.本报表为月度、季度、年度总结报表。填写时对应时间段即可。周结表可用简略版月结表。\n3.本报表可供小班、目标班与一对一各产品综合校区或独立校区填写。如某校区为目标班校区，只需填“目标班”项及“校区总”项。由于业绩归属原因而划分到本校区的业绩，需按实际情况填写到对应产品项上。在业绩对半情况下，对应人头允许以0.5计算。"}, sheet);

		FileOutputStream fileOutputStream = new FileOutputStream("D:\\yalu.xls");
		workbook.write(fileOutputStream);

		return workbook;
	}
	
	@Override
	public List getTeacherCourseGrade(String teacherId, String startDate,
			String endDate) {
		return reportDao.getTeacherCourseGrade(teacherId,startDate,endDate);
	}

	/**
	 * 生成标题
	 * @param rowNum
	 * @param titleArray
	 * @param sheet
	 */
	void genTitle(int rowNum, String[] titleArray, HSSFSheet sheet) {
		HSSFRow row = sheet.createRow(rowNum-1);
		for (int n=0; n<titleArray.length; n++) {
			HSSFCell cell = row.createCell(n);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(titleArray[n]));
		}
	}

	/**
	 * 通过班类型和时间获取总结表对象
	 * @param summaryClassType
	 * @param summaryCycleType
	 * @param summaryStartDate
	 * @param summaryEndDate
	 * @return
	 */
	public SummaryCampus genSummaryObjectByClassType(SummaryClassType summaryClassType, SummaryCycleType summaryCycleType, String summaryStartDate, String summaryEndDate) {
		SummaryCampusVo summaryCampusVo = new SummaryCampusVo();
		summaryCampusVo.setSummaryCycleStartDate(summaryStartDate);
		summaryCampusVo.setSummaryCycleEndDate(summaryEndDate);
		summaryCampusVo.setSummaryCycleType(summaryCycleType);
		summaryCampusVo.setSummaryClassType(summaryClassType);
		SummaryCampus summaryCampus = summaryCampusDao.getOneSummaryCampus(summaryCampusVo);
		return summaryCampus;
	}

	/**
	 * 生成校区（年，季，月）现金流与收入数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfCashFlowAndRevenue(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getConsultorAdvance());// 咨询预收
			rowData.add(summaryCampus.getStudyManagerAdvance());// 学管预收
			rowData.add(summaryCampus.getRefundAmount());// 退费额
			rowData.add(summaryCampus.getCampusAdvance());// 校区总预收
			rowData.add(summaryCampus.getRefundProportion());// 退费占比
			rowData.add(summaryCampus.getAdvanceChain());// 预收环比
			rowData.add(summaryCampus.getAdvanceYearonyear());// 预收同比
			rowData.add(summaryCampus.getCourseConsumeIncome());// 课耗收入
			rowData.add(summaryCampus.getCourseConsumeChain());// 课耗环比
			rowData.add(summaryCampus.getCourseConsumeYearonyear());// 课耗同比
			rowData.add(summaryCampus.getRenewalProportion());// 续费占比
			rowData.add(summaryCampus.getChangeReferralRateNewsigning());// 转介绍占新签
			rowData.add(summaryCampus.getOperationRate());// 营运占比
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成目标达成率数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfTargetReachRate(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getPredictTarget());// 预收目标
			rowData.add(summaryCampus.getAdvanceReachRate());// 预收达成率
			rowData.add(summaryCampus.getAdvanceQuarterReachRate());// 累计季度达成率
			rowData.add(summaryCampus.getAdvanceYearReachRate());// 累计年度达成率
			rowData.add(summaryCampus.getCourseConsumeTarget());// 课耗目标
			rowData.add(summaryCampus.getCourseConsumeReachRate());// 课耗达成率
			rowData.add(summaryCampus.getCourseConsumeQuarterReachrate());// 累计季度达成率
			rowData.add(summaryCampus.getCourseConsumeYearReachrate());// 累计年度达成率
			rowData.add(summaryCampus.getConsultorReachRate());// 咨询达成率
			rowData.add(summaryCampus.getStudyManagerReachRate());// 学管达成率
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成资源利用率数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampusTotalExtend
	 * @param sheet
	 */
	public void genCampusCycleOfResUtilizationRate(int rowNum , String classTypeName, SummaryCampusTotalExtend summaryCampusTotalExtend, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampusTotalExtend != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampusTotalExtend.getDirectVisitNum());// 直访上门数量
			rowData.add(summaryCampusTotalExtend.getIncomingTelNum());// 来电量
			rowData.add(summaryCampusTotalExtend.getEffectiveSpeakPopularizeNum());// 有效口推数量
			rowData.add(summaryCampusTotalExtend.getOuterCallResourceNum());// 外呼部资源
			rowData.add(summaryCampusTotalExtend.getOnlineResourceNum());// 线上资源
			rowData.add(summaryCampusTotalExtend.getOtherResourseNum());// 其它资源
			rowData.add(summaryCampusTotalExtend.getTotalResourceNum());// 总资源量
			rowData.add(summaryCampusTotalExtend.getVisitNum());// 总上门数
			rowData.add(summaryCampusTotalExtend.getDirectSignRate());// 直访签单率
			rowData.add(summaryCampusTotalExtend.getIncomingTelInvitationRate());// 来电邀约率
			rowData.add(summaryCampusTotalExtend.getTotalVisitSignRate());// 总上门签单率
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成校区员工情况数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampusTotalExtend
	 * @param sheet
	 */
	public void genCampusCycleOfCampusStaffCondition(int rowNum , String classTypeName, SummaryCampusTotalExtend summaryCampusTotalExtend, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampusTotalExtend != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampusTotalExtend.getConsultorNum());// 咨询
			rowData.add(summaryCampusTotalExtend.getStudyManagerNum());// 学管
			rowData.add(summaryCampusTotalExtend.getFulltimeTeacherNum());// 全职老师
			rowData.add(summaryCampusTotalExtend.getParttimeTeacherNum());// 兼职老师
			rowData.add(summaryCampusTotalExtend.getOtherPeopleNum());// 其它
			rowData.add(summaryCampusTotalExtend.getBTeacherNum());// B类老师
			rowData.add(summaryCampusTotalExtend.getFulltimeParttimeRate());// 全兼比例
			rowData.add(summaryCampusTotalExtend.getLeaveOfficePeopleNum());// 离职人数
			rowData.add(summaryCampusTotalExtend.getCampusLeaveOfficeRate());// 校区离职率
			rowData.add(summaryCampusTotalExtend.getTeacherLeaveOfficeRate());// 教师离职率
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成校区课时数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfCampusCourseHout(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getPlanCourseHour());// 计划课时数
			rowData.add(summaryCampus.getRealCourseHour());// 校区实际总课时
			rowData.add(summaryCampus.getCourseHourReachRate());// 课时达成率
			rowData.add(summaryCampus.getFulltimeTeacherAverageClasshour());// 全职老师平均课时
			rowData.add(summaryCampus.getBTeacherAverageClasshour());// B类老师平均课时
			rowData.add(summaryCampus.getFulltimeParttimeCoursehourRate());// 全兼职课时比例
			rowData.add(summaryCampus.getOneononeGiveRate());// 一对一赠送比例
			rowData.add(summaryCampus.getOneononeCoursehourAveragePrice());// 一对一课时均价
			rowData.add(summaryCampus.getSurplusCourseHour());// 总剩余课时数
			rowData.add(summaryCampus.getOneononeStudentAverageCourseconsume());// 一对一生均课耗
			rowData.add(summaryCampus.getDepartmentFulltimeAverageCoursehour());// 科组全职平均课时
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成学员与班级第一组数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfStudentAndClass1st(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getNewSigningStudent());// 新签学生
			rowData.add(summaryCampus.getRenewalStudent());// 续费学生
			rowData.add(summaryCampus.getRefundStudent());// 退费学生
			rowData.add(summaryCampus.getSuspendCourseStudent());// 停课学生
			rowData.add(summaryCampus.getFinishCourseStudent());// 结课学生
			rowData.add(summaryCampus.getTotalAtSchoolStudent());// 总在读学生
			rowData.add(summaryCampus.getSuspendCourseStudentRate());// 停课学生比例
			rowData.add(summaryCampus.getRenewalStudentRate());// 续生率
			rowData.add(summaryCampus.getMonthReferralNum());// 月总转介绍人数
			rowData.add(summaryCampus.getUnder20hStudentNum());// 低于20H学生数
			rowData.add(summaryCampus.getUnder10hStudentNum());// 低于10H学生数
			rowData.add(summaryCampus.getRenewalSubjectNum());// 续读科次
			rowData.add(summaryCampus.getNewSigningSubjectNum());// 新签科次
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成学员与班级第二组数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfStudentAndClass2nd(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getRefundSubjectNum());// 退费科次
			rowData.add(summaryCampus.getTargetSubjectNum());// 目标科次
			rowData.add(summaryCampus.getSubjectNumReachRate());// 科次达成率
			rowData.add(summaryCampus.getPeopleAverageSubjectNum());// 人均科次
			rowData.add(summaryCampus.getOpenClassNum());// 总开班数
			rowData.add(summaryCampus.getClassAverageStudentNum());// 班均人数
			rowData.add(summaryCampus.getNewStudentNum());// 新生数量
			rowData.add(summaryCampus.getOldStudentNum());// 旧生数量
			rowData.add(summaryCampus.getChineseSubjectStudentNum());// 语文科学生数
			rowData.add(summaryCampus.getMathSubjectStudentNum());// 数学科学生数
			rowData.add(summaryCampus.getEnglishSubjectStudentNum());// 英语科学生数
			rowData.add(summaryCampus.getPhysicsStudentNum());// 物理科学生数
			rowData.add(summaryCampus.getChemistryStudentNum());// 化学科学生数
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 生成学员与班级第三组数据列
	 * @param rowNum
	 * @param classTypeName
	 * @param summaryCampus
	 * @param sheet
	 */
	public void genCampusCycleOfStudentAndClass3rd(int rowNum , String classTypeName, SummaryCampus summaryCampus, HSSFSheet sheet) {
		HSSFRow row = genNewRowAndFirstData(rowNum, classTypeName, sheet);// 生成新行和第一条标题数据
		if (summaryCampus != null) {
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(summaryCampus.getPoliticsStudentNum());// 政治科学生数
			rowData.add(summaryCampus.getHistoryStudentNum());// 历史科学生数
			rowData.add(summaryCampus.getGeographyStudentNum());// 地理科学生数
			rowData.add(summaryCampus.getBiologyStudentNum());// 生物科学生数
			rowData.add(summaryCampus.getOtherStudentNum());// 其它科学生数
			rowData.add(summaryCampus.getPrimarySchoolGrade1to5Rate());// 小一至五占比
			rowData.add(summaryCampus.getPrimarySchoolGrade6Rate());// 小六占比
			rowData.add(summaryCampus.getMiddleSchoolGrade1Rate());// 初一占比
			rowData.add(summaryCampus.getMiddleSchoolGrade2Rate());// 初二占比
			rowData.add(summaryCampus.getMiddleSchoolGrade3Rate());// 初三占比
			rowData.add(summaryCampus.getHighSchoolGrade1Rate());// 高一占比
			rowData.add(summaryCampus.getHighSchoolGrade2Rate());// 高二占比
			rowData.add(summaryCampus.getHighSchoolGrade3Rate());// 高三占比
			genRowDataByForEach(rowData, row);
		}
	}

	/**
	 * 通过元数据List和行号生成行数据
	 * @param rowData
	 * @param row
	 */
	public void genRowDataByForEach(List<Object> rowData, HSSFRow row) {
		for (int n=0;n<rowData.size();n++) {
			HSSFCell cell = row.createCell(n+1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if (rowData.get(n) != null) {
				cell.setCellValue(new HSSFRichTextString(rowData.get(n).toString()));
			}
		}
	}

	/**
	 * 生成新行和第一条标题数据
	 * @param rowNum
	 * @param classTypeName
	 * @param sheet
	 */
	public HSSFRow genNewRowAndFirstData(int rowNum, String classTypeName, HSSFSheet sheet) {
		HSSFRow row = sheet.createRow(rowNum-1);
		HSSFCell cell_0 = row.createCell(0);
		cell_0.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell_0.setCellValue(new HSSFRichTextString(classTypeName));
		return row;
	}


	@Override
	public Map<String,Integer> getMiniClassEnrollmentAnlysis(String miniClassId) {
		Map<String,Integer> map=new HashMap<String, Integer>();
		if(StringUtil.isNotBlank(miniClassId)){
			map= reportDao.getMiniClassEnrollmentAnalysis(miniClassId);
		}
		return map;
	}

	@Override
	public HSSFWorkbook dowlOperatRpForStuMan(String beginDate,String endDate,Map<String,Object>paramMap) {
		// TODO Auto-generated method stub
		List bonusAndClsHrInxVoList =reportDao.qryDataOperatBonusAndClsHrForStuMan(beginDate,endDate);
		List StuClsCompInxVoList =reportDao.qryDataOperatStuClsCompForStuMan(beginDate, endDate);

		HSSFWorkbook wb=ReportUtil.generateBonusAndClassHourReport(bonusAndClsHrInxVoList, StuClsCompInxVoList);


		return wb;

	}

	@Override
	public DataPackage getCampusEmployeesRpData(String beginDate,String endDate,String campusId) {
		DataPackage dataPackage=new DataPackage();
		List<CampusEmployeesVo>voList=new ArrayList<CampusEmployeesVo>();
		if(StringUtils.isNotEmpty(campusId)){
			//roleCode CONSULTOR
			String[] roleCode_consult={"CONSULTOR", "CONSULTOR_DIRECTOR"};
			CampusEmployeesVo returnVo=new CampusEmployeesVo();

			CampusEmployeesVo roleNum_conSult=reportDao.qryEmployeeNumberByRole(beginDate, endDate,roleCode_consult,campusId);
			returnVo.setConsultorNum(roleNum_conSult.getEmployeesNum());
			returnVo.setCampusId(roleNum_conSult.getCampusId());
			returnVo.setCampusName(roleNum_conSult.getCampusName());

			String[] roleCode_study={"STUDY_MANAGER", "STUDY_MANAGER_HEAD"};
			CampusEmployeesVo roleNum_study=reportDao.qryEmployeeNumberByRole(beginDate, endDate,roleCode_study,campusId);
			returnVo.setStudyManageNum(roleNum_study.getEmployeesNum());

			CampusEmployeesVo othernumVo=reportDao.qryOtherEmpNumByRole(beginDate, endDate, campusId);
			returnVo.setOtherNum(othernumVo.getOtherNum());

			CampusEmployeesVo partFullVo=reportDao.qryPartTmAndFullTmEmpNumber(beginDate, endDate,campusId);
			returnVo.setPartTimeTeacherNum(partFullVo.getPartTimeTeacherNum());
			returnVo.setFullTimeTeacherNum(partFullVo.getFullTimeTeacherNum());
			returnVo.setFullPartRat(partFullVo.getFullTimeTeacherNum()+":"+partFullVo.getPartTimeTeacherNum());

			CampusEmployeesVo bTypeVo=reportDao.qryBTypeEmpNumber(beginDate, endDate,campusId);
			returnVo.setBtypeTeacherNum(bTypeVo.getBtypeTeacherNum());

			CampusEmployeesVo resignNumVo=reportDao.qryResignEmpNumber(beginDate, endDate,campusId);
			returnVo.setResignNum(resignNumVo.getResignNum());

			CampusEmployeesVo resignRatioVo=reportDao.qryResignRatio(beginDate, endDate,campusId);
			returnVo.setResignRat(resignRatioVo.getResignRat());

			CampusEmployeesVo teaResiRatVo=reportDao.qryTeacherResignRatio(beginDate, endDate,campusId);
			returnVo.setTeacherResignRat(teaResiRatVo.getTeacherResignRat());

			voList.add(returnVo);
		}
		dataPackage.setDatas(voList);

		return dataPackage;
	}

	@Override
	public DataPackage getGradeProportionRpData(String startDate,
												String endDate, String campusId) {

		DataPackage dataPackage=new DataPackage();

		List <GradeProportionVo> volist=new ArrayList<GradeProportionVo>();

		List<GradeProportiStatObj>statObjList =reportDao.getGradeProportionRpData(startDate, endDate, campusId);

		Map<String,Integer> smallClsMap=new HashMap<String,Integer>();
		Map<String,Integer> oneOnOneMap=new HashMap<String,Integer>();
		Map <String,Integer>otherMap=new HashMap<String,Integer>();

		for (GradeProportiStatObj gradeProportiStatObj : statObjList) {
			String type=gradeProportiStatObj.getType();
			if(type.equalsIgnoreCase("ONE_ON_ONE_COURSE_NORMAL")){
				oneOnOneMap.put(gradeProportiStatObj.getGradeName().trim(), gradeProportiStatObj.getStatiNum());
			}else if(type.equalsIgnoreCase("SMALL_CLASS")){
				smallClsMap.put(gradeProportiStatObj.getGradeName().trim(), gradeProportiStatObj.getStatiNum());
			}else if(type.equalsIgnoreCase("OTHERS")){
				otherMap.put(gradeProportiStatObj.getGradeName().trim(), gradeProportiStatObj.getStatiNum());
			}
		}

//		List <DataDict>dataDicList=dataDictDao.findBySql("from DataDict where category='STUDENT_GRADE'");
//		Map<String,String>gradeNameMap=new HashMap<String, String>();
//	    for (DataDict object : dataDicList) {
//	    	gradeNameMap.put(object.getName(), object.getId());
//		}


		String gradeNameArray[]=new String[]{"一年级","二年级","三年级","四年级","五年级","六年级","初一","初二","初三","高一","高二","高三"};

		BigDecimal zero=new BigDecimal(0);
		String key=null;
		//一对一 begin
		GradeProportionVo vo_oneOnOne=new GradeProportionVo();
		vo_oneOnOne.setType("一对一");
		//获取一对一总人数
		Integer total_oneOnOne=0;
		for(Integer num :oneOnOneMap.values()){
			total_oneOnOne+=num;
		}

		Integer  oneToFive_oneOnOne=0;
		Integer  priSix_oneOnOne=0;
		Integer  junOne_oneOnOne=0;
		Integer  junTwo_oneOnOne=0;
		Integer  senOne_oneOnOne=0;
		Integer  senTwo_oneOnOne=0;
		Integer  senThree_oneOnOne=0;
		Integer  junThree_oneOnOne=0;

		//如果总和为0，则所有指标均为0,不用做任何计算
		if(total_oneOnOne==0){
			vo_oneOnOne.setPrimaryOneToFiveRat(zero);
			vo_oneOnOne.setPrimarySixRat(zero);
			vo_oneOnOne.setJuniorOneRat(zero);
			vo_oneOnOne.setJuniorTwoRat(zero);
			vo_oneOnOne.setJuniorThreeRat(zero);
			vo_oneOnOne.setSeniorOneRat(zero);
			vo_oneOnOne.setSeniorTwoRat(zero);
			vo_oneOnOne.setSeniorThreeRat(zero);
		}else{

			for (int i = 0; i < 5; i++) {
				key=gradeNameArray[i].trim();
				Integer num=0;
				if(oneOnOneMap.containsKey(key)){
					num=oneOnOneMap.get(key);
				}
				oneToFive_oneOnOne+=num;
			}
			vo_oneOnOne.setPrimaryOneToFiveRat(ReportUtil.getFormatDecimal(new BigDecimal(oneToFive_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[5].trim();
			if(oneOnOneMap.containsKey(key)){
				priSix_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setPrimarySixRat(ReportUtil.getFormatDecimal(new BigDecimal(priSix_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[6].trim();
			if(oneOnOneMap.containsKey(key)){
				junOne_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setJuniorOneRat(ReportUtil.getFormatDecimal(new BigDecimal(junOne_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[7].trim();
			if(oneOnOneMap.containsKey(key)){
				junTwo_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setJuniorTwoRat(ReportUtil.getFormatDecimal(new BigDecimal(junTwo_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[8].trim();
			if(oneOnOneMap.containsKey(key)){
				junThree_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setJuniorThreeRat(ReportUtil.getFormatDecimal(new BigDecimal(junThree_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[9].trim();
			if(oneOnOneMap.containsKey(key)){
				senOne_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setSeniorOneRat(ReportUtil.getFormatDecimal(new BigDecimal(senOne_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));
			BigDecimal temp=ReportUtil.getFormatDecimal(new BigDecimal(senOne_oneOnOne.doubleValue()/total_oneOnOne.doubleValue()));
			String tempStr=temp.toString();

			key=gradeNameArray[10].trim();
			if(oneOnOneMap.containsKey(key)){
				senTwo_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setSeniorTwoRat(ReportUtil.getFormatDecimal(new BigDecimal(senTwo_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));

			key=gradeNameArray[11].trim();
			if(oneOnOneMap.containsKey(key)){
				senThree_oneOnOne=oneOnOneMap.get(key);
			}
			vo_oneOnOne.setSeniorThreeRat(ReportUtil.getFormatDecimal(new BigDecimal(senThree_oneOnOne.doubleValue()/total_oneOnOne.doubleValue())));
		}


		//一对一end


		//小班begin
		GradeProportionVo vo_smCls=new GradeProportionVo();
		vo_smCls.setType("小班");
		//获取一对一总人数
		Integer total_smCls=0;
		for(Integer num :smallClsMap.values()){
			total_smCls+=num;
		}

		Integer  oneToFive_smCls=0;
		Integer  priSix_smCls=0;
		Integer  junOne_smCls=0;
		Integer  junTwo_smCls=0;
		Integer  junThree_smCls=0;
		Integer  senOne_smCls=0;
		Integer  senTwo_smCls=0;
		Integer  senThree_smCls=0;
		if(total_smCls==0){
			vo_smCls.setPrimaryOneToFiveRat(zero);
			vo_smCls.setPrimarySixRat(zero);
			vo_smCls.setJuniorOneRat(zero);
			vo_smCls.setJuniorTwoRat(zero);
			vo_smCls.setJuniorThreeRat(zero);
			vo_smCls.setSeniorOneRat(zero);
			vo_smCls.setSeniorTwoRat(zero);
			vo_smCls.setSeniorThreeRat(zero);
		}else{
			for (int i = 0; i < 5; i++) {
				String key_smCls=gradeNameArray[i].trim();
				Integer num=0;
				if(smallClsMap.containsKey(key_smCls)){
					num=smallClsMap.get(key_smCls);
				}
				oneToFive_smCls+=num;
			}
			vo_smCls.setPrimaryOneToFiveRat(ReportUtil.getFormatDecimal(new BigDecimal(oneToFive_smCls.doubleValue()/total_smCls.doubleValue())));

			key=gradeNameArray[5].trim();
			if(smallClsMap.containsKey(key)){
				priSix_smCls=smallClsMap.get(key);
			}
			vo_smCls.setPrimarySixRat(ReportUtil.getFormatDecimal(new BigDecimal(priSix_smCls.doubleValue()/total_smCls.doubleValue())));

			key=gradeNameArray[6].trim();
			if(smallClsMap.containsKey(key)){
				junOne_smCls=smallClsMap.get(key);
			}
			vo_smCls.setJuniorOneRat(ReportUtil.getFormatDecimal(new BigDecimal(junOne_smCls.doubleValue()/total_smCls.doubleValue())));

			key=gradeNameArray[7].trim();
			if(smallClsMap.containsKey(key)){
				junTwo_smCls=smallClsMap.get(key);
			}
			vo_smCls.setJuniorTwoRat(ReportUtil.getFormatDecimal(new BigDecimal(junTwo_smCls.doubleValue()/total_smCls.doubleValue())));


			key=gradeNameArray[8].trim();
			if(smallClsMap.containsKey(key)){
				junThree_smCls=smallClsMap.get(key);
			}
			vo_smCls.setJuniorThreeRat(ReportUtil.getFormatDecimal(new BigDecimal(junThree_smCls.doubleValue()/total_smCls.doubleValue())));

			key=gradeNameArray[9].trim();
			if(smallClsMap.containsKey(key)){
				senOne_smCls=smallClsMap.get(key);
			}
			vo_smCls.setSeniorOneRat(ReportUtil.getFormatDecimal(new BigDecimal(senOne_smCls.doubleValue()/total_smCls.doubleValue())));
			BigDecimal temp1=new BigDecimal(senOne_smCls/total_smCls);
			String temp1_str=temp1.toString();


			key=gradeNameArray[10].trim();
			if(smallClsMap.containsKey(key)){
				senTwo_smCls=smallClsMap.get(key);
			}
			vo_smCls.setSeniorTwoRat(ReportUtil.getFormatDecimal(new BigDecimal(senTwo_smCls.doubleValue()/total_smCls.doubleValue())));

			key=gradeNameArray[11].trim();
			if(smallClsMap.containsKey(key)){
				senThree_smCls=smallClsMap.get(key);
			}
			vo_smCls.setSeniorThreeRat(ReportUtil.getFormatDecimal(new BigDecimal(senThree_smCls.doubleValue()/total_smCls.doubleValue())));
		}


		//小班

		//Map gradeNameMap=null;
		volist.add(vo_oneOnOne);
		volist.add(vo_smCls);

		//volist.add
		dataPackage.setDatas(volist);

		return dataPackage;

	}

	@Override
	public Map<String, Integer> getMiniClassFirstSchoolTimeStatistics(String miniClassId, String startDate, String endDate) {
		Map<String,Integer> map=new LinkedHashMap<String, Integer>();
		if(StringUtil.isNotBlank(miniClassId)&&StringUtil.isNotBlank(startDate)&&StringUtil.isNotBlank(endDate)){
			map= reportDao.getMiniClassFirstSchoolTimeStatistics(miniClassId,startDate,endDate);
		}
		return map;
	}

	/**
	 * 根据sql查询报表数据（动态表头）
	 * @param sql
	 * @param dp
     * @return
     */
	public DataPackage getReportDataBySql(String sql, DataPackage dp) {
		if (dp!= null &&StringUtils.isNotBlank(dp.getSord()) && StringUtils.isNotBlank(dp.getSidx())) {
			// 前端提交的排序
			sql = sql + " order by " + dp.getSidx() + " " + dp.getSord();
		}
		return null;
//		return reportDao.findMapPageBySQL(sql, dp, false);
	}


	/**
	 * 一对一课消老师视图
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeTeacherView(CourseConsumeTeacherVo vo, DataPackage dp) {
		return reportDao.getOneOnOneCourseConsumeTeacherView(vo,dp);
	}

	/**
	 * 一对一课消老师视图（学生维度）
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeStudentView(String startDate, String endDate, OrganizationType organizationType, String blCampusId, DataPackage dp) {
		return reportDao.getOneOnOneCourseConsumeStudentView(startDate, endDate, organizationType, blCampusId, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 小班剩余资金
	 */
	@Override
	public DataPackage getMiniClassSurplusMoney(ReportMiniClassSurplusMoneyVo reportMiniClassSurplusMoneyVo, DataPackage dp) {
		return reportDao.getMiniClassSurplusMoney(reportMiniClassSurplusMoneyVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 学生剩余资金
	 */
	@Override
	public DataPackage getReportStudentSurplusFunding(ReportStudentSurplusFundingVo reportStudentSurplusFundingVo, DataPackage dp) {
		return reportDao.getReportStudentSurplusFunding(reportStudentSurplusFundingVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}


	/**
	 * 小班学生人数
	 */
	@Override
	public DataPackage getMiniClassStudentCount(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getMiniClassStudentCount(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	@Override
	public DataPackage getMiniClassConsumeAnalyze(String startDate, String endDate,
												  BasicOperationQueryLevelType basicOperationQueryLevelType,
												  String blCampusId, DataPackage dp) {
		return reportDao.getMiniClassConsumeAnalyze(startDate, endDate, basicOperationQueryLevelType, blCampusId, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 现金流
	 */
	@Override
	public DataPackage getFinanceAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getFinanceAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 现金流
	 */
	@Override
//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public DataPackage getFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getFinanceContractAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	@Override
	public DataPackage getFinanceContractAnalyzeRate(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		String yearAndMonth="";
		try {
			String month="";
			String year="";
			if(StringUtil.isNotEmpty(basicOperationQueryVo.getYearId())){
				DataDictVo yearVo = dataDictService.findDataDictById(basicOperationQueryVo.getYearId());
				year=yearVo.getName();
			}
			if(StringUtil.isNotEmpty(basicOperationQueryVo.getMonthId())){
				MonthType.valueOf(basicOperationQueryVo.getMonthId());
				switch (basicOperationQueryVo.getMonthId()) {
					case "JAN":month="01";break;
					case "FEB":month="02";break;
					case "MAR":month="03";break;
					case "APR":month="04";break;
					case "MAY":month="05";break;
					case "JUN":month="06";break;
					case "JUL":month="07";break;
					case "AUG":month="08";break;
					case "SEPT":month="09";break;
					case "OCT":month="10";break;
					case "NOV":month="11";break;
					case "DEC":month="12";break;
					default:throw new ApplicationException("参数不正确");
				}
				if(StringUtil.isNotEmpty(year) && StringUtil.isNotEmpty(month)){
					yearAndMonth=year+"-"+month;
				}
			}
		} catch (Exception e) {
			throw new ApplicationException("参数不正确");
		}
		DataDictVo targetType = dataDictService.findDataDictById("DAT0000000251");
//		findDataDictByNameAndCateGory("金额", DataDictCategory.PLAN_TARGET_TYPE.getValue());
		if(targetType==null){
			throw new ApplicationException("请先在数据字典添加“金额”指标类型！");
		}
		return reportDao.getFinanceContractAnalyzeRate(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus(), yearAndMonth, targetType.getId());
	}

	/**
	 * 手机现金流
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getFinanceAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			return reportDao.getFinanceBrenchAnalyzeForMobile(basicOperationQueryVo, dp);
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) { 
			return reportDao.getFinanceCampusAnalyzeForMobile(basicOperationQueryVo, dp);
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			return reportDao.getFinanceUserAnalyzeForMobile(basicOperationQueryVo, dp);
		}
		
		return reportDao.getFinanceBrenchAnalyzeForMobile(basicOperationQueryVo, dp);
	}
	
	@Override
	public Map getFinanceAnalyzeForMobileLine(
			BasicOperationQueryVo vo,String type) {
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<Object, Object>> list= reportDao.getFinanceBrenchForMobileLine(vo);//获取数据
		
		Map<String, String> datemap = DateTools.getDayBetweenTowDay(vo.getStartDate(), vo.getEndDate());//得到两个日期间的日期map
		for (Map<Object, Object> map2 : list) {//  如果数据中有的日期，就从所有的map里面remove掉
			String transactionTime=map2.get("transactionTime") != null ? map2.get("transactionTime").toString() : "";
			if(datemap.containsKey(transactionTime)){
				datemap.remove(transactionTime);
			}
		}
		for (Object object : datemap.keySet()) {//  remove后的map  遍历生成  为0的数据
			Map<Object,Object> zeroMap=new HashMap<Object, Object>();
			zeroMap.put("transactionTime", object);
			zeroMap.put("countPaidTotalAmount", 0);
			list.add(zeroMap);
		}

		String preStartDate="";
		String preEndDate="";
		String nextStartDate="";
		String nextEndDate="";
		type=StringUtils.isEmpty(type)?"MONTH":type;//默认为月类型
		if("WEEK".equals(type)){
			nextStartDate=vo.getStartDate();
			nextEndDate=vo.getEndDate();
			preStartDate=DateTools.addDateToString(vo.getStartDate(), -7);
			preEndDate=DateTools.addDateToString(vo.getEndDate(), -7);
		}else if("MONTH".equals(type)){
			nextStartDate=DateTools.getDateToString(DateTools.getFirstDayOfMonth(vo.getEndDate()));//最后一个日期当月的第一天
			nextEndDate=vo.getEndDate();
			try {
				preStartDate=DateTools.getDateToString(DateTools.getLastMonthStart(vo.getEndDate()));//最后一个日期上个月的第一天
				preEndDate=DateTools.getDateToString(DateTools.getLastMonthEnd(vo.getEndDate()));//最后一个日期上个月的最后一天
			} catch (Exception e) {
				preStartDate=DateTools.addDateToString(vo.getStartDate(), -30);
				preEndDate=DateTools.addDateToString(vo.getEndDate(), -30);
			}
			
		}
		Map<String,String> rate= reportDao.getFinanceBrenchForMobileTotal(vo, preStartDate, preEndDate, nextStartDate, nextEndDate);//获取数据
		map.putAll(rate);
		map.put("data", list);
		return map;
	}
	
	
	/* (non-Javadoc)
	 * @see com.eduboss.service.ReportService#getCustomerTotalByCusType(com.eduboss.domainVo.BasicOperationQueryVo, com.eduboss.dto.DataPackage)
	 */
	@Override
	public DataPackage getCustomerTotalByCusType(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dataPackage) {
		return reportDao.getCustomerTotalByCusType(basicOperationQueryVo, dataPackage);
	}
	
	@Override
	public DataPackage getAllTotalForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dataPackage) {
		return reportDao.getAllTotalForMobile(basicOperationQueryVo, dataPackage);
	}
	/**
	 * 一对一学生统计
	 */
	@Override
	public DataPackage getOneOnOneStudentCountAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getOneOnOneStudentCountAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	
	/* (non-Javadoc)
	 * @see com.eduboss.service.ReportService#getStudentOOOStatusCount(com.eduboss.domainVo.BasicOperationQueryVo, com.eduboss.dto.DataPackage)
	 */
	@Override
	public DataPackage getStudentOOOStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getStudentOOOStatusCount(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}
	
	
	@Override
	public DataPackage getStudentOTMStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getStudentOTMStatusCount(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}
	/**
	 * 一对一剩余资金
	 */
	@Override
	public DataPackage getOneOnOneStudentRemainAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getOneOnOneStudentRemainAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 课消
	 */
	@Override
	public DataPackage getCourseConsomeAnalyse(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getCourseConsomeAnalyse(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 退费
	 */
	@Override
	public DataPackage getRefundAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getRefundAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 收入
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomingAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		User user = userService.getCurrentLoginUser();
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		Organization belongCampus = null;
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel())){
			belongCampus = userService.getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.CAMPUS);;
		}			
		//return reportDao.getIncomingAnalyze(basicOperationQueryVo, dp, user, belongCampus);
		return reportDao.getIncomingAnalyze_1849(basicOperationQueryVo, dp, user, belongCampus);
	}

	/**
	 * 营收列表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getIncomingAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			return reportDao.getIncomingBrenchForMobile(basicOperationQueryVo, dp);
		}else{
			return reportDao.getIncomingCampusForMobile(basicOperationQueryVo, dp);
		} 
	}

	/**
	 * 小班产品统计
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassPeopleAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		return reportDao.getMiniClassPeopleAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 刷新报表数据
	 * @throws SQLException
	 */
	@Override
	public void reflushReportData(ReflushReportSubject procedureName, String targetDay, String mappingDate, String orgId) throws SQLException {
		if (syncReflushReport.contains(procedureName)){
			throw new ApplicationException(ErrorCode.SYNC_REFLUSH_REPORT);
		} else {
			try {
				reportDao.saveProcedureRefreshs(procedureName.getValue());
				syncReflushReport.add(procedureName);
				if (ReflushReportSubject.FINANCE_ANALYSE.equals(procedureName)) {// 现金流
					operationCountService.updateOdsDayFinance(targetDay);
				} else if (ReflushReportSubject.FINANCE_CONTRACT_ANALYSE.equals(procedureName)) {// 现金流（合同）
					operationCountService.updateOdsDayFinanceContract(targetDay);
				} else if (ReflushReportSubject.TEACHER_COURSE_CONSUME.equals(procedureName)) {// 老师课消
					operationCountService.updateOdsDayCourseConsumeTeacherSubject(targetDay);
				} else if (ReflushReportSubject.STUDENT_COURSE_CONSUME.equals(procedureName)) {// 老师课消
					operationCountService.updateOdsDayCourseConsumeStudentSubject(targetDay);
				} else if (ReflushReportSubject.ONE_ON_ONE_COURSE_CONSUME.equals(procedureName)) {// 一对一课消
					operationCountService.updateOdsDayCourseConsume(targetDay);
				} else if (ReflushReportSubject.ONE_ON_ONE_STUDENT_NUMBER.equals(procedureName)) {// 一对一学生数
					operationCountService.updateOdsRealStudentCount(targetDay);
				} else if (ReflushReportSubject.ONE_ON_ONE_REMAIN_FINANCE.equals(procedureName)) {// 一对一剩余资金
					operationCountService.updateOdsRealStudentRemaining(targetDay);
				} else if (ReflushReportSubject.MINI_CLASS_COURSE_CONSUME.equals(procedureName)) {// 小班课消
					operationCountService.updateOdsDayMiniClassConsume(targetDay);
				} else if (ReflushReportSubject.MINI_CLASS_STUDENT_NUMBER.equals(procedureName)) {// 小班学生数
					operationCountService.updateOdsDayMiniClassStudentCount(targetDay);
				} else if (ReflushReportSubject.MINI_CLASS_REMAIN_FINANCE.equals(procedureName)) {// 小班剩余资金
					operationCountService.updateOdsDayMiniClassSurplusMoney(targetDay);
				} else if (ReflushReportSubject.REFUND_ANALYZE.equals(procedureName)) {// 退费统计
					operationCountService.updateOdsDayRefundAnalyze(targetDay);
				} else if (ReflushReportSubject.INCOMING_ANALYZE.equals(procedureName)) {// 退费统计
					operationCountService.updateOdsDayIncomingAnalyze(targetDay);
				} else if (ReflushReportSubject.MINI_CLASS_ATTENDS_ANALYZE.equals(procedureName)) {// 小班考勤统计
					operationCountService.updateMiniClassAttendsAnalyze(targetDay);
				} else if (ReflushReportSubject.STUDENT_PAY_CLASS_HOUR.equals(procedureName)) {//购买课时统计
					operationCountService.updatePayClassHour(targetDay);
				} else if (ReflushReportSubject.STUDENT_PAY_CLASS_HOUR_EVERY_DAY.equals(procedureName)) {//购买课时统计(每天)
					operationCountService.updatePayClassHourEveryDay(targetDay);
				} else if (ReflushReportSubject.STUDENT_PENDING_MONEY.equals(procedureName)) {// 待收款统计
					operationCountService.updateStudentPendingMoney(targetDay);
				} else if (ReflushReportSubject.MINI_CLASS_PEOPLE_ANALYZE.equals(procedureName)) {// 待收款统计
					operationCountService.updateMiniClassPeople(targetDay);
				} else if(ReflushReportSubject.STUDENT_PAY_CLASS_HOUR_CAMPUS.equals(procedureName)){
					operationCountService.updatePayClassHourCampus(targetDay);
				} else if(ReflushReportSubject.STUDENT_SURPLUS_FUNDING.equals(procedureName)){
					operationCountService.updateStudentSurplusFunding(targetDay);
				} else if(ReflushReportSubject.CONTRACT_BONUS_ORGANIZATION.equals(procedureName)){
					operationCountService.updateOdsDayContractBonusOrganization(targetDay);
				} else if(ReflushReportSubject.CONTRACT_BONUS_STAFF.equals(procedureName)){
					operationCountService.updateOdsDayContractBonusStaff(targetDay);
				}else if (ReflushReportSubject.MINI_CLASS_STUDENT_REAL_NUMBER.equals(procedureName)) {// 小班报读学生数
					operationCountService.updateOdsDayMiniClassStudentRealCount(targetDay);
				}else if (ReflushReportSubject.PRODUCT_MARKET_ANALYZE.equals(procedureName) ){  // 产品销售统计
					operationCountService.updateOdsDayProductMarket(targetDay, targetDay);
				}else if (ReflushReportSubject.PRODUCT_CONSUME_ANALYZE.equals(procedureName) ){  // 产品消耗统计
					operationCountService.updateOdsDayProductConsume(targetDay, targetDay);
				} else if (ReflushReportSubject.ONE_ON_ONE_STUDENT_OOO_NUMBER.equals(procedureName)) {// 一对一学生状态统计
					operationCountService.updateOdsRealStudentOOOCount(targetDay);
				} else if (ReflushReportSubject.ONE_TO_MANY_REMAIN_FINANCE.equals(procedureName)) {
					operationCountService.updateOdsOtmRealStudentRemaining(targetDay);
				} else if (ReflushReportSubject.STUDENT_OTM_NUMBER.equals(procedureName)) {
					operationCountService.updateOdsRealStudentOTMCount(targetDay);
				} else if (ReflushReportSubject.MONTHLY_BALANCE.equals(procedureName)) {
					operationCountService.updateOdsDayMonthlyBalance(targetDay);
				} else if(ReflushReportSubject.FINANCE_MONTHLY_EVIDENCE.equals(procedureName)) {
					operationCountService.updateOdsFinanceMonthlyEvidence(targetDay, mappingDate);
				} else if(ReflushReportSubject.INCOME_MONTHLY_EVIDENCE.equals(procedureName)) {
					operationCountService.updateOdsIncomeMonthlyEvidence(targetDay, mappingDate);
				} else if (ReflushReportSubject.INCOME_MONTHLY_CAMPUS.equals(procedureName)) {
					operationCountService.updateOdsIncomeMonthlyStudentCampusByCampus(targetDay, mappingDate, orgId);
				}
			}finally{
				syncReflushReport.remove(procedureName);
			}
		}
	}


	/**
	 * 学生待收款资金
	 */
	public DataPackage getStudentPendingMoney(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getStudentPendingMoney(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 购买课时数
	 */
	public DataPackage getPayClassHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getPayClassHour(basicOperationQueryVo, dp);
	}

	/**
	 * 购买课时数
	 */
	public DataPackage getPayClassHourEveryDay(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getPayClassHour(basicOperationQueryVo, dp);
	}

	/**
	 * 购买课时数(校区维度)
	 */
	public DataPackage getPayClassHourCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getPayClassHourCampus(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 小班扣费统计
	 */
	public DataPackage getMiniClassKoufeiTongji(BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId, DataPackage dp,String anshazhesuan) {
		User user = userService.getCurrentLoginUser();
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		Organization belongCampus = null;
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel())){
			belongCampus = userService.getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.CAMPUS);;
		}			
		if ("ankeshi".equals(anshazhesuan)){
			return reportDao.getMiniClassKoufeiTongji(basicOperationQueryVo,dp,miniClassTypeId, user, belongCampus);
		}else {
			return reportDao.getMiniClassKoufeiTongjiHour(basicOperationQueryVo,dp,miniClassTypeId, user, belongCampus);
		}

	}
	
	/**
	 * 小班考勤统计
	 */
	public DataPackage getMiniClassKaoqinTongji(BasicOperationQueryVo basicOperationQueryVo,String miniClassTypeId, DataPackage dp,String anshazhesuan) {
		if ("ankeshi".equals(anshazhesuan)){
			return reportDao.getMiniClassKaoqinTongji(basicOperationQueryVo,dp,miniClassTypeId, userService.getCurrentLoginUser(), userService.getBelongCampus());
		}else {
			return reportDao.getMiniClassKaoqinTongjiHour(basicOperationQueryVo,dp,miniClassTypeId, userService.getCurrentLoginUser(), userService.getBelongCampus());
		}
	}



	/**
	 * 每天分配课时数
	 */
	public DataPackage getOsdMoneyArrangeRecord(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		return reportDao.getOsdMoneyArrangeRecord(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	public DataPackage getContractBonusOrganization(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		//return reportDao.getContractBonusOrganization(basicOperationQueryVo, dp);
		return reportDao.getContractBonusOrganization_1849(basicOperationQueryVo, dp);
	}

	public DataPackage getContractBonusStaff(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, String flag) {
//		if (StringUtils.isBlank(flag)){
//			DataPackage contractBonusStaff = reportDao.getContractBonusStaff(basicOperationQueryVo, dp);
//			List list =(List)contractBonusStaff.getDatas();
////			list = setBranchData(list, basicOperationQueryVo, contractBonusStaff);
////			list = setCampusData(list, basicOperationQueryVo, contractBonusStaff);
//			contractBonusStaff.setDatas(list);
//			return contractBonusStaff;
//		}else if ("isBrench".equals(flag)){
//			DataPackage contractBonusStaff = reportDao.getContractBonusBranch(basicOperationQueryVo, dp);
//			return contractBonusStaff;
//		}else  if ("isCampus".equals(flag)){
//			DataPackage contractBonusStaff = reportDao.getContractBonusCampus(basicOperationQueryVo, dp);
//			return contractBonusStaff;
//		}else {
//			return null;
//		}
		//return reportDao.getContractBonusStaff2(basicOperationQueryVo, dp,flag);
		return reportDao.getContractBonusStaff_1849(basicOperationQueryVo, dp,flag);
	}

	private List<Map<Object, Object>> setBranchData(List<Map<Object, Object>> list, BasicOperationQueryVo basicOperationQueryVo, DataPackage contractBonusStaff) {
		String brenchs="";
		for (Map<Object, Object> map : list){
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
				String brench = (String)map.get("brenchId");
				brenchs+=""+brench.split("_")[0]+",";
				List<Map<Object, Object>> result = getBranchBonusList(basicOperationQueryVo, brench);
				addBonus(map, result);
			}

		}
		if(brenchs.length()>0){
			brenchs=brenchs.substring(0, brenchs.length()-1);
			list.addAll(getOtherBranchBonusList(basicOperationQueryVo, brenchs.split(",")));
		} else {
			list.addAll(getOtherBranchBonusList(basicOperationQueryVo, null));
		}
		

		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			Map branch = new HashMap<>();
			Map<Object, Object> map = list.get(0);
			if (map!=null){

				String brench = (String) map.get("brenchId");
				String brenchId = brench.split("_")[0];
				List result = getBranchBonusList(basicOperationQueryVo, brenchId);
				if (result.size()==1){
					Map o = (Map)result.get(0);
					branch.put("groupId", map.get("groupId"));
					branch.put("brenchId", map.get("brenchId"));
					branch.put("campusId", map.get("brenchId"));
					branch.put("bonusAmount_new", o.get("bonusAmount_new"));
					branch.put("bonusAmount_re", o.get("bonusAmount_re"));
					branch.put("bonusAmount", o.get("bonusAmount"));
					branch.put("refundAmount", o.get("refundAmount"));
					branch.put("BL_CAMPUS_ID", map.get("BL_CAMPUS_ID"));
					branch.put("STUDENT_ID", map.get("STUDENT_ID"));
					branch.put("SIGN_STAFF_ID", map.get("SIGN_STAFF_ID"));
					branch.put("CONTRACT_TYPE", map.get("CONTRACT_TYPE"));
					branch.put("isBrench", true);
					list.add(branch);
					int rowCount = contractBonusStaff.getRowCount();
					rowCount++;
					contractBonusStaff.setRowCount(rowCount);
				}
				
				if(!brenchs.contains(brenchId)){
					brenchs+="'"+brenchId+"',";
				}

			}
		}
		
		if(brenchs.length()>0 && brenchs.substring(brenchs.length()-1, brenchs.length()).equals(",")){
			brenchs=brenchs.substring(0,brenchs.length()-1);
			list.addAll(getOtherCampusBonusList(basicOperationQueryVo, brenchs.split(",")));
		} else {
			list.addAll(getOtherCampusBonusList(basicOperationQueryVo, null));
		}
		
		return list;
	}

	private List<Map<Object, Object>> getBranchBonusList(BasicOperationQueryVo basicOperationQueryVo, String brench) {
		Map<String, Object> params = new HashMap<String, Object>();
		String brenchId = brench.split("_")[0];
		StringBuffer sql = new StringBuffer();
		sql.append(" select SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_new, ");
		sql.append(" SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_re,");
		sql.append(" SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) as bonusAmount,");
		sql.append(" SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END) as refundAmount");
		sql.append(" FROM income_distribution f ");
		sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
		sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
		sql.append(" left join organization o on f.organizationId= o.id ");
		sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.sub_bonus_type= 'USER_BRANCH' ");
		sql.append(" AND f.organizationId = :brenchId ");
		params.put("brenchId", brenchId);
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
            sql.append(" AND fch.TRANSACTION_TIME >= '"+basicOperationQueryVo.getStartDate()+" 00:00:00' ");
            params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
        }
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
            sql.append(" AND fch.TRANSACTION_TIME <= '"+basicOperationQueryVo.getEndDate()+" 23:59:59' ");
            params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
        }
		if(basicOperationQueryVo.getProductType()!=null){
            sql.append(" AND f.product_type = '"+basicOperationQueryVo.getProductType()+"' ");
            params.put("productType", basicOperationQueryVo.getProductType());
        }
		if (basicOperationQueryVo.getContractType()!=null) {
            if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
                sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
            }else {
                sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE='"+basicOperationQueryVo.getContractType()+"'");
            }
        }
		
		sql.append(" group by  f.organizationId ");
		return incomeDistributionDao.findMapBySql(sql.toString(), params);
	}
	
	
	private List<Map<Object, Object>> getOtherBranchBonusList(BasicOperationQueryVo basicOperationQueryVo, String[] brenches) {
//		String brenchId = brench.split("_")[0];
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("select true AS isBrench, ");
		sql.append("	CONCAT(o.parentId,'_','星火集团') AS groupId,     ");
		sql.append("    CONCAT(o.id,'_',o.name ) AS brenchId,  ");
		sql.append("    '' AS campusId,  ");

		sql.append("    CONCAT(fch.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
		sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
		sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
				+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");


		if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
			if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
				sql.append(" '' as contractType, '' as productType,");
			}else{
				sql.append(" c.CONTRACT_TYPE as contractType, '' as productType,");
			}
		}else{
			sql.append(" f.product_type as productType,'' as contractType,");
		}


		sql.append("	CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_new, ");
		sql.append("    CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_re, ");
		sql.append("	CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END as bonusAmount, ");
		sql.append("	CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
		sql.append(" FROM income_distribution f ");
		sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
		sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
		sql.append(" left join organization o on f.organizationId= o.id ");
		sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.sub_bonus_type= 'USER_BRANCH'  ");
		
		
		if (brenches != null && brenches.length > 0){
			sql.append(" AND f.organizationId not in (:brenches) ");
			params.put("brenches", brenches);
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
            sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", basicOperationQueryVo.getStartDate()+" 00:00:00");
        }
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
            sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
            params.put("endDate", basicOperationQueryVo.getEndDate()+" 23:59:59");
        }
		if(basicOperationQueryVo.getProductType()!=null){
            sql.append(" AND f.product_type = :productType ");
            params.put("productType", basicOperationQueryVo.getProductType());
        }
		if (basicOperationQueryVo.getContractType()!=null) {
            if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
                sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
            }else {
                sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
                params.put("contractType", basicOperationQueryVo.getContractType());
            }
        }
		
		
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			 params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append("  and (");
            sql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(" )");
        }
		
		sql.append(" group by  f.organizationId ");
		return incomeDistributionDao.findMapBySql(sql.toString(), params);
	}
	
	
	private List<Map<Object, Object>> getOtherCampusBonusList(BasicOperationQueryVo basicOperationQueryVo, String[] brenches) {
//		String brenchId = brench.split("_")[0];
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("select true AS isBrench, ");
		sql.append("	CONCAT(o.parentId,'_','星火集团') AS groupId,     ");
		sql.append("    CONCAT(o2.id,'_',o2.name ) AS brenchId,  ");
		sql.append("    CONCAT(o.id,'_',o.name ) AS campusId,  ");

		sql.append("    CONCAT(fch.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
		sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
		sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
				+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");

		if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
			if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
				sql.append(" '' as contractType, '' as productType,");
			}else{
				sql.append(" c.CONTRACT_TYPE as contractType, '' as productType,");
			}
		}else{
			sql.append(" f.product_type as productType,'' as contractType,");
		}

		sql.append("	CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_new, ");
		sql.append("    CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_re, ");
		sql.append("	CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END as bonusAmount, ");
		sql.append("	CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
		sql.append(" FROM income_distribution f ");
		sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
		sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
		sql.append(" left join organization o on f.organizationId= o.id ");
		sql.append(" left join organization o2 on o.parentId= o2.id ");
		sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.sub_bonus_type= 'USER_BRANCH'  ");
		
		
		if (brenches != null && brenches.length > 0){
			sql.append(" AND o2.id not in (:brenches) ");
			params.put("brenches", brenches);
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
            sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
        }
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
            sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
            params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
        }
		if(basicOperationQueryVo.getProductType()!=null){
            sql.append(" AND f.product_type = :productType ");
            params.put("productType", basicOperationQueryVo.getProductType());
        }
		if (basicOperationQueryVo.getContractType()!=null) {
            if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
                sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
            }else {
                sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
                params.put("contractType", basicOperationQueryVo.getContractType());
            }
        }
		
		 if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} 
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append("  and (");
            sql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(" )");
        }
		
		sql.append(" group by  o2.id ");
		return incomeDistributionDao.findMapBySql(sql.toString(), params);
	}

	private void addBonus(Map<Object, Object> map, List<Map<Object, Object>> result) {
		if (result.size()==1){
			Map<Object, Object> o = (Map<Object, Object>)result.get(0);
            BigDecimal bonusAmount_new = (BigDecimal)map.get("bonusAmount_new");
            bonusAmount_new = bonusAmount_new.add((BigDecimal)o.get("bonusAmount_new"));
            map.put("bonusAmount_new", bonusAmount_new);

            BigDecimal bonusAmount_re = (BigDecimal)map.get("bonusAmount_re");
            bonusAmount_re = bonusAmount_re.add((BigDecimal)o.get("bonusAmount_re"));
            map.put("bonusAmount_re", bonusAmount_re);

            BigDecimal bonusAmount = (BigDecimal)map.get("bonusAmount");
            bonusAmount = bonusAmount.add((BigDecimal)o.get("bonusAmount"));
            map.put("bonusAmount", bonusAmount);

            BigDecimal refundAmount = (BigDecimal)map.get("refundAmount");
            refundAmount = refundAmount.add((BigDecimal)o.get("refundAmount"));
            map.put("refundAmount", refundAmount);

        }
	}

	private List<Map<Object, Object>> setCampusData(List<Map<Object, Object>> list, BasicOperationQueryVo basicOperationQueryVo, DataPackage contractBonusStaff) {
		Map<String, Object> params = new HashMap<String, Object>();
		for (Map<Object, Object> map : list){
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
				String brench = (String)map.get("brenchId");
				List result = getCampusBonusList(basicOperationQueryVo, brench);
				addBonus(map, result);
			}

			if (map.get("isBrench")==null && BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
				String campus = (String)map.get("campusId");
				List result = getCampusIncomeList(basicOperationQueryVo, campus);
				addBonus(map, result);
			}

		}

		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			StringBuffer sb = new StringBuffer();
			for (Map<Object, Object> map:list){
				if (map.get("isBrench")==null){
					String campusIdAndName = (String)map.get("campusId");
					sb.append(campusIdAndName.split("_")[0]).append(",");
				}
			}

			sb = sb.delete(sb.lastIndexOf(","),sb.lastIndexOf(",")+1);
			String campusIds = sb.toString();
			Map<Object, Object> map = list.get(0);
			String group = (String)map.get("groupId");
			String brench = (String)map.get("brenchId");
			String brenchId = brench.split("_")[0];
			StringBuffer sql = new StringBuffer();
			sql.append(" select :group as groupId , ");
			params.put("group", group);
			sql.append(" :brench as brenchId , ");
			params.put("brench", brench);
			sql.append(" CONCAT(f.organizationId,'_',(SELECT o.name FROM organization o WHERE o.id = f.organizationId)) AS campusId ,");
			sql.append(" SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_new, ");
			sql.append(" SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_re,");
			sql.append(" SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) as bonusAmount,");
			sql.append(" SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END) as refundAmount , ");
			sql.append("  c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
			sql.append(" FROM income_distribution f ");
			sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
			sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
			sql.append(" , organization branch,organization campus");
			sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
			sql.append(" AND f.sub_bonus_type= 'USER_CAMPUS' ");
			sql.append(" AND f.organizationId = campus.id ");
			sql.append(" AND campus.parentID = branch.id");
			sql.append(" AND branch.id = :brenchId ");
			params.put("brenchId", brenchId);
			if (campusIds.length() > 0) {
				sql.append(" AND campus.id not in (:campusIds) ");
				params.put("campusIds", campusIds.split(","));
			}
			params.put("brenchId", brenchId);
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
				params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
				params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			if(basicOperationQueryVo.getProductType()!=null){
				sql.append(" AND f.product_type = :productType ");
				params.put("productType", basicOperationQueryVo.getProductType());
			}
			if (basicOperationQueryVo.getContractType()!=null) {
				if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
					sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
				}else {
					sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
					params.put("contractType", basicOperationQueryVo.getContractType());
				}
			}
			sql.append(" group by  f.organizationId ");
			List<Map<Object, Object>> campusList = incomeDistributionDao.findMapBySql(sql.toString(), params);
			int rowCount = contractBonusStaff.getRowCount();
			rowCount = rowCount+campusList.size();
			contractBonusStaff.setRowCount(rowCount);
			list.addAll(campusList);
//			List result = getCampusIncomeList(basicOperationQueryVo, campus);
		}

		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			Map campus = new HashMap<>();
			if (list.size()>0){
				Map<Object, Object> map = list.get(0);
				if (map!=null){
					String campusIdAndName = (String) map.get("campusId");
					String campusId = campusIdAndName.split("_")[0];
					List result = getCampusIncomeList(basicOperationQueryVo, campusId);
					if (result.size()==1){
						Map o = (Map)result.get(0);
						BigDecimal bonusAmount_new = (BigDecimal)o.get("bonusAmount_new");
						BigDecimal bonusAmount_re = (BigDecimal)o.get("bonusAmount_re");
						BigDecimal bonusAmount = (BigDecimal)o.get("bonusAmount");
						BigDecimal refundAmount = (BigDecimal)o.get("refundAmount");
						if (BigDecimal.ZERO.compareTo(bonusAmount_new)==0&&BigDecimal.ZERO.compareTo(bonusAmount_re)==0&&BigDecimal.ZERO.compareTo(bonusAmount)==0&&BigDecimal.ZERO.compareTo(refundAmount)==0){
							return list;
						}
						campus.put("groupId", map.get("groupId"));
						campus.put("brenchId", map.get("brenchId"));
						campus.put("campusId", map.get("campusId"));
						campus.put("bonusStaffId", map.get("campusId"));
						campus.put("bonusAmount_new", o.get("bonusAmount_new"));
						campus.put("bonusAmount_re", o.get("bonusAmount_re"));
						campus.put("bonusAmount", o.get("bonusAmount"));
						campus.put("refundAmount", o.get("refundAmount"));
						campus.put("BL_CAMPUS_ID", map.get("BL_CAMPUS_ID"));
						campus.put("STUDENT_ID", map.get("STUDENT_ID"));
						campus.put("SIGN_STAFF_ID", map.get("SIGN_STAFF_ID"));
						campus.put("CONTRACT_TYPE", map.get("CONTRACT_TYPE"));
						campus.put("isCampus", true);
						list.add(campus);
						int rowCount = contractBonusStaff.getRowCount();
						rowCount++;
						contractBonusStaff.setRowCount(rowCount);
					}
				}
			}else {
				if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())){
					String campusId = basicOperationQueryVo.getBlCampusId();

					List result = getCampusIncomeList(basicOperationQueryVo, campusId);
					if (result.size()==1){
						Map<String, Object> sqlParams = new HashMap<String, Object>();
						sqlParams.put("campusId", campusId);
						String sql = " SELECT CONCAT(g.id,'_',g.name)  AS groupId, CONCAT(b.id,'_',b.name) AS brenchId ,  CONCAT(c.id,'_',c.name) AS campusId from organization g , organization b, organization c WHERE g.id = b.parentId AND b.id = c.parentId AND c.ID = :campusId ";
						List orgList = organizationDao.findMapBySql(sql, sqlParams);
						if (orgList.size()==1){
							Map org = (Map)orgList.get(0);
							Map o = (Map)result.get(0);
							campus.put("groupId", org.get("groupId"));
							campus.put("brenchId", org.get("brenchId"));
							campus.put("campusId", org.get("campusId"));
							campus.put("bonusStaffId", org.get("campusId"));
							campus.put("bonusAmount_new", o.get("bonusAmount_new"));
							campus.put("bonusAmount_re", o.get("bonusAmount_re"));
							campus.put("bonusAmount", o.get("bonusAmount"));
							campus.put("refundAmount", o.get("refundAmount"));
							campus.put("isCampus", true);
							list.add(campus);
							int rowCount = contractBonusStaff.getRowCount();
							rowCount++;
							contractBonusStaff.setRowCount(rowCount);
						}

					}
				}
			}

		}

		return list;
	}

	private List<Map<Object, Object>> getCampusIncomeList(BasicOperationQueryVo basicOperationQueryVo, String campus) {
		Map<String, Object> params = new HashMap<String, Object>();
		String campusId = campus.split("_")[0];
		StringBuffer sql = new StringBuffer();
		sql.append(" select IFNULL(SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) AS bonusAmount_new, ");
		sql.append(" IFNULL(SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) AS bonusAmount_re,");
		sql.append(" IFNULL(SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) as bonusAmount,");
		sql.append(" IFNULL(SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END), 0) as refundAmount");
		sql.append(" FROM income_distribution f ");
		sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
		sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
		sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.sub_bonus_type= 'USER_CAMPUS' ");
		sql.append(" AND f.organizationId = :campusId ");
		params.put("campusId", campusId);
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
            sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
        }
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
            sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
            params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
        }
		if(basicOperationQueryVo.getProductType()!=null){
            sql.append(" AND f.product_type = :productType ");
            params.put("productType", basicOperationQueryVo.getProductType());
        }
		if (basicOperationQueryVo.getContractType()!=null) {
            if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
                sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
            }else {
                sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
                params.put("contractType", basicOperationQueryVo.getContractType());
            }
        }
		return incomeDistributionDao.findMapBySql(sql.toString(), params);
	}

	private List<Map<Object, Object>> getCampusBonusList(BasicOperationQueryVo basicOperationQueryVo, String brench) {
		Map<String, Object> params = new HashMap<String, Object>();
		String brenchId = brench.split("_")[0];
		StringBuffer sql = new StringBuffer();
		sql.append(" select IFNULL(SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) AS bonusAmount_new, ");
		sql.append(" IFNULL(SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) AS bonusAmount_re,");
		sql.append(" IFNULL(SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END), 0) as bonusAmount,");
		sql.append(" IFNULL(SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END), 0) as refundAmount");
		sql.append(" FROM income_distribution f ");
		sql.append(" INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID ");
		sql.append(" left JOIN contract c ON fch.CONTRACT_ID=c.ID ");
		sql.append(" , organization branch,organization campus");
		sql.append(" where fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.sub_bonus_type= 'USER_CAMPUS' ");
		sql.append(" AND f.organizationId = campus.id ");
		sql.append(" AND campus.parentID = branch.id");
		sql.append(" AND branch.id = :brenchId ");
		params.put("brenchId", brenchId);
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
            sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
        }
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
            sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
            params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
        }
		if(basicOperationQueryVo.getProductType()!=null){
            sql.append(" AND f.product_type = :productType ");
            params.put("productType", basicOperationQueryVo.getProductType());
        }
		if (basicOperationQueryVo.getContractType()!=null) {
            if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
                sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
            }else {
                sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
                params.put("contractType", basicOperationQueryVo.getContractType());
            }
        }
		return incomeDistributionDao.findMapBySql(sql.toString(), params);
	}


	public DataPackage getMiniClassCourseSeries(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		return reportDao.getMiniClassCourseSeries(basicOperationQueryVo, dp);
	}

	@Override
	public DataPackage getMiniClassMemberTotal(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			String catchStudentStatu, String profitStatu,
			String lowNormalStudent) {
		return reportDao.getMiniClassCourseSeries(basicOperationQueryVo, dp,catchStudentStatu,profitStatu,lowNormalStudent);
	}

	@Override
	public DataPackage getMiniClassMemberTotalSum(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		// TODO Auto-generated method stub
		return reportDao.getMiniClassMemberTotalSum(basicOperationQueryVo, dp);
	}
	
	@Override
	public DataPackage getPlanManagementTotal(String year, String goalType,
			String targetType, DataPackage dp, String monthOrquarter) {
		String thisYear="2015";
		Integer preYear=2014;
		try{
			DataDictVo yeardo = dataDictService.findDataDictById(year);
			thisYear=yeardo.getName();
			preYear=Integer.parseInt(thisYear)-1;
		}catch(Exception e){
			throw new ApplicationException("查询参数有误！");
		}
		if ("month".equals(monthOrquarter)){
			return reportDao.getPlanManagementTotal(preYear, thisYear, goalType, targetType, dp);
		}else if ("quarter".equals(monthOrquarter)){
			return reportDao.getPlanManagementTotalForQUARTER(preYear, thisYear, goalType, targetType, dp);
		}
		return null;
	}
	@Override
	public DataPackage getMiniClassStudentRealCount(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp) {
		return reportDao.getMiniClassStudentRealCount(basicOperationQueryVo, miniClassStatu,  dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}
	
	@Override
	public DataPackage getMiniClassStudentRealCountDetail(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, String type) {
		return reportDao.getMiniClassStudentRealCountDetail(basicOperationQueryVo, miniClassStatu,  dp, userService.getCurrentLoginUser(), userService.getBelongCampus(),type);
	}
	
	/**
	 *  老师小班课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp) {
		String anshazhesuan = courseConsumeTeacherVo.getAnshazhesuan(); //按什么结算;
		if ("ankeshi".equals(anshazhesuan)){
			return reportDao.getMiniCourseConsumeTeacherView(courseConsumeTeacherVo, dp);
		} else {
			return reportDao.getMiniCourseConsumeTeacherViewHours(courseConsumeTeacherVo, dp);
		}
	}
	
	/**
	 *  老师一对一课时年级
	 * @param courseConsumeTeacherVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOooCourseConsumeTeacher(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp) {
			return reportDao.getOneOnOneCourseConsumeTeacher(courseConsumeTeacherVo, dp);
	}

	/**
	 * 老师1对1+小班+一对多课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param dp
     * @return
     */
	public DataPackage getOooMiniOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp) {

		String startDate=courseConsumeTeacherVo.getStartDate();
		String endDate = courseConsumeTeacherVo.getEndDate();
		OrganizationType organizationType=courseConsumeTeacherVo.getOrganizationType();
		String blCampusId=courseConsumeTeacherVo.getBlCampusId();
		String anshazhesuan = courseConsumeTeacherVo.getAnshazhesuan(); //按什么结算;
		String subject=courseConsumeTeacherVo.getSubject();

		String[] productTypesArrays =null;
		String productTypeIds = courseConsumeTeacherVo.getProductTypeIds();
		if (productTypeIds!=null&&StringUtil.isNotBlank(productTypeIds)){
			productTypesArrays = productTypeIds.split(",");
		}

		List<String> productTypes=null;
		if (productTypesArrays!=null){
			 productTypes = Arrays.asList(productTypesArrays);
		}

		List<Map> miniList =null;
		List<Map> oneOnOneList = null;
		List<Map> otmList = null;
		if ("ankeshi".equals(anshazhesuan)){
			// 按课时查询
			if (productTypes!=null&&productTypes.contains("SMALL_CLASS")){
				dp = reportDao.getMiniCourseConsumeTeacherView(courseConsumeTeacherVo, dp);
				miniList = (List<Map>) dp.getDatas();
			}

			if (productTypes!=null&&productTypes.contains("ONE_ON_ONE_COURSE")){
				dp = reportDao.getOneOnOneCourseConsumeTeacher(courseConsumeTeacherVo,dp);
				oneOnOneList = (List<Map>) dp.getDatas();
			}

			if (productTypes!=null&&productTypes.contains("ONE_ON_MANY")){
				dp = reportDao.getOtmCourseConsumeTeacherView(courseConsumeTeacherVo, null, dp);
				otmList = (List<Map>) dp.getDatas();
			}

		}else if ("anxiaoshi".equals(anshazhesuan)){
			// 按小时查询
			if (productTypes!=null&&productTypes.contains("SMALL_CLASS")){
				dp = reportDao.getMiniCourseConsumeTeacherViewHours(courseConsumeTeacherVo, dp);
				miniList = (List<Map>) dp.getDatas();
			}

			if (productTypes!=null&&productTypes.contains("ONE_ON_ONE_COURSE")){
				dp = reportDao.getOneOnOneCourseConsumeTeacher(courseConsumeTeacherVo,dp);
				oneOnOneList = (List<Map>) dp.getDatas();
			}

			if (productTypes!=null&&productTypes.contains("ONE_ON_MANY")){
				dp = reportDao.getOtmCourseConsumeTeacherViewHours(courseConsumeTeacherVo,null,dp);
				otmList = (List<Map>) dp.getDatas();
			}
		}


		List<Map> oooMiniList = new ArrayList<Map>();
		
		List<Map> allList = new ArrayList<Map>();
		
		if (OrganizationType.GROUNP.equals(organizationType)) {
			oooMiniList = this.uniteCourseConsumeTeacherView(oneOnOneList, miniList, oooMiniList, "BRENCH");
			allList = this.uniteCourseConsumeTeacherView(oooMiniList, otmList, allList, "BRENCH");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			oooMiniList = this.uniteCourseConsumeTeacherView(oneOnOneList, miniList, oooMiniList, "CAMPUS");
			allList = this.uniteCourseConsumeTeacherView(oooMiniList, otmList, allList, "CAMPUS");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			oooMiniList = this.uniteCourseConsumeTeacherView(oneOnOneList, miniList, oooMiniList, "TEACHER_NAME");
			allList = this.uniteCourseConsumeTeacherView(oooMiniList, otmList, allList, "TEACHER_NAME");
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			oooMiniList = this.uniteCourseConsumeTeacherView(oneOnOneList, miniList, oooMiniList, "COURSE_CAMPUS_ID");
			allList = this.uniteCourseConsumeTeacherView(oooMiniList, otmList, allList, "COURSE_CAMPUS_ID");
		}
		
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : allList) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}
			
			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}
			
			newlist.add(map);
		}
		
		
		dp.setDatas(newlist);
		dp.setRowCount(allList.size());
		return dp;
	}
	
	private List<Map> uniteCourseConsumeTeacherView(List<Map> oneOnOneList, List<Map> miniList, List<Map> allList, String type) {
		Map<String, Map> allMap = new HashMap<String, Map>();
		String mapKey = "";
		if (oneOnOneList!=null){
			for (Map map : oneOnOneList) {
				mapKey = (String) map.get(type);
				if (type.equals("TEACHER_NAME")) {
					mapKey = (String) map.get(type) + "_" + (String) map.get("COURSE_CAMPUS_ID");
				}
				allMap.put(mapKey, map);
			}
		}

		if (miniList!=null){
			for (Map map : miniList) {
				mapKey = (String) map.get(type);
				if (type.equals("TEACHER_NAME")) {
					mapKey = (String) map.get(type) + "_" + (String) map.get("COURSE_CAMPUS_ID");
				}
				if (allMap.containsKey(mapKey)) {
					Map tempMap = allMap.get(mapKey);
					this.unitTwoMap(tempMap, map);
				} else {
					allMap.put(mapKey, map);
				}
			}
		}

		
		for (Map.Entry entry : allMap.entrySet()) {
			allList.add((Map) entry.getValue());
		}
		
		if (type.equals("TEACHER_NAME")) {
			Collections.sort(allList, new Comparator<Map>() {
				public int compare(Map o1, Map o2) {
					String b1 = (String) o1.get("TEACHER_NAME");
		            String b2 = (String) o2.get("TEACHER_NAME");
		            if (b2 != null) {
		                return b2.compareTo(b1);
		            }
		            return 0;
				}
			});
		}
		return allList;
	}  
	
	private void unitTwoMap(Map tempMap, Map map) {
		this.unitTwoEntry(tempMap, map, "FIRST_GRADE");
		this.unitTwoEntry(tempMap, map, "SECOND_GRADE");
		this.unitTwoEntry(tempMap, map, "THIRD_GRADE");
		this.unitTwoEntry(tempMap, map, "FOURTH_GRADE");
		this.unitTwoEntry(tempMap, map, "FIFTH_GRADE");
		this.unitTwoEntry(tempMap, map, "SIXTH_GRADE");
		this.unitTwoEntry(tempMap, map, "MIDDLE_SCHOOL_FIRST_GRADE");
		this.unitTwoEntry(tempMap, map, "MIDDLE_SCHOOL_SECOND_GRADE");
		this.unitTwoEntry(tempMap, map, "MIDDLE_SCHOOL_THIRD_GRADE");
		this.unitTwoEntry(tempMap, map, "HIGH_SCHOOL_FIRST_GRADE");
		this.unitTwoEntry(tempMap, map, "HIGH_SCHOOL_SECOND_GRADE");
		this.unitTwoEntry(tempMap, map, "HIGH_SCHOOL_THIRD_GRADE");
		this.unitTwoEntry(tempMap, map, "TOTAL_CONSUME_HOUR");
		this.unitTwoEntry(tempMap, map, "OTHER_GRADE");
	}
	
	private void unitTwoEntry(Map tempMap, Map map, String key) {
		BigDecimal tempBd = (BigDecimal) tempMap.get(key);
		BigDecimal addBd = (BigDecimal) map.get(key);
		
		tempBd=tempBd==null?BigDecimal.ZERO:tempBd;
		addBd=addBd==null?BigDecimal.ZERO:addBd;
		tempMap.put(key, tempBd.add(addBd));
	}
	
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
														   String blCampusId, String productQuarterSearch, DataPackage dp) {
		return reportDao.getMiniChargedPeopleTeacherView(startDate, endDate, organizationType, blCampusId,productQuarterSearch, dp);
	}
	
	
	public DataPackage getClassRoomUseRate(String startDate,
			   String endDate, OrganizationType organizationType,
			   String blCampusId, DataPackage dp,String searchType,String weekDay) {
//	DataPackage dataP = reportDao.getClassRoomUseRate(startDate, endDate, organizationType, blCampusId, dp,searchType);
//	int days=DateTools.daysOfTwo(startDate, endDate)+1;//日期跨过的天数包括当天，所有要加1
//	Collection list = dataP.getDatas();
//	List returnList =new ArrayList();
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {//循环数据，找到
//			Map map = (Map) iterator.next();
//			map.put("days", days);
//			returnList.add(map);
//		}
//	dataP.setDatas(returnList);
//	return dataP;
	return reportDao.getClassRoomUseRate(startDate, endDate, organizationType, blCampusId, dp,searchType,weekDay);
	}
	
	public DataPackage getBoothNumUseRate(String startDate,
			   String endDate, OrganizationType organizationType,
			   String blCampusId, DataPackage dp,String searchType,String weekDay) {
	return reportDao.getBoothNumUseRate(startDate, endDate, organizationType, blCampusId, dp,searchType,weekDay);
	}
	
	/**
	 * 小班未报读信息
	 */
	@Override
	public DataPackage getNotSignMiniProductInfo(BasicOperationQueryVo basicOperationQueryVo, MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp) {
		return reportDao.getNotSignMiniProductInfo(basicOperationQueryVo, miniClassProductSearchVo, dp);
	}
	
	/**
	 * 小班指标添统计
	 */
	@Override
	public DataPackage getMiniClassIndexAnylize(BasicOperationQueryVo basicOperationQueryVo, MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp) {
		dp=reportDao.getMiniClassIndexAnylize(basicOperationQueryVo, miniClassProductSearchVo, dp);
		List<Map> list = new ArrayList<>();
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				|| BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				||BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				||BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			for(Map map :(List<Map>)dp.getDatas()) {
				map.put("realClassMember",smallClassService.getMiniClassRealMember(map.get("MINI_CLASS_ID").toString()));
				list.add(map);
			}
			dp.setDatas(list);
		}
		return dp;
	}
	
	/**
	 * 手机端课程统计接口
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getCoursesAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp){
		return reportDao.getCoursesAnalyzeForMobile(basicOperationQueryVo, dp);
	}
	
	@Override
	public DataPackage getStudentOOOStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp) {
		// TODO Auto-generated method stub
		return reportDao.getStudentOOOStatusDetail(basicOperationQueryVo,oooStatus,dp);
	}
	
	@Override
	public DataPackage getStudentOTMStatusDetail(
			BasicOperationQueryVo basicOperationQueryVo,
			String oooStatus, DataPackage dp) {
		return reportDao.getStudentOTMStatusDetail(basicOperationQueryVo,oooStatus,dp);
	}
	
	@Override
	public Boolean getStudentDetailStatu(String countDate) {
		return reportDao.getStudentDetailStatu(countDate);
	}
	
	@Override
	public Boolean getOtmStudentDetailStatu(String countDate) {
		return reportDao.getOtmStudentDetailStatu(countDate);
	}
	
	@Override
	public DataPackage getConsultResourceUse(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String entranceIds) {
		return reportDao.getConsultResourceUse(basicOperationQueryVo,dp,entranceIds);
	}
	
	/**
	 * 获取一对多课消统计
	 * @param basicOperationQueryVo
	 * @param otmTypeStr
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassAttendsAnalyze(BasicOperationQueryVo basicOperationQueryVo, String otmTypeStr, DataPackage dp) {
		dp = reportDao.getOtmClassAttendsAnalyze(basicOperationQueryVo, otmTypeStr, dp);
		List<Map> list = (List<Map>) dp.getDatas();
		if (list != null && list.size() > 0) {
			
			for (Map map : list) {
				String otmClassCourseId = (String) map.get("OTM_CLASS_COURSE_ID");
				if (StringUtil.isNotBlank(otmClassCourseId)) {
					String studyManagerName = otmClassService.getStudyManagerName(otmClassCourseId);
					map.put("STUDY_MANEGER", studyManagerName);
				} else {
					map.put("STUDY_MANEGER", "");
				}
			}
			
		}
		dp.setDatas(list);
		return dp;
		
	}

	/**
	 * 老师一对多课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param otmTypeStr
	 * @param dp
     * @return
     */
	@Override
	public DataPackage getOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, String otmTypeStr, DataPackage dp) {
		String anshazhesuan = courseConsumeTeacherVo.getAnshazhesuan(); //按什么结算;
		if ("ankeshi".equals(anshazhesuan)){
			return reportDao.getOtmCourseConsumeTeacherView(courseConsumeTeacherVo, otmTypeStr, dp);
		} else {
			return reportDao.getOtmCourseConsumeTeacherViewHours(courseConsumeTeacherVo, otmTypeStr, dp);
		}
	}
	
	/**
	 * 一对多剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmStudentRemainAnalyze(
			BasicOperationQueryVo basicOperationQueryVo,
			DataPackage dp) {
		return reportDao.getOtmStudentRemainAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	/**
	 * 财务审批进度
	 */
	@Override
	public DataPackage fundsChangeHistoryAuditList(String startDate,
			String endDate, String channel,DataPackage dp,String orgType,String groupById,String campusId) {		
		return reportDao.fundsChangeHistoryAuditList(startDate, endDate, channel,dp,orgType,groupById,campusId);
	}
	
	/**
	 * 一对一科次统计报表
	 * @param startDate
	 * @param endDate
	 * @param orgType
	 * @param groupById
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage courseSubjectList(String startDate,String endDate,String orgType,String groupById,String workType,String personnelType,String brenchId,String blSchool,DataPackage dp){
		return reportDao.courseSubjectList(startDate,endDate,orgType,groupById,workType,personnelType,brenchId,blSchool,dp);
	}
	
	/**
	 * 一对一科次科目分布报表
	 */
	@Override
	public DataPackage getcourseSubjectTimesList(BasicOperationQueryVo basicOperationQueryVo, String workType, String roleType, String subject, DataPackage dp) {
		return reportDao.getcourseSubjectTimesList(basicOperationQueryVo, workType, roleType, subject, dp);
	}

	/**
	 * 一对一学生人均课消报表
	 *
	 * @param basicOperationQueryVo
	 * @param branchId
	 * @param blCampus
	 * @param workType
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage courseConsumeRenJunList(BasicOperationQueryVo basicOperationQueryVo,String groupById, String branchId, String blCampus, String workType, DataPackage dp) {
		return reportDao.courseConsumeRenJunList(basicOperationQueryVo,groupById, branchId, blCampus,workType, dp);
	}
	
	
	@Override
	public DataPackage getCampusSignContract(
			BasicOperationQueryVo vo, DataPackage dp) {
		return reportDao.getCampusSignContract(vo,dp);
	}
	
	/**
	 * 一对一月结报表
	 */
	@Override
	public DataPackage getOooMonthlyBalance(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getOooMonthlyBalance(basicOperationQueryVo, dp);
	}
	
	/**
	 * 财务现金流凭证报表
	 */
	@Override
	public DataPackage getFinanceMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getFinanceMonthlyEvidence(basicOperationQueryVo, dp);
	}
	
	/**
	 * 财务扣费凭证报表
	 */
	@Override
	public DataPackage getIncomeMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getIncomeMonthlyEvidence(basicOperationQueryVo, dp);
	}
	
	/**
	 * 营收凭证报表
	 */
	@Override
	public DataPackage getIncomeMonthlyCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getIncomeMonthlyCampus(basicOperationQueryVo, dp);
	}
	
	/**
	 * 营收凭证报表forexcel
	 */
	@Override
	public List<OdsMonthIncomeCampusVo> getIncomeMonthlyCampusForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		dp = reportDao.getIncomeMonthlyCampus(basicOperationQueryVo, dp);
		List<OdsMonthIncomeCampusVo> retList = new ArrayList<OdsMonthIncomeCampusVo>();
		List<Map<String, Object>> list = (List<Map<String, Object>>) dp.getDatas();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				OdsMonthIncomeCampusVo vo = new OdsMonthIncomeCampusVo();
				String groupId = (String) map.get("GROUP_ID");
				String groupName = groupId.substring(groupId.indexOf("_") + 1);
				String brenchId = (String) map.get("BRENCH_ID");
				String brenchName = brenchId.substring(brenchId.indexOf("_") + 1);
				String campusId = (String) map.get("CAMPUS_ID");
				String campusName = campusId.substring(campusId.indexOf("_") + 1);
				vo.setGroupName(groupName);
				vo.setBrenchName(brenchName);
				vo.setCampusName(campusName);
				
				vo.setOneOnOneRealAmount((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"));
				vo.setOneOnOnePromotionAmount((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"));
				vo.setOneOnOneRealWashAmount((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"));
				vo.setOneOnOnePromotionWashAmount((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"));
				BigDecimal ONE_ON_ONE_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"));
				vo.setOneOnOneTotalAmount(ONE_ON_ONE_TOTAL_AMOUNT);
				
				vo.setOtmClassRealAmount((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"));
				vo.setOtmClassPromotionAmount((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"));
				vo.setOtmClassRealWashAmount((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"));
				vo.setOtmClassPromotionWashAmount((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal OTM_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setOtmClassTotalAmount(OTM_CLASS_TOTAL_AMOUNT);
				
				vo.setSmallClassRealAmount((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"));
				vo.setSmallClassPromotionAmount((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"));
				vo.setSmallClassRealWashAmount((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"));
				vo.setSmallClassPromotionWashAmount((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal SMALL_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setSmallClassTotalAmount(SMALL_CLASS_TOTAL_AMOUNT);
				
				vo.setTwoTeacherRealAmount((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"));
                vo.setTwoTeacherPromotionAmount((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"));
                vo.setTwoTeacherRealWashAmount((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"));
                vo.setTwoTeacherPromotionWashAmount((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"));
                BigDecimal TWO_TEACHER_TOTAL_AMOUNT = ((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"))
                        .add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"))
                        .subtract((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"))
                        .subtract((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"));
                vo.setTwoTeacherTotalAmount(TWO_TEACHER_TOTAL_AMOUNT);
                
                /*vo.setLiveRealAmount((BigDecimal) map.get("LIVE_REAL_AMOUNT"));
                vo.setLivePromotionAmount((BigDecimal) map.get("LIVE_PROMOTION_AMOUNT"));
                vo.setLiveRealWashAmount((BigDecimal) map.get("LIVE_REAL_WASH_AMOUNT"));
                vo.setLivePromotionWashAmount((BigDecimal) map.get("LIVE_PROMOTION_WASH_AMOUNT"));
                BigDecimal LIVE_TOTAL_AMOUNT = ((BigDecimal) map.get("LIVE_REAL_AMOUNT"))
                        .add((BigDecimal) map.get("LIVE_PROMOTION_AMOUNT"))
                        .subtract((BigDecimal) map.get("LIVE_REAL_WASH_AMOUNT"))
                        .subtract((BigDecimal) map.get("LIVE_PROMOTION_WASH_AMOUNT"));
                vo.setLiveTotalAmount(LIVE_TOTAL_AMOUNT);*/
                vo.setLiveNewRealAmount((BigDecimal) map.get("LIVE_NEW_REAL_AMOUNT"));
                vo.setLiveNewRealDivide((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"));
                vo.setLiveRenewRealAmount((BigDecimal) map.get("LIVE_RENEW_REAL_AMOUNT"));
                vo.setLiveRenewRealDivide((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"));
                BigDecimal liveTotalAmount = ((BigDecimal) map.get("LIVE_NEW_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_AMOUNT"));
                vo.setLiveTotalAmount(liveTotalAmount);
                BigDecimal liveTotalDivide = ((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"));
                vo.setLiveTotalDivide(liveTotalDivide);
				
				vo.setEcsClassRealAmount((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"));
				vo.setEcsClassPromotionAmount((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"));
				vo.setEcsClassRealWashAmount((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"));
				vo.setEcsClassPromotionWashAmount((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal ECS_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setEcsClassTotalAmount(ECS_CLASS_TOTAL_AMOUNT);
				
				vo.setLectureRealAmount((BigDecimal) map.get("LECTURE_REAL_AMOUNT"));
				vo.setLecturePromotionAmount((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"));
				vo.setLectureRealWashAmount((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"));
				vo.setLecturePromotionWashAmount((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				BigDecimal LECTURE_TOTAL_AMOUNT = ((BigDecimal) map.get("LECTURE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				vo.setLectureTotalAmount(LECTURE_TOTAL_AMOUNT);
				
				vo.setOtherRealAmount((BigDecimal) map.get("OTHERS_REAL_AMOUNT"));
				vo.setOtherPromotionAmount((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"));
				vo.setOtherRealWashAmount((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"));
				vo.setOtherPromotionWashAmount((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"));
				BigDecimal OTHERS_TOTAL_AMOUNT = ((BigDecimal) map.get("OTHERS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"));
				vo.setOtherTotalAmount(OTHERS_TOTAL_AMOUNT);
				
				vo.setIsNormalRealAmount((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"));
				vo.setIsNormalPromotionAmount((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"));
				vo.setIsNormalHistoryWashAmount((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				BigDecimal IS_NORMAL_TOTAL_AMOUNT = ((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				vo.setIsNormalTotalAmount(IS_NORMAL_TOTAL_AMOUNT);
				
				BigDecimal REAL_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"));
				vo.setRealTotalAmount(REAL_TOTAL_AMOUNT);
				
				
				BigDecimal PROMOTION_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"));
				vo.setPromotionTotalAmount(PROMOTION_TOTAL_AMOUNT);
				
				BigDecimal REAL_WASH_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				vo.setRealWashTotalAmount(REAL_WASH_TOTAL_AMOUNT);
				BigDecimal PROMOTION_WASH_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				vo.setPromotionWashTotalAmount(PROMOTION_WASH_TOTAL_AMOUNT);
				BigDecimal HISTORY_WASH_TOTAL_AMOUNT = REAL_WASH_TOTAL_AMOUNT.add(PROMOTION_WASH_TOTAL_AMOUNT);
				vo.setHistoryWashTotalAmount(HISTORY_WASH_TOTAL_AMOUNT);
				
				BigDecimal TOTAL_AMOUNT = REAL_TOTAL_AMOUNT.add(PROMOTION_TOTAL_AMOUNT).subtract(HISTORY_WASH_TOTAL_AMOUNT);
				vo.setTotalAmount(TOTAL_AMOUNT);
				
				BigDecimal ADJUST_TOTAL_AMOUNT = map.get("ADJUST_TOTAL_AMOUNT") != null ? (BigDecimal) map.get("ADJUST_TOTAL_AMOUNT") : BigDecimal.ZERO;
				vo.setAdjustTotalAmount(ADJUST_TOTAL_AMOUNT);
				
				BigDecimal AFTER_ADJUST_TOTAL_AMOUNT = TOTAL_AMOUNT.add(ADJUST_TOTAL_AMOUNT);
				vo.setAfterAdjustTotalAmount(AFTER_ADJUST_TOTAL_AMOUNT);
				
				vo.setEdivenceRemark((String) map.get("EDIVENCE_REMARK"));
				String COUNT_DATE = (String) map.get("COUNT_DATE");
				String evidenceMonth = "";
				if (COUNT_DATE != null) {
					evidenceMonth = COUNT_DATE.substring(0, 4) + "年" + COUNT_DATE.substring(5, 7) + "月";
				}
				vo.setEdivenceMonth(evidenceMonth);
				vo.setMappingDate((String) map.get("MAPPING_DATE"));
				vo.setEvidenceAuditStatusName(EvidenceAuditStatus.valueOf((String) map.get("EVIDENCE_AUDIT_STATUS")).getName());
				
				retList.add(vo);
			}
		}
		return retList;
	}
	
	/**
	 * 营收凭证报表forexcel 明细（学生）
	 */
	@Override
	public List<OdsMonthIncomeStudentVo> getIncomeMonthlyStudentForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		dp = reportDao.getIncomeMonthlyCampus(basicOperationQueryVo, dp);
		List<OdsMonthIncomeStudentVo> retList = new ArrayList<OdsMonthIncomeStudentVo>();
		List<Map<String, Object>> list = (List<Map<String, Object>>) dp.getDatas();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				OdsMonthIncomeStudentVo vo = new OdsMonthIncomeStudentVo();
				String groupId = (String) map.get("GROUP_ID");
				String groupName = groupId.substring(groupId.indexOf("_") + 1);
				String brenchId = (String) map.get("BRENCH_ID");
				String brenchName = brenchId.substring(brenchId.indexOf("_") + 1);
				String campusId = (String) map.get("CAMPUS_ID");
				String campusName = campusId.substring(campusId.indexOf("_") + 1);
				vo.setGroupName(groupName);
				vo.setBrenchName(brenchName);
				vo.setCampusName(campusName);
				vo.setStudentName((String) map.get("STUDENT_NAME"));
				
				vo.setOneOnOneRealAmount((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"));
				vo.setOneOnOnePromotionAmount((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"));
				vo.setOneOnOneRealWashAmount((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"));
				vo.setOneOnOnePromotionWashAmount((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"));
				BigDecimal ONE_ON_ONE_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"));
				vo.setOneOnOneTotalAmount(ONE_ON_ONE_TOTAL_AMOUNT);
				
				vo.setOtmClassRealAmount((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"));
				vo.setOtmClassPromotionAmount((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"));
				vo.setOtmClassRealWashAmount((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"));
				vo.setOtmClassPromotionWashAmount((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal OTM_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setOtmClassTotalAmount(OTM_CLASS_TOTAL_AMOUNT);
				
				vo.setSmallClassRealAmount((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"));
				vo.setSmallClassPromotionAmount((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"));
				vo.setSmallClassRealWashAmount((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"));
				vo.setSmallClassPromotionWashAmount((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal SMALL_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setSmallClassTotalAmount(SMALL_CLASS_TOTAL_AMOUNT);
				
				vo.setTwoTeacherRealAmount((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"));
                vo.setTwoTeacherPromotionAmount((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"));
                vo.setTwoTeacherRealWashAmount((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"));
                vo.setTwoTeacherPromotionWashAmount((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"));
                BigDecimal TWO_TEACHER_TOTAL_AMOUNT = ((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"))
                        .add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"))
                        .subtract((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"))
                        .subtract((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"));
                vo.setTwoTeacherTotalAmount(TWO_TEACHER_TOTAL_AMOUNT);
                
                vo.setLiveNewRealAmount((BigDecimal) map.get("LIVE_NEW_REAL_AMOUNT")); 
                vo.setLiveNewRealDivide((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"));
                vo.setLiveRenewRealAmount((BigDecimal) map.get("LIVE_RENEW_REAL_AMOUNT"));
                vo.setLiveRenewRealDivide((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"));
                BigDecimal liveTotalAmount = ((BigDecimal) map.get("LIVE_NEW_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_AMOUNT"));
                vo.setLiveTotalAmount(liveTotalAmount);
                BigDecimal liveTotalDivide = ((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"));
                vo.setLiveTotalDivide(liveTotalDivide);
				
				vo.setEcsClassRealAmount((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"));
				vo.setEcsClassPromotionAmount((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"));
				vo.setEcsClassRealWashAmount((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"));
				vo.setEcsClassPromotionWashAmount((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"));
				BigDecimal ECS_CLASS_TOTAL_AMOUNT = ((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"));
				vo.setEcsClassTotalAmount(ECS_CLASS_TOTAL_AMOUNT);
				
				vo.setLectureRealAmount((BigDecimal) map.get("LECTURE_REAL_AMOUNT"));
				vo.setLecturePromotionAmount((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"));
				vo.setLectureRealWashAmount((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"));
				vo.setLecturePromotionWashAmount((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				BigDecimal LECTURE_TOTAL_AMOUNT = ((BigDecimal) map.get("LECTURE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				vo.setLectureTotalAmount(LECTURE_TOTAL_AMOUNT);
				
				vo.setOtherRealAmount((BigDecimal) map.get("OTHERS_REAL_AMOUNT"));
				vo.setOtherPromotionAmount((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"));
				vo.setOtherRealWashAmount((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"));
				vo.setOtherPromotionWashAmount((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"));
				BigDecimal OTHERS_TOTAL_AMOUNT = ((BigDecimal) map.get("OTHERS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"))
						.subtract((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"));
				vo.setOtherTotalAmount(OTHERS_TOTAL_AMOUNT);
				
				vo.setIsNormalRealAmount((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"));
				vo.setIsNormalPromotionAmount((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"));
				vo.setIsNormalHistoryWashAmount((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				BigDecimal IS_NORMAL_TOTAL_AMOUNT = ((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"))
						.subtract((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				vo.setIsNormalTotalAmount(IS_NORMAL_TOTAL_AMOUNT);
				
				BigDecimal REAL_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LIVE_NEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("LIVE_RENEW_REAL_DIVIDE"))
						.add((BigDecimal) map.get("ECS_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_REAL_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_REAL_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT"));
				vo.setRealTotalAmount(REAL_TOTAL_AMOUNT);
				
				
				BigDecimal PROMOTION_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT"));
				vo.setPromotionTotalAmount(PROMOTION_TOTAL_AMOUNT);
				
				BigDecimal REAL_WASH_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_REAL_WASH_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_REAL_WASH_AMOUNT"))
						.add((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT"));
				vo.setRealWashTotalAmount(REAL_WASH_TOTAL_AMOUNT);
				BigDecimal PROMOTION_WASH_TOTAL_AMOUNT = ((BigDecimal) map.get("ONE_ON_ONE_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("SMALL_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("TWO_TEACHER_PROMOTION_WASH_AMOUNT"))
						//.add((BigDecimal) map.get("LIVE_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("ECS_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTM_CLASS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("OTHERS_PROMOTION_WASH_AMOUNT"))
						.add((BigDecimal) map.get("LECTURE_PROMOTION_WASH_AMOUNT"));
				vo.setPromotionWashTotalAmount(PROMOTION_WASH_TOTAL_AMOUNT);
				BigDecimal HISTORY_WASH_TOTAL_AMOUNT = REAL_WASH_TOTAL_AMOUNT.add(PROMOTION_WASH_TOTAL_AMOUNT);
				vo.setHistoryWashTotalAmount(HISTORY_WASH_TOTAL_AMOUNT);
				
				BigDecimal TOTAL_AMOUNT = REAL_TOTAL_AMOUNT.add(PROMOTION_TOTAL_AMOUNT).subtract(HISTORY_WASH_TOTAL_AMOUNT);
				vo.setTotalAmount(TOTAL_AMOUNT);
				
				String COUNT_DATE = (String) map.get("COUNT_DATE");
				String evidenceMonth = "";
				if (COUNT_DATE != null) {
					evidenceMonth = COUNT_DATE.substring(0, 4) + "年" + COUNT_DATE.substring(5, 7) + "月";
				}
				vo.setEdivenceMonth(evidenceMonth);
				vo.setMappingDate((String) map.get("MAPPING_DATE"));
				vo.setEvidenceAuditStatusName(EvidenceAuditStatus.valueOf((String) map.get("EVIDENCE_AUDIT_STATUS")).getName());
				
				retList.add(vo);
			}
		}
		return retList;
	}
	
	/**
	 * 剩余资金凭证报表
	 */
	@Override
	public DataPackage getRemainMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getRemainMonthlyEvidence(basicOperationQueryVo, dp);
	}
	
	/**
	 * 剩余资金凭证报表forexcel
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public List<OdsMonthRemainAmountCampusVo> getRemainMonthlyEvidenceToExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		dp = reportDao.getRemainMonthlyEvidence(basicOperationQueryVo, dp);
		List<OdsMonthRemainAmountCampusVo> retList = new ArrayList<OdsMonthRemainAmountCampusVo>();
		List<Map<String, Object>> list = (List<Map<String, Object>>) dp.getDatas();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				OdsMonthRemainAmountCampusVo vo = new OdsMonthRemainAmountCampusVo();
				String groupId = (String) map.get("GROUP_ID");
				String groupName = groupId.substring(groupId.indexOf("_") + 1);
				String brenchId = (String) map.get("BRENCH_ID");
				String brenchName = brenchId.substring(brenchId.indexOf("_") + 1);
				String campusId = (String) map.get("CAMPUS_ID");
				String campusName = campusId.substring(campusId.indexOf("_") + 1);
				
				vo.setGroupName(groupName);
				vo.setBrenchName(brenchName);
				vo.setCampusName(campusName);
				
				vo.setRealRemainAmountInit((BigDecimal) map.get("REAL_REMAIN_AMOUNT_INIT"));
				
				vo.setRealPaidAmountMid((BigDecimal) map.get("REAL_PAID_AMOUNT_MID"));
				vo.setWashAmountMid((BigDecimal) map.get("WASH_AMOUNT_MID"));
				vo.setHistoryWashAmountInMid((BigDecimal) map.get("HISTORY_WASH_AMOUNT_MID"));
				vo.setElectronicTransferInMid((BigDecimal) map.get("ELECTRONIC_TRANSFER_IN_MID"));
				vo.setElectronicRechargeInMid((BigDecimal) map.get("ELECTRONIC_RECHARGE_MID"));
				
				BigDecimal TOTAL_AMOUNT_IN_MID = ((BigDecimal) map.get("REAL_PAID_AMOUNT_MID"))
						.subtract((BigDecimal) map.get("WASH_AMOUNT_MID"))
						.subtract((BigDecimal) map.get("HISTORY_WASH_AMOUNT_MID"))
						.add((BigDecimal) map.get("ELECTRONIC_TRANSFER_IN_MID"))
				        .add((BigDecimal) map.get("ELECTRONIC_RECHARGE_MID"));
				vo.setTotalAmountInMid(TOTAL_AMOUNT_IN_MID);
				
				
				vo.setRealConsumeAmountMid((BigDecimal) map.get("REAL_CONSUME_AMOUNT_MID"));
				vo.setIsNormalRealAmountMid((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT_MID"));
				vo.setRealReturnFeeMid((BigDecimal) map.get("REAL_RETURN_FEE_MID"));
				vo.setElectronicTransferOutMid((BigDecimal) map.get("ELECTRONIC_TRANSFER_OUT_MID"));
				vo.setRealWashAmountMid((BigDecimal) map.get("REAL_WASH_AMOUNT_MID"));
				vo.setIsNormalWashAmountMid((BigDecimal) map.get("IS_NORMAL_WASH_AMOUNT_MID"));
				vo.setRealHistoryConsumeAmountMid((BigDecimal) map.get("REAL_HISTORY_CONSUME_AMOUNT_MID"));
				vo.setRealHistoryWashAmountMid((BigDecimal) map.get("REAL_HISTORY_WASH_AMOUNT_MID"));
				vo.setIsNormalWashAmountMid((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMO_MID"));
				
				BigDecimal TOTAL_AMOUNT_OUT_MID = ((BigDecimal) map.get("REAL_CONSUME_AMOUNT_MID"))
						.add((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT_MID"))
						.add((BigDecimal) map.get("REAL_RETURN_FEE_MID"))
						.add((BigDecimal) map.get("ELECTRONIC_TRANSFER_OUT_MID"))
						.subtract((BigDecimal) map.get("REAL_WASH_AMOUNT_MID"))
						.subtract((BigDecimal) map.get("IS_NORMAL_WASH_AMOUNT_MID"))
						.add((BigDecimal) map.get("REAL_HISTORY_CONSUME_AMOUNT_MID"))
						.subtract((BigDecimal) map.get("REAL_HISTORY_WASH_AMOUNT_MID"))
						.subtract((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMO_MID"));
				vo.setTotalAmountOutMid(TOTAL_AMOUNT_OUT_MID);
				
				vo.setRealRemainAmountFinal((BigDecimal) map.get("REAL_REMAIN_AMOUNT_FINAL"));
				
				String COUNT_DATE = (String) map.get("COUNT_DATE");
				String evidenceMonth = "";
				if (COUNT_DATE != null) {
					evidenceMonth = COUNT_DATE.substring(0, 4) + "年" + COUNT_DATE.substring(5, 7) + "月";
				}
				vo.setEdivenceMonth(evidenceMonth);
				vo.setMappingDate((String) map.get("MAPPING_DATE"));
				vo.setEvidenceAuditStatusName(EvidenceAuditStatus.valueOf((String) map.get("EVIDENCE_AUDIT_STATUS")).getName());
				
				retList.add(vo);
			}
		}
		return retList;
	}
	
	/**
	 * 剩余资金凭证报表forexcel 明细（学生）
	 */
	public List<OdsMonthRemainAmountStudentVo> getRemainMonthlyStudentToExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		dp = reportDao.getRemainMonthlyEvidence(basicOperationQueryVo, dp);
		List<OdsMonthRemainAmountStudentVo> retList = new ArrayList<OdsMonthRemainAmountStudentVo>();
		List<Map<String, Object>> list = (List<Map<String, Object>>) dp.getDatas();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				OdsMonthRemainAmountStudentVo vo = new OdsMonthRemainAmountStudentVo();
				String groupId = (String) map.get("GROUP_ID");
				String groupName = groupId.substring(groupId.indexOf("_") + 1);
				String brenchId = (String) map.get("BRENCH_ID");
				String brenchName = brenchId.substring(brenchId.indexOf("_") + 1);
				String campusId = (String) map.get("CAMPUS_ID");
				String campusName = campusId.substring(campusId.indexOf("_") + 1);
				
				vo.setGroupName(groupName);
				vo.setBrenchName(brenchName);
				vo.setCampusName(campusName);
				vo.setStudentName((String) map.get("STUDENT_NAME"));
				
vo.setRealRemainAmountInit((BigDecimal) map.get("REAL_REMAIN_AMOUNT_INIT"));
                
                vo.setRealPaidAmountMid((BigDecimal) map.get("REAL_PAID_AMOUNT_MID"));
                vo.setWashAmountMid((BigDecimal) map.get("WASH_AMOUNT_MID"));
                vo.setHistoryWashAmountInMid((BigDecimal) map.get("HISTORY_WASH_AMOUNT_MID"));
                vo.setElectronicTransferInMid((BigDecimal) map.get("ELECTRONIC_TRANSFER_IN_MID"));
                vo.setElectronicRechargeInMid((BigDecimal) map.get("ELECTRONIC_RECHARGE_MID"));
                
                BigDecimal TOTAL_AMOUNT_IN_MID = ((BigDecimal) map.get("REAL_PAID_AMOUNT_MID"))
                        .subtract((BigDecimal) map.get("WASH_AMOUNT_MID"))
                        .subtract((BigDecimal) map.get("HISTORY_WASH_AMOUNT_MID"))
                        .add((BigDecimal) map.get("ELECTRONIC_TRANSFER_IN_MID"))
                        .add((BigDecimal) map.get("ELECTRONIC_RECHARGE_MID"));
                vo.setTotalAmountInMid(TOTAL_AMOUNT_IN_MID);
                
                
                vo.setRealConsumeAmountMid((BigDecimal) map.get("REAL_CONSUME_AMOUNT_MID"));
                vo.setIsNormalRealAmountMid((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT_MID"));
                vo.setRealReturnFeeMid((BigDecimal) map.get("REAL_RETURN_FEE_MID"));
                vo.setElectronicTransferOutMid((BigDecimal) map.get("ELECTRONIC_TRANSFER_OUT_MID"));
                vo.setRealWashAmountMid((BigDecimal) map.get("REAL_WASH_AMOUNT_MID"));
                vo.setIsNormalWashAmountMid((BigDecimal) map.get("IS_NORMAL_WASH_AMOUNT_MID"));
                vo.setRealHistoryConsumeAmountMid((BigDecimal) map.get("REAL_HISTORY_CONSUME_AMOUNT_MID"));
                vo.setRealHistoryWashAmountMid((BigDecimal) map.get("REAL_HISTORY_WASH_AMOUNT_MID"));
                vo.setIsNormalWashAmountMid((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMO_MID"));
                
                BigDecimal TOTAL_AMOUNT_OUT_MID = ((BigDecimal) map.get("REAL_CONSUME_AMOUNT_MID"))
                        .add((BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT_MID"))
                        .add((BigDecimal) map.get("REAL_RETURN_FEE_MID"))
                        .add((BigDecimal) map.get("ELECTRONIC_TRANSFER_OUT_MID"))
                        .subtract((BigDecimal) map.get("REAL_WASH_AMOUNT_MID"))
                        .subtract((BigDecimal) map.get("IS_NORMAL_WASH_AMOUNT_MID"))
                        .add((BigDecimal) map.get("REAL_HISTORY_CONSUME_AMOUNT_MID"))
                        .subtract((BigDecimal) map.get("REAL_HISTORY_WASH_AMOUNT_MID"))
                        .subtract((BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMO_MID"));
                vo.setTotalAmountOutMid(TOTAL_AMOUNT_OUT_MID);
                
                vo.setRealRemainAmountFinal((BigDecimal) map.get("REAL_REMAIN_AMOUNT_FINAL"));
				
				String COUNT_DATE = (String) map.get("COUNT_DATE");
				String evidenceMonth = "";
				if (COUNT_DATE != null) {
					evidenceMonth = COUNT_DATE.substring(0, 4) + "年" + COUNT_DATE.substring(5, 7) + "月";
				}
				vo.setEdivenceMonth(evidenceMonth);
				vo.setMappingDate((String) map.get("MAPPING_DATE"));
				vo.setEvidenceAuditStatusName(EvidenceAuditStatus.valueOf((String) map.get("EVIDENCE_AUDIT_STATUS")).getName());
				
				retList.add(vo);
			}
		}
		return retList;
	}
	
	/**
	 * 财务扣费凭证报表forexcel
	 * @return
	 */
	@Override
	public List<IncomeMonthlyEvidenceVo> getIncomeMonthlyEvidenceForExcel(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		dp = reportDao.getIncomeMonthlyEvidence(basicOperationQueryVo, dp);
		List<IncomeMonthlyEvidenceVo> retList = new ArrayList<IncomeMonthlyEvidenceVo>();
		List<Map> list = (List<Map>) dp.getDatas();
		
		for (Map map : list) {
			IncomeMonthlyEvidenceVo vo = new IncomeMonthlyEvidenceVo();
			String groupId = (String) map.get("GROUP_ID");
			String groupName = groupId.substring(groupId.indexOf("_") + 1);
			String brenchId = (String) map.get("BRENCH_ID");
			String brenchName = brenchId.substring(brenchId.indexOf("_") + 1);
			String campusId = (String) map.get("CAMPUS_ID");
			String campusName = campusId.substring(campusId.indexOf("_") + 1);
			
			BigDecimal oneOnOneRealAmount = (BigDecimal) map.get("ONE_ON_ONE_REAL_AMOUNT");
			BigDecimal oneOnOnePromotionAmount = (BigDecimal) map.get("ONE_ON_ONE_PROMOTION_AMOUNT");
			BigDecimal oneOnOneHistoryWashAmount =  (BigDecimal) map.get("ONE_ON_ONE_HISTORY_WASH_AMOUNT");
			BigDecimal oneOnOneTotalAmount = oneOnOneRealAmount.add(oneOnOnePromotionAmount).subtract(oneOnOneHistoryWashAmount);
			vo.setOneOnOneRealAmount(oneOnOneRealAmount);
			vo.setOneOnOnePromotionAmount(oneOnOnePromotionAmount);
			vo.setOneOnOneHistoryWashAmount(oneOnOneHistoryWashAmount);
			vo.setOneOnOneTotalAmount(oneOnOneTotalAmount);
			
			BigDecimal otmClassRealAmount = (BigDecimal) map.get("OTM_CLASS_REAL_AMOUNT");
			BigDecimal otmClassPromotionAmount = (BigDecimal) map.get("OTM_CLASS_PROMOTION_AMOUNT");
			BigDecimal otmClassHistoryWashAmount = (BigDecimal) map.get("OTM_CLASS_HISTORY_WASH_AMOUNT");
			BigDecimal otmClassTotalAmount = otmClassRealAmount.add(otmClassPromotionAmount).subtract(otmClassHistoryWashAmount);
			vo.setOtmClassRealAmount(otmClassRealAmount);
			vo.setOtmClassPromotionAmount(otmClassPromotionAmount);
			vo.setOtmClassHistoryWashAmount(otmClassHistoryWashAmount);
			vo.setOtmClassTotalAmount(otmClassTotalAmount);
			
			BigDecimal smallClassRealAmount = (BigDecimal) map.get("SAMLL_CLASS_REAL_AMOUNT");
			BigDecimal smallClassPromotionAmount = (BigDecimal) map.get("SAMLL_CLASS_PROMOTION_AMOUNT");
			BigDecimal smallClassHistoryWashAmount = (BigDecimal) map.get("SMALL_CLASS_HISTORY_WASH_AMOUNT");
			BigDecimal smallClassTotalAmount = smallClassRealAmount.add(smallClassPromotionAmount).subtract(smallClassHistoryWashAmount);
			vo.setSmallClassRealAmount(smallClassRealAmount);
			vo.setSmallClassPromotionAmount(smallClassPromotionAmount);
			vo.setSmallClassHistoryWashAmount(smallClassHistoryWashAmount);
			vo.setSmallClassTotalAmount(smallClassTotalAmount);
			
			BigDecimal escClassRealAmount = (BigDecimal) map.get("ESC_CLASS_REAL_AMOUNT");
			BigDecimal escClassPromotionAmount = (BigDecimal) map.get("ESC_CLASS_PROMOTION_AMOUNT");
			BigDecimal escClassHistoryWashAmount = (BigDecimal) map.get("ECS_CLASS_HISTORY_WASH_AMOUNT");
			BigDecimal escClassTotalAmount = escClassRealAmount.add(escClassPromotionAmount).subtract(escClassHistoryWashAmount);
			vo.setEscClassRealAmount(escClassRealAmount);
			vo.setEscClassPromotionAmount(escClassPromotionAmount);
			vo.setEscClassHistoryWashAmount(escClassHistoryWashAmount);
			vo.setEscClassTotalAmount(escClassTotalAmount);
			
			BigDecimal lectureRealAmount = (BigDecimal) map.get("LECTURE_REAL_AMOUNT");
			BigDecimal lecturePromotionAmount = (BigDecimal) map.get("LECTURE_PROMOTION_AMOUNT");
			BigDecimal lectureHistoryWashAmount = (BigDecimal) map.get("LECTURE_HISTORY_WASH_AMOUNT");
			BigDecimal lectureTotalAmount = lectureRealAmount.add(lecturePromotionAmount).subtract(lectureHistoryWashAmount);
			vo.setLectureRealAmount(lectureRealAmount);
			vo.setLecturePromotionAmount(lecturePromotionAmount);
			vo.setLectureHistoryWashAmount(lectureHistoryWashAmount);
			vo.setLectureTotalAmount(lectureTotalAmount);
			
			BigDecimal othersRealAmount = (BigDecimal) map.get("OTHERS_REAL_AMOUNT");
			BigDecimal othersPromotionAmount = (BigDecimal) map.get("OTHERS_PROMOTION_AMOUNT");
			BigDecimal othersHistoryWashAmount = (BigDecimal) map.get("OTHERS_HISTORY_WASH_AMOUNT");
			BigDecimal othersTotalAmount = othersRealAmount.add(othersPromotionAmount).subtract(othersHistoryWashAmount);
			vo.setOthersRealAmount(othersRealAmount);
			vo.setOthersPromotionAmount(othersPromotionAmount);
			vo.setOthersHistoryWashAmount(othersHistoryWashAmount);
			vo.setOthersTotalAmount(othersTotalAmount);
			
			BigDecimal isNormalRealAmount = (BigDecimal) map.get("IS_NORMAL_REAL_AMOUNT");
			BigDecimal isNormalPromotionAmount = (BigDecimal) map.get("IS_NORMAL_PROMOTION_AMOUNT");
			BigDecimal isNormalHistoryWashAmount = (BigDecimal) map.get("IS_NORMAL_HISTORY_WASH_AMOUNT");
			BigDecimal isNormalTotalAmount = isNormalRealAmount.add(isNormalPromotionAmount).subtract(isNormalHistoryWashAmount);
			vo.setIsNormalRealAmount(isNormalRealAmount);
			vo.setIsNormalPromotionAmount(isNormalPromotionAmount);
			vo.setIsNormalHistoryWashAmount(isNormalHistoryWashAmount);
			vo.setIsNormalTotalAmount(isNormalTotalAmount);
			
			BigDecimal realTotalAmount = oneOnOneRealAmount.add(otmClassRealAmount).add(smallClassRealAmount).add(escClassRealAmount)
					.add(lectureRealAmount).add(othersRealAmount).add(isNormalRealAmount);
			BigDecimal promotionTotalAmount = oneOnOnePromotionAmount.add(otmClassPromotionAmount).add(smallClassPromotionAmount).add(escClassPromotionAmount)
					.add(lectureRealAmount).add(othersRealAmount).add(isNormalRealAmount);
			BigDecimal historyWashTtoalAmount = oneOnOneHistoryWashAmount.add(otmClassHistoryWashAmount).add(smallClassHistoryWashAmount).add(escClassHistoryWashAmount)
					.add(lectureRealAmount).add(othersRealAmount).add(isNormalRealAmount);
			BigDecimal totalAmount = realTotalAmount.add(promotionTotalAmount).subtract(historyWashTtoalAmount);
			String incomeRate = totalAmount.divide(realTotalAmount.add(promotionTotalAmount), RoundingMode.FLOOR).multiply(new BigDecimal(100)) + "%";
			vo.setRealTotalAmount(realTotalAmount);
			vo.setPromotionTotalAmount(promotionTotalAmount);
			vo.setHistoryWashTtoalAmount(historyWashTtoalAmount);
			vo.setTotalAmount(totalAmount);
			vo.setIncomeRate(incomeRate);
			
			vo.setGroupName(groupName);
			vo.setBrenchName(brenchName);
			vo.setCampusName(campusName);
			retList.add(vo);
		}
		return retList;
	}
	
	@Override
	public DataPackage getStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus, DataPackage dp) {
		
		return reportDao.getStudentSubjectStatusReport(basicOperationQueryVo, studentOooStatus, dp);
	}
	
	@Override
	public List<OdsStudentSubjectStatusVo> exportStudentSubjectStatusReport(
			 BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus) {
		List<OdsStudentSubjectStatusVo> listVo= new ArrayList<OdsStudentSubjectStatusVo>();
		List<Map<Object,Object>> list= reportDao.exportStudentSubjectStatusReport(basicOperationQueryVo, studentOooStatus);
		 for (Map<Object,Object> map : list) {
			 OdsStudentSubjectStatusVo vo = new OdsStudentSubjectStatusVo();
			 if(map.get("STUDENT_ONEONONE_STATUS")!=null){
				 String studentStatus = StudentOneOnOneStatus.valueOf((String)map.get("STUDENT_ONEONONE_STATUS")).getName();
				 studentStatus = studentStatus.equals("上课中") ? "在读" : studentStatus;
				 vo.setOneOnOneStudentStatus(studentStatus);
			 } else {
				 vo.setOneOnOneStudentStatus("-");
			 }
			 
			 vo.setGradeName((String)map.get("gradeName"));
			 vo.setBrenchName((String)map.get("BRENCH_NAME"));
			 vo.setCampusName((String)map.get("CAMPUS_NAME"));
			 vo.setBIOLOGY_SUBJECT(this.getSubejctStatusName(map, "BIOLOGY_SUBJECT"));
			 vo.setCHEMISTRY_SUBJECT(this.getSubejctStatusName(map, "CHEMISTRY_SUBJECT"));
			 vo.setCHINESE_SUBJECT(this.getSubejctStatusName(map, "CHINESE_SUBJECT"));
			 vo.setENGLISH_SUBJECT(this.getSubejctStatusName(map, "ENGLISH_SUBJECT"));
			 vo.setGEOGRAPHY_SUBJECT(this.getSubejctStatusName(map, "GEOGRAPHY_SUBJECT"));
			 vo.setGROWING(this.getSubejctStatusName(map, "GROWING"));
			 vo.setHISTORY_SUBJECT(this.getSubejctStatusName(map, "HISTORY_SUBJECT"));
			 vo.setINFO_TECHNOLOGY(this.getSubejctStatusName(map, "INFO_TECHNOLOGY"));
			 vo.setJANPANESE(this.getSubejctStatusName(map, "JANPANESE"));
			 vo.setKNOWLEDGE(this.getSubejctStatusName(map, "KNOWLEDGE"));
			 vo.setMATH_SUBJECT(this.getSubejctStatusName(map, "MATH_SUBJECT"));
			 vo.setOTHER(this.getSubejctStatusName(map, "OTHER"));
			 vo.setOTHER_CHINESE(this.getSubejctStatusName(map, "OTHER_CHINESE"));
			 vo.setOTHER_ENGLISH(this.getSubejctStatusName(map, "OTHER_ENGLISH"));
			 vo.setOTHER_MATH(this.getSubejctStatusName(map, "OTHER_MATH"));
			 vo.setPHYSICS_SUBJECT(this.getSubejctStatusName(map, "PHYSICS_SUBJECT"));
			 vo.setPOLITICS_SUBJECT(this.getSubejctStatusName(map, "POLITICS_SUBJECT"));
			 vo.setPSYCHOLOGY(this.getSubejctStatusName(map, "PSYCHOLOGY"));
			 
			 vo.setStudentName((String)map.get("STUDENT_NAME"));
			 vo.setBIOLOGY_SUBJECT_HOURS((BigDecimal)map.get("BIOLOGY_SUBJECT_HOURS"));
			 vo.setCHEMISTRY_SUBJECT_HOURS((BigDecimal)map.get("CHEMISTRY_SUBJECT_HOURS"));
			 vo.setCHINESE_SUBJECT_HOURS((BigDecimal)map.get("CHINESE_SUBJECT_HOURS"));
			 vo.setENGLISH_SUBJECT_HOURS((BigDecimal)map.get("ENGLISH_SUBJECT_HOURS"));
			 vo.setGEOGRAPHY_SUBJECT_HOURS((BigDecimal)map.get("GEOGRAPHY_SUBJECT_HOURS"));
			 vo.setGROWING_HOURS((BigDecimal)map.get("GROWING_HOURS"));
			 vo.setHISTORY_SUBJECT_HOURS((BigDecimal)map.get("HISTORY_SUBJECT_HOURS"));
			 vo.setINFO_TECHNOLOGY_HOURS((BigDecimal)map.get("INFO_TECHNOLOGY_HOURS"));
			 vo.setJANPANESE_HOURS((BigDecimal)map.get("JANPANESE_HOURS"));
			 vo.setKNOWLEDGE_HOURS((BigDecimal)map.get("KNOWLEDGE_HOURS"));
			 vo.setMATH_SUBJECT_HOURS((BigDecimal)map.get("MATH_SUBJECT_HOURS"));
			 vo.setOTHER_HOURS((BigDecimal)map.get("OTHER_HOURS"));
			 vo.setOTHER_CHINESE_HOURS((BigDecimal)map.get("OTHER_CHINESE_HOURS"));
			 vo.setOTHER_ENGLISH_HOURS((BigDecimal)map.get("OTHER_ENGLISH_HOURS"));
			 vo.setOTHER_MATH_HOURS((BigDecimal)map.get("OTHER_MATH_HOURS"));
			 vo.setPHYSICS_SUBJECT_HOURS((BigDecimal)map.get("PHYSICS_SUBJECT_HOURS"));
			 vo.setPOLITICS_SUBJECT_HOURS((BigDecimal)map.get("POLITICS_SUBJECT_HOURS"));
			 vo.setPSYCHOLOGY_HOURS((BigDecimal)map.get("PSYCHOLOGY_HOURS"));
			 
			 BigDecimal totalHours = BigDecimal.ZERO;
			 totalHours = vo.getBIOLOGY_SUBJECT_HOURS() != null ? totalHours.add(vo.getBIOLOGY_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getCHEMISTRY_SUBJECT_HOURS() != null ? totalHours.add(vo.getCHEMISTRY_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getCHINESE_SUBJECT_HOURS() != null ? totalHours.add(vo.getCHINESE_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getENGLISH_SUBJECT_HOURS() != null ? totalHours.add(vo.getENGLISH_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getGEOGRAPHY_SUBJECT_HOURS() != null ? totalHours.add(vo.getGEOGRAPHY_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getGROWING_HOURS() != null ? totalHours.add(vo.getGROWING_HOURS()) : totalHours;
			 totalHours = vo.getHISTORY_SUBJECT_HOURS() != null ? totalHours.add(vo.getHISTORY_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getINFO_TECHNOLOGY_HOURS() != null ? totalHours.add(vo.getINFO_TECHNOLOGY_HOURS()) : totalHours;
			 totalHours = vo.getJANPANESE_HOURS() != null ? totalHours.add(vo.getJANPANESE_HOURS()) : totalHours;
			 totalHours = vo.getKNOWLEDGE_HOURS() != null ? totalHours.add(vo.getKNOWLEDGE_HOURS()) : totalHours;
			 totalHours = vo.getMATH_SUBJECT_HOURS() != null ? totalHours.add(vo.getMATH_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getOTHER_HOURS() != null ? totalHours.add(vo.getOTHER_HOURS()) : totalHours;
			 totalHours = vo.getOTHER_CHINESE_HOURS() != null ? totalHours.add(vo.getOTHER_CHINESE_HOURS()) : totalHours;
			 totalHours = vo.getOTHER_ENGLISH_HOURS() != null ? totalHours.add(vo.getOTHER_ENGLISH_HOURS()) : totalHours;
			 totalHours = vo.getOTHER_MATH_HOURS() != null ? totalHours.add(vo.getOTHER_MATH_HOURS()) : totalHours;
			 totalHours = vo.getPHYSICS_SUBJECT_HOURS() != null ? totalHours.add(vo.getPHYSICS_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getPOLITICS_SUBJECT_HOURS() != null ? totalHours.add(vo.getPOLITICS_SUBJECT_HOURS()) : totalHours;
			 totalHours = vo.getPSYCHOLOGY_HOURS() != null ? totalHours.add(vo.getPSYCHOLOGY_HOURS()) : totalHours;
			 vo.setTotalHours(totalHours.toString());
			 
			 if(map.get("STUDY_MANAGER_NAME")==null){
				 vo.setStudyManagerName("无学管");
			 }else{
				 vo.setStudyManagerName((String)map.get("STUDY_MANAGER_NAME"));
			 }
			 
			 listVo.add(vo);
		 }
		 
		 return listVo;
	}

    @Override
    public DataPackage getTwoTeacherCourseTotal(ModelVo modelVo,DataPackage dataPackage) {
        return reportDao.getTwoTeacherCourseTotal(modelVo,dataPackage);
    }

	@Override
	public DataPackage getPublicFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		return reportDao.getPublicFinanceContractAnalyze(basicOperationQueryVo, dp, userService.getCurrentLoginUser(), userService.getBelongCampus());
	}

	private String getSubejctStatusName(Map map, String subject) {
		String subjectName = map.get(subject) != null ? ((String)map.get(subject)).replace("red", "") : "";
		if (subjectName.equals("上课中")) {
			subjectName = "在读";
		}
		return subjectName;
	}

    @Override
    public List<TwoTeacherMainClassesExcelVo> exportTwoTeacherMainClassesReport(
            ModelVo modelVo) {
        List<Map<Object,Object>> list = reportDao.exportTwoTeacherMainClassesReport(modelVo);
        List<TwoTeacherMainClassesExcelVo> listVo= new ArrayList<TwoTeacherMainClassesExcelVo>();
        for (Map<Object,Object> map : list) {
            TwoTeacherMainClassesExcelVo vo = new TwoTeacherMainClassesExcelVo();
            vo.setGroupName(map.get("GROUP_NAME").toString());
            vo.setBranchName(map.get("BRANCH_NAME").toString());
            vo.setTeacherName(map.get("TEACHER_NAME").toString());
            vo.setCampusName(map.get("CAMPUS_NAME").toString());
            vo.setOneToSixClasses((BigDecimal)map.get("ONE_TO_SIX_CLASSES"));
            vo.setSevenClasses((BigDecimal)map.get("SEVEN_CLASSES"));
            vo.setEightClasses((BigDecimal)map.get("EIGHT_CLASSES"));
            vo.setNineClasses((BigDecimal)map.get("NINE_CLASSES"));
            vo.setTenClasses((BigDecimal)map.get("TEN_CLASSES"));
            vo.setElevenClasses((BigDecimal)map.get("ELEVEN_CLASSES"));
            vo.setTwelveClasses((BigDecimal)map.get("TWELVE_CLASSES"));
            vo.setThirteenClasses((BigDecimal)map.get("TWELVE_CLASSES"));
            vo.setFourteenClasses((BigDecimal)map.get("FOURTEEN_CLASSES"));
            vo.setFifteenClasses((BigDecimal)map.get("FIFTEEN_CLASSES"));
            vo.setSixteenClasses((BigDecimal)map.get("SIXTEEN_CLASSES"));
            vo.setSeventeenClasses((BigDecimal)map.get("SEVENTEEN_CLASSES"));
            vo.setEightteenClasses((BigDecimal)map.get("EIGHTTEEN_CLASSES"));
            vo.setNineteenClasses((BigDecimal)map.get("NINETEEN_CLASSES"));
            vo.setTwentyClasses((BigDecimal)map.get("TWENTY_CLASSES"));
            listVo.add(vo);
        }
        return listVo;
    }

    @Override
    public List<TwoTeacherAuxiliaryClassesExcelVo> exportTwoTeacherAuxiliaryClassesReport(
            ModelVo modelVo) {
        List<Map<Object,Object>> list = reportDao.exportTwoTeacherAuxiliaryClassesReport(modelVo);
        List<TwoTeacherAuxiliaryClassesExcelVo> listVo= new ArrayList<TwoTeacherAuxiliaryClassesExcelVo>();
        for (Map<Object,Object> map : list) {
            TwoTeacherAuxiliaryClassesExcelVo vo = new TwoTeacherAuxiliaryClassesExcelVo();
            vo.setGroupName(map.get("GROUP_NAME").toString());
            vo.setBranchName(map.get("BRANCH_NAME").toString());
            vo.setTeacherName(map.get("TEACHER_NAME").toString());
            vo.setCampusName(map.get("CAMPUS_NAME").toString());
            vo.setOneStudents((BigDecimal)map.get("ONE_STUDENTS"));
            vo.setTwoStudents((BigDecimal)map.get("TWO_STUDENTS"));
            vo.setThreeStudents((BigDecimal)map.get("THREE_STUDENTS"));
            vo.setFourStudents((BigDecimal)map.get("FOUR_STUDENTS"));
            vo.setFiveStudents((BigDecimal)map.get("FIVE_STUDENTS"));
            vo.setSixStudents((BigDecimal)map.get("SIX_STUDENTS"));
            vo.setSevenStudents((BigDecimal)map.get("SEVEN_STUDENTS"));
            vo.setEightStudents((BigDecimal)map.get("EIGHT_STUDENTS"));
            vo.setNineStudents((BigDecimal)map.get("NINE_STUDENTS"));
            vo.setTenStudents((BigDecimal)map.get("TEN_STUDENTS"));
            vo.setElevenStudents((BigDecimal)map.get("ELEVEN_STUDENTS"));
            vo.setTwelveStudents((BigDecimal)map.get("TWELVE_STUDENTS"));
            vo.setThirteenStudents((BigDecimal)map.get("THIRTEEN_STUDENTS"));
            vo.setFourteenStudents((BigDecimal)map.get("FOURTEEN_STUDENTS"));
            vo.setFifteenStudents((BigDecimal)map.get("FIFTEEN_STUDENTS"));
            vo.setSixteenStudents((BigDecimal)map.get("SIXTEEN_STUDENTS"));
            vo.setSeventeenStudents((BigDecimal)map.get("SEVENTEEN_STUDENTS"));
            vo.setEightteenStudents((BigDecimal)map.get("EIGHTTEEN_STUDENTS"));
            vo.setNineteenStudents((BigDecimal)map.get("NINETEEN_STUDENTS"));
            vo.setTwentyStudents((BigDecimal)map.get("TWENTY_STUDENTS"));
            vo.setTwentyOneStudents((BigDecimal)map.get("TWENTY_ONE_STUDENTS"));
            vo.setTwentyTwoStudents((BigDecimal)map.get("TWENTY_TWO_STUDENTS"));
            vo.setTwentyThreeStudents((BigDecimal)map.get("TWENTY_THREE_STUDENTS"));
            vo.setTwentyFourStudents((BigDecimal)map.get("TWENTY_FOUR_STUDENTS"));
            vo.setTwentyFiveStudents((BigDecimal)map.get("TWENTY_FIVE_STUDENTS"));
            listVo.add(vo);
        }
        return listVo;
    }

	@Override
	public DataPackage getMiniClassFullClassTeacherRate(DataPackage dataPackage, MiniClassFullRateVo miniClassRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId()); 
    	
    	
    	sql.append("select sum(case when c.STUDENT_ID is NULL then 0 else 1 end) as paidFullAmount,u.`NAME` as teacherName,c.TEACHER_ID as teacherId,");
    	sql.append("c.MINI_CLASS_ID as miniClassId,o.`name` as blCampusName,oo.`name` as blBrenchName,d.`NAME` as grade,");
    	sql.append("dd.`NAME` as `subject`,ddd.`NAME` as classType,dddd.`NAME` as productVersion,");
    	sql.append("ddddd.`NAME` as productQuarter,c.PEOPLE_QUANTITY as planAmount,");
    	sql.append(" ROUND(sum(case when c.STUDENT_ID is NULL then 0 else 1 end)/c.PEOPLE_QUANTITY*100,2) as fullClassRate ");
    	sql.append(" from ( ");
    	sql.append(" select a.MINI_CLASS_ID,a.TEACHER_ID,a.BL_CAMPUS_ID,a.GRADE,a.`SUBJECT`,a.CLASS_TYPE_ID,a.PRODUCT_VERSION_ID,a.PRODUCT_QUARTER_ID,a.PEOPLE_QUANTITY,b.STUDENT_ID from ( ");
        sql.append("select mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID,mc.GRADE,mc.`SUBJECT`,p.CLASS_TYPE_ID,p.PRODUCT_VERSION_ID,p.PRODUCT_QUARTER_ID,mc.PEOPLE_QUANTITY from mini_class mc,product p ");
    	sql.append("where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null  ");
    	

    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}

    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
	
    	sql.append(") as a left join  ");
    	sql.append("(select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp ");
    	sql.append("where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append("and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID'  ");
    	sql.append(")as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID) c,`user` u,organization o ,organization oo, data_dict d, ");
    	sql.append("data_dict dd, data_dict ddd,data_dict dddd,data_dict ddddd ");
    	sql.append("where c.TEACHER_ID = u.USER_ID and o.id = c.BL_CAMPUS_ID and c.GRADE = d.ID and c.`SUBJECT` = dd.ID ");
    	sql.append("and c.CLASS_TYPE_ID = ddd.ID and c.PRODUCT_VERSION_ID = dddd.ID and c.PRODUCT_QUARTER_ID =ddddd.ID ");
    	sql.append("and o.parentID = oo.id ");
    	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");

		if (StringUtil.isNotBlank(miniClassRateVo.getBlBrenchId())){
			sql.append("  and oo.id = :blBranchId  ");
			params.put("blBranchId", miniClassRateVo.getBlBrenchId());
		}
    	
    	sql.append("group by c.TEACHER_ID ");
    	
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		} 
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(miniClassDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params, 300));
    	
		//另外设置 计划招生人数
		//找到老师userId
		Map<String, BigDecimal> teacherPlanAmount = null;
		if(list!=null && !list.isEmpty()){
			StringBuffer teacherIdsBuffer = new StringBuffer();
			for(Map<Object, Object> map: list){
				teacherIdsBuffer.append("'"+map.get("teacherId")+"',");
			}
			String teacherIds = teacherIdsBuffer.substring(0, teacherIdsBuffer.length()-1); 
			
			teacherPlanAmount = getTeacherPlanAmount(miniClassRateVo, teacherIds,organization.getOrgLevel());		
		}		
		
		List<MiniClassFullRateVo> miniClassFullRateVos = new ArrayList<>();
		MiniClassFullRateVo vo = null;
		String teacherId = null;
		BigDecimal zero = new BigDecimal("0.0");
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				teacherId = map.get("teacherId").toString();
				vo = new MiniClassFullRateVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setClassType(map.get("classType")!=null?map.get("classType").toString():"");
				vo.setPaidFullAmount(map.get("paidFullAmount")!=null?(BigDecimal)map.get("paidFullAmount"):new BigDecimal("0"));
				
				//根据计算结果设置
				if(teacherPlanAmount!=null){
					if(teacherPlanAmount.get(teacherId)!=null){
						vo.setPlanAmount(teacherPlanAmount.get(teacherId).intValue());
					}else{
						vo.setPlanAmount(0);
					}
				}else{
					vo.setPlanAmount(0);
				}
				if(vo.getPlanAmount()==0){
					vo.setFullClassRate(zero);
				}else{
					vo.setFullClassRate(vo.getPaidFullAmount().divide(new BigDecimal(vo.getPlanAmount()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));;
				}
				
				//vo.setPlanAmount(map.get("planAmount")!=null?(Integer)map.get("planAmount"):0);
				//vo.setFullClassRate(map.get("fullClassRate")!=null?(BigDecimal)map.get("fullClassRate"):new BigDecimal("0.0"));
				
				miniClassFullRateVos.add(vo);
			}
		}		
		dataPackage.setDatas(miniClassFullRateVos);
		return dataPackage;
	}
	
	public Map<String, BigDecimal> getTeacherPlanAmount(MiniClassFullRateVo miniClassRateVo,String teacherIds,String orgLevel) {
		Map<String, BigDecimal> map = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer(512);
		sql.append("select COALESCE(sum(fc.PEOPLE_QUANTITY),0) as planAmount,fc.TEACHER_ID as teacherId from (select ");
		sql.append(" a1.PEOPLE_QUANTITY,a1.TEACHER_ID FROM ( "); 
		sql.append(" SELECT mc1.MINI_CLASS_ID,mc1.TEACHER_ID,mc1.PEOPLE_QUANTITY FROM ");
		sql.append(" mini_class mc1,product p1,organization o1,organization oo1 WHERE ");
		sql.append(" mc1.PRODUCE_ID = p1.ID and mc1.BL_CAMPUS_ID = o1.id and o1.parentID = oo1.id") ;
		sql.append(" AND mc1.TEACHER_ID IS NOT NULL ");
		
    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p1.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p1.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p1.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc1.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc1.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}

    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc1.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc1.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
    	//数据权限
    	sql.append(" and o1.orgLevel like :orgLevel ");
    	params.put("orgLevel", orgLevel+"%");

		if (StringUtil.isNotBlank(miniClassRateVo.getBlBrenchId())){
			sql.append(" and oo1.id = :blBranchId  ");
			params.put("blBranchId", miniClassRateVo.getBlBrenchId());
		}
		sql.append(") AS a1 LEFT JOIN ( ");
		sql.append(" SELECT mcs1.MINI_CLASS_ID,mcs1.STUDENT_ID");
		sql.append(" FROM mini_class_student mcs1,contract_product cp1 ");
		sql.append(" WHERE mcs1.CONTRACT_PRODUCT_ID = cp1.ID ");
		sql.append(" AND cp1.`STATUS` <> 'CLOSE_PRODUCT' ");
		sql.append(" AND cp1.`STATUS` <> 'REFUNDED' AND cp1.PAID_STATUS = 'PAID' ");
		sql.append(" ) AS b1 ON a1.MINI_CLASS_ID = b1.MINI_CLASS_ID ");
		sql.append(" WHERE a1.TEACHER_ID in ("+teacherIds+")GROUP BY a1.MINI_CLASS_ID) as fc GROUP BY fc.TEACHER_ID ");
		
		List<Map<Object,Object>> result = miniClassDao.findMapBySql(sql.toString(), params);
		
		if(result!=null && !result.isEmpty()){
			for(Map<Object, Object> pMap:result){
				map.put(pMap.get("teacherId").toString(), pMap.get("planAmount")!=null?(BigDecimal)pMap.get("planAmount"):new BigDecimal("0"));
			}
		}
	    return map;
		
	}
	
	
	
	@Override
	public DataPackage getMiniClassFullClassClassRate(DataPackage dataPackage, MiniClassFullRateVo miniClassRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());   	
    	sql.append(" select sum(case when c.STUDENT_ID is NULL then 0 else 1 end) as paidFullAmount,c.`NAME` as miniClassName,u.`NAME` as teacherName, ");
    	sql.append(" c.MINI_CLASS_ID as miniClassId,o.`name` as blCampusName,oo.`name` as blBrenchName,d.`NAME` as grade, ");
    	sql.append(" dd.`NAME` as `subject`,ddd.`NAME` as classType,dddd.`NAME` as productVersion, ");
    	sql.append(" ddddd.`NAME` as productQuarter,c.PEOPLE_QUANTITY as planAmount, ");
    	sql.append(" ROUND(sum(case when c.STUDENT_ID is NULL then 0 else 1 end)/c.PEOPLE_QUANTITY*100,2) as fullClassRate ");
    	sql.append(" from ( ");
    	sql.append(" select a.`NAME`,a.MINI_CLASS_ID,a.TEACHER_ID,a.BL_CAMPUS_ID,a.GRADE,a.`SUBJECT`,a.CLASS_TYPE_ID,a.PRODUCT_VERSION_ID,a.PRODUCT_QUARTER_ID,a.PEOPLE_QUANTITY,b.STUDENT_ID from ( ");
    	sql.append(" select mc.`NAME`,mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID,mc.GRADE,mc.`SUBJECT`,p.CLASS_TYPE_ID,p.PRODUCT_VERSION_ID,p.PRODUCT_QUARTER_ID,mc.PEOPLE_QUANTITY from mini_class mc,product p "); 
    	sql.append(" where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null ");

    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}
    	//小班名称
    	if(StringUtil.isNotBlank(miniClassRateVo.getMiniClassName())){
    		sql.append(" and mc.`NAME` like :miniClassName ");
    		params.put("miniClassName", "%"+miniClassRateVo.getMiniClassName()+"%");
    	}
    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
   	
    	sql.append(") as a left join ");
    	sql.append(" (select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp  ");
    	sql.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append(" and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID' ");
    	sql.append(" )as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID) c,`user` u,organization o ,organization oo, data_dict d,");
    	sql.append(" data_dict dd, data_dict ddd,data_dict dddd,data_dict ddddd ");
    	sql.append(" where c.TEACHER_ID = u.USER_ID and o.id = c.BL_CAMPUS_ID and c.GRADE = d.ID and c.`SUBJECT` = dd.ID ");
    	sql.append(" and c.CLASS_TYPE_ID = ddd.ID and c.PRODUCT_VERSION_ID = dddd.ID and c.PRODUCT_QUARTER_ID =ddddd.ID "); 
    	sql.append(" and o.parentID = oo.id  ");
    	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");

		if (StringUtil.isNotBlank(miniClassRateVo.getBlBrenchId())){
			sql.append("  and oo.id = :blBranchId  ");
			params.put("blBranchId", miniClassRateVo.getBlBrenchId());
		}
    	
    	sql.append(" group by c.MINI_CLASS_ID  ");

		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		} 
 
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(miniClassDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params, 300));
    	
		List<MiniClassFullRateVo> miniClassFullRateVos = new ArrayList<>();
		MiniClassFullRateVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassFullRateVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setMiniClassName(map.get("miniClassName")!=null?map.get("miniClassName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setClassType(map.get("classType")!=null?map.get("classType").toString():"");
				vo.setPaidFullAmount(map.get("paidFullAmount")!=null?(BigDecimal)map.get("paidFullAmount"):new BigDecimal("0"));
				vo.setPlanAmount(map.get("planAmount")!=null?(Integer)map.get("planAmount"):0);
				vo.setFullClassRate(map.get("fullClassRate")!=null?(BigDecimal)map.get("fullClassRate"):new BigDecimal("0.0"));
				
				miniClassFullRateVos.add(vo);
			}
		}		
		dataPackage.setDatas(miniClassFullRateVos);
		return dataPackage;
	}

	@Override
	public DataPackage getMiniClassQuitClassTeacherRate(DataPackage dataPackage, MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	sql.append(" select xx.*,ROUND(paidZeroConsumeNum/zeroConsumeNum*100,2) as preQuitClassRate, ");
    	sql.append(" ROUND(paidConsumeNum/consumeNum*100,2) as afterQuitClassRate from ( ");
    	sql.append(" select mc.TEACHER_ID,u.`NAME` as teacherName,d.`NAME` as productVersion,dd.`NAME` as productQuarter, oo.`name` as blBrenchName, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT =0 then 1 else 0 end)as paidZeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' AND cp.CONSUME_AMOUNT =0 then 1 else 0 end)as zeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT >0 then 1 else 0 end)as paidConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' AND cp.CONSUME_AMOUNT >0 then 1 else 0 end)as consumeNum ");   	
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");
    	sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
    	sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
    	sql.append(" left join `user` u on u.USER_ID = mc.TEACHER_ID ");
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" left join organization oo on oo.id = o.parentID  ");
    	sql.append(" where mc.TEACHER_ID is not null ");
       	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassQuitRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}

		if (StringUtil.isNotBlank(miniClassQuitRateVo.getBlBrenchId())){
			sql.append("  and oo.id = :blBranchId  ");
			params.put("blBranchId", miniClassQuitRateVo.getBlBrenchId());
		}
 	
    	
    	sql.append(" group by mc.TEACHER_ID ) as xx ");
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		}
		
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(miniClassDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params, 300));
    	
		List<MiniClassQuitRateVo> miniClassQuitRateVos = new ArrayList<>();
		MiniClassQuitRateVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassQuitRateVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setPaidZeroConsumeNum(map.get("paidZeroConsumeNum")!=null?(BigDecimal)map.get("paidZeroConsumeNum"):new BigDecimal("0"));
				vo.setZeroConsumeNum(map.get("zeroConsumeNum")!=null?(BigDecimal)map.get("zeroConsumeNum"):new BigDecimal("0"));
				vo.setPreQuitClassRate(map.get("preQuitClassRate")!=null?(BigDecimal)map.get("preQuitClassRate"):new BigDecimal("0.0"));
				vo.setPaidConsumeNum(map.get("paidConsumeNum")!=null?(BigDecimal)map.get("paidConsumeNum"):new BigDecimal("0"));
				vo.setConsumeNum(map.get("consumeNum")!=null?(BigDecimal)map.get("consumeNum"):new BigDecimal("0"));
				vo.setAfterQuitClassRate(map.get("afterQuitClassRate")!=null?(BigDecimal)map.get("afterQuitClassRate"):new BigDecimal("0.0"));
				
				miniClassQuitRateVos.add(vo);
			}
		}		
		dataPackage.setDatas(miniClassQuitRateVos);
		return dataPackage;
	}

	@Override
	public DataPackage getMiniClassQuitClassProductRate(DataPackage dataPackage, MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	sql.append(" select xx.*,ROUND(paidZeroConsumeNum/zeroConsumeNum*100,2) as preQuitClassRate, ");
    	sql.append(" ROUND(paidConsumeNum/consumeNum*100,2) as afterQuitClassRate from ( ");
    	sql.append(" select mc.PRODUCE_ID,d.`NAME` as productVersion,dd.`NAME` as productQuarter,o.`name` as blCampusName,oo.`name` as blBrenchName, ");
    	sql.append(" g.`NAME` as grade,sb.`NAME` as `subject`, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT =0 then 1 else 0 end)as paidZeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' AND cp.CONSUME_AMOUNT =0 then 1 else 0 end)as zeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT >0 then 1 else 0 end)as paidConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' AND cp.CONSUME_AMOUNT >0 then 1 else 0 end)as consumeNum ");
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");
    	sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
    	sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" left join organization oo on oo.id = o.parentID ");
    	sql.append(" left join data_dict g on mc.GRADE = g.ID ");
    	sql.append(" left join data_dict sb on mc.`SUBJECT` = sb.ID ");
    	
    	sql.append(" where 1=1 ");
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}

    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}

		if (StringUtil.isNotBlank(miniClassQuitRateVo.getBlBrenchId())){
			sql.append("  and oo.id = :blBranchId  ");
			params.put("blBranchId", miniClassQuitRateVo.getBlBrenchId());
		}
       	
    	sql.append(" group by mc.PRODUCE_ID ) as xx  ");
    	
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		}
    	
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(miniClassDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params, 300));
    	
		List<MiniClassQuitRateVo> miniClassQuitRateVos = new ArrayList<>();
		MiniClassQuitRateVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassQuitRateVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setPaidZeroConsumeNum(map.get("paidZeroConsumeNum")!=null?(BigDecimal)map.get("paidZeroConsumeNum"):new BigDecimal("0"));
				vo.setZeroConsumeNum(map.get("zeroConsumeNum")!=null?(BigDecimal)map.get("zeroConsumeNum"):new BigDecimal("0"));
				vo.setPreQuitClassRate(map.get("preQuitClassRate")!=null?(BigDecimal)map.get("preQuitClassRate"):new BigDecimal("0.0"));
				vo.setPaidConsumeNum(map.get("paidConsumeNum")!=null?(BigDecimal)map.get("paidConsumeNum"):new BigDecimal("0"));
				vo.setConsumeNum(map.get("consumeNum")!=null?(BigDecimal)map.get("consumeNum"):new BigDecimal("0"));
				vo.setAfterQuitClassRate(map.get("afterQuitClassRate")!=null?(BigDecimal)map.get("afterQuitClassRate"):new BigDecimal("0.0"));				
				miniClassQuitRateVos.add(vo);
			}
		}		
		dataPackage.setDatas(miniClassQuitRateVos);
		return dataPackage;
	}

	@Override
	public List<Map<Object, Object>> getMiniClassFullClassTeacherRateSize(MiniClassFullRateVo miniClassRateVo) {
		
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	
    	sql.append(" select a.MINI_CLASS_ID,a.TEACHER_ID,b.STUDENT_ID from ( ");
        sql.append(" select mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID from mini_class mc,product p "); 
    	sql.append(" where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null  ");
    		
    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}
    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
 	
    	sql.append(" ) as a left join  ");
    	sql.append(" (select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp ");
    	sql.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append(" and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID'  ");
    	sql.append(" )as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID ");
    	sql.append(" left join organization o on a.BL_CAMPUS_ID = o.id ");
    	
    	//数据权限
    	sql.append(" where o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	sql.append(" group by a.TEACHER_ID");
    	
		List<Map<Object,Object>> list=miniClassDao.findMapBySql(sql.toString(), params);
		
		return list;
	}

	@Override
	public List<Map<Object, Object>> getMiniClassFullClassClassRateSize(MiniClassFullRateVo miniClassRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());    
    	sql.append(" select a.MINI_CLASS_ID,a.TEACHER_ID,b.STUDENT_ID from ( ");
    	sql.append(" select mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID from mini_class mc,product p "); 
    	sql.append(" where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null ");

    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}
    	//小班名称
    	if(StringUtil.isNotBlank(miniClassRateVo.getMiniClassName())){
    		sql.append(" and mc.`NAME` like :miniClassName ");
    		params.put("miniClassName", "%"+miniClassRateVo.getMiniClassName()+"%");
    	}
    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}  	
    	
    	sql.append(") as a left join ");
    	sql.append(" (select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp  ");
    	sql.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append(" and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID' ");
    	sql.append(" )as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID   ");
    	sql.append(" left join organization o on a.BL_CAMPUS_ID = o.id ");
    	//数据权限
    	sql.append(" where o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	sql.append(" group by a.MINI_CLASS_ID ");

		List<Map<Object,Object>> list=miniClassDao.findMapBySql(sql.toString(), params);
		return list;
	}

	@Override
	public List<Map<Object, Object>> getMiniClassQuitClassTeacherRateSize(MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	sql.append(" select * ");   	
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" where mc.TEACHER_ID is not null ");
    	   	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
       	
    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassQuitRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}    	
    	sql.append(" group by mc.TEACHER_ID ");	
		List<Map<Object,Object>> list=miniClassDao.findMapBySql(sql.toString(), params);
		return list;
	}

	@Override
	public List<Map<Object, Object>> getMiniClassQuitClassProductRateSize(MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	
    	sql.append(" select * ");
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");   	
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" where 1=1 ");    	   	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	

    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}

    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}
       	
    	sql.append(" group by mc.PRODUCE_ID  ");
    	
    	
		List<Map<Object,Object>> list=miniClassDao.findMapBySql(sql.toString(), params);
		return list;
	}

	@Override
	public List<MiniClassFullRateTeacherExcelVo> getMCFullClassTeacheerRateToExcel(DataPackage dataPackage,
			MiniClassFullRateVo miniClassRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId()); 
    	
    	
    	sql.append("select sum(case when c.STUDENT_ID is NULL then 0 else 1 end) as paidFullAmount,u.`NAME` as teacherName,c.TEACHER_ID as teacherId,");
    	sql.append("c.MINI_CLASS_ID as miniClassId,o.`name` as blCampusName,oo.`name` as blBrenchName,d.`NAME` as grade,");
    	sql.append("dd.`NAME` as `subject`,ddd.`NAME` as classType,dddd.`NAME` as productVersion,");
    	sql.append("ddddd.`NAME` as productQuarter,c.PEOPLE_QUANTITY as planAmount,");
    	sql.append(" ROUND(sum(case when c.STUDENT_ID is NULL then 0 else 1 end)/c.PEOPLE_QUANTITY*100,2) as fullClassRate ");
    	sql.append(" from ( ");
    	sql.append(" select a.MINI_CLASS_ID,a.TEACHER_ID,a.BL_CAMPUS_ID,a.GRADE,a.`SUBJECT`,a.CLASS_TYPE_ID,a.PRODUCT_VERSION_ID,a.PRODUCT_QUARTER_ID,a.PEOPLE_QUANTITY,b.STUDENT_ID from ( ");
        sql.append("select mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID,mc.GRADE,mc.`SUBJECT`,p.CLASS_TYPE_ID,p.PRODUCT_VERSION_ID,p.PRODUCT_QUARTER_ID,mc.PEOPLE_QUANTITY from mini_class mc,product p ");
    	sql.append("where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null  ");
    	

    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}
    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
	
    	sql.append(") as a left join  ");
    	sql.append("(select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp ");
    	sql.append("where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append("and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID'  ");
    	sql.append(")as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID) c,`user` u,organization o ,organization oo, data_dict d, ");
    	sql.append("data_dict dd, data_dict ddd,data_dict dddd,data_dict ddddd ");
    	sql.append("where c.TEACHER_ID = u.USER_ID and o.id = c.BL_CAMPUS_ID and c.GRADE = d.ID and c.`SUBJECT` = dd.ID ");
    	sql.append("and c.CLASS_TYPE_ID = ddd.ID and c.PRODUCT_VERSION_ID = dddd.ID and c.PRODUCT_QUARTER_ID =ddddd.ID ");
    	sql.append("and o.parentID = oo.id ");
    	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	sql.append("group by c.TEACHER_ID ");
    	
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		} 
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);

		
		//另外设置 计划招生人数
		//找到老师userId
		Map<String, BigDecimal> teacherPlanAmount = null;
		if (list != null && !list.isEmpty()) {
			StringBuffer teacherIdsBuffer = new StringBuffer();
			for (Map<Object, Object> map : list) {
				teacherIdsBuffer.append("'" + map.get("teacherId") + "',");
			}
			String teacherIds = teacherIdsBuffer.substring(0, teacherIdsBuffer.length() - 1);

			teacherPlanAmount = getTeacherPlanAmount(miniClassRateVo, teacherIds, organization.getOrgLevel());
		}	
				
		
		
		List<MiniClassFullRateTeacherExcelVo> miniClassFullRateVos = new ArrayList<>();
		MiniClassFullRateTeacherExcelVo vo = null;
		String teacherId = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				teacherId = map.get("teacherId").toString();
				vo = new MiniClassFullRateTeacherExcelVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setClassType(map.get("classType")!=null?map.get("classType").toString():"");
				vo.setPaidFullAmount(map.get("paidFullAmount")!=null?(BigDecimal)map.get("paidFullAmount"):new BigDecimal("0"));
				
				//根据计算结果设置
				if(teacherPlanAmount!=null){
					if(teacherPlanAmount.get(teacherId)!=null){
						vo.setPlanAmount(teacherPlanAmount.get(teacherId).intValue());
					}else{
						vo.setPlanAmount(0);
					}
				}else{
					vo.setPlanAmount(0);
				}
				if(vo.getPlanAmount()==0){
					vo.setFullClassRate("0.00%");
				}else{
					vo.setFullClassRate(vo.getPaidFullAmount().divide(new BigDecimal(vo.getPlanAmount()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).toString()+"%");
				}
			
//				vo.setPlanAmount(map.get("planAmount")!=null?(Integer)map.get("planAmount"):0);
//				vo.setFullClassRate(map.get("fullClassRate")!=null?map.get("fullClassRate").toString()+"%":"0.00%");				
				miniClassFullRateVos.add(vo);
			}
		}		

		return miniClassFullRateVos;
	}

	@Override
	public List<MiniClassFullRateClassExcelVo> getMCFullClassClassRateToExcel(DataPackage dataPackage,
			MiniClassFullRateVo miniClassRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());   	
    	sql.append(" select sum(case when c.STUDENT_ID is NULL then 0 else 1 end) as paidFullAmount,c.`NAME` as miniClassName,u.`NAME` as teacherName, ");
    	sql.append(" c.MINI_CLASS_ID as miniClassId,o.`name` as blCampusName,oo.`name` as blBrenchName,d.`NAME` as grade, ");
    	sql.append(" dd.`NAME` as `subject`,ddd.`NAME` as classType,dddd.`NAME` as productVersion, ");
    	sql.append(" ddddd.`NAME` as productQuarter,c.PEOPLE_QUANTITY as planAmount, ");
    	sql.append(" ROUND(sum(case when c.STUDENT_ID is NULL then 0 else 1 end)/c.PEOPLE_QUANTITY*100,2) as fullClassRate ");
    	sql.append(" from ( ");
    	sql.append(" select a.`NAME`,a.MINI_CLASS_ID,a.TEACHER_ID,a.BL_CAMPUS_ID,a.GRADE,a.`SUBJECT`,a.CLASS_TYPE_ID,a.PRODUCT_VERSION_ID,a.PRODUCT_QUARTER_ID,a.PEOPLE_QUANTITY,b.STUDENT_ID from ( ");
    	sql.append(" select mc.`NAME`,mc.MINI_CLASS_ID,mc.TEACHER_ID,mc.BL_CAMPUS_ID,mc.GRADE,mc.`SUBJECT`,p.CLASS_TYPE_ID,p.PRODUCT_VERSION_ID,p.PRODUCT_QUARTER_ID,mc.PEOPLE_QUANTITY from mini_class mc,product p "); 
    	sql.append(" where mc.PRODUCE_ID = p.ID and mc.TEACHER_ID is not null ");

    	//年份
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassRateVo.getBlCampusId());
    	}
    	//小班名称
    	if(StringUtil.isNotBlank(miniClassRateVo.getMiniClassName())){
    		sql.append(" and mc.`NAME` like :miniClassName ");
    		params.put("miniClassName", "%"+miniClassRateVo.getMiniClassName()+"%");
    	}
    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassRateVo.getSubject());
    	}
   	
    	sql.append(") as a left join ");
    	sql.append(" (select mcs.MINI_CLASS_ID,mcs.STUDENT_ID from mini_class_student mcs,contract_product cp  ");
    	sql.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append(" and cp.`STATUS` <> 'CLOSE_PRODUCT' and cp.`STATUS` <> 'REFUNDED' and cp.PAID_STATUS ='PAID' ");
    	sql.append(" )as b on a.MINI_CLASS_ID = b.MINI_CLASS_ID) c,`user` u,organization o ,organization oo, data_dict d,");
    	sql.append(" data_dict dd, data_dict ddd,data_dict dddd,data_dict ddddd ");
    	sql.append(" where c.TEACHER_ID = u.USER_ID and o.id = c.BL_CAMPUS_ID and c.GRADE = d.ID and c.`SUBJECT` = dd.ID ");
    	sql.append(" and c.CLASS_TYPE_ID = ddd.ID and c.PRODUCT_VERSION_ID = dddd.ID and c.PRODUCT_QUARTER_ID =ddddd.ID "); 
    	sql.append(" and o.parentID = oo.id  ");
    	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	sql.append(" group by c.MINI_CLASS_ID  ");

		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		} 
 
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
	
		List<MiniClassFullRateClassExcelVo> miniClassFullRateVos = new ArrayList<>();
		MiniClassFullRateClassExcelVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassFullRateClassExcelVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setMiniClassName(map.get("miniClassName")!=null?map.get("miniClassName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setClassType(map.get("classType")!=null?map.get("classType").toString():"");
				vo.setPaidFullAmount(map.get("paidFullAmount")!=null?(BigDecimal)map.get("paidFullAmount"):new BigDecimal("0"));
				vo.setPlanAmount(map.get("planAmount")!=null?(Integer)map.get("planAmount"):0);
				vo.setFullClassRate(map.get("fullClassRate")!=null?map.get("fullClassRate")+"%":"0.00%");
				
				miniClassFullRateVos.add(vo);
			}
		}		
		return miniClassFullRateVos;
	}

	@Override
	public List<MiniClassQuitRateTeacherExcelVo> getMCQuitClassTeacheerRateToExcel(DataPackage dataPackage,
			MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	sql.append(" select xx.*,ROUND(zeroConsumeNum/paidZeroConsumeNum*100,2) as preQuitClassRate, ");
    	sql.append(" ROUND(consumeNum/paidConsumeNum*100,2) as afterQuitClassRate from ( ");
    	sql.append(" select mc.TEACHER_ID,u.`NAME` as teacherName,d.`NAME` as productVersion,dd.`NAME` as productQuarter, oo.`name` as blBrenchName,");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT =0 then 1 else 0 end)as paidZeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`<>'CLOSE_PRODUCT' AND cp.CONSUME_AMOUNT =0 then 1 else 0 end)as zeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT >0 then 1 else 0 end)as paidConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`<>'CLOSE_PRODUCT' AND cp.CONSUME_AMOUNT >0 then 1 else 0 end)as consumeNum ");   	
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");
    	sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
    	sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
    	sql.append(" left join `user` u on u.USER_ID = mc.TEACHER_ID ");
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" left join organization oo on oo.id = o.parentID  ");
    	sql.append(" where mc.TEACHER_ID is not null ");
       	
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	//老师
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getTeacherId())){
    		sql.append(" and mc.TEACHER_ID = :teacherId ");
    		params.put("teacherId", miniClassQuitRateVo.getTeacherId());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}
 	
    	
    	sql.append(" group by mc.TEACHER_ID ) as xx ");
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		}
		
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);


		List<MiniClassQuitRateTeacherExcelVo> miniClassQuitRateVos = new ArrayList<>();
		MiniClassQuitRateTeacherExcelVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassQuitRateTeacherExcelVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
				vo.setPaidZeroConsumeNum(map.get("paidZeroConsumeNum")!=null?(BigDecimal)map.get("paidZeroConsumeNum"):new BigDecimal("0"));
				vo.setZeroConsumeNum(map.get("zeroConsumeNum")!=null?(BigDecimal)map.get("zeroConsumeNum"):new BigDecimal("0"));
				vo.setPreQuitClassRate(map.get("preQuitClassRate")!=null?map.get("preQuitClassRate")+"%":"0.00%");
				vo.setPaidConsumeNum(map.get("paidConsumeNum")!=null?(BigDecimal)map.get("paidConsumeNum"):new BigDecimal("0"));
				vo.setConsumeNum(map.get("consumeNum")!=null?(BigDecimal)map.get("consumeNum"):new BigDecimal("0"));
				vo.setAfterQuitClassRate(map.get("afterQuitClassRate")!=null?map.get("afterQuitClassRate")+"%":"0.00%");
				miniClassQuitRateVos.add(vo);
			}
		}		
		return miniClassQuitRateVos;
	}

	@Override
	public List<MiniClassQuitRateProductExcelVo> getMCQuitClassProductRateToExcel(DataPackage dataPackage,
			MiniClassQuitRateVo miniClassQuitRateVo) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	User user = userService.getCurrentLoginUser();
    	Organization organization = organizationService.findById(user.getOrganizationId());
    	sql.append(" select xx.*,ROUND(zeroConsumeNum/paidZeroConsumeNum*100,2) as preQuitClassRate, ");
    	sql.append(" ROUND(consumeNum/paidConsumeNum*100,2) as afterQuitClassRate from ( ");
    	sql.append(" select mc.PRODUCE_ID,d.`NAME` as productVersion,dd.`NAME` as productQuarter,o.`name` as blCampusName,oo.`name` as blBrenchName, ");
    	sql.append(" g.`NAME` as grade,sb.`NAME` as `subject`, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT =0 then 1 else 0 end)as paidZeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`<>'CLOSE_PRODUCT' AND cp.CONSUME_AMOUNT =0 then 1 else 0 end)as zeroConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`='CLOSE_PRODUCT' and cp.CONSUME_AMOUNT >0 then 1 else 0 end)as paidConsumeNum, ");
    	sql.append(" sum(case when cp.PAID_STATUS='PAID' and cp.`STATUS`<>'CLOSE_PRODUCT' AND cp.CONSUME_AMOUNT >0 then 1 else 0 end)as consumeNum ");
    	sql.append(" from mini_class mc ");
    	sql.append(" left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
    	sql.append(" left join contract_product cp on cp.id = mcs.CONTRACT_PRODUCT_ID ");
    	sql.append(" left join product p on mc.PRODUCE_ID = p.ID ");
    	sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
    	sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
    	sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
    	sql.append(" left join organization oo on oo.id = o.parentID ");
    	sql.append(" left join data_dict g on mc.GRADE = g.ID ");
    	sql.append(" left join data_dict sb on mc.`SUBJECT` = sb.ID ");
    	
    	sql.append(" where 1=1 ");
    	//数据权限
    	sql.append(" and o.orgLevel like :orgLevel ");
    	params.put("orgLevel", organization.getOrgLevel()+"%");
    	
    	
    	//年份
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductVersion())){
    		sql.append(" and p.PRODUCT_VERSION_ID = :productVersion ");
    		params.put("productVersion", miniClassQuitRateVo.getProductVersion());
    	}
    	//季度
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getProductQuarter())){
    		sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarter ");
    		params.put("productQuarter", miniClassQuitRateVo.getProductQuarter());
    	}
    	
    	//班级类型
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getClassType())){
    		sql.append(" and p.CLASS_TYPE_ID = :classType ");
    		params.put("classType", miniClassQuitRateVo.getClassType());
    	}
    	
    	//校区id
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getBlCampusId())){
    		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
    		params.put("blCampusId", miniClassQuitRateVo.getBlCampusId());
    	}

    	
    	//年级
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getGrade())){
    		sql.append(" and mc.GRADE = :grade ");
    		params.put("grade", miniClassQuitRateVo.getGrade());
    	}
    	//科目
    	if(StringUtil.isNotBlank(miniClassQuitRateVo.getSubject())){
    		sql.append(" and mc.`SUBJECT` = :subject ");
    		params.put("subject", miniClassQuitRateVo.getSubject());
    	}
       	
    	sql.append(" group by mc.PRODUCE_ID ) as xx  ");
    	
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			
		}
    	
		List<Map<Object,Object>> list=miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
	
		List<MiniClassQuitRateProductExcelVo> miniClassQuitRateVos = new ArrayList<>();
		MiniClassQuitRateProductExcelVo vo = null;
		if(list!=null && !list.isEmpty()){
			for(Map<Object, Object> map: list){
				vo = new MiniClassQuitRateProductExcelVo();
				vo.setProductVersion(map.get("productVersion")!=null?map.get("productVersion").toString():"");
				vo.setProductQuarter(map.get("productQuarter")!=null?map.get("productQuarter").toString():"");
				vo.setBlBrenchName(map.get("blBrenchName")!=null?map.get("blBrenchName").toString():"");
				vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
				vo.setSubject(map.get("subject")!=null?map.get("subject").toString():"");
				vo.setGrade(map.get("grade")!=null?map.get("grade").toString():"");
				vo.setPaidZeroConsumeNum(map.get("paidZeroConsumeNum")!=null?(BigDecimal)map.get("paidZeroConsumeNum"):new BigDecimal("0"));
				vo.setZeroConsumeNum(map.get("zeroConsumeNum")!=null?(BigDecimal)map.get("zeroConsumeNum"):new BigDecimal("0"));
				vo.setPreQuitClassRate(map.get("preQuitClassRate")!=null?map.get("preQuitClassRate")+"%":"0.00%");
				vo.setPaidConsumeNum(map.get("paidConsumeNum")!=null?(BigDecimal)map.get("paidConsumeNum"):new BigDecimal("0"));
				vo.setConsumeNum(map.get("consumeNum")!=null?(BigDecimal)map.get("consumeNum"):new BigDecimal("0"));
				vo.setAfterQuitClassRate(map.get("afterQuitClassRate")!=null?map.get("afterQuitClassRate")+"%":"0.00%");				
				miniClassQuitRateVos.add(vo);
			}
		}		
		return miniClassQuitRateVos;
	}

	/**
	 * 按pos机类型获取收款笔数
	 */
    @Override
    public List<Map<Object, Object>> getFundsCountGroupByPosType(
            OrganizationDateReqVo reqVo) {
        return reportDao.getFundsCountGroupByPosType(reqVo);
    }

    /**
     * 按pos机类型获取收款金额
     */
    @Override
    public List<Map<Object, Object>> getFundsAmountGroupByPosType(
            OrganizationDateReqVo reqVo) {
        return reportDao.getFundsAmountGroupByPosType(reqVo);
    }

}
