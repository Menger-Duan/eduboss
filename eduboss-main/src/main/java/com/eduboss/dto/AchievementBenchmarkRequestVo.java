package com.eduboss.dto;

import java.io.Serializable;

import com.eduboss.common.AchievementType;

/**
 * 成绩基准科目编辑Vo
 * @author arvin
 *
 */
public class AchievementBenchmarkRequestVo implements Serializable {

    private static final long serialVersionUID = -4253147002365642772L;

    private Integer id;
    
    private Integer studentAchievementId;
    
    private Integer studentAchievementSubjectId;
    
    private String subjectId;
    
    private AchievementType achievementType;

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
     * 学生成绩编号
     * @return
     */
    public Integer getStudentAchievementId() {
        return studentAchievementId;
    }

    public void setStudentAchievementId(Integer studentAchievementId) {
        this.studentAchievementId = studentAchievementId;
    }

    /**
     * 学生成绩科目编号
     * @return
     */
    public Integer getStudentAchievementSubjectId() {
        return studentAchievementSubjectId;
    }

    public void setStudentAchievementSubjectId(Integer studentAchievementSubjectId) {
        this.studentAchievementSubjectId = studentAchievementSubjectId;
    }

    /**
     * 科目编号
     * @return
     */
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
     *  成绩类型
     * @return
     */
    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    @Override
    public String toString() {
        return "AchievementBenchmarkRequestVo [id=" + id
                + ", studentAchievementId=" + studentAchievementId
                + ", studentAchievementSubjectId="
                + studentAchievementSubjectId + ", subjectId=" + subjectId
                + ", achievementType=" + achievementType + "]";
    }

}
