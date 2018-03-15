package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.AchievementTemplateGrade;
import com.eduboss.domainVo.AchievementTemplateGradeVo;

/**
 * 成绩模板年级service
 * @author arvin
 *
 */
public interface AchievementTemplateGradeService {
    
    /**
     * 根据模板编号删除所有模板年级
     * @param templateId
     */
    void deleteAchievementTemplateGradeByTemplateId(Integer templateId);
    
    /**
     * 保存或更新模板年级
     * @param grade
     */
    void saveOrUpdateAchievementTemplateGrade(AchievementTemplateGrade grade);
    
    /**
     * 根据模板编号和年级编号查询成绩模板年级
     * @param templateId
     * @param gradeId
     * @return
     */
    AchievementTemplateGrade findAchievementTemplateGradeByTemplateAndGrade(Integer templateId, String gradeId);
    
    /**
     * 根据模板编号查询所有成绩模板年级
     * @param templateId
     * @return
     */
    List<AchievementTemplateGradeVo> listAllTemplateGradeByTemplateId(Integer templateId);
    
}
