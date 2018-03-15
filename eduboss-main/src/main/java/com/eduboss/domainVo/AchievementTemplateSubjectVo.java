package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩模板科目vo
 * @author arvin
 *
 */
public class AchievementTemplateSubjectVo implements Serializable {

    private static final long serialVersionUID = -6319430817741476734L;

    private Integer id;
    
    private String subjectId;
    
    private String subjectName;
    
    private Integer totalScore;

    /**
     * 成绩模板科目编号
     * @return
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
     * 科目名称
     * @return
     */
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * 总分
     * @return
     */
    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "AchievementTemplateSubjectVo [id=" + id + ", subjectId="
                + subjectId + ", subjectName=" + subjectName + ", totalScore="
                + totalScore + "]";
    }
    
}
