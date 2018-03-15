package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.FinanceUserDao;
import com.eduboss.domain.FinanceUser;
import com.google.common.collect.Maps;

@Repository("FinanceUserDao")
public class FinanceUserDaoImpl extends GenericDaoImpl<FinanceUser, String> implements FinanceUserDao {
	private static final Logger log = LoggerFactory.getLogger(FinanceUserDaoImpl.class);

	@Override
	public FinanceUser findInfoByUserAndDate(String userId, String countDate,String campusId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("campusId", campusId);
		params.put("countDate", countDate);
		String hql =" from FinanceUser where userId= :userId and countDate= :countDate and campusId= :campusId ";
		return this.findOneByHQL(hql,params);
	}
}
