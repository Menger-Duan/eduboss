package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementTemplateDao;
import com.eduboss.domain.AchievementTemplate;

/**
 * 成绩模板DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementTemplateDao")
public class AchievementTemplateDaoImpl extends GenericDaoImpl<AchievementTemplate, Integer> implements AchievementTemplateDao {

}
