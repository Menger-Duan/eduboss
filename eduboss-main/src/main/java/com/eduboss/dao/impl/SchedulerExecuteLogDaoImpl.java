package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SchedulerExecuteLogDao;
import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.HibernateUtils;


/**
 * @author lixuejun
 * @version v1.0
 * 2015-09-17
 *
 */

@Repository("SchedulerExecuteLogDao")
public class SchedulerExecuteLogDaoImpl   extends GenericDaoImpl<SchedulerExecuteLog, String> implements SchedulerExecuteLogDao {

	@Override
	public DataPackage getSchedulerExecuteLogList(
			SchedulerExecuteLog schedulerExecuteLog, DataPackage dp) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(schedulerExecuteLog.getStartTime())) {
			String startTime = schedulerExecuteLog.getStartTime() + " 00:00:00";
			criterionList.add(Restrictions.ge("startTime", startTime));
		}
		
		if (StringUtils.isNotBlank(schedulerExecuteLog.getEndTime())) {
			String endTime = schedulerExecuteLog.getEndTime() + " 23:59:59";
			criterionList.add(Restrictions.le("startTime", endTime));
		}
		
		if (null != schedulerExecuteLog.getStatus() && 
				StringUtils.isNotBlank(schedulerExecuteLog.getStatus().getValue())) {
			criterionList.add(Restrictions.eq("status", schedulerExecuteLog.getStatus()));
		}
		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "startTime", "desc"),
				criterionList);
	}

}
