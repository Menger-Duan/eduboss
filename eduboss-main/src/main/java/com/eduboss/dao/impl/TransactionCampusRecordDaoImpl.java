package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.TransactionCampusRecordDao;
import com.eduboss.domain.TransactionCampusRecord;

@Repository("TransactionCampusRecordDao")
public class TransactionCampusRecordDaoImpl extends GenericDaoImpl<TransactionCampusRecord,String> implements TransactionCampusRecordDao{

}
