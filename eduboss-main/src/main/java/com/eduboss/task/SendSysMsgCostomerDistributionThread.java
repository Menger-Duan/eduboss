package com.eduboss.task;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.eduboss.domain.MobileUser;
import com.eduboss.domain.SentRecord;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.SentRecordService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;

@Component  
@Scope("prototype")
public class SendSysMsgCostomerDistributionThread implements Runnable {
	
	private SentRecordService sentRecordService;
	private MobilePushMsgService mobilePushMsgService;
	private SentRecord sentRecord;
	private MobileUser mobileUser;
	
	public SendSysMsgCostomerDistributionThread(){}
	public SendSysMsgCostomerDistributionThread(SentRecord sentRecord, MobileUser mobileUser) {
		this.sentRecord = sentRecord;
		this.mobileUser = mobileUser;
		this.sentRecordService = ApplicationContextUtil.getContext().getBean(SentRecordService.class);
		this.mobilePushMsgService = ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);
	}
	
	@Override
	public void run() {
		sentRecord.setIsReading("1");
		sentRecord.setSendType("SYS_MSG");
		sentRecordService.saveOrUpdateSentRecord(sentRecord);
		String msgTypeName = sentRecord.getMsgType() != null ? sentRecord.getMsgType().getName() : "";
		mobilePushMsgService.pushMsg(mobileUser, sentRecord.getMsgContent(), sentRecord.getId(), 
				sentRecord.getPushMsgType().getValue(), DateTools.getCurrentDateTime(), sentRecord.getSysMsgType(), 
				sentRecord.getDetailId(), msgTypeName);
	}
}
