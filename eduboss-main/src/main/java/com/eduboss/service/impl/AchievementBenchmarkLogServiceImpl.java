package com.eduboss.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.AchievementBenchmarkLogDao;
import com.eduboss.domain.AchievementBenchmarkLog;
import com.eduboss.service.AchievementBenchmarkLogService;
import com.google.common.collect.Maps;

/**
 * 成绩类别操作日志serviceImpl
 * @author arvin
 *
 */
@Service("AchievementBenchmarkLogService")
public class AchievementBenchmarkLogServiceImpl implements AchievementBenchmarkLogService {

    @Autowired
    private AchievementBenchmarkLogDao achievementBenchmarkLogDao;
    
    /**
     * 保存或修改成绩基准科目操作日志
     */
    @Override
    public void saveOrUpdateAchievementBenchmarkLog(AchievementBenchmarkLog log) {
        achievementBenchmarkLogDao.save(log);
    }

    /**
     * 根据成绩基准科目编号计算日志数量
     */
    @Override
    public Integer countAchievementBenchmarkLog(Integer achievementBenchmarkId) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " select count(*) from AchievementBenchmarkLog where achievementBenchmarkId = :achievementBenchmarkId ";
        params.put("achievementBenchmarkId", achievementBenchmarkId);
        return achievementBenchmarkLogDao.findCountHql(hql, params);
    }

}
