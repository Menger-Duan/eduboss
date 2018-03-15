package com.eduboss.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

public class SendSysMsgWashOtmCourseThread implements Runnable {
	static private SystemMessageManageService sysMsgManageService = (SystemMessageManageService) ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class); 
	static private SentRecordService sentRecordService = (SentRecordService) ApplicationContextUtil.getContext().getBean(SentRecordService.class);
	static private MobileUserService mobileUserService = (MobileUserService) ApplicationContextUtil.getContext().getBean(MobileUserService.class);
	static private MobilePushMsgService mobilePushMsgService = (MobilePushMsgService) ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);
	static private UserService userService = (UserService) ApplicationContextUtil.getContext().getBean(UserService.class);
	static private DataDictService dataDictService = (DataDictService) ApplicationContextUtil.getContext().getBean(DataDictService.class);
	
	private static final Log log = LogFactory.getLog(SendSysMsgThread.class);
	
	private String otmCourseId;
	private String currentUserId;
	private String transactionId;
	
	public SendSysMsgWashOtmCourseThread(){}
	public SendSysMsgWashOtmCourseThread(String otmCourseId, String currentUserId, String transactionId){
		this.otmCourseId=otmCourseId;
		this.currentUserId=currentUserId;
		this.transactionId=transactionId;
	}
	
	@Override
	public void run(){
		SystemMessageManage sysMsg = sysMsgManageService.findSystemMessageManageByMsgNo(MsgNo.M10);
		if (sysMsg != null) {
			String sql = sysMsg.getLogicSql();
			if (StringUtil.isNotBlank(sql)) {
				sql += " AND c.OTM_CLASS_COURSE_ID = '"+ otmCourseId + "' ";
				sql += " AND mwr.TRANSACTION_ID = '"+ transactionId + "' ";		
				sql += " GROUP BY c.OTM_CLASS_COURSE_ID  ";
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
						if (sendType.indexOf("SMS") > -1) {
							SentRecord record = null;
							if (StringUtil.isNotBlank(phoneNumBer)) {
								tempSmsMsg = tempSendMsg.replaceAll(" ", "\b");
								if(sendDetailedMsg == null || StringUtils.isBlank(sendDetailedMsg)){
									sendDetailedMsg=null;
								}else{
									sendDetailedMsg=sendDetailedMsg.replaceAll(" ", "\b");
								}
//									String result =MessageUtil.readContentFromGet(phoneNumBer, tempSmsMsg);
								String otmClassName = (String) map.get("otmClassName");
								otmClassName = otmClassName.length() > 15 ? otmClassName.substring(0, 12) + "..." : otmClassName;
								if (AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_48495001", phoneNumBer, 
	            							"{\"courseDate\":\""+  map.get("courseDate") + "\",\"courseTime\":\"" + map.get("courseTime") + "\","
	            								+ "\"gradeSubject\":\"" + map.get("gradeSubject") + "\","
	            								+ "\"otmClassName\":\"" + otmClassName + "\","
	    										+ "\"cause\":\"" + map.get("cause") + "\"}")) {
									 record = sentRecordService.saveSentRecord(sysMsg, "SMS", tempSendMsg,sendDetailedMsg, currentUserId, msgRecipient, phoneNumBer, MsgNo.M10);
									 record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
									 record.setSysMsgType(SysMsgType.OTM_CLASS);
									 record.setDetailId(otmCourseId);
									 sentRecordService.saveOrUpdateSentRecord(record);
								 } else {
									 throw new ApplicationException("发送失败");
								 }
							} else {
								log.error("系统信息，编号：" +MsgNo.M2.getValue() + "课程：" + otmCourseId + "的老师没有配置手机号码!");
							}
						} 
						if (sendType.indexOf("SYS_MSG") > -1) {
							SentRecord record = null;								
							record = sentRecordService.saveSentRecord(sysMsg, "SYS_MSG", tempSendMsg,sendDetailedMsg, currentUserId, msgRecipient, phoneNumBer,MsgNo.M10);
							record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
							record.setSysMsgType(SysMsgType.OTM_CLASS);
							record.setDetailId(otmCourseId);
							sentRecordService.saveOrUpdateSentRecord(record);
							
							
							MobileUser user = mobileUserService.findMobileUserByStaffId(userId);
							String msgTypeName = dataDictService.findDataDictById(sysMsg.getMsgType().getId()).getName();
							mobilePushMsgService.pushMsg(user, tempSendMsg, record.getId(), PushMsgType.SYSTEM_MESSAGE.getValue(), DateTools.getCurrentDateTime(), SysMsgType.OTM_CLASS, otmCourseId, msgTypeName);
						}
					}
				} else {
					log.error("系统信息，编号：" +MsgNo.M2.getValue() + "没有配置发送类型!");
				}
			} else {
				log.error("系统信息，编号：" +MsgNo.M2.getValue() + "没有配置sql!");
			}
		}
	}

}
