package com.eduboss.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.EvaluationType;

/**
 * 学生成绩科目requestVo
 * @author arvin
 *
 */
public class StudentAchievementSubjectRequestVo implements Serializable {

    private static final long serialVersionUID = -1330824329647427141L;

    private Integer id;
    
    private String subjectId;
    
    private BigDecimal score;
    
    private Integer classRanking;
    
    private Integer gradeRanking;
    
    private EvaluationType evaluationType;

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
    @NotBlank
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * 分数
     * @return
     */
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 班级排名
     * @return
     */
    public Integer getClassRanking() {
        return classRanking;
    }

    public void setClassRanking(Integer classRanking) {
        this.classRanking = classRanking;
    }

    /**
     * 年级排名
     * @return
     */
    public Integer getGradeRanking() {
        return gradeRanking;
    }

    public void setGradeRanking(Integer gradeRanking) {
        this.gradeRanking = gradeRanking;
    }

    /**
     * 评级
     * @return
     */
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    @Override
    public String toString() {
        return "StudentAchievementSubjectRequestVo [id=" + id + ", subjectId="
                + subjectId + ", score=" + score + ", classRanking="
                + classRanking + ", gradeRanking=" + gradeRanking
                + ", evaluationType=" + evaluationType + "]";
    }
    
}
