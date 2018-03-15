package com.eduboss.service;

import com.eduboss.domain.AchievementBenchmarkLog;

/**
 * 成绩基准科目操作日志service
 * @author arvin
 *
 */
public interface AchievementBenchmarkLogService {

    /**
     * 保存或修改成绩基准科目操作日志
     * @param log
     */
    void saveOrUpdateAchievementBenchmarkLog(AchievementBenchmarkLog log);
    
    /**
     * 根据成绩基准科目编号计算日志数量
     * @param achievementBenchmarkId
     * @return
     */
    Integer countAchievementBenchmarkLog(Integer achievementBenchmarkId);
    
}
