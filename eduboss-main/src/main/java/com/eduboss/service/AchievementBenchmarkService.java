package com.eduboss.service;

import java.util.List;
import java.util.Set;

import com.eduboss.domain.AchievementBenchmark;
import com.eduboss.domainVo.AchievementBenchmarkVo;
import com.eduboss.dto.AchievementComparisonUpdateVo;

/**
 * 成绩基准科目service
 * @author arvin
 *
 */
public interface AchievementBenchmarkService {

    /**
     * 新增或修改成绩基准科目
     * @param achievementBenchmark
     */
    void saveOrUpdateAchievementBenchmark(AchievementBenchmark achievementBenchmark);
    
    /**
     * 根据成绩对比编号查询学生基准科目列表
     * @param comparisonId
     * @return
     */
    List<AchievementBenchmark> listAchievementBenchmarkByComparisonId(Integer comparisonId);
    
    /**
     * 根据成绩对比编号查询学生基准科目vo列表
     * @param comparisonId
     * @return
     */
    List<AchievementBenchmarkVo> listAllAchievementBenchmarkByComparisonId(Integer comparisonId);
    
    /**
     * 根据成绩对比编号删除学生基准科目列表
     * @param comparisonId
     */
    void deleteAchievementBenchmarkByComparisonId(Integer comparisonId);
    
    /**
     * 进步判断更改
     * @param achievementComparisonUpdateVo
     */
    void updateStandardCategoryForAchievementBenchmark(AchievementComparisonUpdateVo achievementComparisonUpdateVo);
    
    /**
     * 根据学生成绩科目编号删除学生基准科目
     * @param benchmarkSubjectId
     */
    void deleteAchievementBenchmarkByBenchmarkSubjectIds(Set<Integer> benchmarkSubjectIds);
    
    /**
     * 根据学生学生成绩科目编号计算学生基准科目
     * @param benchmarkSubjectIds
     * @return
     */
    int countAchievementBenchmarkByBenchmarkSubjectIds(Set<Integer> benchmarkSubjectIds);
    
}
