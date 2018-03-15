package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementComparisonDao;
import com.eduboss.domain.AchievementComparison;

/**
 * 成绩对比记录DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementComparisonDao")
public class AchievementComparisonDaoImpl extends GenericDaoImpl<AchievementComparison, Integer> implements AchievementComparisonDao {

}
