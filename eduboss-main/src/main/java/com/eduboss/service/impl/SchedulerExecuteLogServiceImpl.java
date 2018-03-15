package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.SchedulerExecuteLogDao;
import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.domainVo.SchedulerExecuteLogVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.SchedulerExecuteLogService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;

/**
 * @author lixuejun
 * @version v1.0
 * 2015-09-17
 *
 */

@Service("com.eduboss.service.SchedulerExecuteLogService")
public class SchedulerExecuteLogServiceImpl implements
		SchedulerExecuteLogService {
	
	@Autowired
	private SchedulerExecuteLogDao schedulerExecuteLogDao;

	@Override
	public void saveSchedulerExecuteLog(SchedulerExecuteLog log) {
		schedulerExecuteLogDao.save(log);
	}

	@Override
	public DataPackage getSchedulerExecuteLogList(
			SchedulerExecuteLog schedulerExecuteLog, DataPackage dp) {
		
		dp = schedulerExecuteLogDao.getSchedulerExecuteLogList(schedulerExecuteLog, dp);
		List<SchedulerExecuteLog> schedulerExecuteLogList = (List<SchedulerExecuteLog>) dp.getDatas();
		List<SchedulerExecuteLogVo> voList = HibernateUtils.voListMapping(schedulerExecuteLogList, SchedulerExecuteLogVo.class);
		for (SchedulerExecuteLogVo vo : voList) {
			String chineseSchedulerName = PropertiesUtils.getStringValue(vo.getSchedulerName());
			if (null != chineseSchedulerName) {
				vo.setChineseSchedulerName(chineseSchedulerName);
			} else {
				vo.setChineseSchedulerName("-");
			}
		}
		dp.setDatas(voList);
		return dp;
			
//		return schedulerExecuteLogDao.getSchedulerExecuteLogList(schedulerExecuteLog, dp);
	}

}
