package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.AchievementType;
import com.eduboss.common.EvaluationType;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domainVo.AchievementCategoryVo;
import com.eduboss.dto.AchievementCategoryRequestVo;

/**
 * 成绩类别service
 * @author arvin
 *
 */
public interface AchievementCategoryService {

    /**
     * 获取成绩类别列表
     * @return
     */
    List<AchievementCategoryVo> listAllAchievementCategory();
    
    /**
     * 修改成绩类别
     * @param achivementCategoryEditVo
     * @return
     */
    void updateAchievementCategory(AchievementCategoryRequestVo achievementCategoryRequestVo);
    
    /**
     * 根据分数来判断成绩类别
     * @param totalScore
     * @param score
     * @return
     */
    AchievementCategory judgeAchievementCategoryByScore(Integer totalScore, BigDecimal score);
    
    /**
     * 根据排名来判断成绩类别
     * @param ranking
     * @param achievementType
     * @return
     */
    AchievementCategory judgeAchievementCategoryByRanking(Integer ranking, AchievementType achievementType);
    
    /**
     * 根据评级来判断成绩类别
     * @param evaluationType
     * @return
     */
    AchievementCategory judgeAchievementCategoryByEvaluationType(EvaluationType evaluationType);
    
}
