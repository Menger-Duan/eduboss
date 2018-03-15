package com.eduboss.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.eduboss.domainVo.AchievementCategoryVo;

/**
 * 成绩类别编辑Vo
 * @author arvin
 *
 */
public class AchievementCategoryRequestVo implements Serializable {

    private static final long serialVersionUID = 7306143792058876508L;
    
    private Set<AchievementCategoryVo> achievementCategorys;

    /**
     * 成绩类型列表
     * @return
     */
    @NotNull(message="成绩列表不能为空")
    public Set<AchievementCategoryVo> getAchievementCategorys() {
        return achievementCategorys;
    }

    public void setAchievementCategorys(
            Set<AchievementCategoryVo> achievementCategorys) {
        this.achievementCategorys = achievementCategorys;
    }

    @Override
    public String toString() {
        return "AchivementCategoryEditVo [achievementCategorys="
                + achievementCategorys + "]";
    }
    
    
    
}
