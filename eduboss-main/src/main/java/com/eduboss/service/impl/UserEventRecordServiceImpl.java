package com.eduboss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.UserEventRecordDao;
import com.eduboss.domain.UserEventRecord;
import com.eduboss.service.UserEventRecordService;

@Service("userEventRecordService")
public class UserEventRecordServiceImpl implements UserEventRecordService {

	@Autowired
	private UserEventRecordDao userEventRecordDao;
	
	@Override
	public Long saveUserEventRecord(UserEventRecord record) {
		userEventRecordDao.save(record);
		return record.getId();
	}

}
