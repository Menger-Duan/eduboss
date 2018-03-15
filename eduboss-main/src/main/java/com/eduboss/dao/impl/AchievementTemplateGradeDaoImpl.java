package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementTemplateGradeDao;
import com.eduboss.domain.AchievementTemplateGrade;

/**
 * 成绩模板年级DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementTemplateGradeDao")
public class AchievementTemplateGradeDaoImpl extends GenericDaoImpl<AchievementTemplateGrade, Integer> implements AchievementTemplateGradeDao {

}
