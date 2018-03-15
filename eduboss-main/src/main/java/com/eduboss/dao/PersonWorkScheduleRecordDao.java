package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.PersonWorkScheduleRecord;

/**
 * 个人工作日程
 * @author lixuejun
 * 2015-11-13
 */
public interface PersonWorkScheduleRecordDao extends GenericDAO<PersonWorkScheduleRecord, String> {

	/**
	 * 获取指定时间内的个人工作日程
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	public List<PersonWorkScheduleRecord> getPersonWorkScheduleRecords(String startDate, String endDate, String userId);
	
	/**
	 * 获取登录人当天的工作日程个数
	 */
	public int getTodayWorkNumberByUserId(String userId,String workDate);

}
