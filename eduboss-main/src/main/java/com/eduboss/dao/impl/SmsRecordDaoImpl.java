package com.eduboss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SmsRecordDao;
import com.eduboss.domain.SmsRecord;

@Repository("SmsRecordDaoImpl")
public class SmsRecordDaoImpl extends GenericDaoImpl<SmsRecord, String> implements SmsRecordDao {
	
	private static final Logger log = LoggerFactory.getLogger(SmsRecordDaoImpl.class);


}
