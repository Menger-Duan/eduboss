package com.eduboss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.common.MonitorSubject;
import com.eduboss.common.MonitorUiSubject;
import com.eduboss.domainVo.CountUserOperationVo;
import com.eduboss.dto.DataPackage;

@Service
public interface SysMonitorService {

	/**
	 * 获取用户界面监控数据
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 * @throws Exception
	 */
	public DataPackage getSysMonitorUiData(
			CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception;
	
	/**
	 * 获取监测数据
	 * @param countUserOperationVo
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getSysMonitorData(
			CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception;

	/**
	 * 刷新老师未考勤数据
	 * @throws Exception 
	 */
	public void refreshTeacherNotAttendanceData(MonitorUiSubject monitorUiSubject) throws Exception;

	/**
	 * 获取老师未考勤课时数
	 * @return
	 */
	public Double getTeacherNotAttendanceCourseHour();
}
