package com.eduboss.service;

import com.eduboss.domain.AchievementTemplateSubject;

/**
 * 成绩模板科目service
 * @author arvin
 *
 */
public interface AchievementTemplateSubjectService {

    /**
     * 根据模板编号删除所有模板科目
     * @param templateId
     */
    void deleteAchievementTemplateSubjectByTemplateId(Integer templateId);
    
    /**
     * 新增或保存成绩模板科目
     * @param subject
     */
    void saveOrUpdateAchievementTemplateSubject(AchievementTemplateSubject subject);
    
    /**
     * 根据模板编号，年级编号，科目编号查询成绩模板科目
     * @param templateId
     * @param gradeId
     * @param subjectId
     * @return
     */
    AchievementTemplateSubject findAchievementTemplateSubjectByTemplateAndGradeSubject(Integer templateId, String gradeId, String subjectId);
    
}
