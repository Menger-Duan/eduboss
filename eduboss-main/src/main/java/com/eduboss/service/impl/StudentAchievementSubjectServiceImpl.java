package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.StudentAchievementSubjectDao;
import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domainVo.StudentAchievementSubjectVo;
import com.eduboss.service.AchievementTemplateSubjectService;
import com.eduboss.service.StudentAchievementSubjectService;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

/**
 * 学生成绩科目serviceImpl
 * @author arvin
 *
 */
@Service("StudentAchievementSubjectService")
public class StudentAchievementSubjectServiceImpl implements StudentAchievementSubjectService {
    
    @Autowired
    private AchievementTemplateSubjectService achievementTemplateSubjectService;
    
    @Autowired
    private StudentAchievementSubjectDao studentAchievementSubjectDao;

    /**
     * 根据学生成绩删除学生成绩科目
     */
    @Override
    public void deleteStudentAchievementSubjectByStuAchId(
            Integer studentAchievementId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("studentAchievementId", studentAchievementId);
        String hql = " delete from StudentAchievementSubject where studentAchievement.id = :studentAchievementId ";
        studentAchievementSubjectDao.excuteHql(hql, params);
    }

    /**
     * 新增或修改学生成绩科目
     */
    @Override
    public void saveOrUpdateStudentAchievementSubject(
            StudentAchievementSubject subject) {
        if (subject.getId() != null) {
            studentAchievementSubjectDao.merge(subject);
        } else {
            studentAchievementSubjectDao.save(subject);
        }
    }

    /**
     * 根据成绩模板，年级，科目删除学生成绩科目
     */
    @Override
    public void deleteStudentAchievementSubjectByTemplate(Integer templateId,
            String gradeId, String subjectId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        params.put("gradeId", gradeId);
        params.put("subjectId", subjectId);
        String inSql = " select sas.id from student_achievement_subject sas, student_achievement sa where sas.student_achievement_id = sa.id "
                + " and sa.achievement_template_id = :templateId and sa.grade_id = :gradeId and sas.subject_id = :subjectId ";
        String benchmarkSql = " delete from achievement_benchmark where benchmark_subject_id in (" + inSql + ") or compare_subject_id in(" + inSql + ") ";
        studentAchievementSubjectDao.excuteSql(benchmarkSql, params); // 删除学生对比成绩基准科目
        
        String sql = " delete sas from student_achievement_subject sas, student_achievement sa where sas.student_achievement_id = sa.id and sa.achievement_template_id = :templateId "
                + " and sa.grade_id = :gradeId and sas.subject_id = :subjectId ";
        studentAchievementSubjectDao.excuteSql(sql, params); // 删除学生成绩科目
    }

    /**
     * 根据学生成绩编号查询所有成绩科目
     */
    @Override
    public List<StudentAchievementSubjectVo> listAllAchievementSubjectByAchievementId(
            Integer achievementId) {
        List<StudentAchievementSubjectVo> resultList = null;
        Map<String, Object> params = Maps.newHashMap();
        params.put("achievementId", achievementId);
        String hql = " from StudentAchievementSubject where studentAchievement.id = :achievementId ";
        List<StudentAchievementSubject> list = studentAchievementSubjectDao.findAllByHQL(hql, params);
        
        if (list != null && !list.isEmpty()) {
            resultList = new ArrayList<StudentAchievementSubjectVo>();
            for (StudentAchievementSubject subject : list) {
                StudentAchievementSubjectVo subejctVo = HibernateUtils.voObjectMapping(subject, StudentAchievementSubjectVo.class);
                resultList.add(subejctVo);
            }
        }
        return resultList;
    }

    /**
     * 根据编号查询成绩科目
     */
    @Override
    public StudentAchievementSubject findStudentAchievementSubjectById(
            Integer id) {
        return studentAchievementSubjectDao.findById(id);
    }

    @Override
    public StudentAchievementSubject findStudentAchievementSubjectByStuAchIdAndSubjectId(
            Integer achievementId, String subjectId) {
        StudentAchievementSubject result = null;
        Map<String, Object> params = Maps.newHashMap();
        params.put("achievementId", achievementId);
        params.put("subjectId", subjectId);
        String hql = " from StudentAchievementSubject where studentAchievement.id = :achievementId and subject.id = :subjectId ";
        List<StudentAchievementSubject> list = studentAchievementSubjectDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    /**
     * 删除学生成绩科目
     */
    @Override
    public void deleteStudentAchievementSubject(
            StudentAchievementSubject studentAchievementSubject) {
        studentAchievementSubjectDao.delete(studentAchievementSubject);
    }

    /**
     * 更新模板科目总分联动更新学生成绩科目的满分
     */
    @Override
    public void updateStudentAchievementSubjectTotalScore(
            AchievementTemplateSubject achievementTemplateSubject) {
        Map<String, Object> params = Maps.newHashMap();
        String sql = " update student_achievement_subject sas, student_achievement sa set sas.total_score = :totalScore  "
                    + "where sas.student_achievement_id = sa.id and sas.subject_id = :subjectId "
                    + " and sa.grade_id = :gradeId and sa.achievement_template_id = :achievmentTemplateId ";
        params.put("totalScore", achievementTemplateSubject.getTotalScore());
        params.put("subjectId", achievementTemplateSubject.getSubject().getId());
        params.put("gradeId", achievementTemplateSubject.getTemplateGrade().getGrade().getId());
        params.put("achievmentTemplateId", achievementTemplateSubject.getTemplateGrade().getAchievementTemplate().getId());
        studentAchievementSubjectDao.excuteSql(sql, params);
        
    }

}
