package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.AchievementTemplateGradeDao;
import com.eduboss.domain.AchievementTemplateGrade;
import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.domainVo.AchievementTemplateGradeVo;
import com.eduboss.domainVo.AchievementTemplateSubjectVo;
import com.eduboss.service.AchievementTemplateGradeService;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

/**
 * 成绩模板年级serviceImpl
 * @author arvin
 *
 */
@Service("AchievementTemplateGradeService")
public class AchievementTemplateGradeServiceImpl implements AchievementTemplateGradeService {
    
    @Autowired
    private AchievementTemplateGradeDao achievementTemplateGradeDao;

    /**
     * 根据模板编号删除所有模板年级
     */
    @Override
    public void deleteAchievementTemplateGradeByTemplateId(Integer templateId) {
        String sql = " delete from achievement_template_grade where achievement_template_id = :templateId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        achievementTemplateGradeDao.excuteSql(sql, params);
    }

    /**
     * 保存或更新模板年级
     */
    @Override
    public void saveOrUpdateAchievementTemplateGrade(
            AchievementTemplateGrade grade) {
        achievementTemplateGradeDao.save(grade);
    }

    /**
     * 根据模板编号和年级编号查询成绩模板年级
     */
    @Override
    public AchievementTemplateGrade findAchievementTemplateGradeByTemplateAndGrade(
            Integer templateId, String gradeId) {
        AchievementTemplateGrade result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AchievementTemplateGrade where achievementTemplate.id = :templateId and grade.id = :gradeId ";
        params.put("templateId", templateId);
        params.put("gradeId", gradeId);
        List<AchievementTemplateGrade> list = achievementTemplateGradeDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    /**
     * 根据模板编号查询所有成绩模板年级
     */
    @Override
    public List<AchievementTemplateGradeVo> listAllTemplateGradeByTemplateId(
            Integer templateId) {
        List<AchievementTemplateGradeVo> reusltList = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AchievementTemplateGrade where achievementTemplate.id = :templateId ";
        params.put("templateId", templateId);
        List<AchievementTemplateGrade> list = achievementTemplateGradeDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            reusltList = new ArrayList<AchievementTemplateGradeVo>();
            for (AchievementTemplateGrade grade: list) {
                AchievementTemplateGradeVo gradeVo = HibernateUtils.voObjectMapping(grade, AchievementTemplateGradeVo.class);
                Set<AchievementTemplateSubject> subjects = grade.getSubjects();
                List<AchievementTemplateSubjectVo> subjectVos = new ArrayList<AchievementTemplateSubjectVo>();
                for (AchievementTemplateSubject subejct : subjects) {
                    subjectVos.add(HibernateUtils.voObjectMapping(subejct, AchievementTemplateSubjectVo.class));
                }
                gradeVo.setSubjects(subjectVos);
                reusltList.add(gradeVo);
            }
        }
        return reusltList;
    }

}
