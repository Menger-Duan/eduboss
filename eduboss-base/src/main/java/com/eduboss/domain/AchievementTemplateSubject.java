package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 成绩模板科目
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_template_subject")
public class AchievementTemplateSubject extends BaseDomain {

    private static final long serialVersionUID = 5235759245675718923L;

    private Integer id;
    
    private AchievementTemplateGrade templateGrade;
    
    private DataDict subject;
    
    private Integer totalScore;

    public AchievementTemplateSubject() {
        super();
    }

    public AchievementTemplateSubject(Integer id,
            AchievementTemplateGrade templateGrade, DataDict subject,
            Integer totalScore) {
        super();
        this.id = id;
        this.templateGrade = templateGrade;
        this.subject = subject;
        this.totalScore = totalScore;
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
     * 成绩模板年级编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_grade_id")
    public AchievementTemplateGrade getTemplateGrade() {
        return templateGrade;
    }

    public void setTemplateGrade(AchievementTemplateGrade templateGrade) {
        this.templateGrade = templateGrade;
    }

    /**
     * 科目编号
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
     * 总分
     * @return
     */
    @Column(name = "total_score")
    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "AchievementTemplateSubject [id=" + id + ", templateGrade="
                + templateGrade + ", subject=" + subject + ", totalScore="
                + totalScore + super.toString() + "]";
    }
    
}
