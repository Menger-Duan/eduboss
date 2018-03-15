package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MoneyRollbackRecordsDao;
import com.eduboss.domain.MoneyRollbackRecords;

@Repository("MoneyRollbackRecordsDao")
public class MoneyRollbackRecordsDaoImpl extends GenericDaoImpl<MoneyRollbackRecords, String> implements MoneyRollbackRecordsDao {

	
}
