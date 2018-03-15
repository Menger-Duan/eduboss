package com.eduboss.dao;

import com.eduboss.domain.TwoTeacherClassStudent;

public interface TwoTeacherClassStudentDao extends GenericDAO<TwoTeacherClassStudent, String> {


    TwoTeacherClassStudent getTwoTeacherClassStudent(String studentId, int classTwoId);
}
