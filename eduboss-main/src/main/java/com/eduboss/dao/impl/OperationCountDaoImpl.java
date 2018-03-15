package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.OperationCountDao;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.TeacherSubjectId;

@Repository
public class OperationCountDaoImpl extends GenericDaoImpl<TeacherSubject, TeacherSubjectId> implements OperationCountDao {
	
}
