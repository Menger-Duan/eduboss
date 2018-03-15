package com.eduboss.task;

import com.eduboss.common.MsgNo;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemMessageManageVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.SentRecordService;
import com.eduboss.service.SystemMessageManageService;
import com.eduboss.service.UserService;
import com.eduboss.sms.MessageUtil;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/8.
 */
@Component
@Scope("prototype")
public class SendUserPassWordMsgThread implements Runnable {

    private SystemMessageManageService sysMsgManageService ;
    private SentRecordService sentRecordService;
    private UserService userService;


    private static final Log log = LogFactory.getLog(SendRefundSysMsgThread.class);

    private User user;

    private String currentUserId;

    private MsgNo msgNo;

    private String loginAddress;


    public SendUserPassWordMsgThread() {
    }


    public SendUserPassWordMsgThread(User user, String currentUserId, MsgNo msgNo) {
        this.user = user;
        this.currentUserId = currentUserId;
        this.msgNo = msgNo;
        this.sysMsgManageService =  ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class);
        this.sentRecordService = ApplicationContextUtil.getContext().getBean(SentRecordService.class);
        this.userService = ApplicationContextUtil.getContext().getBean(UserService.class);
    }

    public SendUserPassWordMsgThread(User user, String currentUserId, MsgNo msgNo, String loginAddress) {
        this.user = user;
        this.currentUserId = currentUserId;
        this.msgNo = msgNo;
        this.loginAddress = loginAddress;
        this.sysMsgManageService =  ApplicationContextUtil.getContext().getBean(SystemMessageManageService.class);
        this.sentRecordService = ApplicationContextUtil.getContext().getBean(SentRecordService.class);
        this.userService = ApplicationContextUtil.getContext().getBean(UserService.class);
    }

    @Override
    public void run() {
        SystemMessageManage systemMessageManage = sysMsgManageService.findSystemMessageManageByMsgNo(msgNo);
//        SystemMessageManage systemMessageManage =sysMsgManageService.findMsgById(msgId);
        if (systemMessageManage!=null){
            String sendType = systemMessageManage.getSendType();
            if (StringUtil.isNotBlank(sendType)){
                String msgContent = systemMessageManage.getMsgContent();//配置中的发送信息内容
                String tempSendMsg = ""; // 发送的信息内容
                String tempSmsMsg = ""; // sms的发送信息内容
                String sendDetailedMsg = systemMessageManage.getDetailedInfromation(); //发送的详细信息内容
                Map<String, Object> map = new HashMap();
                map.put("S1", user.getAccount());
                map.put("S2", user.getPassword());
                if (msgContent.contains("S3")){
                    map.put("S3", user.getLoginAddress());
                }
                tempSendMsg = StringUtil.replacePlaceholder(msgContent, map, "${", "}");
                String phoneNumBer = user.getContact();
//                String userId = user.getUserId();
                SentRecord record = null;
                if (sendType.indexOf("SMS") > -1){
                    if (StringUtil.isNotBlank(phoneNumBer)){
                        try {
                            tempSmsMsg = tempSendMsg.replaceAll(" ", "\b");
                            String result = MessageUtil.readContentFromGet(phoneNumBer, tempSmsMsg);
                            if (Integer.parseInt(result) > 0){
                                record = sentRecordService.saveSentRecord(systemMessageManage, "SMS", tempSendMsg,sendDetailedMsg, currentUserId, user, phoneNumBer,systemMessageManage.getMsgNo());
                                sentRecordService.saveOrUpdateSentRecord(record);
                            }else {
                                throw new ApplicationException("短信发送失败:" + result);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        log.error("系统信息，编号：" +systemMessageManage.getMsgNo().getValue() + "用户：" + user.getName() + "没有配置手机号码!");
                    }
                }
            }

        }
    }
}
