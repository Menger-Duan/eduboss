package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MessageDeliverRecordDao;
import com.eduboss.domain.MessageDeliverRecord;
import com.eduboss.domain.MessageDeliverRecordId;

@Repository
public class MessageDeliverRecordDaoImpl extends GenericDaoImpl<MessageDeliverRecord, MessageDeliverRecordId> implements MessageDeliverRecordDao{

}
