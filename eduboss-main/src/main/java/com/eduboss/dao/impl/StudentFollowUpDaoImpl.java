package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentFollowUpDao;
import com.eduboss.domain.StudentFollowUp;

@Repository(value = "StudentFollowUpDao")
public class StudentFollowUpDaoImpl extends GenericDaoImpl<StudentFollowUp, String> implements StudentFollowUpDao {
	
	

}
