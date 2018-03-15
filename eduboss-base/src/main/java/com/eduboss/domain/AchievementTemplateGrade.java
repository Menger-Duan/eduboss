package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 成绩模板年级
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_template_grade")
public class AchievementTemplateGrade extends BaseDomain {
    
    private static final long serialVersionUID = -4038068040023107576L;

    private Integer id;
    
    private AchievementTemplate achievementTemplate;
    
    private DataDict grade;
    
    private String achievementTypeList;
    
    private Integer subjectNum;
    
    private Set<AchievementTemplateSubject> subjects;

    public AchievementTemplateGrade() {
        super();
    }

    public AchievementTemplateGrade(Integer id,
            AchievementTemplate achievementTemplate, DataDict grade,
            String achievementTypeList, Integer subjectNum,
            Set<AchievementTemplateSubject> subjects) {
        super();
        this.id = id;
        this.achievementTemplate = achievementTemplate;
        this.grade = grade;
        this.achievementTypeList = achievementTypeList;
        this.subjectNum = subjectNum;
        this.subjects = subjects;
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
     * 成绩模板编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_template_id")
    public AchievementTemplate getAchievementTemplate() {
        return achievementTemplate;
    }

    public void setAchievementTemplate(AchievementTemplate achievementTemplate) {
        this.achievementTemplate = achievementTemplate;
    }

    /**
     * 年级编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    public DataDict getGrade() {
        return grade;
    }

    public void setGrade(DataDict grade) {
        this.grade = grade;
    }

    /**
     * 成绩类型列表
     * @return
     */
    @Column(name = "achievement_type_list")
    public String getAchievementTypeList() {
        return achievementTypeList;
    }

    public void setAchievementTypeList(String achievementTypeList) {
        this.achievementTypeList = achievementTypeList;
    }

    /**
     * 科目数量
     * @return
     */
    @Column(name = "subject_num")
    public Integer getSubjectNum() {
        return subjectNum;
    }

    public void setSubjectNum(Integer subjectNum) {
        this.subjectNum = subjectNum;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "templateGrade")
    @OrderBy("id ASC")
    public Set<AchievementTemplateSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<AchievementTemplateSubject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "AchievementTemplateGrade [id=" + id + ", achievementTemplate="
                + achievementTemplate + ", grade=" + grade
                + ", achievementTypeList=" + achievementTypeList
                + ", subjectNum=" + subjectNum + ", subjects=" + subjects
                + super.toString() + "]";
    }

    
}
