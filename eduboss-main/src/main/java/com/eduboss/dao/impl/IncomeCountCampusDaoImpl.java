package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.IncomeCountCampusDao;
import com.eduboss.domain.IncomeCountCampus;
import com.google.common.collect.Maps;

@Repository("IncomeCountCampusDao")
public class IncomeCountCampusDaoImpl extends GenericDaoImpl<IncomeCountCampus, String> implements IncomeCountCampusDao {
	private static final Logger log = LoggerFactory.getLogger(IncomeCountCampusDaoImpl.class);

	@Override
	public IncomeCountCampus findInfoByCampusAndDate(String campusId, String countDate) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("campusId", campusId);
		params.put("countDate", countDate);
		String hql =" from IncomeCountCampus where campusId= :campusId and countDate= :countDate ";
		return this.findOneByHQL(hql,params);
	}

	
}
