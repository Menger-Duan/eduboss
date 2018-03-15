package com.eduboss.service;

import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public interface OperationCountService {
	
	
	/**
	 * 更新市场报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayCustypeTotal(String targetDayStart,String targetDayEnd) throws SQLException;
	/**
	 * 更新现金流
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateCashTotal(String targetDayStart,String targetDayEnd) throws SQLException;
	
	/**
	 * 更新总览
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateAllTotal(String targetDayStart,String targetDayEnd) throws SQLException;
	
	/**
	 * 更新手机端组织架构
	 * @throws SQLException
	 */
	public void updateMobileOranization() throws SQLException;
	
	/**
	 * 更新现金流报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayFinance(String targetDay) throws SQLException;

    /**
     * 更新现金流（合同）报表数据
     * @param targetDay
     * @throws SQLException
     */
    public void updateOdsDayFinanceContract(String targetDay) throws SQLException;

	/**
	 * 更新课消报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayCourseConsume(String targetDay) throws SQLException;

    /**
     * 更新退费统计数据
     * @param targetDay
     * @throws SQLException
     */
    public void updateOdsDayRefundAnalyze(String targetDay) throws SQLException;

    /**
     * 更新收费统计数据
     * @param targetDay
     * @throws SQLException
     */
    public void updateOdsDayIncomingAnalyze(String targetDay) throws SQLException;

	/**
	 * 更新一对一剩余资金报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsRealStudentRemaining(String targetDay) throws SQLException;

	/**
	 * 更新一对一学生人数报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsRealStudentCount(String targetDay) throws SQLException;
	
	/**
	 * 更新一对一学生人数报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsRealStudentOOOCount(String targetDay) throws SQLException;
	
	public void updateOdsRealStudentOTMCount(String targetDay) throws SQLException;

	/**
	 * 更新老师课消报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayCourseConsumeTeacherSubject(String targetDay)
			throws SQLException;

	/**
	 * 更新老师课消(小时)报表数据
	 * @param targetDay
	 * @throws SQLException
     */
	void updateOdsDayCourseConsumeTeacherXiaoShi(String targetDay)
			throws SQLException;

    /**
     * 更新老师课消报表数据（学生维度）
     * @param targetDay
     * @throws SQLException
     */
    public void updateOdsDayCourseConsumeStudentSubject(String targetDay)
            throws SQLException;

	/**
	 * 更新小班剩余资金报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassSurplusMoney(String targetDay)
			throws SQLException;

	/**
	 * 更新 - 学生剩余资金 - 报表数据
	 * @throws SQLException
	 */
	public void updateStudentSurplusFunding(String targetDay)
			throws SQLException;

	/**
	 * 更新小班人数报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassStudentCount(String targetDay)
			throws SQLException;

	/**
	 * 更新小班课消报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassConsume(String targetDay) throws SQLException;

	/**
	 * 更新系统监控报表数据
	 * @throws SQLException
	 */
	public void updateSystemMonitor() throws SQLException;

	/**
	 * 更新用户组织架构报表数据
	 * @throws SQLException
	 */
	public void updateUserOrg() throws SQLException;

	/**
	 * 更新老师未考勤课时报表数据
	 * @throws SQLException
	 */
	public void updateTeacherNotAttendanceCourseHour() throws SQLException;

	/**
	 * 更新学管未绑定考勤编号的学生数量报表数据
	 * @throws SQLException
	 */
	public void updateUnboundAttendanceNumber() throws SQLException;

	
	/**
	 * 待收金额
	 * @throws SQLException
	 */
	public void updateStudentPendingMoney(String targetDay) throws SQLException;

    /**
     * 小班产品人数统计
     * @throws SQLException
     */
    public void updateMiniClassPeople(String targetDay) throws SQLException;
	
	/**
	 * 购买课时
	 * @throws SQLException
	 */
	public void updatePayClassHour(String targetDay) throws SQLException;
	
	/**
	 * 购买课时(每天)
	 * @throws SQLException
	 */
	public void updatePayClassHourEveryDay(String targetDay) throws SQLException;
	
	/**
	 * 购买课时(校区维度)
	 * @throws SQLException
	 */
	public void updatePayClassHourCampus(String targetDay) throws SQLException;
	
	/**
	 * 小班考勤分析
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateMiniClassAttendsAnalyze(String targetDay) throws SQLException;

	/**
	 * 更新资金分配表统计， 每天分配到一对一 小班的具体的钱， 购买的课时
	 * @param yesterday
	 */
	public void updateMoneyArrangeRecord(String yesterday)throws SQLException;
	
	/**
	 * 更新学生的课程状态（在读、停课、结课）
	 * @throws SQLException
	 */
	public void updateStudentOneOnOneClassStatus() throws SQLException;

	/**
	 * 业绩（校区）
	 */
	public void updateOdsDayContractBonusOrganization(String targetDay)
			throws SQLException;
	
	/**
	 * 业绩（签单人）
	 */
	public void updateOdsDayContractBonusStaff(String targetDay)
			throws SQLException;

	/**
	 * 更改学生状态
	 */
	public void updateStudentStatus() throws SQLException;
	
	/**
	 * 更新学生剩余资金剩余课时
	 * @throws SQLException
	 */
	public void updateStudentAccMvRemainAmount() throws SQLException;
	
	/**
	 * 更新实际小班人数报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassStudentRealCount(String targetDay) throws SQLException;
	
	/**
	 * 更新营收统计表
	 * @param targetStartDay
	 * @param targetEndDay
	 * @throws SQLException
	 */
    public void updateOdsDayIncomingQuantityAnalyze(String targetStartDay, String targetEndDay) throws SQLException;
    
    /**
     * 更新产品销售分析报表数据
     */
	public void updateOdsDayProductMarket(String targetStartDay, String targetEndDay) throws SQLException;
	
	 /**
     * 更新产品消耗分析报表数据
     */
	public void updateOdsDayProductConsume(String targetStartDay, String targetEndDay) throws SQLException;
	
	/**
	 * 更新一对一学生状态
	 */
	public void updateStudentOOOStatus() throws SQLException;
	
	/**
	 * 更新一对多学生状态
	 */
	public void updateStudentOTMStatus() throws SQLException;
	
	/**
	 * 更新一对多剩余资金报表数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsOtmRealStudentRemaining(String targetDay) throws SQLException;
	
	
	
	 /**
    * 目标班自动月结
    */
	public void autoCompleteEcsClass(int year, int month,String yearMonth,String lastDay) throws SQLException;
	
	public void autoArrangeArea(String get2NextMonthStart,String name) throws SQLException;
	
	public void updateOdsDayMonthlyBalance(String targetDay, String mappingDate, String lastMonthDay) throws SQLException;
	
	public void updateOdsDayMonthlyBalance(String targetDay) throws SQLException;
	
	/**
	 * 刷新财务现金流凭证
	 * @param targetDay
	 * @param mappingDate
	 * @throws SQLException
	 */
	public void updateOdsFinanceMonthlyEvidence(String targetDay, String mappingDate) throws SQLException;
	
	/**
	 * 刷新财务扣费凭证
	 * @param targetDay
	 * @param mappingDate
	 * @throws SQLException
	 */
	public void updateOdsIncomeMonthlyEvidence(String targetDay, String mappingDate) throws SQLException;
	
	/**
	 * 刷新营收凭证
	 * @param targetDay
	 * @param mappingDate
	 * @throws SQLException
	 */
	public void updateOdsIncomeMonthlyStudentCampus(String targetDay, String mappingDate) throws SQLException;
	
	/**
	 * 刷新营收凭证 校区
	 * @param targetDay
	 * @param mappingDate
	 * @throws SQLException
	 */
	public void updateOdsIncomeMonthlyStudentCampusByCampus(String targetDay, String mappingDate, String campusId) throws SQLException;
	
	/**
	 * 刷新剩余资金凭证
	 * @param targetDay
	 * @param mappingDate
	 * @throws SQLException
	 */
	public void updateOdsRemainMonthlyStudentCampus(String targetDay, String mappingDate) throws SQLException;
	
	public void updateOdsPaymentReciept(String targetDay,String campusId) throws SQLException;

	/**
	 * 月头 2-8号刷新没有开始审批的数据
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsPaymentReciept(String targetDay) throws SQLException;
	
	/**
	 * 执行系统审批收款记录
	 */
	public void auditFundsChangeHistory() throws SQLException;
	
	/**
	 * 更新导入收款数据的校区
	 */
	public void updatePosPayDataBlCampusId();
	
	/**
	 * 学生科目状态
	 * @param currentDate
	 */
	public void flushStudentSubjectStatus(String currentDate);
	
	/**
	 * 更新下个月校区的科组
	 * @param blBrenchId
	 * @param blCampusId
	 * @param verison
	 * @param nextVersion
	 */
	public void updateToNextVersion(String blBrenchId, String blCampusId, int verison, int nextVersion);
	
	/**
	 * 初始化新建的校区科组
	 * @param blBrenchId
	 * @param blCampusId
	 * @param verison
	 * @param nextVersion
	 */
	public void createCampusSubejctGroup(String blBrenchId, String blCampusId, int verison, int nextVersion);
	
	/**
	 * 更新老师版本
	 */
	public void updateTeacherVersion();
	
	/**
	 * 每月初更新科组
	 * @param preVersion
	 * @param currentVersion
	 * @param nextVersion
	 */
	public void updateSubjectGroup(int preVersion, int currentVersion, int nextVersion);
	
	/**
     * 下载并导入银联支付数据检查30天没下载的重复下载
     * @param targeDate
     */
    public void downLoadAndImportPosDataReview(String targetDate);
	
	/**
	 * 下载并导入银联支付数据
	 * @param targeDate
	 */
	public boolean downLoadAndImportPosData(String targetDate);
	
	/**
     * 下载并导入快钱支付数据检查30天没下载的重复下载
     * @param targetDate
     */
    public void downLoadAndImportPosDataKuaiQianReview(String targetDate);
	
	/**
	 * 下载并导入快钱支付数据
	 * @param targetDate
	 */
	public boolean downLoadAndImportPosDataKuaiQian(String targetDate);
	
	/**
     * 更新成绩模板版本
     */
    public void updateAchievementTemplateVesion();
    
    /**
     * 月头1号跑校区业绩凭证
     * @author: duanmenrun
     * @Title: updateOdsCampusAchievement 
     * @Description: TODO 
     * @param targetDay
     * @param campusId
     * @throws SQLException
     */
    public void updateOdsCampusAchievement(String targetDay,String campusId) throws SQLException;

	/**
	 * 月头 2-8号刷新没有开始审批的数据
	 * @author: duanmenrun
	 * @Title: updateOdsCampusAchievementFlush 
	 * @Description: TODO 
	 * @param targetDay
	 * @throws SQLException
	 */
	public void updateOdsCampusAchievementFlush(String targetDay) throws SQLException;
	
}
