package com.eduboss.service;

import com.eduboss.domain.AchievementCategoryScore;

public interface AchievementCategoryScoreService {
    
    /**
     * 新增或修改成绩类别分数
     * @param score
     */
    void saveOrUpdateAchievementCategoryScore(AchievementCategoryScore score);
    
    /**
     * 删除所有成绩类别分数
     * @param categoryId
     */
    void deleteAllAchievementCategoryScore();
    
}
