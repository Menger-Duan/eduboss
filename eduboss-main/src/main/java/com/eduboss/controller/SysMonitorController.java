package com.eduboss.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.MonitorUiSubject;
import com.eduboss.domainVo.CountUserOperationVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.SysMonitorService;

@Controller
@RequestMapping(value="/SysMonitorController")
public class SysMonitorController {
	
	@Autowired
	SysMonitorService sysMonitorService;

	@RequestMapping(value = "/getSysmonitorRpData")
	@ResponseBody
	public DataPackageForJqGrid getSysmonitorRpData(@ModelAttribute GridRequest gridRequest, CountUserOperationVo countUserOperationVo) throws Exception{
		DataPackage dp = new DataPackage(gridRequest);
		dp = sysMonitorService.getSysMonitorUiData(countUserOperationVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 刷新老师未考勤数据
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/refreshTeacherNotAttendanceData")
	@ResponseBody
	public Response refreshTeacherNotAttendanceData(@RequestParam MonitorUiSubject monitorUiSubject) throws Exception{
		sysMonitorService.refreshTeacherNotAttendanceData(monitorUiSubject);
		return new Response();
	}
	
	/**
	 * 获取当前登录老师未考勤的课时
	 * @return
	 */
	@RequestMapping(value = "/getCurrTeacherNotAttendanceHour")
	@ResponseBody
	public String getCurrTeacherNotAttendanceHour(){
		Double notAttendanceCourseHour = sysMonitorService.getTeacherNotAttendanceCourseHour();
		return notAttendanceCourseHour.toString();
	}
}
