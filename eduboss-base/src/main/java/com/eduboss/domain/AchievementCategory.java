package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 成绩类别
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_category")
public class AchievementCategory extends BaseVersionDomain {

    private static final long serialVersionUID = 7108116918987136603L;
    
    private Integer id;

    private String name;
    
    private Integer classRankingStart;
    
    private Integer classRankingEnd;
    
    private Integer gradeRankingStart;
    
    private Integer gradeRankingEnd;
    
    private String evaluationList;
    
    private Set<AchievementCategoryScore> scoreSet;

    public AchievementCategory() {
        super();
    }

    public AchievementCategory(Integer id, String name,
            Integer classRankingStart, Integer classRankingEnd,
            Integer gradeRankingStart, Integer gradeRankingEnd,
            String evaluationList, Set<AchievementCategoryScore> scoreSet) {
        super();
        this.id = id;
        this.name = name;
        this.classRankingStart = classRankingStart;
        this.classRankingEnd = classRankingEnd;
        this.gradeRankingStart = gradeRankingStart;
        this.gradeRankingEnd = gradeRankingEnd;
        this.evaluationList = evaluationList;
        this.scoreSet = scoreSet;
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
     * 类别
     * @return
     */
    @Column(name = "name", unique=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 班级排名起
     * @return
     */
    @Column(name = "class_ranking_start")
    public Integer getClassRankingStart() {
        return classRankingStart;
    }

    public void setClassRankingStart(Integer classRankingStart) {
        this.classRankingStart = classRankingStart;
    }

    /**
     * 班级名次止
     * @return
     */
    @Column(name = "class_ranking_end")
    public Integer getClassRankingEnd() {
        return classRankingEnd;
    }

    public void setClassRankingEnd(Integer classRankingEnd) {
        this.classRankingEnd = classRankingEnd;
    }

    /**
     * 年级排名起
     * @return
     */
    @Column(name = "grade_ranking_start")
    public Integer getGradeRankingStart() {
        return gradeRankingStart;
    }

    public void setGradeRankingStart(Integer gradeRankingStart) {
        this.gradeRankingStart = gradeRankingStart;
    }

    /**
     * 年级排名止
     * @return
     */
    @Column(name = "grade_ranking_end")
    public Integer getGradeRankingEnd() {
        return gradeRankingEnd;
    }

    public void setGradeRankingEnd(Integer gradeRankingEnd) {
        this.gradeRankingEnd = gradeRankingEnd;
    }
    
    /**
     * 类别
     * @return
     */
    @Column(name = "evaluation_list", length = 10)
    public String getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(String evaluationList) {
        this.evaluationList = evaluationList;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "achievementCategory")
    @OrderBy("id ASC")
    public Set<AchievementCategoryScore> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<AchievementCategoryScore> scoreSet) {
        this.scoreSet = scoreSet;
    }

    @Override
    public String toString() {
        return "AchievementCategory [id=" + id + ", name=" + name
                + ", classRankingStart=" + classRankingStart
                + ", classRankingEnd=" + classRankingEnd
                + ", gradeRankingStart=" + gradeRankingStart
                + ", gradeRankingEnd=" + gradeRankingEnd + ", evaluationList="
                + evaluationList + ", scoreSet=" + scoreSet
                + super.toString() + "]";
    }

}
