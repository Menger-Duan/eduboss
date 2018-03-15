package com.eduboss.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class AchievementProStandardRequestVo implements Serializable {

    private static final long serialVersionUID = 4656483340878807021L;
    
    private Set<AchievementProStandardEditVo> achievementProStandards;

    /**
     * 成绩进步标准列表
     * @return
     */
    @NotNull(message="成绩进步标准列表不能为空")
    public Set<AchievementProStandardEditVo> getAchievementProStandards() {
        return achievementProStandards;
    }

    public void setAchievementProStandards(
            Set<AchievementProStandardEditVo> achievementProStandards) {
        this.achievementProStandards = achievementProStandards;
    }

    @Override
    public String toString() {
        return "AchievementProStandardRequestVo [achievementProStandards="
                + achievementProStandards + "]";
    }
    
}
