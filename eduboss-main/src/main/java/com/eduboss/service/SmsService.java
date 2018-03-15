package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.domain.SmsRecord;
import com.eduboss.dto.DataPackage;

@Service
public interface SmsService {
	
	/**
	 * 获取短信列表
	 * @param mobileNumber
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getStudentSmsRecords(String mobileNumber, DataPackage dataPackage);
	
	/**
	 * 发送短信
	 * @param message
	 */
	public void sendSms(SmsRecord message);

	/**
	 * 接收回复短信
	 * @param mobile
	 * @param msg
	 * @param receivetime
	 */
	public void receiveSms(String mobile, String msg, String receivetime);

	/**
	 * 通知学生的老师和学管师,学生的剩余资金不够消耗
	 * @param teacherId
	 * @param studyManagerId
	 * @param content
	 */
    void notifyTeacherAndStudyManager(String teacherId, String studyManagerId, String content);

}
