package com.eduboss.dao.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.TransactionRecordDao;
import com.eduboss.domain.TransactionRecord;
import com.eduboss.exception.ApplicationException;

@Repository("TransactionRecordDao")
public class TransactionRecordDaoImpl extends GenericDaoImpl<TransactionRecord, String> implements TransactionRecordDao {

	@Override
	public void saveTransactionRecord(String transactionUuid) {
		TransactionRecord record = new TransactionRecord(transactionUuid);
		try {
			this.getHibernateTemplate().save(record);
			this.commit();
		} catch (ConstraintViolationException e) {
			throw new ApplicationException("请不要重复提交请求...");
		}
	}
	
}
