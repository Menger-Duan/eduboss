package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.FinanceCampusDao;
import com.eduboss.domain.FinanceBrench;
import com.eduboss.domain.FinanceCampus;
import com.google.common.collect.Maps;

@Repository("FinanceCampusDao")
public class FinanceCampusDaoImpl extends GenericDaoImpl<FinanceCampus, String> implements FinanceCampusDao {
	private static final Logger log = LoggerFactory.getLogger(FinanceCampusDaoImpl.class);


	@Override
	public FinanceCampus findInfoByCampusAndDate(String campusId, String countDate) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("campusId", campusId);
		params.put("countDate", countDate);
		String hql =" from FinanceCampus where campusId= :campusId and countDate= :countDate ";
		return this.findOneByHQL(hql,params);
	}

}
