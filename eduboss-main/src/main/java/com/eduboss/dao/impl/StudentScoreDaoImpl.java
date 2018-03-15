package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentScoreDao;
import com.eduboss.domain.StudentScore;

@Repository
public class StudentScoreDaoImpl extends GenericDaoImpl<StudentScore, String> implements StudentScoreDao {
	
}