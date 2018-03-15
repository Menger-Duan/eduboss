package com.eduboss.service.impl;

import com.eduboss.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MonitorSubject;
import com.eduboss.common.MonitorUiSubject;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.CountUserOperationDao;
import com.eduboss.domain.User;
import com.eduboss.domainVo.CountUserOperationVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.SysMonitorService;
import com.eduboss.service.UserService;


@Service
public class SysMonitorServiceImpl implements SysMonitorService {
	
	@Autowired
	private CountUserOperationDao countUserOperationDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OperationCountService operationCountService;
	
	
	/**
	 * 获取监测数据
	 * @throws Exception 
	 */
	@Override
	public DataPackage getSysMonitorUiData(CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception {
		if (null == countUserOperationVo.getMonitorUiSubject()) {
//			throw new ApplicationException("监控项目（入参）的数据为空！");
			return dp;
		}
		
		if (MonitorUiSubject.TEACHER_NOT_ATTENDANCE_DETAIL.equals(countUserOperationVo.getMonitorUiSubject())) {
			// 老师未考勤课时数（明细）
			countUserOperationVo.setCountDate(null);
			dp = countUserOperationDao.getRealTimeTeacherNotAttendanceCourseHourData(countUserOperationVo, dp);
			
		} else if (MonitorUiSubject.TEACHER_NOT_ATTENDANCE_SHAME_TOP.equals(countUserOperationVo.getMonitorUiSubject())) {
			// 老师未考勤荣辱榜
			countUserOperationVo.setBlCampusId(null);
			countUserOperationVo.setCountDate(null);
			dp = countUserOperationDao.getRealTimeTeacherNotAttendanceCourseHourData(countUserOperationVo, dp);
			
		} else if (MonitorUiSubject.STUDYMANAGER_UNBOUND_ATTNUM_STU_QUANTITY_DETAIL.equals(countUserOperationVo.getMonitorUiSubject())) {
			// 学管未绑定考勤编号的学生数量（明细）
			dp = countUserOperationDao.getProcStudyManagerWithCardAndFingerprintAndStudent(countUserOperationVo, dp);
			
		} else if (MonitorUiSubject.CAMPUS_MONITOR_DATA_SUMMARY.equals(countUserOperationVo.getMonitorUiSubject())) {
			// 校区数据监控（汇总）
			dp = countUserOperationDao.getCampusMonitorDataSummary(countUserOperationVo, dp);
			
		} else if (MonitorUiSubject.COURSE_MONITOR_SUMMARY.equals(countUserOperationVo.getMonitorUiSubject())) {
			// 课程监控（汇总）
		}
		
		return dp;
	}
	
	/**
	 * 获取监测数据
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getSysMonitorData(CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception {
		if (null == countUserOperationVo.getMonitorSubject()) {
			countUserOperationVo.setMonitorSubject(MonitorSubject.RECEPTIONIST_INPUT_RES);
		}
		
		if (MonitorSubject.STUDY_MANAGER_WITH_CARD_AND_FINGERPRINT_AND_STUDENT.equals(countUserOperationVo.getMonitorSubject())) {
			// 如果是查询学管所带学生、绑卡、绑指纹数量（即时数据），特殊处理
			dp = countUserOperationDao.getRealTimeStudyManagerWithCardAndFingerprintAndStudent(countUserOperationVo, dp);
			
		} else if (MonitorSubject.ATTENDANCE_STUDENT_QUANTITY.equals(countUserOperationVo.getMonitorSubject())) {
			// 查询考勤学生数量
			dp = countUserOperationDao.getAttendanceStudentQuantity(countUserOperationVo, dp);
			
		} else if (MonitorSubject.NEW_COURSE_STUDENT_QUANTITY.equals(countUserOperationVo.getMonitorSubject())) {
			// 查询考未上课学生数量
			dp = countUserOperationDao.getNewCourseStudentQuantity(countUserOperationVo, dp);
			
		} else if (MonitorSubject.TEACHER_NOT_ATTENDANCE_COURSE_HOUR.equals(countUserOperationVo.getMonitorSubject())) {
			// 查询老师未考勤课时
			countUserOperationVo.setCountDate(null);
			dp = countUserOperationDao.getCountTeacherNotAttendanceCourseHourList(countUserOperationVo, dp);
			
		} 
//		else if (MonitorSubject.COURSE_HOUR_CONSUME.equals(countUserOperationVo.getMonitorSubject())) {
//			// 课程消耗
//			dp = countUserOperationDao.getCourseHourConsume(countUserOperationVo, dp);
//			
//		} 
		else {
			// 其他类型一般处理，查统计表
			dp = countUserOperationDao.getCountUserOperation(countUserOperationVo, dp);
		}
		
		return dp;
	}
	
	/**
	 * 获取老师未考勤课时数
	 * @return
	 */
	@Override
	public Double getTeacherNotAttendanceCourseHour() {
		User currUser = userService.getCurrentLoginUser();
		if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
			CountUserOperationVo countUserOperationVo = new CountUserOperationVo();
			countUserOperationVo.setUserId(currUser.getUserId());
			Double notAttendanceCourseHour = countUserOperationDao.getTeacherNotAttendanceCourseHour(countUserOperationVo);
			if (null == notAttendanceCourseHour) {
				return 0.0;
			} else {
				return notAttendanceCourseHour;
			}
		} else {
			return 0.0;
		}
	}
	
	/**
	 * 刷新老师未考勤数据
	 * @throws Exception 
	 */
	@Override
	public void refreshTeacherNotAttendanceData(MonitorUiSubject monitorUiSubject) throws Exception {
		if (monitorUiSubject.equals(MonitorUiSubject.TEACHER_NOT_ATTENDANCE_DETAIL)) {// 老师未考勤课时
			operationCountService.updateTeacherNotAttendanceCourseHour();
		} else if (monitorUiSubject.equals(MonitorUiSubject.STUDYMANAGER_UNBOUND_ATTNUM_STU_QUANTITY_DETAIL)) {
			operationCountService.updateUnboundAttendanceNumber();
		}
//		else if (monitorSubject.equals(MonitorSubject.COURSE_HOUR_CONSUME_OF_TEACHER)) {// 老师课程消耗
//		} else if (monitorSubject.equals(MonitorSubject.COURSE_HOUR_CONSUME_OF_STUDY_MANAGER)) {// 学管课程消耗
//		} 
		else {
			throw new ApplicationException("未支持该条目刷新数据："+monitorUiSubject);
		}
	}
	

}
