package com.eduboss.dao;

import com.eduboss.domain.FinanceCampus;

public interface FinanceCampusDao extends GenericDAO<FinanceCampus, String> {

	FinanceCampus findInfoByCampusAndDate(String campusId, String countDate);

	
}
