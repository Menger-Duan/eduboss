package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.EvaluationType;

/**
 * 学生成绩科目
 * @author arvin
 *
 */
@Entity
@Table(name = "student_achievement_subject")
public class StudentAchievementSubject extends BaseDomain {

    private static final long serialVersionUID = 5345755474161394732L;

    private Integer id;
    
    private StudentAchievement studentAchievement;
    
    private DataDict subject;
    
    private BigDecimal score;
    
    private Integer totalScore;
    
    private Integer classRanking;
    
    private Integer gradeRanking;
    
    private EvaluationType evaluationType;

    public StudentAchievementSubject() {
        super();
    }

    public StudentAchievementSubject(Integer id) {
        super();
        this.id = id;
    }


    public StudentAchievementSubject(Integer id,
            StudentAchievement studentAchievement, DataDict subject,
            BigDecimal score, Integer totalScore, Integer classRanking,
            Integer gradeRanking, EvaluationType evaluationType) {
        super();
        this.id = id;
        this.studentAchievement = studentAchievement;
        this.subject = subject;
        this.score = score;
        this.totalScore = totalScore;
        this.classRanking = classRanking;
        this.gradeRanking = gradeRanking;
        this.evaluationType = evaluationType;
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
     * 学生成绩编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_achievement_id")
    public StudentAchievement getStudentAchievement() {
        return studentAchievement;
    }

    public void setStudentAchievement(StudentAchievement studentAchievement) {
        this.studentAchievement = studentAchievement;
    }

    /**
     * 科目
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    public DataDict getSubject() {
        return subject;
    }

    public void setSubject(DataDict subject) {
        this.subject = subject;
    }

    /**
     * 分数
     * @return
     */
    @Column(name = "score", precision = 5)
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 满分
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
     * 班级排名
     * @return
     */
    @Column(name = "class_ranking")
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
    @Column(name = "grade_ranking")
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
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    @Override
    public String toString() {
        return "StudentAchievementSubject [id=" + id + ", studentAchievement="
                + studentAchievement + ", subject=" + subject + ", score="
                + score + ", classRanking=" + classRanking + ", gradeRanking="
                + gradeRanking + ", evaluationType=" + evaluationType
                + super.toString() + "]";
    }

}
