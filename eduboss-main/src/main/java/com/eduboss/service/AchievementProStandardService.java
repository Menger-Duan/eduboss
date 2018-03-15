package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.AchievementType;
import com.eduboss.common.StandardCategory;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementProStandard;
import com.eduboss.domainVo.AchievementProStandardVo;
import com.eduboss.dto.AchievementProStandardRequestVo;

/**
 * 成绩进步标准service
 * @author arvin
 *
 */
public interface AchievementProStandardService {

    /**
     * 获取成绩进步标准列表
     * @return
     */
    List<AchievementProStandardVo> listAllAchievementProStandard();
    
    /**
     * 修改成绩进步标准
     * @param achivementCategoryEditVo
     * @return
     */
    void updateAchievementProStandard(AchievementProStandardRequestVo achievementProStandardRequestVo);
    
    /**
     * 根据分数差和成绩类别判断进步类别
     * @param difScore
     * @param benchmarkCategory
     * @return
     */
    StandardCategory judgeStandardCategoryByScore(BigDecimal difScore, AchievementCategory benchmarkCategory);
    
    /**
     * 根据成绩类型和成绩类别判断进步类别
     * @param evaluationType
     * @param benchmarkCategory
     * @return
     */
    StandardCategory judgeStandardCategoryByEvaluationType(String evaluationTypes, AchievementCategory benchmarkCategory);
    
    /**
     * 根据班级排名或者年级排名判断进步类别
     * @param ranking
     * @param achievementType
     * @param benchmarkCategory
     * @return
     */
    StandardCategory judgeStandardCategoryByClassOrGradeRanking(Integer difRanking, AchievementType achievementType, AchievementCategory benchmarkCategory);
    
    /**
     * 获取名称查询所有成绩进步标准
     * @param name
     * @return
     */
    List<AchievementProStandard> listAchievementProStandardByName(String name);
    
}
