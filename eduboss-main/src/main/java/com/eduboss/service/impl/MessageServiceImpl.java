package com.eduboss.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MessageDeliverType;
import com.eduboss.common.MessageType;
import com.eduboss.domain.MessageDeliverRecord;
import com.eduboss.domain.MessageDeliverRecordId;
import com.eduboss.domain.MessageRecord;
import com.eduboss.domain.User;
import com.eduboss.service.MessageRecordService;
import com.eduboss.service.MessageService;
import com.eduboss.service.UserService;
import com.eduboss.service.dwr.DwrMessageManager;
import com.eduboss.utils.DateTools;

@Service("com.eduboss.service.MessageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private DwrMessageManager dwrMessageManager;

	@Autowired
	private MessageRecordService messageRecordService;
	
	@Autowired
	private UserService userService;
	
//	@Override
//	public List<PushMessage> getMessage(PushMessage pushMessage) {
//		// TODO Auto-generated method stub
//		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(pushMessage);
//		return pushMessageDao.findAllByCriteria(criterionList) ;
//	}

	@Override
	public void readMessage(MessageDeliverRecordId messageDeliverId) {
		MessageDeliverRecord messageDeliverRecord = messageRecordService.findMessageDeliverRecordById(messageDeliverId);
		messageDeliverRecord.setReadTime(DateTools.getCurrentDateTime());
		messageDeliverRecord.setUser(userService.getCurrentLoginUser());
	}

	@Override
	public void sendMessage(MessageType messageType, String messageTitle, String mesasge,
			MessageDeliverType deliverType, String deliverTargets) {
		if(deliverTargets == null) {
			return;
		} else {
			//保存消息记录到数据库
			MessageRecord messageRecord = new MessageRecord();
			messageRecord.setMessageType(messageType);
			messageRecord.setDeliverType(deliverType);
			messageRecord.setTitle(messageTitle);
			messageRecord.setContent(mesasge);
			messageRecord.setDeliverTagetId(deliverTargets);
			messageRecordService.saveOrUpdateMessageRecord(messageRecord);
			
			//保存消息发送人记录并发送消息
			if (MessageDeliverType.SINGLE.equals(deliverType) || MessageDeliverType.LIST_USER.equals(deliverType)) {
				String[] targetArray = deliverTargets.split(",");
				for (String userId : targetArray) {
					MessageDeliverRecord messageDeliverRecord = new MessageDeliverRecord();
					messageDeliverRecord.setMessageRecord(messageRecord);
					messageDeliverRecord.setUser(new User(userId));
					messageRecordService.saveOrUpdateMessageDeliverRecord(messageDeliverRecord);
				}
				
				//发送消息给相关的其他人  关闭dwr  2016-12-17
//				dwrMessageManager.sendMessageToUserList(messageRecord,  Arrays.asList(targetArray));
			}
		}
	}

}
