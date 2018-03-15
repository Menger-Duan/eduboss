package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.PersonWorkScheduleRecordDao;
import com.eduboss.domain.PersonWorkScheduleRecord;

/**
 * 个人工作日程
 * @author lixuejun
 * 2015-11-13
 */
@Repository
public class PersonWorkScheduleRecordDaoImpl extends GenericDaoImpl<PersonWorkScheduleRecord, String> implements PersonWorkScheduleRecordDao {

	/**
	 * 获取指定时间内的个人工作日程
	 */
	@Override
	public List<PersonWorkScheduleRecord> getPersonWorkScheduleRecords(
			String startDate, String endDate, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from PersonWorkScheduleRecord ");
		hql.append(" where 1=1 ");
		hql.append(" and createUserId = :userId ");
		params.put("userId", userId);
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			hql.append(" and (startDate between :startDate and :endDate ");
			hql.append(" or endDate between :startDate2 and :endDate2 ");
			hql.append(" or :startDate3 between startDate and endDate ");
			hql.append(" or :endDate3 between startDate and endDate) ");
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			params.put("startDate2", startDate);
			params.put("endDate2", endDate);
			params.put("startDate3", startDate);
			params.put("endDate3", endDate);
		}
		if (startDate.equals(endDate)) {
			hql.append(" order by scheduleStartTime ");
		} else {
			hql.append(" order by startDate, scheduleStartTime ");
		}
		
		return super.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 获取登录人当天的工作日程个数
	 */
	public int getTodayWorkNumberByUserId(String userId,String workDate){
		Map<String, Object> params = new HashMap<String, Object>();
		int count=0;
		StringBuffer sql=new StringBuffer();
		sql.append(" select COUNT(1) from person_work_schedule_record where CREATE_USER_ID = :userId and :workDate >= START_DATE and :workDate2 <=END_DATE ");
		params.put("userId", userId);
		params.put("workDate", workDate);
		params.put("workDate2", workDate);
		count=super.findCountSql(sql.toString(), params);
		return count;
	}
	

}
