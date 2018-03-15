package com.eduboss.task;

import java.util.List;
import java.util.Map;

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
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

/**
 * Created by Administrator on 2016/4/25.
 */
@Component
@Scope("prototype")
public class SendJoinOtmCourseMsgThread implements Runnable {


    static private SystemMessageManageService sysMsgManageService = (SystemMessageManageService) ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class);
    static private SentRecordService sentRecordService = (SentRecordService) ApplicationContextUtil.getContext().getBean(SentRecordService.class);
    static private MobileUserService mobileUserService = (MobileUserService) ApplicationContextUtil.getContext().getBean(MobileUserService.class);
    static private MobilePushMsgService mobilePushMsgService = (MobilePushMsgService) ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);
    static private UserService userService = (UserService) ApplicationContextUtil.getContext().getBean(UserService.class);
    static private DataDictService dataDictService = (DataDictService) ApplicationContextUtil.getContext().getBean(DataDictService.class);


    private String otmClassCourseId;
    private String currentUserId;
    private String studentId;

    private static final Log log = LogFactory.getLog(SendJoinOtmCourseMsgThread.class);

    public SendJoinOtmCourseMsgThread() {
    }

    public SendJoinOtmCourseMsgThread(String otmClassCourseId, String studentId, String currentUserId) {
        this.otmClassCourseId = otmClassCourseId;
        this.studentId = studentId;
        this.currentUserId = currentUserId;
    }

    @Override
    public void run() {
        log.debug("进入方法！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        SystemMessageManage sysMsg = sysMsgManageService.findSystemMessageManageByMsgNo(MsgNo.M12);
        if (sysMsg != null) {
            String sql = sysMsg.getLogicSql();
            if (StringUtil.isNotBlank(sql)){
            	sql = sql.replace("${STUDENT_ID}", studentId);
                sql += " AND occ.OTM_CLASS_COURSE_ID = '"+ otmClassCourseId + "' ";
//                sql += " AND ocsa.STUDENT_ID =  '" + studentId + "' ";
                List<Map<String, Object>> list = sysMsgManageService.findMapBySql(sql);
                String sendType = sysMsg.getSendType();
                if (StringUtil.isNotBlank(sendType)){
                    String msgContent = sysMsg.getMsgContent(); // 配置中的发送信息内容
                    String tempSendMsg = ""; // 发送的信息内容
                    String tempSmsMsg = ""; // sms的发送信息内容
                    String sendDetailedMsg = sysMsg.getDetailedInfromation(); //发送的详细信息内容

                    for (Map<String, Object> map : list) {
                        tempSendMsg = StringUtil.replacePlaceholder(msgContent, map, "${", "}");
                        String teacherPhone = (String)map.get("P0"); // 老师手机号码
                        String teacherId = (String)map.get("T0"); // 老师USER_ID
                        String studyManagerPhone = (String)map.get("P1"); // 学管手机号码
                        String studyManagerId = (String)map.get("T1"); // 学管USER_ID
                        String phones = "";
                        if (StringUtil.isNotBlank(teacherPhone)) {
                        	if (StringUtil.isNotBlank(studyManagerPhone)) {
                        		phones = teacherPhone + "," + studyManagerPhone;
                        	} else {
                        		phones = teacherPhone;
                        	}
                        } else {
                        	phones = studyManagerPhone;
                        }
                        
                        User teacherMsgRecipient = userService.findUserById(teacherId);
                        User studyManagerMsgRecipient = userService.findUserById(studyManagerId);
                        log.debug("sendType:"+sendType+"teacherPhone:"+teacherPhone+ ",studyManagerPhone:" + studyManagerPhone+"msgContent:"+msgContent);
                        if (sendType.indexOf("SMS") > -1) {
                            log.debug("进入SMS》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》");
                            if (StringUtil.isNotBlank(phones)) {
                                tempSmsMsg = tempSendMsg.replaceAll(" ", "\b");
                                if(sendDetailedMsg == null || StringUtils.isBlank(sendDetailedMsg)){
									sendDetailedMsg=null;
								}else{
									sendDetailedMsg=sendDetailedMsg.replaceAll(" ", "\b");
								}
//                                    String result = MessageUtil.readContentFromGet(phones, tempSmsMsg);
                                String otmClassName = (String) map.get("otmClassName");
                                otmClassName = otmClassName.length() > 15 ? otmClassName.substring(0, 12) + "..." : otmClassName;
                                if (AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_48725039", phones, 
            							"{\"otmClassName\":\""+  otmClassName + "\",\"courseDate\":\"" + map.get("courseDate") + "\","
            								+ "\"courseTime\":\"" + map.get("courseTime") + "\","
            								+ "\"studentName\":\"" + map.get("studentName") + "\"}")) {
                                	if (StringUtil.isNotBlank(teacherPhone)) {
                                		SentRecord record = sentRecordService.saveSentRecord(sysMsg, "SMS", tempSendMsg, sendDetailedMsg, currentUserId, teacherMsgRecipient, teacherPhone, MsgNo.M12);
                                		record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                                		record.setSysMsgType(SysMsgType.MINI_CLASS);
                                		record.setDetailId(otmClassCourseId);
                                		sentRecordService.saveOrUpdateSentRecord(record);
                                	}
                                	if (StringUtil.isNotBlank(studyManagerPhone)) {
                                		SentRecord record = sentRecordService.saveSentRecord(sysMsg, "SMS", tempSendMsg, sendDetailedMsg, currentUserId, studyManagerMsgRecipient, studyManagerPhone, MsgNo.M12);
                                		record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                                		record.setSysMsgType(SysMsgType.MINI_CLASS);
                                		record.setDetailId(otmClassCourseId);
                                		sentRecordService.saveOrUpdateSentRecord(record);
                                	}
                                } else {
                                	throw new ApplicationException("发送失败");
                                }
                            } else {
                                log.error("系统信息，编号：" +MsgNo.M12.getValue() + "课程：" + otmClassCourseId + "的老师和学管没有配置手机号码!");
                            }
                        }
                        if (sendType.indexOf("SYS_MSG") > -1) {   
                            MobileUser teacher = mobileUserService.findMobileUserByStaffId(teacherId);
                            MobileUser studyManager = mobileUserService.findMobileUserByStaffId(studyManagerId);
                            String msgTypeName = dataDictService.findDataDictById(sysMsg.getMsgType().getId()).getName();
                            if (StringUtil.isNotBlank(teacherPhone)) {
                            	SentRecord record = sentRecordService.saveSentRecord(sysMsg, "SYS_MSG", tempSendMsg, sendDetailedMsg, currentUserId, teacherMsgRecipient, teacherPhone, MsgNo.M12);
                            	record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                            	record.setSysMsgType(SysMsgType.MINI_CLASS);
                            	record.setDetailId(otmClassCourseId);
                            	sentRecordService.saveOrUpdateSentRecord(record);
                            	mobilePushMsgService.pushMsg(teacher, tempSendMsg, record.getId(), PushMsgType.SYSTEM_MESSAGE.getValue(), DateTools.getCurrentDateTime(), SysMsgType.OTM_CLASS, otmClassCourseId, msgTypeName);
                            }
                            if (StringUtil.isNotBlank(studyManagerPhone)) {
                            	SentRecord record = sentRecordService.saveSentRecord(sysMsg, "SYS_MSG", tempSendMsg, sendDetailedMsg, currentUserId, studyManagerMsgRecipient, studyManagerPhone, MsgNo.M12);
                            	record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                            	record.setSysMsgType(SysMsgType.MINI_CLASS);
                            	record.setDetailId(otmClassCourseId);
                            	sentRecordService.saveOrUpdateSentRecord(record);
                            	mobilePushMsgService.pushMsg(studyManager, tempSendMsg, record.getId(), PushMsgType.SYSTEM_MESSAGE.getValue(), DateTools.getCurrentDateTime(), SysMsgType.OTM_CLASS, otmClassCourseId, msgTypeName);
                            }
                        }
                    }
                }else {
                    log.error("系统信息，编号：" +MsgNo.M12.getValue() + "没有配置发送类型!");
                }
            } else {
                log.error("系统信息，编号：" +MsgNo.M12.getValue() + "没有配置sql!");
            }
        }

    }
}
