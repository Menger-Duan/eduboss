package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.AchievementTemplateSubjectDao;
import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.service.AchievementTemplateSubjectService;
import com.google.common.collect.Maps;

/**
 * 成绩模板科目serviceImpl
 * @author arvin
 *
 */
@Service("AchievementTemplateSubjectService")
public class AchievementTemplateSubjectServiceImpl implements AchievementTemplateSubjectService {

    @Autowired
    private AchievementTemplateSubjectDao achievementTemplateSubjectDao;
    
    /**
     * 根据模板编号删除所有模板科目
     */
    @Override
    public void deleteAchievementTemplateSubjectByTemplateId(Integer templateId) {
        String sql = " delete ats.* from achievement_template_subject ats, achievement_template_grade atg "
                + " where ats.template_grade_id = atg.id and atg.achievement_template_id = :templateId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        achievementTemplateSubjectDao.excuteSql(sql, params);
    }

    @Override
    public void saveOrUpdateAchievementTemplateSubject(
            AchievementTemplateSubject subject) {
        achievementTemplateSubjectDao.save(subject);
    }

    /**
     * 根据模板编号，年级编号，科目编号查询成绩模板科目
     */
    @Override
    public AchievementTemplateSubject findAchievementTemplateSubjectByTemplateAndGradeSubject(
            Integer templateId, String gradeId, String subjectId) {
        AchievementTemplateSubject result = null;
        String sql = " select ats.* from achievement_template_subject ats, achievement_template_grade atg "
                + " where ats.template_grade_id = atg.id and atg.achievement_template_id = :templateId "
                + " and atg.grade_id = :gradeId and ats.subject_id = :subjectId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        params.put("gradeId", gradeId);
        params.put("subjectId", subjectId);
        List<AchievementTemplateSubject> list = achievementTemplateSubjectDao.findBySql(sql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

}
