package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.AchievementType;
import com.eduboss.common.EvaluationType;
import com.eduboss.common.StandardCategory;
import com.eduboss.dao.StudentAchievementDao;
import com.eduboss.domain.AchievementBenchmark;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementTemplate;
import com.eduboss.domain.AchievementTemplateGrade;
import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentAchievement;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentAchievementVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.StudentAchievementRequestVo;
import com.eduboss.dto.StudentAchievementSearchVo;
import com.eduboss.dto.StudentAchievementSubjectRequestVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.AchievementBenchmarkService;
import com.eduboss.service.AchievementComparisonService;
import com.eduboss.service.AchievementProStandardService;
import com.eduboss.service.AchievementTemplateGradeService;
import com.eduboss.service.AchievementTemplateService;
import com.eduboss.service.AchievementTemplateSubjectService;
import com.eduboss.service.StudentAchievementService;
import com.eduboss.service.StudentAchievementSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * 学生成绩serviceImpl
 * @author arvin
 *
 */
@Service("StudentAchievementService")
public class StudentAchievementServiceImpl implements StudentAchievementService {

    @Autowired
    private StudentAchievementSubjectService studentAchievementSubjectService;
    
    @Autowired
    private AchievementTemplateGradeService achievementTemplateGradeService;
    
    @Autowired
    private AchievementTemplateSubjectService achievementTemplateSubjectService;
    
    @Autowired
    private AchievementTemplateService achievementTemplateService;
    
    @Autowired
    private AchievementProStandardService achievementProStandardService;
    
    @Autowired
    private AchievementBenchmarkService achievementBenchmarkService;
    
    @Autowired
    private AchievementComparisonService achievementComparisonService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentAchievementDao studentAchievementDao;
    
    private static final String urlPrefix = PropertiesUtils.getStringValue("oss.access.url.prefix");
    
    /**
     * 检查是否影响到学生成绩基准科目
     */
    @Override
    public boolean checkHashRelatedAchievementBenchmark(StudentAchievementRequestVo studentAchievementRequestVo) {
        if (studentAchievementRequestVo.getId() == null) {
            return false;
        }
        StudentAchievement studentAchievementInDB = studentAchievementDao.findById(studentAchievementRequestVo.getId());
        Map<String, StudentAchievementSubject> subjectMap = this.getStudentAchievementSubjectMap(studentAchievementInDB);
        List<StudentAchievementSubjectRequestVo> subjectVos = studentAchievementRequestVo.getSubjectSet();
        Set<Integer> benchmarkSubjectIds = null; // 计算学生基准科目的成绩科目编号set
        for (StudentAchievementSubjectRequestVo subjectVo : subjectVos) {
            if (subjectMap != null &&(subjectMap.get(subjectVo.getSubjectId()) != null)) { // 修改
                StudentAchievementSubject subject = subjectMap.get(subjectVo.getSubjectId());
                if (this.isModifyStudentAchievementSubject(subjectVo, subject)) {
                    if (benchmarkSubjectIds == null) {
                        benchmarkSubjectIds = new HashSet<Integer>();
                    }
                    benchmarkSubjectIds.add(subject.getId()); // 添加到计算set
                }
            }
        }
        if (benchmarkSubjectIds != null && benchmarkSubjectIds.size() > 0) { // 计算学生基准科目的成绩科目编号set
            if (achievementBenchmarkService.countAchievementBenchmarkByBenchmarkSubjectIds(benchmarkSubjectIds) > 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 新增或修改学生成绩
     */
    @Override
    public void saveFullStudentAchievement(
            StudentAchievementRequestVo studentAchievementRequestVo) {
        if (this.countStduentAchievement(studentAchievementRequestVo.getId() , 
                studentAchievementRequestVo.getStudentId(), studentAchievementRequestVo.getExaminationDate()) > 0) { // 考试日期不能重复
            throw new ApplicationException("该学生已经存在考试时间" + studentAchievementRequestVo.getExaminationDate() + "的成绩");
        }
        User currentUser = userService.getCurrentLoginUser();
        StudentAchievement studentAchievement = HibernateUtils.voObjectMapping(studentAchievementRequestVo, StudentAchievement.class);
        Map<String, StudentAchievementSubject> subjectMap = null;
        StudentAchievement studentAchievementInDB = null;
        if (studentAchievementRequestVo.getId() != null && studentAchievementRequestVo.getId() > 0) { // 修改
            studentAchievementInDB = studentAchievementDao.findById(studentAchievementRequestVo.getId());
            studentAchievementInDB.checkVesion(studentAchievementRequestVo.getVersion());
            // 删除学生成绩关联科目
//            studentAchievementSubjectService.deleteStudentAchievementSubjectByStuAchId(studentAchievementRequestVo.getId());
            subjectMap = this.getStudentAchievementSubjectMap(studentAchievementInDB);
        } else {
            studentAchievement.setCreateUser(currentUser);
        }
        String achievementEvidence = studentAchievement.getAchievementEvidence();
        String urlPrefix2 = urlPrefix.replace("https", "http");
        if (StringUtil.isNotBlank(achievementEvidence) && (achievementEvidence.contains(urlPrefix) || achievementEvidence.contains(urlPrefix2))) {
            achievementEvidence = achievementEvidence.replace(urlPrefix, "");
            achievementEvidence = achievementEvidence.replace(urlPrefix2, "");
            studentAchievement.setAchievementEvidence(achievementEvidence);
        }
        // 保存studentAchievement
        studentAchievement.setModifyUser(currentUser);
        studentAchievement.setAchievementTemplate(new AchievementTemplate(studentAchievementRequestVo.getAchievementTemplateId()));
        studentAchievement.setStudent(new Student(studentAchievementRequestVo.getStudentId()));
        studentAchievement.setSchoolYear(new DataDict(studentAchievementRequestVo.getSchoolYearId()));
        studentAchievement.setExaminationType(new DataDict(studentAchievementRequestVo.getExaminationTypeId()));
        studentAchievement.setCity(new Region(studentAchievementRequestVo.getCityId()));
        studentAchievement.setGrade(new DataDict(studentAchievementRequestVo.getGradeId()));
        studentAchievement.setIsDeleted(1); // 删除状态为未删除
        
        List<StudentAchievementSubjectRequestVo> subjectVos = studentAchievementRequestVo.getSubjectSet();
        if (subjectVos != null) {
            int subjectNum = subjectVos.size();
//            if (subjectNum <= 0) {
//                throw new ApplicationException("科目不能为空");
//            }
            studentAchievement.setSubjectNum(subjectNum);
        }
        // 查询成绩模板年级科目数
        AchievementTemplateGrade templateGrade = achievementTemplateGradeService.findAchievementTemplateGradeByTemplateAndGrade(studentAchievementRequestVo.getAchievementTemplateId(), 
                studentAchievementRequestVo.getGradeId());
        if (templateGrade == null) {
            throw new ApplicationException("对应成绩模板年级为空");
        }
        int templateSubjectNum = templateGrade.getSubjectNum();
        studentAchievement.setTemplateSubjectNum(templateSubjectNum);
        studentAchievement.setSubjectSet(null);
        if (studentAchievementRequestVo.getId() != null && studentAchievementRequestVo.getId() > 0) {
            studentAchievementDao.merge(studentAchievement);
        } else {
            studentAchievementDao.save(studentAchievement);
        }
        Set<Integer> deleteAchievementSubjectIds = null; // 需要删除基准成绩科目的成绩科目编号set
        
        if (subjectVos != null && !subjectVos.isEmpty()) {
            for (StudentAchievementSubjectRequestVo subjectVo : subjectVos) {
                if (subjectVo.getScore() == null && subjectVo.getEvaluationType() == null) {
                    throw new ApplicationException("分数，评价不能同时为空");
                }
                AchievementTemplateSubject templateSubject = achievementTemplateSubjectService
                        .findAchievementTemplateSubjectByTemplateAndGradeSubject(studentAchievementRequestVo.getAchievementTemplateId(), 
                                studentAchievementRequestVo.getGradeId(), subjectVo.getSubjectId());
                Integer totalScore = templateSubject != null ? templateSubject.getTotalScore() : null; // 学生成绩科目满分 
                // 保存subject
                if (subjectMap == null || (subjectMap.get(subjectVo.getSubjectId()) == null)) { // 新增
                    this.saveStudentAchievementSubject(subjectVo, studentAchievement, currentUser, totalScore);
                } else { // 修改
                    StudentAchievementSubject subject = subjectMap.get(subjectVo.getSubjectId());
                    subjectMap.remove(subjectVo.getSubjectId());
                    if (this.isModifyStudentAchievementSubject(subjectVo, subject)) { // 改动了，改动过则删除所有相关的成绩准基科目
                        if (deleteAchievementSubjectIds == null) {
                            deleteAchievementSubjectIds = new HashSet<Integer>();
                        }
                        deleteAchievementSubjectIds.add(subject.getId()); // 添加到删除set
                    }
                    if (templateSubject != null) {
                        subject.setTotalScore(templateSubject.getTotalScore());
                    }
                    subject.setTotalScore(totalScore);
                    subject.setModifyUser(currentUser);
                    subject.setScore(subjectVo.getScore());
                    subject.setClassRanking(subjectVo.getClassRanking());
                    subject.setGradeRanking(subjectVo.getGradeRanking());
                    subject.setEvaluationType(subjectVo.getEvaluationType());
                    studentAchievementSubjectService.saveOrUpdateStudentAchievementSubject(subject);
                }
            }
        }
        // subjectMap 还有则为删除成绩科目
       if (subjectMap != null && subjectMap.size() > 0) { // 删除所有相关的成绩准基科目
           if (deleteAchievementSubjectIds == null) {
               deleteAchievementSubjectIds = new HashSet<Integer>();
           }
           for (String key : subjectMap.keySet()) {
               StudentAchievementSubject delSubject = subjectMap.get(key);
               studentAchievementSubjectService.deleteStudentAchievementSubject(delSubject);
               deleteAchievementSubjectIds.add(delSubject.getId()); // 添加到删除set
           }
       }
       if (deleteAchievementSubjectIds != null && deleteAchievementSubjectIds.size() > 0) { // 删除所有相关的成绩准基科目
           achievementBenchmarkService.deleteAchievementBenchmarkByBenchmarkSubjectIds(deleteAchievementSubjectIds);
       }
       if (studentAchievementInDB != null && StringUtil.isNotBlank(studentAchievementInDB.getAchievementEvidence()) 
               && StringUtil.isBlank(studentAchievementRequestVo.getAchievementEvidence())) { // 清掉考试凭证地址，删除考试凭证
           AliyunOSSUtils.remove(studentAchievement.getAchievementEvidence());
       }
    }
    
    /**
     * 根据成绩编号删除成绩图片凭证
     */
    @Override
    public void deleteStudentAchievementEvidence(Integer achievementId) {
        StudentAchievement studentAchievement = this.findStudentAchievementById(achievementId);
        if (studentAchievement == null) {
            throw new ApplicationException(ErrorCode.PARAMETER_ERROR);
        }
        if (StringUtil.isNotBlank(studentAchievement.getAchievementEvidence())) {
            AliyunOSSUtils.remove(studentAchievement.getAchievementEvidence());
        }
        studentAchievement.setAchievementEvidence(null);
        studentAchievementDao.merge(studentAchievement);
    }
    
    /**
     * 获取学生成绩关联的学生成绩科目map
     * @param studentAchievementInDB
     * @return
     */
    private Map<String, StudentAchievementSubject> getStudentAchievementSubjectMap(StudentAchievement studentAchievementInDB) {
        Set<StudentAchievementSubject> subjectSet = studentAchievementInDB.getSubjectSet();
        if (subjectSet != null && subjectSet.size() >0) {
            Map<String, StudentAchievementSubject> subjectMap = Maps.newHashMap();
            for (StudentAchievementSubject subject : subjectSet) {
                subjectMap.put(subject.getSubject().getId(), subject);
            }
            return subjectMap;
        }
        return null;
    } 
    
    /**
     * 判断学生成绩科目是否修改了
     * @param subjectVo
     * @param subject
     * @return
     */
    private boolean isModifyStudentAchievementSubject(StudentAchievementSubjectRequestVo subjectVo, StudentAchievementSubject subject) {
        if ((subjectVo.getScore() == null && subject.getScore() != null) || (subjectVo.getScore() != null && subject.getScore() == null) 
                || (subjectVo.getScore() != null && subject.getScore() != null && subjectVo.getScore().compareTo(subject.getScore()) != 0)) {
            return true;
        }
        if ((subjectVo.getClassRanking() == null && subject.getClassRanking() != null) || (subjectVo.getClassRanking() != null && subject.getClassRanking() == null) 
                || (subjectVo.getClassRanking() != null && subject.getClassRanking() != null && subjectVo.getClassRanking().intValue() != subject.getClassRanking().intValue())) {
            return true;
        }
        if ((subjectVo.getGradeRanking() == null && subject.getGradeRanking() != null) || (subjectVo.getGradeRanking() != null && subject.getGradeRanking() == null) 
                || (subjectVo.getGradeRanking() != null && subject.getGradeRanking() != null && subjectVo.getGradeRanking().intValue() != subject.getGradeRanking().intValue())) {
            return true;
        }
        if (subjectVo.getEvaluationType() != subject.getEvaluationType()) {
            return true;
        }
        return false;
    }
    
    /**
     * 保存学生成绩科目
     * @param subjectVo
     * @param studentAchievement
     * @param currentUser
     */
    private void saveStudentAchievementSubject(StudentAchievementSubjectRequestVo subjectVo, StudentAchievement studentAchievement, User currentUser, Integer totalScore) {
        StudentAchievementSubject subject = HibernateUtils.voObjectMapping(subjectVo, StudentAchievementSubject.class);
        subject.setCreateUser(currentUser);
        subject.setModifyUser(currentUser);
        subject.setSubject(new DataDict(subjectVo.getSubjectId()));
        subject.setStudentAchievement(studentAchievement);
        subject.setTotalScore(totalScore);
        studentAchievementSubjectService.saveOrUpdateStudentAchievementSubject(subject);
    }
    
    /**
     * 分页查询学生成绩
     */
    @Override
    @SuppressWarnings("unchecked")
    public DataPackage findPageStudentAchievement(DataPackage dp,
            StudentAchievementSearchVo studentAchievementSearchVo) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from StudentAchievement where student.id = :studentId and isDeleted = 1 ";
        params.put("studentId", studentAchievementSearchVo.getStudentId());
        if (StringUtil.isNotBlank(studentAchievementSearchVo.getSchoolYearId())) {
            hql += " and schoolYear.id = :schoolYearId ";
            params.put("schoolYearId", studentAchievementSearchVo.getSchoolYearId());
        }
        if (studentAchievementSearchVo.getSemester() != null) {
            hql += " and semester = :semester ";
            params.put("semester", studentAchievementSearchVo.getSemester());
        }
        if (StringUtil.isNotBlank(studentAchievementSearchVo.getExaminationTypeId())) {
            hql += " and examinationType.id = :examinationTypeId ";
            params.put("examinationTypeId", studentAchievementSearchVo.getExaminationTypeId());
        }
        if (StringUtil.isNotBlank(studentAchievementSearchVo.getExaminationDate())) {
            hql += " and examinationDate = :examinationDate ";
            params.put("examinationDate", studentAchievementSearchVo.getExaminationDate());
        }
        hql += " order by examinationDate desc ";
        dp = studentAchievementDao.findPageByHQL(hql, dp, true, params);
        List<StudentAchievement> list = (List<StudentAchievement>) dp.getDatas();
        List<StudentAchievementVo> voList = null;
        if (list != null && list.size() > 0) {
            voList = HibernateUtils.voListMapping(list, StudentAchievementVo.class);
            for (StudentAchievementVo vo : voList) {
                if (vo.getTemplateSubjectNum() == null || vo.getSubjectNum() == null || vo.getTemplateSubjectNum() <= 0) {
                    throw new ApplicationException("计算科目统计率出错");
                }
                vo.setName(vo.getSchoolYearName() + vo.getSemesterName() + vo.getExaminationTypeName());
                vo.setStatisticalRate(CommonUtil.getPercent(vo.getSubjectNum(), vo.getTemplateSubjectNum()));
                if (StringUtil.isNotBlank(vo.getAchievementEvidence())) {
                    vo.setAchievementEvidence(urlPrefix + vo.getAchievementEvidence());
                }
            }
            
        }
        dp.setDatas(voList);
        return dp;
    }
    
    /**
     * 根据学生成绩编号查询学生成绩Vo
     */
    @Override
    public StudentAchievementVo findStudentAchievementVoById(Integer id) {
        StudentAchievementVo result = null;
        StudentAchievement studentAchievement = this.findStudentAchievementById(id);
        if (studentAchievement != null) {
            result = HibernateUtils.voObjectMapping(studentAchievement, StudentAchievementVo.class);
        }
        return result;
    }
    
    /**
     * 根据学生成绩编号删除学生成绩 逻辑删除
     * @param achievementId
     * @return
     */
    @Override
    public void deleteStudentAchievementByAchievementId(
            Integer achievementId) {
        StudentAchievement studentAchievement = this.findStudentAchievementById(achievementId);
        achievementComparisonService.deleteComparisonByAchievementId(achievementId);
        studentAchievement.setIsDeleted(0);
        studentAchievementDao.merge(studentAchievement);
    }
    
    /**
     * 根据编号查询学生成绩
     */
    @Override
    public StudentAchievement findStudentAchievementById(Integer id) {
        return studentAchievementDao.findById(id);
    }

    /**
     * 根据版本编号，年级编号，成绩类型删除学生成绩
     */
    @Override
    public void deleteStudentAchievementByTemplate(Integer templateId, String gradeId,
            AchievementAbstractType achievementType) {
        String sql = " update achievement_comparison set is_deleted = 0 where comparative_achievement_id in "
                + " (select id from student_achievement where achievement_template_id = :templateId and grade_id = :gradeId and achievement_type = :achievementType) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        params.put("gradeId", gradeId);
        params.put("achievementType", achievementType);
        studentAchievementDao.excuteSql(sql, params); // 删除成绩对比记录
        
        String hql = " update StudentAchievement set isDeleted = 0 where achievementTemplate.id = :templateId "
                + " and grade.id = :gradeId and achievementType = :achievementType ";
        studentAchievementDao.excuteHql(hql, params); // 删除学生成绩
    }

    /**
     * 根据版本编号，年级编号更新学生曾经的版本科目数
     */
    @Override
    public void updateTemplateSubjectNumByTemplate(Integer templateId,
            String gradeId, Integer templateSubjectNum) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("templateId", templateId);
        params.put("gradeId", gradeId);
        params.put("templateSubjectNum", templateSubjectNum);
        String sql = " update student_achievement sa set sa.template_subject_num = :templateSubjectNum, "
                + " sa.subject_num = (select count(*) from student_achievement_subject where student_achievement_id = sa.id) "
                + " where sa.achievement_template_id = :templateId and sa.grade_id = :gradeId  ";
        studentAchievementDao.excuteSql(sql, params);
    }

    /**
     * 根据学生编号查询学生成绩select
     */
    @Override
    public List<Map<String, String>> getStudentAchievementSelectByStudentId(
            String studentId) {
        List<Map<String, String>> nvs = new ArrayList<Map<String, String>>();
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from StudentAchievement where student.id = :studentId and isDeleted = 1 order by examinationDate desc ";
        params.put("studentId", studentId);
        List<StudentAchievement> list = studentAchievementDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            List<StudentAchievementVo> voList = HibernateUtils.voListMapping(list, StudentAchievementVo.class);
            for (StudentAchievementVo vo : voList) {
                Map<String, String> map = Maps.newHashMap();
                map.put("text", vo.getSchoolYearName() + vo.getSemesterName() + vo.getExaminationTypeName() 
                        + "（" + vo.getExaminationDate() + "）");
                map.put("val", vo.getId().toString());
                nvs.add(map);
            }
        } 
        return nvs;
    }

    /**
     * 根据学生成绩基准科目、基准成绩类型、基准成绩类别，对比科目、对比成绩类型、对比成绩类别来判断进步状态
     */
    @Override
    public StandardCategory judgeStandardCategory(Integer benchmarkSubjectId, AchievementType benchmarkAchievementType, AchievementCategory benchmarkCategory,
            Integer compareSubjectId, AchievementType compareAchievementType, AchievementCategory compareCategory) {
        if (compareCategory == null) {
            return null;
        }
        // 学生成绩基准科目
        StudentAchievementSubject benchmarkSubject = studentAchievementSubjectService
                .findStudentAchievementSubjectById(benchmarkSubjectId);
        // 学生成绩对比科目
        StudentAchievementSubject compareSubject = studentAchievementSubjectService
                .findStudentAchievementSubjectById(compareSubjectId);
        if (benchmarkAchievementType == AchievementType.SCORE) {
            if (compareAchievementType == AchievementType.SCORE) { // 成绩类别都是分数
                BigDecimal benchmarkScore = benchmarkSubject.getScore();
                if (benchmarkScore == null) {
                    throw new ApplicationException("参数不对，成绩类型为分数的对比，基准科目成绩不能为空");
                }
                BigDecimal compareScore = compareSubject.getScore();
                // 获取基准，对比的成绩科目模板总分
                AchievementTemplateSubject benchmarkTemplateSubject = achievementTemplateSubjectService
                        .findAchievementTemplateSubjectByTemplateAndGradeSubject(benchmarkSubject.getStudentAchievement().getAchievementTemplate().getId(),
                                benchmarkSubject.getStudentAchievement().getGrade().getId(), benchmarkSubject.getSubject().getId());
                AchievementTemplateSubject compareTemplateSubject = achievementTemplateSubjectService
                        .findAchievementTemplateSubjectByTemplateAndGradeSubject(compareSubject.getStudentAchievement().getAchievementTemplate().getId(),
                                compareSubject.getStudentAchievement().getGrade().getId(), compareSubject.getSubject().getId());
                if (benchmarkTemplateSubject != null && compareTemplateSubject != null) {
                    // 总分对比
                    if (!benchmarkTemplateSubject.getTotalScore().equals(compareTemplateSubject.getTotalScore())) {
                        benchmarkScore = benchmarkTemplateSubject.getTotalScore().compareTo(100) == 0 ? 
                                benchmarkScore : benchmarkScore.multiply(new BigDecimal(100.0/benchmarkTemplateSubject.getTotalScore()));
                        compareScore = compareTemplateSubject.getTotalScore().compareTo(100) == 0 ? 
                                compareScore : compareScore.multiply(new BigDecimal(100.0/compareTemplateSubject.getTotalScore()));
                    }
                    BigDecimal difScore = compareScore.subtract(benchmarkScore);
                    return achievementProStandardService.judgeStandardCategoryByScore(difScore, benchmarkCategory);
                }
            } else { // 对比成绩类别是级别
                return achievementProStandardService.judgeStandardCategoryByEvaluationType(compareSubject.getEvaluationType().getValue(), benchmarkCategory);
            }
        } else if (benchmarkAchievementType == AchievementType.CLASS_RANKING) {
            if (compareAchievementType == AchievementType.CLASS_RANKING) { // 成绩类别都是分数
                Integer benchmarkClassRanking = benchmarkSubject.getClassRanking();
                Integer compareClassRanking = compareSubject.getClassRanking();
                if (benchmarkClassRanking != null && compareClassRanking != null) {
                    return achievementProStandardService.judgeStandardCategoryByClassOrGradeRanking(compareClassRanking - benchmarkClassRanking,
                            AchievementType.CLASS_RANKING, benchmarkCategory);
                }
            } else { // 对比成绩类别是级别
                return achievementProStandardService.judgeStandardCategoryByEvaluationType(compareSubject.getEvaluationType().getValue(), benchmarkCategory);
            }
        } else if (benchmarkAchievementType == AchievementType.GRADE_RANKING) {
            if (compareAchievementType == AchievementType.GRADE_RANKING) {
                Integer benchmarkGradeRanking = benchmarkSubject.getGradeRanking();
                Integer compareGradeRanking = compareSubject.getGradeRanking();
                if (benchmarkGradeRanking != null && compareGradeRanking != null) {
                    return achievementProStandardService.judgeStandardCategoryByClassOrGradeRanking(compareGradeRanking - benchmarkGradeRanking,
                            AchievementType.CLASS_RANKING, benchmarkCategory);
                }
            } else { // 对比成绩类别是级别
                return achievementProStandardService.judgeStandardCategoryByEvaluationType(compareSubject.getEvaluationType().getValue(), benchmarkCategory);
            }
        } else {
            if (compareAchievementType == AchievementType.GRADE) {
                EvaluationType benchmarkEvaluationType = benchmarkSubject.getEvaluationType();
                EvaluationType compareEvaluationType = compareSubject.getEvaluationType();
                if (benchmarkEvaluationType != null && compareEvaluationType != null) {
                    return achievementProStandardService.judgeStandardCategoryByEvaluationType(compareSubject.getEvaluationType().getValue(), benchmarkCategory);
                }
            } else {
                return achievementProStandardService.judgeStandardCategoryByEvaluationType(compareCategory.getEvaluationList(), benchmarkCategory);
            }
        }
        return null;
    }

    /**
     * 根据学生成绩基准科目，对比科目，成绩类型设置基准、对比成绩类别
     */
    @Override
    public void setAchievementBenchmarkAchievementCategory(
            AchievementBenchmark benchmark, Integer benchmarkSubjectId,
            Integer compareSubjectId, AchievementType achievementType) {
        StudentAchievementSubject benchmarkSubject = studentAchievementSubjectService
                .findStudentAchievementSubjectById(benchmarkSubjectId);
        // 基准学生成绩
        StudentAchievement benchmarkStudent = benchmarkSubject.getStudentAchievement();
        // 基准科目对应的成绩类别
        AchievementCategory benchmarkCategory = achievementTemplateService.judgeAchievementCategory(benchmarkStudent.getAchievementTemplate().getId(), 
                        benchmarkStudent.getGrade().getId(), benchmarkSubject, achievementType);
        if (benchmarkCategory == null) {
            throw new ApplicationException("学生类别设置有误，请联系相关人员在成绩模板管理页面修改");
        }
        benchmark.setBenchmarkCategory(benchmarkCategory);
        // 获取对比成绩类别
        if (compareSubjectId != null && compareSubjectId > 0) {
            // 学生成绩对比科目
            StudentAchievementSubject compareSubject = studentAchievementSubjectService
                    .findStudentAchievementSubjectById(compareSubjectId);
            StudentAchievement compareStudent = compareSubject.getStudentAchievement();
            // 获取学生成绩对比科目成绩类型
            AchievementType compareAchievementType = this.getCompareAchievementType(compareSubject, achievementType);
            if (compareAchievementType != null) { // 学生成绩对比科目成绩类型不为空
                benchmark.setCompareAchievementType(compareAchievementType);
                // 对比科目对应的成绩类别
                AchievementCategory compareCategory = achievementTemplateService.judgeAchievementCategory(compareStudent.getAchievementTemplate().getId(), 
                        compareStudent.getGrade().getId(), compareSubject, compareAchievementType);
                benchmark.setCompareCategory(compareCategory);
            }
        }
    }
    
    /**
     * 获取学生成绩数量
     */
    @Override
    public int countStduentAchievement(Integer achievementId, String studentId, String examinationDate) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " select count(*) from StudentAchievement where student.id = :studentId and examinationDate = :examinationDate and isDeleted = 1 ";
        params.put("studentId", studentId);
        params.put("examinationDate", examinationDate);
        if (achievementId != null && achievementId > 0) {
            hql += " and id != :achievementId ";
            params.put("achievementId", achievementId);
        }
        return studentAchievementDao.findCountHql(hql, params);
    }
    
    /**
     * 根据学生和成绩类型获取学生成绩列表
     */
    @Override
    public List<StudentAchievement> listStudentAchievement(String studentId,
            String examinationType) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from StudentAchievement where student.id = :studentId and isDeleted = 1 ";
        params.put("studentId", studentId);
        if (StringUtil.isNotBlank(examinationType)) {
            hql += " and examinationType.id = :examinationType ";
            params.put("examinationType", examinationType);
        }
        hql += " order by examinationDate asc ";
        return studentAchievementDao.findAllByHQL(hql, params);
    }
    
    /**
     * 根据学生成绩编号查询最近的考试日期
     */
    @Override
    public String getLastExamizationDateByAchievementIds(
            Integer[] achievementIds) {
        String result = null;
        Map<String, Object> params = Maps.newHashMap();
        String sql = " select max(examination_date) examinationDate from student_achievement where id in(:achievementIds) ";
        params.put("achievementIds", achievementIds);
        List<Map<Object, Object>> list = studentAchievementDao.findMapBySql(sql, params);
        if (list != null && !list.isEmpty()) {
            result = (String) list.get(0).get("examinationDate");
        }
        return result;
    }
    
    /**
     * 获取学生成绩对比科目成绩类型
     * @param compareSubject
     * @param achievementType
     * @return
     */
    private AchievementType getCompareAchievementType(StudentAchievementSubject compareSubject, AchievementType achievementType) {
        if (achievementType == AchievementType.SCORE) {
            // 顺序：分数、评级
            if (compareSubject.getScore() != null) {
                return AchievementType.SCORE;
            } else if (compareSubject.getEvaluationType() != null) {
                return AchievementType.GRADE;
            }
        } else if (achievementType == AchievementType.CLASS_RANKING) {
            // 顺序：班级排名、评级
            if (compareSubject.getClassRanking() != null) {
                return AchievementType.CLASS_RANKING;
            } else if (compareSubject.getEvaluationType() != null) {
                return AchievementType.GRADE;
            }
        } else if (achievementType == AchievementType.GRADE_RANKING) {
            // 顺序：年级排名、评级
            if (compareSubject.getGradeRanking() != null) {
                return AchievementType.GRADE_RANKING;
            } else if (compareSubject.getEvaluationType() != null) {
                return AchievementType.GRADE;
            }
        } else {
            // 顺序：评级、分数、班级排名、年级排名
            if (compareSubject.getEvaluationType() != null) {
                return AchievementType.GRADE;
            } else if (compareSubject.getScore() != null) {
                return AchievementType.SCORE;
            } else if (compareSubject.getClassRanking() != null) {
                return AchievementType.CLASS_RANKING;
            } else if (compareSubject.getGradeRanking() != null) {
                return AchievementType.GRADE_RANKING;
            }
        }
        return null;
    }

}
