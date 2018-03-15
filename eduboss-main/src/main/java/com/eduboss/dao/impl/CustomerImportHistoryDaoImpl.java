package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerImportHistoryDao;
import com.eduboss.domain.CustomerImportHistory;

@Repository("CustomerImportHistoryDaoImpl")
public class CustomerImportHistoryDaoImpl extends GenericDaoImpl<CustomerImportHistory,String> implements CustomerImportHistoryDao {


}
