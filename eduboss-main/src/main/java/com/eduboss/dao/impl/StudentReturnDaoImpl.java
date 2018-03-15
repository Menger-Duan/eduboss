package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentReturnDao;
import com.eduboss.domain.StudentReturnFee;

@Repository("StudentReturnDao")
public class StudentReturnDaoImpl extends GenericDaoImpl<StudentReturnFee, String> implements
StudentReturnDao {
	
	
}
