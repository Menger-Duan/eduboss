package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.AchievementBenchmarkLogDao;
import com.eduboss.domain.AchievementBenchmarkLog;

/**
 * 成绩基准科目操作日志DaoImpl
 * @author arvin
 *
 */
@Repository("AchievementBenchmarkLogDao")
public class AchievementBenchmarkLogDaoImpl extends GenericDaoImpl<AchievementBenchmarkLog, Integer>
    implements AchievementBenchmarkLogDao {

}
