package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.SentRecord;

@Repository
public interface SentRecordDao extends GenericDAO<SentRecord, String> {

}
