package com.eduboss.dao;

import com.eduboss.domain.IncomeCountBrench;

public interface IncomeCountBrenchDao extends GenericDAO<IncomeCountBrench, String> {

	IncomeCountBrench findInfoByBrenchAndDate(String brenchId, String countDate);
	
}
