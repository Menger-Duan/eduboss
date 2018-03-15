package com.eduboss.task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.eduboss.common.MsgNo;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.SysMsgType;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.DataDictService;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.SentRecordService;
import com.eduboss.service.SystemMessageManageService;
import com.eduboss.service.UserService;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.sms.MessageUtil;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Component  
@Scope("prototype")
public class SendSysMsgWashCourseThread implements Runnable {
	
	private SystemMessageManageService sysMsgManageService ;
	private SentRecordService sentRecordService;
	 private MobileUserService mobileUserService;
	 private MobilePushMsgService mobilePushMsgService;
	 private UserService userService;
	 private DataDictService dataDictService;
	
	private static final Log log = LogFactory.getLog(SendSysMsgWashCourseThread.class);
	private String courseId;
	private String currentUserId;
	private String transactionId;
	public SendSysMsgWashCourseThread(){}
	public SendSysMsgWashCourseThread(String courseId, String currentUserId, String transactionId) {
		this.courseId = courseId;
		this.currentUserId = currentUserId;
		this.transactionId = transactionId;
		this.sysMsgManageService =  ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class); 
		this.sentRecordService = ApplicationContextUtil.getContext().getBean(SentRecordService.class);
		this.mobileUserService =  ApplicationContextUtil.getContext().getBean(MobileUserService.class);
		this.mobilePushMsgService = ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);
		this.userService = ApplicationContextUtil.getContext().getBean(UserService.class);
		this.dataDictService =  ApplicationContextUtil.getContext().getBean(DataDictService.class);
	}
	
	@Override
	public void run() {
		log.debug("进入方法！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
		SystemMessageManage sysMsg = sysMsgManageService.findSystemMessageManageByMsgNo(MsgNo.M8);
		if (sysMsg != null) {
			String sql = sysMsg.getLogicSql();
			if (StringUtil.isNotBlank(sql)) {
				sql += " AND c.COURSE_ID = '"+ courseId + "' ";
				sql += " AND mwr.TRANSACTION_ID = '"+ transactionId + "' ";
				List<Map<String, Object>> list = sysMsgManageService.findMapBySql(sql);
				String sendType = sysMsg.getSendType();
				if (StringUtil.isNotBlank(sendType)) {
					String msgContent = sysMsg.getMsgContent(); // 配置中的发送信息内容
					String tempSendMsg = ""; // 发送的信息内容
					String tempSmsMsg = ""; // sms的发送信息内容
					String sendDetailedMsg = sysMsg.getDetailedInfromation(); //发送的详细信息内容
					
					for (Map<String, Object> map : list) {
						tempSendMsg = StringUtil.replacePlaceholder(msgContent, map, "${", "}");
						String phoneNumBer = (String)map.get("P0"); // 老师手机号码
						String userId = (String)map.get("T0"); // 老师USER_ID
						User msgRecipient = userService.findUserById(userId);
						SentRecord record = null;
						log.debug("sendType:"+sendType+"phoneNumBer:"+phoneNumBer+"msgContent:"+msgContent);
						if (sendType.indexOf("SMS") > -1) {
							log.debug("进入SMS》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》");
							if (StringUtil.isNotBlank(phoneNumBer)) {
								tempSmsMsg = tempSendMsg.replaceAll(" ", "\b");
								if(sendDetailedMsg == null || StringUtils.isBlank(sendDetailedMsg)){
									sendDetailedMsg=null;
								}else{
									sendDetailedMsg=sendDetailedMsg.replaceAll(" ", "\b");
								}
//									String result =MessageUtil.readContentFromGet(phoneNumBer, tempSmsMsg);
								if (AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_47955291", phoneNumBer, 
            							"{\"studentName\":\""+  map.get("studentName") + "\",\"courseDate\":\"" + map.get("courseDate") + "\","
            								+ "\"courseTime\":\"" + map.get("courseTime") + "\","
            								+ "\"gradeSubject\":\"" + map.get("gradeSubject")+ "\","
    										+ "\"cause\":\"" + map.get("cause") + "\"}")) {
									record = sentRecordService.saveSentRecord(sysMsg, "SMS", tempSendMsg,sendDetailedMsg, currentUserId, msgRecipient, phoneNumBer,MsgNo.M8);
									record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
									record.setSysMsgType(SysMsgType.ONE_ON_ONE_COURSE);
									record.setDetailId(courseId);
									sentRecordService.saveOrUpdateSentRecord(record);
								} else {
									throw new ApplicationException("发送失败");
								}
							} else {
								log.error("系统信息，编号：" +MsgNo.M1.getValue() + "课程：" + courseId + "的老师没有配置手机号码!");
							}
						} 
						if (sendType.indexOf("SYS_MSG") > -1) {
							record = sentRecordService.saveSentRecord(sysMsg, "SYS_MSG", tempSendMsg,sendDetailedMsg, currentUserId, msgRecipient, phoneNumBer,MsgNo.M8);
							record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
							record.setSysMsgType(SysMsgType.ONE_ON_ONE_COURSE);
							record.setDetailId(courseId);
							sentRecordService.saveOrUpdateSentRecord(record);
							
							MobileUser user = mobileUserService.findMobileUserByStaffId(userId);
//							String id = UUID.randomUUID().toString();
							String msgTypeName = dataDictService.findDataDictById(sysMsg.getMsgType().getId()).getName();
							mobilePushMsgService.pushMsg(user, tempSendMsg, record.getId(), PushMsgType.SYSTEM_MESSAGE.getValue(), DateTools.getCurrentDateTime(), SysMsgType.ONE_ON_ONE_COURSE, courseId, msgTypeName);
						}
					}
				} else {
					log.error("系统信息，编号：" +MsgNo.M1.getValue() + "没有配置发送类型!");
				}
			} else {
				log.error("系统信息，编号：" +MsgNo.M1.getValue() + "没有配置sql!");
			}
		}
	}
}
