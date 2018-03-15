package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserEventRecordDao;
import com.eduboss.domain.UserEventRecord;

@Repository("userEventRecordDao")
public class UserEventRecordDaoImpl extends GenericDaoImpl<UserEventRecord,Long> implements UserEventRecordDao{

}
