package com.eduboss.domainVo;

import javax.validation.constraints.NotNull;

public class AchievementCategoryScoreVo {

    private Integer id;
    
    private Integer totalScore;
    
    private Integer scoreStart;
    
    private Integer scoreEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @NotNull
    public Integer getScoreStart() {
        return scoreStart;
    }

    public void setScoreStart(Integer scoreStart) {
        this.scoreStart = scoreStart;
    }

    @NotNull
    public Integer getScoreEnd() {
        return scoreEnd;
    }

    public void setScoreEnd(Integer scoreEnd) {
        this.scoreEnd = scoreEnd;
    }
    
    
}
