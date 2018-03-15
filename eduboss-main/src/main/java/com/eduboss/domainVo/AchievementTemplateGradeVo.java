package com.eduboss.domainVo;

import java.util.List;
import java.util.Set;

/**
 * 成绩模板年级vo
 * @author arvin
 *
 */
public class AchievementTemplateGradeVo {

    private Integer id;
    
    private Integer achievementTemplateId;
    
    private String gradeId;
    
    private String gradeName;
    
    private String achievementTypeList;
    
    private List<AchievementTemplateSubjectVo> subjects;

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
    public Integer getAchievementTemplateId() {
        return achievementTemplateId;
    }

    public void setAchievementTemplateId(Integer achievementTemplateId) {
        this.achievementTemplateId = achievementTemplateId;
    }

    /**
     * 年级编号
     * @return
     */
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    /**
     * 年级名称
     * @return
     */
    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    /**
     * 成绩类型列表 逗号隔开
     * @return
     */
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
    public List<AchievementTemplateSubjectVo> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<AchievementTemplateSubjectVo> subjects) {
        this.subjects = subjects;
    }
    
}
