package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.LivePaymentRecordDao;
import com.eduboss.domain.LivePaymentRecord;

@Repository("livePaymentRecordDao")
public class LivePaymentRecordDaoImpl extends GenericDaoImpl<LivePaymentRecord,String> implements LivePaymentRecordDao {

}
