package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.MessageDeliverRecord;
import com.eduboss.domain.MessageDeliverRecordId;
import com.eduboss.domain.MessageRecord;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MessageVo;

public interface MessageRecordService {


	public DataPackage getMessageRecordList(MessageRecord messageRecord, DataPackage dataPackage,MessageVo messageVo);

	public MessageRecord findMessageRecordById(String id);

	public void deleteMessageRecord(MessageRecord messageRecord);

	public void saveOrUpdateMessageRecord(MessageRecord messageRecord);

	public DataPackage getMessageDeliverRecordList(MessageDeliverRecord messageDeliverRecord, DataPackage dataPackage,MessageVo messageVo);

	public void deleteMessageDeliverRecord(MessageDeliverRecord messageDeliverRecord);

	public void saveOrUpdateMessageDeliverRecord(MessageDeliverRecord messageDeliverRecord);

	public MessageDeliverRecord findMessageDeliverRecordById(MessageDeliverRecordId id);
	
	public List<MessageDeliverRecord> getMessageDeliverrecordByUserId(String userId);

}
