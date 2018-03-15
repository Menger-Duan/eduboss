package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseDateTemplateDao;
import com.eduboss.domain.CourseDateTemplate;

@Repository
public class CourseDateTemplateDaoImpl extends GenericDaoImpl<CourseDateTemplate, String> implements CourseDateTemplateDao {

	@Override
	public void deleteById(String id) {
		getHibernateTemplate().bulkUpdate("delete from CourseDateTemplate where templateId = ?",id);
	}
	
}
