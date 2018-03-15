package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domainVo.StudentAchievementSubjectVo;

/**
 * 学生成绩科目service
 * @author arvin
 *
 */
public interface StudentAchievementSubjectService {

    /**
     * 根据学生成绩删除学生成绩科目
     * @param studentAchievementId
     */
    void deleteStudentAchievementSubjectByStuAchId(Integer studentAchievementId);
    
    /**
     * 新增或修改学生成绩科目
     * @param subject
     */
    void saveOrUpdateStudentAchievementSubject(StudentAchievementSubject subject);
    
    /**
     * 根据成绩模板，年级，科目删除学生成绩科目
     * @param templateId
     * @param gradeId
     * @param subjectId
     */
    void deleteStudentAchievementSubjectByTemplate(Integer templateId, String gradeId, String subjectId);
    
    /**
     * 根据学生成绩编号查询所有成绩科目
     * @param achievementId
     * @return
     */
    List<StudentAchievementSubjectVo> listAllAchievementSubjectByAchievementId(Integer achievementId);
    
    /**
     * 根据编号查询成绩科目
     * @param id
     * @return
     */
    StudentAchievementSubject findStudentAchievementSubjectById(Integer id);
    
    /**
     * 根据学生成绩编号和科目编号查找学生成绩科目
     * @param achievementId
     * @param subjectId
     * @return
     */
    StudentAchievementSubject findStudentAchievementSubjectByStuAchIdAndSubjectId(Integer achievementId, String subjectId);
    
    /**
     * 删除学生成绩科目
     * @param studentAchievementSubject
     */
    void deleteStudentAchievementSubject(StudentAchievementSubject studentAchievementSubject);
    
    /**
     * 更新模板科目总分联动更新学生成绩科目的满分
     * @param achievementTemplateSubject
     */
    void updateStudentAchievementSubjectTotalScore(AchievementTemplateSubject achievementTemplateSubject);
    
}
