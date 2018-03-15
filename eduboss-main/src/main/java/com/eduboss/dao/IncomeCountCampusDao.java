package com.eduboss.dao;

import com.eduboss.domain.IncomeCountCampus;

public interface IncomeCountCampusDao extends GenericDAO<IncomeCountCampus, String> {

	IncomeCountCampus findInfoByCampusAndDate(String brenchId, String countDate);
	
}
