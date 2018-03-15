package com.eduboss.task;

import com.eduboss.common.MsgNo;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.SysMsgType;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.*;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30.
 */
@Component
@Scope("prototype")
public class SendSysMsgWashTwoTeacherCourseThread implements Runnable {

    static private SystemMessageManageService sysMsgManageService = (SystemMessageManageService) ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class);
    static private SentRecordService sentRecordService = (SentRecordService) ApplicationContextUtil.getContext().getBean(SentRecordService.class);
    static private MobileUserService mobileUserService = (MobileUserService) ApplicationContextUtil.getContext().getBean(MobileUserService.class);
    static private DataDictService dataDictService = (DataDictService) ApplicationContextUtil.getContext().getBean(DataDictService.class);
    static private UserService userService = (UserService) ApplicationContextUtil.getContext().getBean(UserService.class);
    static private MobilePushMsgService mobilePushMsgService = (MobilePushMsgService) ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);

    private static final Log log = LogFactory.getLog(SendSysMsgWashTwoTeacherCourseThread.class);

    private String currentUserId;
    private int twoTeacherClassStudentAttendentId;
    private String transactionId;
    private int twoTeacherCourseId;
    private int twoTeacherClassTwoId;

    public SendSysMsgWashTwoTeacherCourseThread(String currentUserId, int twoTeacherClassStudentAttendentId, String transactionId, int twoTeacherCourseId, int twoTeacherClassTwoId) {
        this.currentUserId = currentUserId;
        this.twoTeacherClassStudentAttendentId = twoTeacherClassStudentAttendentId;
        this.transactionId = transactionId;
        this.twoTeacherCourseId = twoTeacherCourseId;
        this.twoTeacherClassTwoId = twoTeacherClassTwoId;
    }

    @Override
    public void run() {
        /**
         * 学生${studentname}在课程日期${datetime}所上${classtwoname}课程发生扣费冲销，冲销原因是${reason}，请查实修改后重新提交课程结算流程。
         */
        SystemMessageManage sysMsg = sysMsgManageService.findSystemMessageManageByMsgNo(MsgNo.M14);
        if (sysMsg != null){

            String teacherSql = "SELECT u.USER_ID T0, u.CONTACT P0, ttct.NAME classTwoName  FROM two_teacher_class_two ttct , `user` u WHERE ttct.TEACHER_ID=u.USER_ID AND  CLASS_TWO_ID="+twoTeacherClassTwoId;
            List<Map<String, Object>> list = sysMsgManageService.findMapBySql(teacherSql);
            String userId =null;//老师id
            String phoneNumBer = null;//老师手机
            String classTwoName = null;//辅班名字
            for (Map<String, Object> map : list){
                userId = (String)map.get("T0"); // 老师USER_ID
                phoneNumBer = (String)map.get("P0"); // 老师手机号码
                classTwoName = (String)map.get("classTwoName");
            }

            String studentSql = "SELECT s.name studentName, ttcsa.course_date_time dateTime  FROM two_teacher_class_student_attendent ttcsa,student s  WHERE ttcsa.student_id=s.id AND  ttcsa.ID="+twoTeacherClassStudentAttendentId;
            List<Map<String, Object>> student = sysMsgManageService.findMapBySql(studentSql);
            String studentName = null;
            String dateTime =null;
            String time = null;
            for (Map<String, Object> map :student){
                studentName = (String)map.get("studentName");
                dateTime =(String)map.get("dateTime");
                String[] array = dateTime.split(" ");
                dateTime = array[0];
                time = array[1];
            }
            String washRecordsSql = " SELECT DETAIL_REASON reason FROM money_wash_records WHERE TRANSACTION_ID='"+transactionId+"'";
            List<Map<String, Object>> reason = sysMsgManageService.findMapBySql(washRecordsSql);
            String reson = "";
            for (Map<String, Object> map :reason){
                reson = (String)map.get("reason");
            }
            String sendType = sysMsg.getSendType();
            if (StringUtil.isNotBlank(sendType)){
                //学生${studentname}在课程日期${datetime}课程时间${time}所上${classtwoname}课程发生扣费冲销，冲销原因是${reason}，请查实修改后重新提交课程结算流程。
                String tempSendMsg = "学生"+studentName+"在课程日期"+dateTime+"课程时间"+time+"所上"+classTwoName+"课程发生扣费冲销，冲销原因是"+reson+"，请查实修改后重新提交课程结算流程。";
                String sendDetailedMsg = tempSendMsg;

                User msgRecipient = userService.findUserById(userId);
                log.debug("sendType:"+sendType+"phoneNumBer:"+phoneNumBer+"msgContent:"+sendDetailedMsg);
                if (sendType.indexOf("SMS") > -1){//sendType.indexOf("SMS") > -1
                    SentRecord record = null;
                    //短信
                    log.debug("进入SMS》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》");
                    if (StringUtil.isNotBlank(phoneNumBer)){
//                        tempSmsMsg = tempSendMsg.replaceAll(" ", "\b");
                        if(sendDetailedMsg == null || StringUtils.isBlank(sendDetailedMsg)){
                            sendDetailedMsg=null;
                        }else{
                            sendDetailedMsg=sendDetailedMsg.replaceAll(" ", "\b");
                        }
                        boolean flag = AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_75825034", phoneNumBer,
                                "{\"studentname\":\""+  studentName + "\",\"classtwoname\":\"" + classTwoName + "\","
                                        + "\"datetime\":\"" + dateTime + "\",\"time\":\"" + time + "\","
                                        + "\"reason\":\"" + reson+ "\"}");
//                                        + "\"cause\":\"" + map.get("cause") + "\"}");
                        if (flag){
                            record = sentRecordService.saveSentRecord(sysMsg, "SMS", tempSendMsg, sendDetailedMsg, currentUserId, msgRecipient
                                    , phoneNumBer, MsgNo.M14);
                            record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                            record.setSysMsgType(SysMsgType.TWO_TEACHER);
                            record.setDetailId("");
                            sentRecordService.saveOrUpdateSentRecord(record);
                        } else {
                            throw new ApplicationException("发送失败");
                        }
                    } else {
                        log.error("系统信息，编号：" +MsgNo.M14.getValue() + "双师课程考勤id为：" + twoTeacherClassStudentAttendentId + "的辅导老师没有配置手机号码!");
                    }
                }
                if (sendType.indexOf("SYS_MSG") > -1){
                    SentRecord record = null;
                    record = sentRecordService.saveSentRecord(sysMsg, "SYS_MSG", tempSendMsg, sendDetailedMsg, currentUserId, msgRecipient, phoneNumBer, MsgNo.M14);
                    record.setPushMsgType(PushMsgType.SYSTEM_MESSAGE);
                    record.setSysMsgType(SysMsgType.TWO_TEACHER);
                    String courseId =  StringUtil.toString(twoTeacherCourseId);
                    String classTwoId = StringUtil.toString(twoTeacherClassTwoId);
                    String detailId = courseId+"_"+classTwoId;
                    record.setDetailId(detailId);
                    sentRecordService.saveOrUpdateSentRecord(record);
                    MobileUser user = mobileUserService.findMobileUserByStaffId(userId);
                    String msgTypeName = dataDictService.findDataDictById(sysMsg.getMsgType().getId()).getName();
                    mobilePushMsgService.pushMsg(user, tempSendMsg, record.getId(), PushMsgType.SYSTEM_MESSAGE.getValue()
                            , DateTools.getCurrentDateTime(), SysMsgType.TWO_TEACHER, detailId, msgTypeName);
                }


            }else {
                log.error("系统信息，编号："+MsgNo.M14.getValue()+"没有配置发送类型!");
            }
        }
    }
}
