package com.eduboss.dao;

import com.eduboss.domain.TeacherSubject;

public interface TeacherSubjectDao extends GenericDAO<TeacherSubject, Integer> {

	TeacherSubject findOneTeacherSubject(TeacherSubject teacherSubject);
	
}
