package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementCategoryDao;
import com.eduboss.domain.AchievementCategory;

/**
 * 成绩类别DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementCategoryDao")
public class AchievementCategoryDaoImpl extends GenericDaoImpl<AchievementCategory, Integer> implements AchievementCategoryDao {

}
