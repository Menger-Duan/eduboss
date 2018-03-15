package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentDocumentDao;
import com.eduboss.domain.StudentDocument;

@Repository(value = "StudentDocumentDao")
public class StudentDocumentDaoImpl extends GenericDaoImpl<StudentDocument, String> implements StudentDocumentDao {
	
	

}
