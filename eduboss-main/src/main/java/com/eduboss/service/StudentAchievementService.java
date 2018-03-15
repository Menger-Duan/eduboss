package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.AchievementType;
import com.eduboss.common.StandardCategory;
import com.eduboss.domain.AchievementBenchmark;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.StudentAchievement;
import com.eduboss.domainVo.StudentAchievementVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.StudentAchievementRequestVo;
import com.eduboss.dto.StudentAchievementSearchVo;

/**
 * 学生成绩service
 * @author arvin
 *
 */
public interface StudentAchievementService {
    
    /**
     * 检查是否影响到学生成绩基准科目
     * @param studentAchievementRequestVo
     * @return
     */
    boolean checkHashRelatedAchievementBenchmark(StudentAchievementRequestVo studentAchievementRequestVo);

    /**
     * 新增或修改学生成绩
     * @param studentAchievementRequestVo
     */
    void saveFullStudentAchievement(StudentAchievementRequestVo studentAchievementRequestVo);
    
    /**
     * 根据成绩编号删除成绩图片凭证
     * @param achievementId
     */
    void deleteStudentAchievementEvidence(Integer achievementId);
    
    /**
     * 分页查询学生成绩
     * @param dp
     * @param studentAchievementSearchVo
     * @return
     */
    DataPackage findPageStudentAchievement(DataPackage dp, StudentAchievementSearchVo studentAchievementSearchVo);
    
    /**
     * 根据学生成绩编号查询学生成绩Vo
     * @param studentAchievementId
     * @return
     */
    StudentAchievementVo findStudentAchievementVoById(Integer id);
    
    /**
     * 根据学生成绩编号删除学生成绩 逻辑删除
     * @param achievementId
     * @return
     */
    void deleteStudentAchievementByAchievementId(Integer achievementId);
    
    /**
     * 根据编号查询学生成绩
     * @param id
     * @return
     */
    StudentAchievement findStudentAchievementById(Integer id);
    
    /**
     * 根据版本编号，年级编号，成绩类型删除学生成绩
     * @param templateId
     * @param gradeId
     * @param AchievementType
     */
    void deleteStudentAchievementByTemplate(Integer templateId, String gradeId, AchievementAbstractType achievementType);
    
    /**
     * 根据版本编号，年级编号更新学生曾经的版本科目数
     * @param templateId
     * @param gradeId
     * @param templateSubjectNum
     */
    void updateTemplateSubjectNumByTemplate(Integer templateId, String gradeId, Integer templateSubjectNum);
    
    /**
     * 根据学生编号查询学生成绩select
     * @param studentId
     * @return
     */
    List<Map<String, String>> getStudentAchievementSelectByStudentId(String studentId);
    
    /**
     * 根据学生成绩基准科目、基准成绩类型、基准成绩类别，对比科目、对比成绩类型、对比成绩类别来判断进步状态
     * @param benchmarkSubjectId
     * @param benchmarkAchievementType
     * @param benchmarkCategory
     * @param compareSubjectId
     * @param compareAchievementType
     * @param compareCategory
     * @return
     */
    StandardCategory judgeStandardCategory(Integer benchmarkSubjectId, AchievementType benchmarkAchievementType, AchievementCategory benchmarkCategory,
            Integer compareSubjectId, AchievementType compareAchievementType, AchievementCategory compareCategory);
    
    /**
     * 根据学生成绩基准科目，对比科目，成绩类型设置基准、对比成绩类别
     * @param benchmark
     * @param benchmarkSubjectId
     * @param compareSubjectId
     * @param achievementType
     */
    void setAchievementBenchmarkAchievementCategory(AchievementBenchmark benchmark, 
            Integer benchmarkSubjectId, Integer compareSubjectId, AchievementType achievementType);
    
    /**
     * 获取学生成绩数量
     * @param achievementId
     * @param studendId
     * @param examinationDate
     * @return
     */
    int countStduentAchievement(Integer achievementId, String studentId, String examinationDate);
    
    /**
     * 根据学生和成绩类型获取学生成绩列表
     * @param studentId
     * @param examninationType
     * @return
     */
    List<StudentAchievement> listStudentAchievement(String studentId, String examinationType);
    
    /**
     * 根据学生成绩编号查询最近的考试日期
     * @param achievementIds
     * @return
     */
    String getLastExamizationDateByAchievementIds(Integer[] achievementIds);
    
}
