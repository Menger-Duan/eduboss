package com.eduboss.dto;

import java.io.Serializable;

/**
 * 成绩模板科目requestVo
 * @author arvin
 *
 */
public class AchievementTemplateSubjectRequestVo implements Serializable {

    private static final long serialVersionUID = 8551443070055677804L;

    private Integer id;
    
    private String subjectId;
    
    private Integer totalScore; 
    
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
        return "AchievementTemplateSubjectRequestVo [id=" + id + ", subjectId="
                + subjectId + ", totalScore=" + totalScore + "]";
    }
    
}
