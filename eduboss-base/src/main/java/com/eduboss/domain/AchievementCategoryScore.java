package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 成绩类别分数关联表
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_category_score")
public class AchievementCategoryScore extends BaseDomain {

    private static final long serialVersionUID = -251502408183498480L;

    private Integer id;
    
    private AchievementCategory achievementCategory;
    
    private Integer totalScore;
    
    private Integer scoreStart;
    
    private Integer scoreEnd;

    public AchievementCategoryScore() {
        super();
    }

    public AchievementCategoryScore(Integer id, AchievementCategory achievementCategory,
            Integer totalScore, Integer scoreStart, Integer scoreEnd) {
        super();
        this.id = id;
        this.achievementCategory = achievementCategory;
        this.totalScore = totalScore;
        this.scoreStart = scoreStart;
        this.scoreEnd = scoreEnd;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 成绩类别
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    public AchievementCategory getAchievementCategory() {
        return achievementCategory;
    }

    public void setAchievementCategory(AchievementCategory achievementCategory) {
        this.achievementCategory = achievementCategory;
    }

    /**
     * 总分
     * @return
     */
    @Column(name = "total_score")
    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 分数起
     * @return
     */
    @Column(name = "score_start")
    public Integer getScoreStart() {
        return scoreStart;
    }

    public void setScoreStart(Integer scoreStart) {
        this.scoreStart = scoreStart;
    }

    /**
     * 分数止
     * @return
     */
    @Column(name = "score_end")
    public Integer getScoreEnd() {
        return scoreEnd;
    }

    public void setScoreEnd(Integer scoreEnd) {
        this.scoreEnd = scoreEnd;
    }

    @Override
    public String toString() {
        return "AchievementCategoryScore [id=" + id + ", achievementCategory=" + achievementCategory
                + ", totalScore=" + totalScore + ", scoreStart=" + scoreStart
                + ", scoreEnd=" + scoreEnd
                + super.toString() + "]";
    }
    
}
