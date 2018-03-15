package com.eduboss.dao;

import com.eduboss.domain.CountUserOperation;
import com.eduboss.domainVo.CountUserOperationVo;
import com.eduboss.dto.DataPackage;

public interface CountUserOperationDao extends GenericDAO<CountUserOperation, String> {

	/**
	 * 获取前台录入资源统计数据
	 * @param countUserOperationVo
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getCountUserOperation(
			CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception;

	/**
	 * 获取学管所带学生、绑卡、绑指纹数量
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	public DataPackage getRealTimeStudyManagerWithCardAndFingerprintAndStudent(
			CountUserOperationVo countUserOperationVo, DataPackage dp);

	/**
	 * 获取未上课学生数量
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	public DataPackage getNewCourseStudentQuantity(
			CountUserOperationVo countUserOperationVo, DataPackage dp);

	/**
	 * 获取考勤学生数量
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	public DataPackage getAttendanceStudentQuantity(
			CountUserOperationVo countUserOperationVo, DataPackage dp);

	/**
	 * 获取老师未考勤课时数
	 * @param countUserOperationVo
	 * @return
	 */
	public Double getTeacherNotAttendanceCourseHour(
			CountUserOperationVo countUserOperationVo);

	/**
	 * 获取老师未考勤课时数（总未考勤课时数，一对一未考勤课时数 + 小班未考勤课时数）
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 * @throws Exception
	 */
	public DataPackage getCountTeacherNotAttendanceCourseHourList(
			CountUserOperationVo countUserOperationVo, DataPackage dp)
			throws Exception;

	/**
	 * 获取老师课时消耗
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseHourConsume(CountUserOperationVo countUserOperationVo,
			DataPackage dp);

	/**
	 * 获取校区数据监控列表
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCampusMonitorDataSummary(
			CountUserOperationVo countUserOperationVo, DataPackage dp);

	/**
	 * 获取老师未考勤课时数（实时查询，实时数据）
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 * @throws Exception
	 */
	public DataPackage getRealTimeTeacherNotAttendanceCourseHourData(
			CountUserOperationVo countUserOperationVo, DataPackage dp)
			throws Exception;

	/**
	 * 获取学管所带学生、绑卡、绑指纹数量（取昨晚跑存储过程的数据）
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Deprecated
	public DataPackage getProcStudyManagerWithCardAndFingerprintAndStudent(
			CountUserOperationVo countUserOperationVo, DataPackage dp);

	/**
	 * 获取老师未考勤课时
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
//	public DataPackage getTeacherNotAttendanceCourseHour(
//			CountUserOperationVo countUserOperationVo, DataPackage dp);

}
