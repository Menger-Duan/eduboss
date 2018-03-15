package com.eduboss.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 成绩模板年级RequestVo
 * @author arvin
 *
 */
public class AchievementTemplateGradeRequestVo implements Serializable {

    private static final long serialVersionUID = -7879347551830760884L;

    private Integer id;
    
    private String gradeId;
    
    private String achievementTypeList;
    
    private Set<AchievementTemplateSubjectRequestVo> subjects;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 年级编号
     * @return
     */
    @NotBlank
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    /**
     * 成绩类型列表
     * @return
     */
    @NotBlank
    public String getAchievementTypeList() {
        return achievementTypeList;
    }

    public void setAchievementTypeList(String achievementTypeList) {
        this.achievementTypeList = achievementTypeList;
    }

    /**
     * 成绩模板科目set
     * @return
     */
    @NotNull
    public Set<AchievementTemplateSubjectRequestVo> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<AchievementTemplateSubjectRequestVo> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "AchievementTemplateGradeRequestVo [id=" + id + ", gradeId="
                + gradeId + ", achievementTypeList=" + achievementTypeList
                + ", subjects=" + subjects + "]";
    }
    
}
