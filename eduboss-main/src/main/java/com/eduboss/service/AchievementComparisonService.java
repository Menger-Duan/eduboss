package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.AchievementComparison;
import com.eduboss.domainVo.AchievementComparisonDetailVo;
import com.eduboss.domainVo.AchievementComparisonVo;
import com.eduboss.dto.AchievementComparisonRequestVo;
import com.eduboss.dto.AchievementComparisonSearchVo;

/**
 * 成绩对比记录service
 * @author arvin
 *
 */
public interface AchievementComparisonService {

    /**
     * 保存成绩对比记录
     * @param achievementComparisonRequestVo
     */
    Integer saveFullAchievementComparison(AchievementComparisonRequestVo achievementComparisonRequestVo);
    
    /**
     * 初始化成绩对比记录
     */
    void initAchievementComparisons();
    
    /**
     * 根据学生编号初始化成绩对比记录
     * @param studentId
     */
    void initAchievementComparisonByStudentId(String studentId);
    
    /**
     * 根据成绩对比记录编号查询成绩对比详情vo
     * @param comparisonId
     * @return
     */
    AchievementComparisonDetailVo findAchievementComparisonDetailById(Integer comparisonId);
    
    /**
     * 根据学生编号查询成绩对比记录列表
     * @param achievementComparisonSearchVo
     * @return
     */
    List<AchievementComparisonVo> listAchievementComparisonForSearch(AchievementComparisonSearchVo achievementComparisonSearchVo);
    
    /**
     * 根据id查找学生成绩对比记录
     * @param comparisonId
     * @return
     */
    AchievementComparison findAchievementComparisonById(Integer comparisonId);
    
    /**
     * 根据学生编号查询最近的成绩对比详情vo
     * @param studentId
     * @return
     */
    AchievementComparisonDetailVo findLastAchievementComparisonDetail(String studentId);
    
    /**
     * 根据对比学生成绩编号删除对比记录
     * @param achievementId
     */
    void deleteComparisonByAchievementId(Integer achievementId);
    
}
