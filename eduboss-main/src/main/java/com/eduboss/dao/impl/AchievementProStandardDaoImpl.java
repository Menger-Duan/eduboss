package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementProStandardDao;
import com.eduboss.domain.AchievementProStandard;

/**
 * 成绩进步标准DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementProStandardDao")
public class AchievementProStandardDaoImpl extends GenericDaoImpl<AchievementProStandard, String> implements AchievementProStandardDao {

}
