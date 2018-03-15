package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentAchievementDao;
import com.eduboss.domain.StudentAchievement;

/**
 * 学生成绩DaoImpl
 * @author arvin
 *
 */
@Repository("StudentAchievementDao")
public class StudentAchievementDaoImpl extends GenericDaoImpl<StudentAchievement, Integer> implements StudentAchievementDao {

}
