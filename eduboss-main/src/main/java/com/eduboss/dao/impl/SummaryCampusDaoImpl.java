package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SummaryCampusDao;
import com.eduboss.domain.SummaryCampus;
import com.eduboss.domainVo.SummaryCampusVo;

@Repository
public class SummaryCampusDaoImpl extends GenericDaoImpl<SummaryCampus, String>
		implements SummaryCampusDao {
	
	/**
	 * 取单个校区总结表
	 */
	public SummaryCampus getOneSummaryCampus(SummaryCampusVo summaryCampusVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from SummaryCampus where 1=1 ";
		if (summaryCampusVo.getSummaryCycleType() != null) {
			hql += " and summaryCycleType = :summaryCycleType ";
			params.put("summaryCycleType", summaryCampusVo.getSummaryCycleType());
		}
		if (summaryCampusVo.getSummaryClassType() != null) {
			hql += " and summaryClassType = :summaryClassType ";
			params.put("summaryClassType", summaryCampusVo.getSummaryClassType());
		}
		if (StringUtils.isNotBlank(summaryCampusVo.getSummaryCycleStartDate())) {
			hql += " and summaryCycleStartDate = :summaryCycleStartDate ";
			params.put("summaryCycleStartDate", summaryCampusVo.getSummaryCycleStartDate());
		}
		if (StringUtils.isNotBlank(summaryCampusVo.getSummaryCycleEndDate())) {
			hql += " and summaryCycleEndDate = :summaryCycleEndDate ";
			params.put("summaryCycleEndDate", summaryCampusVo.getSummaryCycleEndDate());
		}
		
		List<SummaryCampus> summaryCampusList = super.findAllByHQL(hql, params);
		if(summaryCampusList!=null && summaryCampusList.size()>0){
			return summaryCampusList.get(0);
		}
		return null;
	}

}