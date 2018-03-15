package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.IncomeCountBrenchDao;
import com.eduboss.domain.IncomeCountBrench;
import com.google.common.collect.Maps;

@Repository("IncomeCountBrenchDao")
public class IncomeCountBrenchDaoImpl extends GenericDaoImpl<IncomeCountBrench, String> implements IncomeCountBrenchDao {
	private static final Logger log = LoggerFactory.getLogger(IncomeCountBrenchDaoImpl.class);

	@Override
	public IncomeCountBrench findInfoByBrenchAndDate(String brenchId, String countDate) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("brenchId", brenchId);
		params.put("countDate", countDate);
		String hql =" from IncomeCountBrench where brenchId= :brenchId and countDate= :countDate ";
		return this.findOneByHQL(hql,params);
	}

	
}
