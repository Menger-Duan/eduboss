package com.eduboss.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.CompareType;
import com.eduboss.common.StandardCategory;

/**
 * 成绩进步标准vo
 * @author arvin
 *
 */
public class AchievementProStandardEditVo implements Serializable {

    private static final long serialVersionUID = 8742623742371802409L;

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
    
    private Integer version;

    /**
     * 编号
     * @return
     */
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
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 进步类别
     * @return
     */
    @NotNull
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
    public CompareType getGreaterType() {
        return greaterType;
    }
    
    public void setGreaterType(CompareType greaterType) {
        this.greaterType = greaterType;
    }

    /**
     * 大于分数
     * @return
     */
    public Integer getScoreBetweenStart() {
        return scoreBetweenStart;
    }

    public void setScoreBetweenStart(Integer scoreBetweenStart) {
        this.scoreBetweenStart = scoreBetweenStart;
    }

    /**
     * 小于类型
     * @return
     */
    public CompareType getLessType() {
        return lessType;
    }

    public void setLessType(CompareType lessType) {
        this.lessType = lessType;
    }

    /**
     * 小于分数
     * @return
     */
    public Integer getScoreBetweenEnd() {
        return scoreBetweenEnd;
    }

    public void setScoreBetweenEnd(Integer scoreBetweenEnd) {
        this.scoreBetweenEnd = scoreBetweenEnd;
    }

    /**
     * 大于等于班级排名
     * @return
     */
    public Integer getClassRankingBtwStart() {
        return classRankingBtwStart;
    }

    public void setClassRankingBtwStart(Integer classRankingBtwStart) {
        this.classRankingBtwStart = classRankingBtwStart;
    }

    /**
     * 小于等于班级排名
     * @return
     */
    public Integer getClassRankingBtwEnd() {
        return classRankingBtwEnd;
    }

    public void setClassRankingBtwEnd(Integer classRankingBtwEnd) {
        this.classRankingBtwEnd = classRankingBtwEnd;
    }

    /**
     * 大于等于年级排名
     * @return
     */
    public Integer getGradeRankingBtwStart() {
        return gradeRankingBtwStart;
    }

    public void setGradeRankingBtwStart(Integer gradeRankingBtwStart) {
        this.gradeRankingBtwStart = gradeRankingBtwStart;
    }

    /**
     * 小于等于年级排名
     * @return
     */
    public Integer getGradeRankingBtwEnd() {
        return gradeRankingBtwEnd;
    }

    public void setGradeRankingBtwEnd(Integer gradeRankingBtwEnd) {
        this.gradeRankingBtwEnd = gradeRankingBtwEnd;
    }

    /**
     * 评价值列表
     * @return
     */
    public String getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(String evaluationList) {
        this.evaluationList = evaluationList;
    }

    /**
     * 版本
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AchievementProStandardEditVo [id=" + id + ", name=" + name
                + ", standardCategory=" + standardCategory + ", greaterType="
                + greaterType + ", scoreBetweenStart=" + scoreBetweenStart
                + ", lessType=" + lessType + ", scoreBetweenEnd="
                + scoreBetweenEnd + ", classRankingBtwStart="
                + classRankingBtwStart + ", classRankingBtwEnd="
                + classRankingBtwEnd + ", gradeRankingBtwStart="
                + gradeRankingBtwStart + ", gradeRankingBtwEnd="
                + gradeRankingBtwEnd + ", evaluationList=" + evaluationList
                + ", version=" + version + "]";
    }

}
