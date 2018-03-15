package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eduboss.common.CompareType;
import com.eduboss.common.StandardCategory;

/**
 * 成绩进步标准
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_pro_standard")
public class AchievementProStandard extends BaseVersionDomain {

    private static final long serialVersionUID = -4878877506367007337L;

    private Integer id;
    
    private String name;
    
    private StandardCategory standardCategory;
    
    private CompareType greaterType;
    
    private Integer scoreBetweenStart;
    
    private CompareType lessType;
    
    private Integer scoreBetweenEnd;
    
    private Integer classRankingBtwStart;
    
    private Integer classRankingBtwEnd;
    
    private Integer gradeRankingBtwStart;
    
    private Integer gradeRankingBtwEnd;
    
    private String evaluationList;

    public AchievementProStandard() {
        super();
    }

    public AchievementProStandard(Integer id, String name,
            StandardCategory standardCategory, CompareType greaterType,
            Integer scoreBetweenStart, CompareType lessType,
            Integer scoreBetweenEnd, Integer classRankingBtwStart,
            Integer classRankingBtwEnd, Integer gradeRankingBtwStart,
            Integer gradeRankingBtwEnd, String evaluationList) {
        super();
        this.id = id;
        this.name = name;
        this.standardCategory = standardCategory;
        this.greaterType = greaterType;
        this.scoreBetweenStart = scoreBetweenStart;
        this.lessType = lessType;
        this.scoreBetweenEnd = scoreBetweenEnd;
        this.classRankingBtwStart = classRankingBtwStart;
        this.classRankingBtwEnd = classRankingBtwEnd;
        this.gradeRankingBtwStart = gradeRankingBtwStart;
        this.gradeRankingBtwEnd = gradeRankingBtwEnd;
        this.evaluationList = evaluationList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable=false)
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
    @Column(name = "name", updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 标准类别
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "standard_category", updatable = false)
    public StandardCategory getStandardCategory() {
        return standardCategory;
    }

    public void setStandardCategory(StandardCategory standardCategory) {
        this.standardCategory = standardCategory;
    }

    /**
     * 大于类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "greater_type", updatable = false)
    public CompareType getGreaterType() {
        return greaterType;
    }

    public void setGreaterType(CompareType greaterType) {
        this.greaterType = greaterType;
    }

    /**
     * 分数相差起
     * @return
     */
    @Column(name = "score_between_start")
    public Integer getScoreBetweenStart() {
        return scoreBetweenStart;
    }

    public void setScoreBetweenStart(Integer scoreBetweenStart) {
        this.scoreBetweenStart = scoreBetweenStart;
    }

    /**
     * 分数相差止
     * @return
     */
    @Column(name = "score_between_end")
    public Integer getScoreBetweenEnd() {
        return scoreBetweenEnd;
    }

    public void setScoreBetweenEnd(Integer scoreBetweenEnd) {
        this.scoreBetweenEnd = scoreBetweenEnd;
    }

    /**
     * 小于类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "less_type", updatable = false)
    public CompareType getLessType() {
        return lessType;
    }

    public void setLessType(CompareType lessType) {
        this.lessType = lessType;
    }

    /**
     * 班级排名相差起
     * @return
     */
    @Column(name = "class_ranking_btw_start")
    public Integer getClassRankingBtwStart() {
        return classRankingBtwStart;
    }

    public void setClassRankingBtwStart(Integer classRankingBtwStart) {
        this.classRankingBtwStart = classRankingBtwStart;
    }

    /**
     * 班级排名相差止
     * @return
     */
    @Column(name = "class_ranking_btw_end")
    public Integer getClassRankingBtwEnd() {
        return classRankingBtwEnd;
    }

    public void setClassRankingBtwEnd(Integer classRankingBtwEnd) {
        this.classRankingBtwEnd = classRankingBtwEnd;
    }

    /**
     * 年级排名相差起
     * @return
     */
    @Column(name = "grade_ranking_btw_start")
    public Integer getGradeRankingBtwStart() {
        return gradeRankingBtwStart;
    }

    public void setGradeRankingBtwStart(Integer gradeRankingBtwStart) {
        this.gradeRankingBtwStart = gradeRankingBtwStart;
    }

    /**
     * 年级排名相差止
     * @return
     */
    @Column(name = "grade_ranking_btw_end")
    public Integer getGradeRankingBtwEnd() {
        return gradeRankingBtwEnd;
    }

    public void setGradeRankingBtwEnd(Integer gradeRankingBtwEnd) {
        this.gradeRankingBtwEnd = gradeRankingBtwEnd;
    }

    /**
     * 评级列表
     * @return
     */
    @Column(name = "evaluation_list", length=10)
    public String getEvaluationList() {
        return evaluationList;
    }
    
    public void setEvaluationList(String evaluationList) {
        this.evaluationList = evaluationList;
    }

    @Override
    public String toString() {
        return "AchievementProStandard [id=" + id + ", name=" + name
                + ", standardCategory=" + standardCategory + ", greaterType="
                + greaterType + ", scoreBetweenStart=" + scoreBetweenStart
                + ", lessType=" + lessType + ", scoreBetweenEnd="
                + scoreBetweenEnd + ", classRankingBtwStart="
                + classRankingBtwStart + ", classRankingBtwEnd="
                + classRankingBtwEnd + ", gradeRankingBtwStart="
                + gradeRankingBtwStart + ", gradeRankingBtwEnd="
                + gradeRankingBtwEnd + ", evaluationList=" + evaluationList
                + super.toString() + "]";
    }

}
