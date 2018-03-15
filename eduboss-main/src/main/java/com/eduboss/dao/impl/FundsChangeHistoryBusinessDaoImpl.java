package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.FundsChangeHistoryBusinessDao;
import com.eduboss.domain.FundsChangeHistoryBusiness;

@Repository("FundsChangeHistoryBusinessDao")
public class FundsChangeHistoryBusinessDaoImpl extends GenericDaoImpl<FundsChangeHistoryBusiness, String> implements FundsChangeHistoryBusinessDao {

}
