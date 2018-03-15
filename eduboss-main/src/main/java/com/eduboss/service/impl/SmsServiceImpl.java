package com.eduboss.service.impl;

import java.io.IOException;
import java.util.List;

import com.eduboss.utils.StringUtil;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MessageDeliverType;
import com.eduboss.common.MessageType;
import com.eduboss.dao.SmsRecordDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.domain.SmsRecord;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SmsRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MessageService;
import com.eduboss.service.SmsService;
import com.eduboss.service.UserService;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.sms.MessageUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("com.eduboss.service.SmsService")
public class SmsServiceImpl implements SmsService {
	
	@Autowired
	private SmsRecordDao smsRecordDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public DataPackage getStudentSmsRecords(String mobileNumber, DataPackage dataPackage) {
		dataPackage = smsRecordDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "sendTime", "asc"), Expression.like("mobileNumber", mobileNumber, MatchMode.ANYWHERE));
		if (dataPackage.getDatas().size() > 0) {
			dataPackage.setDatas(HibernateUtils.voListMapping((List<SmsRecord>) dataPackage.getDatas(), SmsRecordVo.class));
		}
		return dataPackage;
	}
	

	@Override
	public void sendSms(SmsRecord message) {
		message.setSendTime(DateTools.getCurrentDateTime());
		message.setSendType(SmsRecord.SEND);
		User sendUser = userService.getCurrentLoginUser();
		if (sendUser != null) {
			message.setSendUser(sendUser);
		}
		smsRecordDao.save(message);
		smsRecordDao.flush();
		
		String result;
		try {
			result = MessageUtil.readContentFromGet(message.getMobileNumber(), message.getContent());
		
		//发送出去
//		SendSMS ss = new SendSMS();
//		ss.setMessage(message.getContent());
//		ss.setMobiles(message.getMobileNumber());
//		ss.setSmstype(0);
//		ss.setTimerid("0");
//		ss.setTimertype(0);
//		Map<String, String> map = ss.sendSMS();
		
//		if ("0".equalsIgnoreCase(map.get("errorcode"))) {
		if (result!=null && Integer.parseInt(result)>0) {
			message.setSendStatus("SEND");
		} else {
//			throw new ApplicationException("短信发送错误：" + map.get("errordescription"));
			throw new ApplicationException("短信发送错误");
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 由外部回调，没有session
	 */
	@Override
	public void receiveSms(String mobile, String msg, String receivetime) {
		SmsRecord message = new SmsRecord();
		message.setContent(msg);
		message.setMobileNumber(mobile);
		message.setSendTime(SmsRecord.RECEIVE);
		message.setSendTime(receivetime);
		smsRecordDao.save(message);
		
		//通知该学生的学管    关闭dwr   2016-12-17
//		List<Student> students = studentDao.findByCriteria(Expression.or(Expression.eq("contact", mobile), Expression.eq("fatherPhone", mobile)));
//		if(students.size() > 0) {
//			for (Student stu : students) {
//				//发送
//				messageService.sendMessage(MessageType.SYSTEM_MSG, "收到学生"+stu.getName()+"发来的短信", msg, MessageDeliverType.SINGLE, stu.getStudyManeger().getUserId());
//			}
//		}
	}

	/**
	 * 通知学生的老师和学管师,学生的剩余资金不够消耗
	 * @param teacherId
	 * @param studyManagerId
	 * @param content
	 */
	@Override
	public void notifyTeacherAndStudyManager(String teacherId, String studyManagerId, String content) {
		User teacher = null;
		User studyManager = null;
		if (StringUtil.isNotBlank(teacherId)){
		     teacher = userService.getUserById(teacherId);
		}
		if (StringUtil.isNotBlank(studyManagerId)){
			studyManager = userService.getUserById(studyManagerId);
		}
		if (teacher!=null){
			if (StringUtil.isNotBlank(teacher.getContact())){
				content = getStudentNamesForSms(content);
				if (!AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_47905090", teacher.getContact(), 
						"{\"studentNames\":\"" + content + "\"}")) {
					throw new ApplicationException("发送短信给老师失败");
				}
//					String result = MessageUtil.readContentFromGet(teacher.getContact(), content);
			}else {
				throw new ApplicationException("要短信通知的该上课老师没有填写手机号");
			}
		}
		if (studyManager!=null){
			if (StringUtil.isNotBlank(studyManager.getContact())){
//					String result = MessageUtil.readContentFromGet(studyManager.getContact(), content);
				content = getStudentNamesForSms(content);
				if (!AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_47905090", studyManager.getContact(), 
						"{\"studentNames\":\"" + content + "\"}")) {
					throw new ApplicationException("发送短信给学管师失败");
				}
			}else {
				throw new ApplicationException("要短信通知的该学管师没有填写手机号");
			}
		}
	}

	private String getStudentNamesForSms(String content) {
		if (content.length() > 15) {
			content = content.substring(0, 16);
			if (!content.substring(content.length() - 1, content.length()).equals("，")) {
				content = content.substring(0, content.lastIndexOf("，"));
				if (content.length() > 12) {
					content = content.substring(0, content.lastIndexOf("，"));
				}
				content += "...";
			}
		}
		return content;
	}
}
