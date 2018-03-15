package com.eduboss.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.eduboss.domain.PersonWorkScheduleRecord;
import com.eduboss.domainVo.CountVo;
import com.eduboss.domainVo.PersonWorkScheduleRecordVo;

/**
 * 个人工作日程
 * @author lixuejun
 * 2015-11-13
 */
public interface PersonWorkService {

	/**
	 * 获取指定时间内的个人工作日程
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	public List<PersonWorkScheduleRecord> getPersonWorkScheduleRecords(String startDate, String endDate) throws IOException;
	
	/**
	 * 保存或修改个人工作日程
	 * @param record
	 */
	public void saveEditPersonWorkScheduleRecord(PersonWorkScheduleRecord record) throws IOException, IllegalAccessException;
	
	/**
	 * 删除个人工作日程
	 * @param record
	 */
	public void deletePersonWorkScheduleRecord(PersonWorkScheduleRecord record) throws IOException;
	
	/**
	 * 根据id查询个人工作日程
	 * @param id
	 * @return
	 */
	public PersonWorkScheduleRecord findPersonWorkScheduleRecordById(String id) throws IOException;
	
	/**
	 * 查询需要发送短信到客户端的个人日程 先预留
	
	public void sendSMSToUser();  */
	
	/**
	 * 供手机端做日程标记
	 * @param personWorks
	 * @return
	 */
	public List<PersonWorkScheduleRecordVo> getPersonWorkScheduleRecordsAndDates(String startDate, String endDate);
	
	/**
	 * APP获取登录人当天的工作日程个数
	 */
	public CountVo getTodayWorkNumberByUserId(String userId,String workDate);
	
}
