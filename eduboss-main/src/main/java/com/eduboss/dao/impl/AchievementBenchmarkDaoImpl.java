package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementBenchmarkDao;
import com.eduboss.domain.AchievementBenchmark;

/**
 * 成绩基准科目DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementBenchmarkDao")
public class AchievementBenchmarkDaoImpl extends GenericDaoImpl<AchievementBenchmark, Integer> implements AchievementBenchmarkDao {

}
