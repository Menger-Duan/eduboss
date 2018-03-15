package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.AchievementType;
import com.eduboss.common.EvaluationType;
import com.eduboss.dao.AchievementTemplateDao;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementTemplate;
import com.eduboss.domain.AchievementTemplateGrade;
import com.eduboss.domain.AchievementTemplateSubject;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AchievementTemplateGradeVo;
import com.eduboss.domainVo.AchievementTemplateSubjectVo;
import com.eduboss.domainVo.AchievementTemplateVo;
import com.eduboss.dto.AchievementTemplateGradeRequestVo;
import com.eduboss.dto.AchievementTemplateGradeSearVo;
import com.eduboss.dto.AchievementTemplateRequestVo;
import com.eduboss.dto.AchievementTemplateSubjectRequestVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AchievementCategoryService;
import com.eduboss.service.AchievementTemplateGradeService;
import com.eduboss.service.AchievementTemplateService;
import com.eduboss.service.AchievementTemplateSubjectService;
import com.eduboss.service.StudentAchievementService;
import com.eduboss.service.StudentAchievementSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * 成绩模板serviceImpl
 * @author arvin
 *
 */
@Service("AchievementTemplateService")
public class AchievementTemplateServiceImpl implements AchievementTemplateService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AchievementTemplateSubjectService achievementTemplateSubjectService;
    
    @Autowired
    private AchievementTemplateGradeService achievementTemplateGradeService;
    
    @Autowired
    private StudentAchievementService studentAchievementService;
    
    @Autowired
    private StudentAchievementSubjectService studentAchievementSubjectService;
    
    @Autowired
    private AchievementCategoryService achievementCategoryService;

    @Autowired
    private AchievementTemplateDao achievementTemplateDao;
    
    /**
     * 根据省份编号获取已排成绩模板的城市编号列表
     */
    @Override
    public List<String> listTemplateCityIdsByProvinceId(String provinceId) {
        List<String> result = null;
        Map<String, Object> params = Maps.newHashMap();
        String sql = "select distinct a.city_id "
                + " from achievement_template a left join region r on a.city_id = r.id where r.parent_id = :provinceId or r.id = :provinceId ";
        params.put("provinceId", provinceId);
        List<Map<Object, Object>> list = achievementTemplateDao.findMapBySql(sql, params);
        if (list != null && list.size() > 0 ) {
            result = new ArrayList<String>();
            for (Map<Object, Object> map : list) {
                result.add(map.get("city_id").toString());
            }
        }
        return result;
    }

    @Override
    public void createAchievementTemplateByCitys(String cityIds) {
        User currentUser = userService.getCurrentLoginUser();
        if (StringUtil.isNotBlank(cityIds)) {
            String[] cityIdArr = cityIds.split(",");
            for (String cityId : cityIdArr) {
                if (this.countAchievementTemplatesByCityId(cityId) > 0) {
                    throw new ApplicationException("城市编号："+ cityId + "已存在成绩模板");
                }
                AchievementTemplate achievementTemplate = new AchievementTemplate();
                achievementTemplate.setCity(new Region(cityId));
                achievementTemplate.setIsCurrentVersion(0); // 初始化模板为当前版本
                achievementTemplate.setIsDeleted(1); // 初始化模板为未删除
                achievementTemplate.setAchievementVersion(0); // 初始化为版本0
                achievementTemplate.setCreateUser(currentUser);
                achievementTemplate.setModifyUser(currentUser);
                achievementTemplateDao.save(achievementTemplate);
            }
        }
    }

    /**
     * 根据城市编号查询成绩模板列表
     */
    @Override
    public List<AchievementTemplate> listAchievementTemplatesByCityId(
            String cityId) {
        String hql = " from AchievementTemplate where city.id = :cityId and isDeleted = 1 order by achievementVersion desc ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("cityId", cityId);
        return achievementTemplateDao.findAllByHQL(hql, params);
    }
    
    /**
     * 根据城市编号查询成绩模板Vo列表
     */
    @Override
    public List<AchievementTemplateVo> listAchievementTemplateVosByCityId(String cityId) {
        String hql = " from AchievementTemplate where city.id = :cityId and isDeleted = 1"
                + " and effectiveDate is not null order by effectiveDate desc, createTime desc ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("cityId", cityId);
        List<AchievementTemplate> list = achievementTemplateDao.findAllByHQL(hql, params);
        return HibernateUtils.voListMapping(list, AchievementTemplateVo.class);
    }
    
    /**
     * 根据城市编号分页查询成绩模板Vo列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public DataPackage findPageAchievementTemplateByCityId(DataPackage dp, String cityId) {
        List<AchievementTemplateVo> result = null;
        String hql = " from AchievementTemplate where city.id = :cityId and isDeleted = 1"
                + " and effectiveDate is not null order by effectiveDate desc, createTime desc ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("cityId", cityId);
        dp = achievementTemplateDao.findPageByHQL(hql, dp, true, params);
        List<AchievementTemplate> list = (List<AchievementTemplate>) dp.getDatas();
        if (list != null && !list.isEmpty()) {
            result = HibernateUtils.voListMapping(list, AchievementTemplateVo.class);
            dp.setDatas(result);
        }
        return dp;
    }
    
    /**
     * 根据城市编号查询成绩生效日期列表
     * @param cityId
     * @return
     */
    @Override
    public List<String> listTemplateEffectiveDateByCityId(String cityId) {
        List<String> result = null;
        List<AchievementTemplateVo> list = this.listAchievementTemplateVosByCityId(cityId);
        if (list != null && !list.isEmpty()) {
            result = new ArrayList<String>();
            for (AchievementTemplateVo vo : list) {
                result.add(vo.getEffectiveDate());
            }
        }
        return result;
    }

    /**
     * 根据城市编号计算成绩模板数
     */
    @Override
    public int countAchievementTemplatesByCityId(String cityId) {
        String hql = " select count(*) from AchievementTemplate where city.id = :cityId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("cityId", cityId);
        return achievementTemplateDao.findCountHql(hql, params);
    }

    /**
     * 新增或修改成绩模板
     */
    @Override
    public void saveFullAchievementTemplate(
            AchievementTemplateRequestVo achievementTemplateRequestVo) {
        User currentUser = userService.getCurrentLoginUser();
        if (achievementTemplateRequestVo.getGrades().size() < 12) {
            throw new ApplicationException("年级必须全选");
        }
        // 获取最新版本号 跟新，判断是否为最新日期
        Map<String, AchievementTemplate> templateMaps = this.getCurrentAndMaxVersionTemplate(achievementTemplateRequestVo.getCityId());
        AchievementTemplate template = HibernateUtils.voObjectMapping(achievementTemplateRequestVo, AchievementTemplate.class);
        if (achievementTemplateRequestVo.getId() != null && achievementTemplateRequestVo.getId() > 0) { //修改
            AchievementTemplate templateInDb = this.findAchievementTemplateById(achievementTemplateRequestVo.getId());
            templateInDb.checkVesion(achievementTemplateRequestVo.getVersion());
            if (templateInDb != null && templateInDb.getGrades() != null && templateInDb.getGrades().size() > 0) {
                // 对比删除学生成绩，学生成绩科目
                this.deleteStudentAchievementByTemplate(templateInDb, achievementTemplateRequestVo);
                // 删除成绩模板年级、科目
                achievementTemplateSubjectService.deleteAchievementTemplateSubjectByTemplateId(achievementTemplateRequestVo.getId());
                achievementTemplateGradeService.deleteAchievementTemplateGradeByTemplateId(achievementTemplateRequestVo.getId());
            }
            template.setAchievementVersion(templateInDb.getAchievementVersion());
            template.setIsCurrentVersion(templateInDb.getIsCurrentVersion());
            template.setIsDeleted(templateInDb.getIsDeleted());
        } else {
            AchievementTemplate maxVersionTemplate = templateMaps.get("MAX_VERSION");
            int version = 1;
            if (maxVersionTemplate != null) {
                version = maxVersionTemplate.getAchievementVersion() + 1;
            }
            template.setAchievementVersion(version);
            template.setIsDeleted(1);
            template.setCreateUser(currentUser);
        }
        if (templateMaps == null) {
            throw new ApplicationException("请先初始化成绩模板");
        }
        AchievementTemplate currentTemplate = templateMaps.get("CURRENT");
        // 有当前版本并且不是当前修改模板
        if (currentTemplate != null 
                && (achievementTemplateRequestVo.getId() == null 
                    || currentTemplate.getId().intValue() != achievementTemplateRequestVo.getId().intValue())) {
            // currentTemp_date <= edit_date 就设置为当前版本
            if (currentTemplate.getEffectiveDate() == null 
                    || DateTools.daysBetween(achievementTemplateRequestVo.getEffectiveDate(), currentTemplate.getEffectiveDate()) <= 0) {
                currentTemplate.setIsCurrentVersion(1);
                template.setIsCurrentVersion(0);
                achievementTemplateDao.merge(currentTemplate);
            } else {
                template.setIsCurrentVersion(1);
            }
        } else {
            if (achievementTemplateRequestVo.getId() != null 
                    && currentTemplate.getId().intValue() == achievementTemplateRequestVo.getId().intValue()) { // 修改当前版本模板，判断重置当前版本
                AchievementTemplate seetingTemplate = 
                        this.getSettingCurrentVersionTemplate(achievementTemplateRequestVo.getId(), achievementTemplateRequestVo.getCityId(), achievementTemplateRequestVo.getEffectiveDate());
                if (seetingTemplate != null) {
                    seetingTemplate.setIsCurrentVersion(0);
                    template.setIsCurrentVersion(1);
                    achievementTemplateDao.merge(seetingTemplate);
                }
            }
        }
        template.setGrades(null);
        template.setCity(new Region(achievementTemplateRequestVo.getCityId()));
        template.setModifyUser(currentUser);
        if (template.getId() != null && template.getId() >= 0) {
            achievementTemplateDao.merge(template);
        } else {
            achievementTemplateDao.save(template);
        }
        for (AchievementTemplateGradeRequestVo gradeVo :achievementTemplateRequestVo.getGrades()) {
            AchievementTemplateGrade grade = HibernateUtils.voObjectMapping(gradeVo, AchievementTemplateGrade.class);
            if (grade.getId() == null || grade.getId() <= 0) {
                grade.setCreateUser(currentUser);
            }
            grade.setGrade(new DataDict(gradeVo.getGradeId()));
            if (StringUtil.isBlank(gradeVo.getAchievementTypeList())) {
                throw new ApplicationException("年级对应成绩类型至少关联一种");
            }
            grade.setAchievementTemplate(template);
            // 科目数量
            int subjectNum = gradeVo.getSubjects().size();
            // 重置学生成绩模板科目数量 
            studentAchievementService.updateTemplateSubjectNumByTemplate(template.getId(), gradeVo.getGradeId(), subjectNum);
            grade.setSubjectNum(subjectNum);
            grade.setSubjects(null);
            // 新增或修改成绩模板年级
            achievementTemplateGradeService.saveOrUpdateAchievementTemplateGrade(grade);
            if (subjectNum <= 0) {
                throw new ApplicationException("年级对应科目不能为空");
            }
            for (AchievementTemplateSubjectRequestVo subjectVo : gradeVo.getSubjects()) {
                AchievementTemplateSubject subject = HibernateUtils.voObjectMapping(subjectVo, AchievementTemplateSubject.class);
                if (subjectVo.getTotalScore() == null || subjectVo.getTotalScore() <= 0) {
                    throw new ApplicationException("科目的总分不能为空");
                }
                if (subject.getId() == null || subject.getId() <= 0) {
                    subject.setCreateUser(currentUser);
                }
                subject.setSubject(new DataDict(subjectVo.getSubjectId()));
                subject.setModifyUser(currentUser);
                subject.setTemplateGrade(grade);
                // 更新模板科目总分联动更新学生成绩科目的满分
                studentAchievementSubjectService.updateStudentAchievementSubjectTotalScore(subject);
                // 新增或修改成绩模板科目
                achievementTemplateSubjectService.saveOrUpdateAchievementTemplateSubject(subject);
            }
            
        }
    }
    
    /**
     * 获取将成为当前版本的成绩模板
     * @param templateId
     * @param effectiveDate
     * @return
     */
    private AchievementTemplate getSettingCurrentVersionTemplate(Integer templateId, String cityId, String effectiveDate) {
        Map<String, Object> params = Maps.newHashMap();
        AchievementTemplate result = null;
        String hql = " from AchievementTemplate where city.id = :cityId and id != :templateId ";
        if (StringUtil.isNotBlank(effectiveDate)) {
            hql += " and effectiveDate > :effectiveDate ";
            params.put("effectiveDate", effectiveDate);
        }        
        hql += " order by effectiveDate desc, createTime desc ";
        params.put("cityId", cityId);
        params.put("templateId", templateId);
        List<AchievementTemplate> list = achievementTemplateDao.findAllByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        } else {
            if (StringUtil.isBlank(effectiveDate)) { // 删除,重置v0版本为当前有效
                String v0Hql = " from AchievementTemplate where city.id = :cityId and achievementVersion = 0 ";
                Map<String, Object> v0Params = Maps.newHashMap();
                v0Params.put("cityId", cityId);
                List<AchievementTemplate> v0List = achievementTemplateDao.findAllByHQL(v0Hql, v0Params);
                if (v0List != null && !v0List.isEmpty()) {
                    result = v0List.get(0);
                }
            }
        }
        return result; 
    }
    
    /**
     * 获取当前和最大版本
     * @param cityId
     * @return
     */
    private Map<String, AchievementTemplate> getCurrentAndMaxVersionTemplate(String cityId) {
        Map<String, AchievementTemplate> result = null;
        List<AchievementTemplate> list = this.listAchievementTemplatesByCityId(cityId);
        if (list != null && list.size() > 0) {
            result = Maps.newHashMap();
            result.put("MAX_VERSION", list.get(0));
            for (AchievementTemplate template : list) {
                if (template.getIsCurrentVersion() == 0) {
                    result.put("CURRENT", template);
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * 对比删除学生成绩，学生成绩科目
     * @param template
     * @param achievementTemplateRequestVo
     */
    private void deleteStudentAchievementByTemplate(AchievementTemplate template, AchievementTemplateRequestVo achievementTemplateRequestVo) {
        Map<String, AchievementTemplateGradeRequestVo> gradeMap = this.transferGradeSetToMap(achievementTemplateRequestVo.getGrades());
        for (AchievementTemplateGrade grade : template.getGrades()) {
            if (grade != null && grade.getSubjects() != null && grade.getSubjects().size() > 0) {
                AchievementTemplateGradeRequestVo gradeVo = gradeMap.get(grade.getGrade().getId());
                if (gradeVo.getAchievementTypeList().split(",").length < 2 
                        && !gradeVo.getAchievementTypeList().equals(grade.getAchievementTypeList())) { // 成绩类型发生变更
                    String deleteType = "";
                    if (grade.getAchievementTypeList().contains(gradeVo.getAchievementTypeList())) { // 成绩类型减少了
                        deleteType = grade.getAchievementTypeList().replace(gradeVo.getAchievementTypeList(), "");
                    } else {
                        deleteType = grade.getAchievementTypeList();
                    }
                    deleteType = deleteType.replaceAll(",", "");
                    // 删除grade, deleteType的学生成绩
                    studentAchievementService.deleteStudentAchievementByTemplate(template.getId(), gradeVo.getGradeId(), AchievementAbstractType.valueOf(deleteType));
                }  else {
                    Map<String, AchievementTemplateSubjectRequestVo> subjectMap = this.transferSubjectSetToMap(gradeVo.getSubjects());
                    for (AchievementTemplateSubject subject : grade.getSubjects()) {
                        if (!subjectMap.containsKey(subject.getSubject().getId())) { // 科目被删除了
                            // 删除grade, subject的学生成绩科目
                            studentAchievementSubjectService.deleteStudentAchievementSubjectByTemplate(template.getId(), gradeVo.getGradeId(), subject.getSubject().getId());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 把成绩模板年级set转换成map
     * @param grades
     * @return
     */
    private Map<String, AchievementTemplateGradeRequestVo> transferGradeSetToMap(Set<AchievementTemplateGradeRequestVo> grades) {
        Map<String, AchievementTemplateGradeRequestVo> gradeMap = Maps.newHashMap();
        for (AchievementTemplateGradeRequestVo grade : grades) {
            gradeMap.put(grade.getGradeId(), grade);
        }
        return gradeMap;
    }
    
    /**
     * 把成绩模板科目set转换成map
     * @param grades
     * @return
     */
    private Map<String, AchievementTemplateSubjectRequestVo> transferSubjectSetToMap(Set<AchievementTemplateSubjectRequestVo> subjects) {
        Map<String, AchievementTemplateSubjectRequestVo> subjectMap = Maps.newHashMap();
        for (AchievementTemplateSubjectRequestVo subject : subjects) {
            subjectMap.put(subject.getSubjectId(), subject);
        }
        return subjectMap;
    }

    /**
     * 根据编号查询成绩模板
     */
    @Override
    public AchievementTemplate findAchievementTemplateById(Integer id) {
        return achievementTemplateDao.findById(id);
    }

    /**
     * 分页查询成绩模板
     */
    @Override
    @SuppressWarnings("unchecked")
    public DataPackage findPageAchievementTemplate(DataPackage dp, String cityId) {
        Map<String, Object> params = Maps.newHashMap();
        /*User currentUser = userService.getCurrentLoginUser();
        List<Organization> orgList = currentUser.getOrganization();
        boolean isGroup = false;
        Set<Integer> cityIds = new HashSet<Integer>();
        for (Organization org : orgList) {
            if (org.getOrgType() == OrganizationType.GROUNP) {
                isGroup = true;
                break;
            }
        }*/
        String hql = " from AchievementTemplate where isDeleted = 1 and isCurrentVersion = 0 ";
        if (StringUtil.isNotBlank(cityId)) {
            hql += " and city.id = :cityId ";
            params.put("cityId", cityId);
        }
        hql += " order by createTime desc ";
        dp = achievementTemplateDao.findPageByHQL(hql, dp, true, params);
        List<AchievementTemplate> list = (List<AchievementTemplate>) dp.getDatas();
        List<AchievementTemplateVo> voList = HibernateUtils.voListMapping(list, AchievementTemplateVo.class);
        dp.setDatas(voList);
        return dp;
    }

    /**
     * 根据模板编号删除模板
     */
    @Override
    public void deleteAchievementTemplateById(Integer templateId) {
        AchievementTemplate template = this.findAchievementTemplateById(templateId);
        if (template.getIsCurrentVersion() == 0) {
            AchievementTemplate seetingTemplate = 
                    this.getSettingCurrentVersionTemplate(templateId, template.getCity().getId(), null);
            if (seetingTemplate != null) {
                seetingTemplate.setIsCurrentVersion(0);
                template.setIsCurrentVersion(1);
                achievementTemplateDao.merge(seetingTemplate);
            }
        }
        template.setIsDeleted(0); //逻辑删除
        achievementTemplateDao.merge(template);
    }
    
    /**
     * 根据学生成绩查询最符合的模板年级
     */
    @Override
    public AchievementTemplateGradeVo findMostConsistentTemplateGrade(
            AchievementTemplateGradeSearVo searchVo) {
        AchievementTemplateGradeVo result = null;
        AchievementTemplate achievementTemplate = this.
                findMostConsistentTemplateGrade(searchVo.getCityId(), searchVo.getExaminationDate());
        if (achievementTemplate != null) {
            AchievementTemplateGrade grade = achievementTemplateGradeService
                    .findAchievementTemplateGradeByTemplateAndGrade(achievementTemplate.getId(), searchVo.getGradeId());
            result = HibernateUtils.voObjectMapping(grade, AchievementTemplateGradeVo.class);
            Set<AchievementTemplateSubject> subjects = grade.getSubjects();
            List<AchievementTemplateSubjectVo> subjectVos = new ArrayList<AchievementTemplateSubjectVo>();
            for (AchievementTemplateSubject subejct : subjects) {
                subjectVos.add(HibernateUtils.voObjectMapping(subejct, AchievementTemplateSubjectVo.class));
            }
            result.setSubjects(subjectVos);
        }
        return result;
    }

    /**
     * 查询最符合的成绩模板
     */
    @Override
    public AchievementTemplate findMostConsistentTemplateGrade(String cityId,
            String examinationDate) {
        AchievementTemplate result = null;
        Map<String, Object> params = Maps.newHashMap();
        params.put("cityId", cityId);
        params.put("examinationDate", examinationDate);
        String hql = " from AchievementTemplate where city.id = :cityId and effectiveDate <= :examinationDate "
                + " and isDeleted = 1 order by effectiveDate desc ";
        List<AchievementTemplate> list = achievementTemplateDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    /**
     * 根据模板，年级，学生成绩科目，成绩类型判断学生成绩类别
     */
    @Override
    public AchievementCategory judgeAchievementCategory(
            Integer achievementTemplateId, String gradeId,
            StudentAchievementSubject subject, AchievementType achievementType) {
        if (achievementType == AchievementType.SCORE) {
            BigDecimal score = subject.getScore();
            AchievementTemplateSubject templateSubject = achievementTemplateSubjectService
                    .findAchievementTemplateSubjectByTemplateAndGradeSubject(achievementTemplateId, gradeId, subject.getSubject().getId());
            if (templateSubject != null) {
                Integer totalScore = templateSubject.getTotalScore();
                return achievementCategoryService.judgeAchievementCategoryByScore(totalScore, score);
            }
        } else if (achievementType == AchievementType.CLASS_RANKING) {
            Integer classRanking = subject.getClassRanking();
            return achievementCategoryService.judgeAchievementCategoryByRanking(classRanking, AchievementType.CLASS_RANKING);
        } else if (achievementType == AchievementType.GRADE_RANKING) {
            Integer gradeRanking = subject.getGradeRanking();
            return achievementCategoryService.judgeAchievementCategoryByRanking(gradeRanking, AchievementType.GRADE_RANKING);
        } else {
            EvaluationType evaluationType = subject.getEvaluationType();
            return achievementCategoryService.judgeAchievementCategoryByEvaluationType(evaluationType);
        }
        return null;
    }

}
