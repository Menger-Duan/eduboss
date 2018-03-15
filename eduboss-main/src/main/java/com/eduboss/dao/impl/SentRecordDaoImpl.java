package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.SentRecordDao;
import com.eduboss.domain.SentRecord;

@Repository("SentRecordDao")
public class SentRecordDaoImpl extends GenericDaoImpl<SentRecord, String> implements SentRecordDao {


}
