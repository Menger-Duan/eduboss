package com.eduboss.domainVo;

import java.io.Serializable;
import java.util.List;

/**
 * 成绩对比记录详情vo
 * @author arvin
 *
 */
public class AchievementComparisonDetailVo implements Serializable {

    private static final long serialVersionUID = -355716984473164483L;

    private Integer id;
    
    private String name;
    
    private String comparativeName;
    
    private boolean canUpdated;
    
    private List<AchievementBenchmarkDetailVo> achievementBenchmarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * 名称
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 对比成绩的名称
     * @return
     */
    public String getComparativeName() {
        return comparativeName;
    }

    public void setComparativeName(String comparativeName) {
        this.comparativeName = comparativeName;
    }

    /**
     * 是否可以更新
     * @return
     */
    public boolean isCanUpdated() {
        return canUpdated;
    }

    public void setCanUpdated(boolean canUpdated) {
        this.canUpdated = canUpdated;
    }

    /**
     * 成绩基准科目详情set
     * @return
     */
    public List<AchievementBenchmarkDetailVo> getAchievementBenchmarks() {
        return achievementBenchmarks;
    }

    public void setAchievementBenchmarks(
            List<AchievementBenchmarkDetailVo> achievementBenchmarks) {
        this.achievementBenchmarks = achievementBenchmarks;
    }

    @Override
    public String toString() {
        return "AchievementComparisonDetailVo [id=" + id + ", name=" + name
                + ", comparativeName=" + comparativeName + ", canUpdated="
                + canUpdated + ", achievementBenchmarks="
                + achievementBenchmarks + "]";
    }

}
