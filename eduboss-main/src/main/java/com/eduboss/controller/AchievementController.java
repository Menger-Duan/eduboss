package com.eduboss.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.AchievementCategoryVo;
import com.eduboss.domainVo.AchievementProStandardVo;
import com.eduboss.domainVo.AchievementTemplateGradeVo;
import com.eduboss.domainVo.AchievementTemplateVo;
import com.eduboss.dto.AchievementCategoryRequestVo;
import com.eduboss.dto.AchievementProStandardRequestVo;
import com.eduboss.dto.AchievementTemplateGradeSearVo;
import com.eduboss.dto.AchievementTemplateRequestVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.AchievementCategoryService;
import com.eduboss.service.AchievementProStandardService;
import com.eduboss.service.AchievementTemplateGradeService;
import com.eduboss.service.AchievementTemplateService;

/**
 * 成绩模板模块controller
 * @author arvin
 *
 */
@Controller
@RequestMapping(value="/AchievementController")
public class AchievementController {
    
    @Autowired
    private AchievementCategoryService achievementCategoryService;
    
    @Autowired
    private AchievementProStandardService achievementProStandardService;
    
    @Autowired
    private AchievementTemplateService achievementTemplateService;
    
    @Autowired
    private AchievementTemplateGradeService achievementTemplateGradeService;

    /**
     * 获取成绩类别列表
     * @return
     */
    @RequestMapping(value="/listAllAchievementCategory")
    @ResponseBody
    public List<AchievementCategoryVo> listAllAchievementCategory() {
        return achievementCategoryService.listAllAchievementCategory();
    }
    
    /**
     * 修改成绩类别
     * @param achivementCategoryEditVo
     * @return
     */
    @RequestMapping(value="/updateAchievementCategory")
    @ResponseBody
    public Response updateAchievementCategory(@Valid @RequestBody AchievementCategoryRequestVo achievementCategoryRequestVo) {
        achievementCategoryService.updateAchievementCategory(achievementCategoryRequestVo);
        return new Response();
    }
    
    /**
     * 获取成绩进步标准列表
     * @return
     */
    @RequestMapping(value="/listAllAchievementProStandard")
    @ResponseBody
    public List<AchievementProStandardVo> listAllAchievementProStandard() {
        return achievementProStandardService.listAllAchievementProStandard();
    }
    
    /**
     * 修改成绩进步标准
     * @param achivementCategoryEditVo
     * @return
     */
    @RequestMapping(value="/updateAchievementProStandard")
    @ResponseBody
    public Response updateAchievementProStandard(@Valid @RequestBody AchievementProStandardRequestVo achievementProStandardRequestVo) {
        achievementProStandardService.updateAchievementProStandard(achievementProStandardRequestVo);
        return new Response();
    }
    
    /**
     * 根据省份编号获取已排成绩模板的城市编号列表
     * @return
     */
    @RequestMapping(value="/listTemplateCityIdsByProvinceId")
    @ResponseBody
    public List<String> listTemplateCityIdsByProvinceId(@RequestParam(required=true)String provinceId) {
        return achievementTemplateService.listTemplateCityIdsByProvinceId(provinceId);
    }
    
    /**
     * 根据城市编号初始化成绩模板
     * @param cityIds 用逗号隔开
     * @return
     */
    @RequestMapping(value="/initAchievementTemplateByCitys")
    @ResponseBody
    public Response initAchievementTemplateByCitys(@RequestParam(required=true) String cityIds) {
        achievementTemplateService.createAchievementTemplateByCitys(cityIds);
        return new Response();
    }
    
    /**
     * 新增或修改成绩模板
     * @param achievementTemplateGradeRequestVo
     * @return
     */
    @RequestMapping(value="/saveFullAchievementTemplate")
    @ResponseBody
    public Response saveFullAchievementTemplate(@Valid @RequestBody AchievementTemplateRequestVo achievementTemplateRequestVo) {
        achievementTemplateService.saveFullAchievementTemplate(achievementTemplateRequestVo);
        return new Response();
    }
    
    /**
     * 分页查询成绩模板
     * @param gridRequest
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/findPageAchievementTemplate")
    @ResponseBody
    public DataPackageForJqGrid findPageAchievementTemplate(@ModelAttribute GridRequest gridRequest, String cityId) {
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage = achievementTemplateService.findPageAchievementTemplate(dataPackage, cityId);
        DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
        return dataPackageForJqGrid ;
    }
    
    /**
     * 根据城市编号查询成绩模板Vo列表
     * @param gridRequest
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/listAchievementTemplateByCityId")
    @ResponseBody
    public List<AchievementTemplateVo> listAchievementTemplateByCityId(@RequestParam(required=true) String cityId) {
        return achievementTemplateService.listAchievementTemplateVosByCityId(cityId);
    }
    
    /**
     * 根据城市编号分页查询成绩模板Vo列表
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/findPageAchievementTemplateByCityId")
    @ResponseBody
    public DataPackageForJqGrid findPageAchievementTemplateByCityId(@ModelAttribute GridRequest gridRequest, 
            @RequestParam(required=true) String cityId) {
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage = achievementTemplateService.findPageAchievementTemplateByCityId(dataPackage, cityId);
        DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
        return dataPackageForJqGrid ;
    }
    
    /**
     * 根据城市编号查询成绩生效日期列表
     * @param gridRequest
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/listTemplateEffectiveDateByCityId")
    @ResponseBody
    public List<String> listTemplateEffectiveDateByCityId(@RequestParam(required=true) String cityId) {
        return achievementTemplateService.listTemplateEffectiveDateByCityId(cityId);
    }
    
    /**
     * 根据模板编号删除模板
     * @param templateId
     * @return
     */
    @RequestMapping(value="/deleteAchievementTemplateById")
    @ResponseBody
    public Response deleteAchievementTemplateById(@RequestParam(required=true) Integer templateId) {
        achievementTemplateService.deleteAchievementTemplateById(templateId);
        return new Response();
    }
    
    /**
     * 根据模板编号查询所有成绩模板年级
     * @param templateId
     * @return
     */
    @RequestMapping(value="/listAllTemplateGradeByTemplateId")
    @ResponseBody
    public List<AchievementTemplateGradeVo> listAllTemplateGradeByTemplateId(@RequestParam(required=true) Integer templateId) {
        return achievementTemplateGradeService.listAllTemplateGradeByTemplateId(templateId);
    }
    
    /**
     * 根据学生成绩查询最符合的模板年级
     * @param searchVo
     * @return
     */
    @RequestMapping(value="/findMostConsistentTemplateGrade")
    @ResponseBody
    public AchievementTemplateGradeVo findMostConsistentTemplateGrade(@Valid AchievementTemplateGradeSearVo searchVo) {
        return achievementTemplateService.findMostConsistentTemplateGrade(searchVo);
    }
    
}
