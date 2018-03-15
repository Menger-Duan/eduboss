package com.eduboss.service;

import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.dto.DataPackage;

/**
 * @author lixuejun
 * @version v1.0
 * 2015-09-17
 *
 */
public interface SchedulerExecuteLogService {
	
	public  void saveSchedulerExecuteLog(SchedulerExecuteLog log);
	
	public DataPackage getSchedulerExecuteLogList(SchedulerExecuteLog schedulerExecuteLog, DataPackage dp);

}
