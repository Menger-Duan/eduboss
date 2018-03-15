package com.eduboss.service;

import java.util.List;

import com.eduboss.common.AchievementType;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementTemplate;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domainVo.AchievementTemplateGradeVo;
import com.eduboss.domainVo.AchievementTemplateVo;
import com.eduboss.dto.AchievementTemplateGradeSearVo;
import com.eduboss.dto.AchievementTemplateRequestVo;
import com.eduboss.dto.DataPackage;

/**
 * 成绩模板service
 * @author arvin
 *
 */
public interface AchievementTemplateService {

    /**
     * 根据省份编号获取已排成绩模板的城市编号列表
     * @param provinceId
     * @return
     */
    List<String> listTemplateCityIdsByProvinceId(String provinceId);
    
    /**
     * 根据城市编号初始化成绩模板
     * @param cityIds
     */
    void createAchievementTemplateByCitys(String cityIds);
    
    /**
     * 根据城市编号查询成绩模板列表
     * @param cityId
     * @return
     */
    List<AchievementTemplate> listAchievementTemplatesByCityId(String cityId);
    
    /**
     * 根据城市编号查询成绩模板Vo列表
     * @param cityId
     * @return
     */
    List<AchievementTemplateVo> listAchievementTemplateVosByCityId(String cityId);
    
    /**
     * 根据城市编号分页查询成绩模板Vo列表
     * @param dp
     * @param cityId
     * @return
     */
    DataPackage findPageAchievementTemplateByCityId(DataPackage dp, String cityId);
    
    /**
     * 根据城市编号查询成绩生效日期列表
     * @param cityId
     * @return
     */
    List<String> listTemplateEffectiveDateByCityId(String cityId);
    
    /**
     * 根据城市编号计算成绩模板数
     * @param cityId
     * @return
     */
    int countAchievementTemplatesByCityId(String cityId);
    
    /**
     * 新增或修改成绩模板
     * @param achievementTemplateGradeRequestVo
     */
    void saveFullAchievementTemplate(AchievementTemplateRequestVo achievementTemplateRequestVo);
    
    /**
     * 根据编号查询成绩模板
     * @param id
     * @return
     */
    AchievementTemplate findAchievementTemplateById(Integer id);
    
    /**
     * 分页查询成绩模板
     * @param dp
     * @param cityId
     * @return
     */
    DataPackage findPageAchievementTemplate(DataPackage dp, String cityId);
    
    /**
     * 根据模板编号删除模板
     * @param templateId
     */
    void deleteAchievementTemplateById(Integer templateId);
    
    /**
     * 根据学生成绩查询最符合的模板年级
     * @param searchVo
     * @return
     */
    AchievementTemplateGradeVo findMostConsistentTemplateGrade(AchievementTemplateGradeSearVo searchVo);
    
    /**
     * 查询最符合的成绩模板
     * @param cityId
     * @param examinationDate
     * @return
     */
    AchievementTemplate findMostConsistentTemplateGrade(String cityId, String examinationDate);
    
    /**
     * 根据模板，年级，学生成绩科目，成绩类型判断学生成绩类别
     * @param achievementTemplateId
     * @param gradeId
     * @param subject
     * @param achievementType
     * @return
     */
    AchievementCategory judgeAchievementCategory(Integer achievementTemplateId, String gradeId, StudentAchievementSubject subject, AchievementType achievementType);
    
}
