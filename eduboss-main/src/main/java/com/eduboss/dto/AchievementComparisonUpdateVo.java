package com.eduboss.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * 成绩对比记录更新Vo
 * @author arvin
 *
 */
public class AchievementComparisonUpdateVo implements Serializable {

    private static final long serialVersionUID = -355716984473164483L;

    private Integer id;
    
    private Set<AchievementBenchmarkUpdateVo> achievementBenchmarks;

    @NotNull(message="成绩对比记录编号不能为空")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 成绩基准科目更新set
     * @return
     */
    @NotNull(message="成绩基准科目更新set不能为空")
    public Set<AchievementBenchmarkUpdateVo> getAchievementBenchmarks() {
        return achievementBenchmarks;
    }

    public void setAchievementBenchmarks(
            Set<AchievementBenchmarkUpdateVo> achievementBenchmarks) {
        this.achievementBenchmarks = achievementBenchmarks;
    }

    @Override
    public String toString() {
        return "AchievementComparisonDetailVo [id=" + id
                + ", achievementBenchmarks=" + achievementBenchmarks + "]";
    }
    
}
