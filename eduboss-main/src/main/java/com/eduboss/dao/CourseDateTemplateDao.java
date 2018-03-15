package com.eduboss.dao;

import com.eduboss.domain.CourseDateTemplate;

public interface CourseDateTemplateDao extends GenericDAO<CourseDateTemplate, String> {
	
	public void deleteById(String id);
	
}
