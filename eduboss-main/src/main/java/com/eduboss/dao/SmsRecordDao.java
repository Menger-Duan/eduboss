package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.SmsRecord;


@Repository
public interface SmsRecordDao extends GenericDAO<SmsRecord, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
}
