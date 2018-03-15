package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.FinanceBrenchDao;
import com.eduboss.domain.FinanceBrench;
import com.google.common.collect.Maps;

@Repository("FinanceBrenchDao")
public class FinanceBrenchDaoImpl extends GenericDaoImpl<FinanceBrench, String> implements FinanceBrenchDao {
	private static final Logger log = LoggerFactory.getLogger(FinanceBrenchDaoImpl.class);

	@Override
	public FinanceBrench findInfoByBrenchAndDate(String brenchId, String countDate) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("brenchId", brenchId);
		params.put("countDate", countDate);
		String hql =" from FinanceBrench where brenchId= :brenchId and countDate= :countDate ";
		return this.findOneByHQL(hql,params);
	}

	
}
