package com.eduboss.service;

import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public interface SchedulerCountService {


	/**
	 * 2015-09-11
	 * 更新市场统计报表
	 * @throws SQLException
	 */
	public void updateCustomerCusTypeTotal() throws SQLException;


	/**
	 * 2015-09-11
	 * 更新总览
	 * @throws SQLException
	 */
	public void updateAllTotal() throws SQLException;


	/**
	 * 更新一对一剩余资金报表数据
	 * @throws SQLException
	 */
	public void updateOdsRealStudentRemaining() throws SQLException;

	/**
	 * 更新一对一学生人数报表数据
	 * @throws SQLException
	 */
	public void updateOdsRealStudentCount() throws SQLException;


	/**
	 * 更新小班剩余资金报表数据
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassSurplusMoney()
			throws SQLException;

	/**
	 * 更新小班人数报表数据
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassStudentCount()
			throws SQLException;

	/**
	 * 更新小班课消报表数据
	 * @throws SQLException
	 */
	public void updateOdsDayMiniClassConsume() throws SQLException;

	/**
	 * 更新退费报表数据
	 */
//    public void updateOdsDayRefundAnalyze() throws SQLException;

	/**
	 * 更新收入报表数据
	 */
//    public void updateOdsDayIncomingAnalyze() throws SQLException;

	/**
	 * 小班产品人数统计
	 */
//    public void updateMiniClassPeopleAnalyze() throws SQLException;

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
	 * 更新一对多剩余资金报表数据
	 * @throws SQLException
	 */
	public void updateOdsOtmRealStudentRemaining() throws SQLException;

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
	 * 分配资金，分配的购买课时
	 * @throws SQLException
	 */
	public void updateMoneyArrangeRecord() throws SQLException;

	public void updateStudentAccMv() throws Exception;

	public void auditFundsChangeHistory() throws Exception;

	void updateStudentStatus() throws SQLException;

	/**
	 * 小班报读人数统计
	 */
	public void updateMiniClassStudentRealCount() throws SQLException;

	/**
	 * 学生剩余资金
	 * @throws SQLException
	 */
	public void updateStudentSurplusFunding() throws SQLException;

	/**
	 * 每5分钟更新营收统计表当天的记录
	 * @throws SQLException
	 */
	public void updateOdsDayIncomingQuantityAnalyzeVery5Second() throws SQLException;

	@Deprecated
	void sycOrganizationAndJob();
	
	public void excuteBonusQueueTask() throws Exception;

	 void excuteStuQueueTask() throws Exception;

	/**
	 * 每天回流周期内无跟进资源
	 */
	public void resourceCallbackEveryday() throws SQLException;

	/**
	 * 客户资源导入从临时表到客户表
	 */

	public void importToCustomer() throws SQLException;

	/**
	 * 清理过季小班产品
	 * @throws SQLException
	 */
	public void cleanSmallclassProduct() throws SQLException;

	/**
	 * 每3分钟执行给用户发送提醒个人日程  先预留
	 * @throws SQLException

	public void sendSMSToUser() throws SQLException;
	 */

	/**
	 * 短信发送批处理失败信息
	 */
	public void sendFailureLogBySms();

	/**
	 * 一对一学生状态统计
	 * @throws SQLException
	 */
	public void updateOdsRealStudentOOOCount() throws SQLException;
	public void updateOdsRealStudentOTMCount() throws SQLException;

	/**
	 * 一对一状态更新
	 * @throws SQLException
	 */
	public void updateStudentOOOStatus() throws SQLException;

	public void updateStudentOTMStatus() throws SQLException;

	/**
	 * 删除上传到阿里云无用的文件
	 */
	public void deleteUploadFileFromAliyunOSS();

	void autoCompleteEcsClass() throws SQLException;

	void updateOdsDayMonthlyBalance() throws SQLException;

	void updateMonthlyEvidence() throws SQLException;

	void updatePaymentReceiptUntilLastAudit() throws SQLException;

	/**
	 * 更新学生科目状态
	 * @throws SQLException
	 */
	void flushStudentSubjectStatus() throws SQLException;

	/**
	 * 更新老师版本
	 * @throws SQLException
	 */
	void updateTeacherVersion() throws SQLException;

	void updateSubjectGroup() throws SQLException;

	/**
	 * 下载并导入银联支付数据(不对接银联支付了)
	 * @throws SQLException
	 */
//	void downLoadAndImportPosData() throws SQLException;

	void downLoadAndImportPosDataKuaiQian() throws SQLException;
	
	/**
	 * 从无效客户手里释放客户资源 目前只是释放  无效的咨询师  外呼主管 外呼专员  网络主管 网络专员
	 */
	public void releaseCustomerByInvalidUser();
	/**
	 * 校区营运主任和咨询师换校区后释放客户到原来的校区资源池
	 */
	public void releaseCustomerByChangeCampus();
	
	public void updateCustomerActive() throws SQLException;

	
	/**
	 * 定时扫描同步数据到教学那边去
	 */
	public void updateAttentanceInfo();
	
	public void updateCustomerVisitEveryday() throws SQLException;
	
	public void updateTextBookBindingData();
	
	public void chargeNotice();

	/**
	 * 直播下单流水记录
	 * @author: duanmenrun
	 * @Title: saveLiveFinance 
	 * @Description: TODO
	 */
	public void saveLiveFinance();
	
	public void excuteFinanceQueueTask() throws Exception;

	public void excuteIncomeQueueTask() throws Exception;

	/**
	 * 通联支付回查队列
	 * @author: duanmenrun
	 * @Title: excuteTongLianPayStatusHandler 
	 * @Description: TODO 
	 * @throws Exception
	 */
	public void excuteTongLianPayStatusHandler() throws Exception;
}
