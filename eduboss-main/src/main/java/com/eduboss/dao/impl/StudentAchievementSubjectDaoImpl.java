package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentAchievementSubjectDao;
import com.eduboss.domain.StudentAchievementSubject;

/**
 * 学生成绩科目DaoImpl
 * @author arvin
 *
 */
@Repository("StudentAchievementSubjectDao")
public class StudentAchievementSubjectDaoImpl extends GenericDaoImpl<StudentAchievementSubject, Integer> implements StudentAchievementSubjectDao {

}
