package com.eduboss.dao;

import com.eduboss.domain.FinanceBrench;

public interface FinanceBrenchDao extends GenericDAO<FinanceBrench, String> {

	FinanceBrench findInfoByBrenchAndDate(String brenchId, String countDate);
	
}
