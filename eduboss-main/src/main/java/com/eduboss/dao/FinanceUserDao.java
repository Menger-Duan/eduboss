package com.eduboss.dao;

import com.eduboss.domain.FinanceUser;

public interface FinanceUserDao extends GenericDAO<FinanceUser, String> {

	FinanceUser findInfoByUserAndDate(String userId, String countDate,String campusId);

	
}
