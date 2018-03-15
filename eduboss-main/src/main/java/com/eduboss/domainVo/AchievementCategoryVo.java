package com.eduboss.domainVo;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 成绩类别设置
 * @author arvin
 *
 */
public class AchievementCategoryVo implements Serializable {

    private static final long serialVersionUID = 886665244607049796L;
    
    private Integer id;

    private String name;
    
    private Integer classRankingStart;
    
    private Integer classRankingEnd;
    
    private Integer gradeRankingStart;
    
    private Integer gradeRankingEnd; 
    
    private String evaluationList;
    
    private Integer version;
    
    private Set<AchievementCategoryScoreVo> scoreSet;

    /**
     * 编号
     * @return
     */
    @NotNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 类别名称
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
     * 班级排名起
     * @retun
     */
    @NotNull
    public Integer getClassRankingStart() {
        return classRankingStart;
    }

    public void setClassRankingStart(Integer classRankingStart) {
        this.classRankingStart = classRankingStart;
    }

    /**
     * 班级排名止
     * @return
     */
    @NotNull
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
    @NotNull
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
    @NotNull
    public Integer getGradeRankingEnd() {
        return gradeRankingEnd;
    }

    public void setGradeRankingEnd(Integer gradeRankingEnd) {
        this.gradeRankingEnd = gradeRankingEnd;
    }

    /**
     * 评级列表
     * @return
     */
    @NotBlank
    public String getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(String evaluationList) {
        this.evaluationList = evaluationList;
    }

    /**
     * 成绩类别分数列表
     * @return
     */
    @NotNull
    public Set<AchievementCategoryScoreVo> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<AchievementCategoryScoreVo> scoreSet) {
        this.scoreSet = scoreSet;
    }

    /**
     * 版本
     * @return
     */
    @NotNull
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AchievementCategoryVo [id=" + id + ", name=" + name
                + ", classRankingStart=" + classRankingStart
                + ", classRankingEnd=" + classRankingEnd
                + ", gradeRankingStart=" + gradeRankingStart
                + ", gradeRankingEnd=" + gradeRankingEnd + ", evaluationList="
                + evaluationList + ", version=" + version + ", scoreSet="
                + scoreSet + "]";
    }

}
