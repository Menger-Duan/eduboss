package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementTemplateSubjectDao;
import com.eduboss.domain.AchievementTemplateSubject;

/**
 * 成绩模板科目DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementTemplateSubjectDao")
public class AchievementTemplateSubjectDaoImpl extends 
    GenericDaoImpl<AchievementTemplateSubject, Integer> implements AchievementTemplateSubjectDao {

}
