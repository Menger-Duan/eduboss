package com.eduboss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.MobilePushMsgUserRecordDao;
import com.eduboss.domain.MobilePushMsgUserRecord;




@Repository("MobilePushMsgUserRecordDao")
public class MobilePushMsgUserRecordDaoImpl extends GenericDaoImpl<MobilePushMsgUserRecord, String> implements MobilePushMsgUserRecordDao {

	private static final Logger log = LoggerFactory.getLogger(MobilePushMsgUserRecordDaoImpl.class);

	
}
