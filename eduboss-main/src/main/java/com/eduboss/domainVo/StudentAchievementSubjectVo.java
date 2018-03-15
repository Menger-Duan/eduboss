package com.eduboss.domainVo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.eduboss.common.EvaluationType;

/**
 * 学生成绩科目vo
 * @author arvin
 *
 */
public class StudentAchievementSubjectVo implements Serializable {

    private static final long serialVersionUID = 748835265381828390L;

    private Integer id;
    
    private Integer studentAchievementId;
    
    private String subjectId;
    
    private String subjectName;
    
    private BigDecimal score;
    
    private Integer classRanking;
    
    private Integer gradeRanking;
    
    private EvaluationType evaluationType;
    
    private Integer totalScore;

    public StudentAchievementSubjectVo() {
        super();
    }

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
     * 评价
     * @return
     */
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
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

    public StudentAchievementSubjectVo(Integer id,
            Integer studentAchievementId, String subjectId, String subjectName,
            BigDecimal score, Integer classRanking, Integer gradeRanking,
            EvaluationType evaluationType, Integer totalScore) {
        super();
        this.id = id;
        this.studentAchievementId = studentAchievementId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.score = score;
        this.classRanking = classRanking;
        this.gradeRanking = gradeRanking;
        this.evaluationType = evaluationType;
        this.totalScore = totalScore;
    }

}
