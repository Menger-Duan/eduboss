package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.dto.DataPackage;

/**
 * @author lixuejun
 * @version v1.0
 * 2015-09-17
 *
 */
@Repository
public interface SchedulerExecuteLogDao extends GenericDAO<SchedulerExecuteLog, String> {

	public DataPackage getSchedulerExecuteLogList(SchedulerExecuteLog schedulerExecuteLog, DataPackage dp);
	
}
