package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MessageRecordDao;
import com.eduboss.domain.MessageRecord;

@Repository
public class MessageRecordDaoImpl extends GenericDaoImpl<MessageRecord, String> implements MessageRecordDao {
	
}
