package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementCategoryScoreDao;
import com.eduboss.domain.AchievementCategoryScore;

/**
 * 成绩类别分数DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementCategoryScoreDao")
public class AchievementCategoryScoreDaoImpl extends GenericDaoImpl<AchievementCategoryScore, Integer> implements AchievementCategoryScoreDao {

}
