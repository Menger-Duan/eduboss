package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerCallsLogDao;
import com.eduboss.domain.CustomerCallsLog;

@Repository
public class CustomerCallsLogDaoImpl extends GenericDaoImpl<CustomerCallsLog, String> implements CustomerCallsLogDao {
	
}
