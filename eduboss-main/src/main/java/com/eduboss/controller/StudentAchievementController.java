package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.DataDict;
import com.eduboss.domainVo.AchievementBenchmarkVo;
import com.eduboss.domainVo.AchievementComparisonDetailVo;
import com.eduboss.domainVo.AchievementComparisonVo;
import com.eduboss.domainVo.StudentAchievementSubjectVo;
import com.eduboss.domainVo.StudentAchievementVo;
import com.eduboss.domainVo.StudentCityGradeVo;
import com.eduboss.dto.AchievementComparisonRequestVo;
import com.eduboss.dto.AchievementComparisonSearchVo;
import com.eduboss.dto.AchievementComparisonUpdateVo;
import com.eduboss.dto.BenchmarkSubjectSearchVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.dto.StudentAchievementRequestVo;
import com.eduboss.dto.StudentAchievementSearchVo;
import com.eduboss.service.AchievementBenchmarkService;
import com.eduboss.service.AchievementComparisonService;
import com.eduboss.service.CourseService;
import com.eduboss.service.StudentAchievementService;
import com.eduboss.service.StudentAchievementSubjectService;
import com.eduboss.service.StudentService;

/**
 * 学生成绩模块controller
 * @author arvin
 *
 */
@Controller
@RequestMapping(value="/StudentAchievementController")
public class StudentAchievementController {
    
    @Autowired
    private StudentAchievementService studentAchievementService;
    
    @Autowired
    private StudentAchievementSubjectService studentAchievementSubjectService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AchievementComparisonService achievementComparisonService;
    
    @Autowired
    private AchievementBenchmarkService achievementBenchmarkService;
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 检查是否影响到学生成绩基准科目
     * @param studentAchievementRequestVo
     * @return
     */
    @RequestMapping(value="/checkHashRelatedAchievementBenchmark")
    @ResponseBody
    public boolean checkHashRelatedAchievementBenchmark(@Valid @RequestBody StudentAchievementRequestVo studentAchievementRequestVo) {
        return studentAchievementService.checkHashRelatedAchievementBenchmark(studentAchievementRequestVo);
    }
    
    /**
     * 新增或修改学生成绩
     * @param studentAchievementRequestVo
     * @return
     */
    @RequestMapping(value="/saveFullStudentAchievement")
    @ResponseBody
    public Response saveFullStudentAchievement(@Valid @RequestBody StudentAchievementRequestVo studentAchievementRequestVo) {
        studentAchievementService.saveFullStudentAchievement(studentAchievementRequestVo);
        return new Response();
    }
    
   /**
    * 根据成绩编号删除成绩图片凭证
    * @param achievementId
    * @return
    */
   @RequestMapping(value="/deleteStudentAchievementEvidence")
   @ResponseBody
   public Response deleteStudentAchievementEvidence(@RequestParam(required=true)Integer achievementId) {
       studentAchievementService.deleteStudentAchievementEvidence(achievementId);
       return new Response();
   }
    
    /**
     * 分页查询学生成绩
     * @param gridRequest
     * @param studentAchievementSearchVo
     * @return
     */
    @RequestMapping(value="/findPageStudentAchievement")
    @ResponseBody
    public DataPackageForJqGrid findPageStudentAchievement(@ModelAttribute GridRequest gridRequest, 
            @Valid @ModelAttribute StudentAchievementSearchVo studentAchievementSearchVo) {
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage = studentAchievementService.findPageStudentAchievement(dataPackage, studentAchievementSearchVo);
        DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
        return dataPackageForJqGrid ;
    }
    
    /**
     * 根据学生成绩编号查询学生成绩
     * @param studentAchievementId
     * @return
     */
    @RequestMapping(value="/findStudentAchievementById")
    @ResponseBody
    public StudentAchievementVo findStudentAchievementById(Integer studentAchievementId) {
        return studentAchievementService.findStudentAchievementVoById(studentAchievementId);
    }
    
    /**
     * 根据学生成绩编号查询所有成绩科目
     * @param achievementId
     * @return
     */
    @RequestMapping(value="/listAllAchievementSubjectByAchievementId")
    @ResponseBody
    public List<StudentAchievementSubjectVo> listAllAchievementSubjectByAchievementId
        (@RequestParam(required=true)Integer achievementId) {
        return studentAchievementSubjectService.listAllAchievementSubjectByAchievementId(achievementId);
    }
    
    /**
     * 根据学生成绩编号删除学生成绩
     * @param achievementId
     * @return
     */
    @RequestMapping(value="/deleteStudentAchievementByAchievementId")
    @ResponseBody
    public Response deleteStudentAchievementByAchievementId(@RequestParam(required=true)Integer achievementId) {
        studentAchievementService.deleteStudentAchievementByAchievementId(achievementId);
        return new Response();
    }
    
    /**
     * 根据学生编号查询学生成绩select
     * @param studentId
     * @return
     */
    @RequestMapping(value="/getStudentAchievementSelectByStudentId")
    @ResponseBody
    public List<Map<String, String>>  getStudentAchievementSelectByStudentId(@RequestParam(required=true)String studentId) {
        return studentAchievementService.getStudentAchievementSelectByStudentId(studentId);
    }
    
    /**
     * 根据学生成绩基准查询学生在读一对一科目
     * @param benchmarkSubjectSearchVo
     * @return
     */
    @RequestMapping(value="/getSubjectsByBenchmark")
    @ResponseBody
    public NameValue getSubjectsByBenchmark(@Valid BenchmarkSubjectSearchVo benchmarkSubjectSearchVo) {
        return courseService.getSubjectsByBenchmark(benchmarkSubjectSearchVo);
    }
    
    /**
     * 保存成绩对比记录
     * @param achievementComparisonRequestVo
     * @return
     */
    @RequestMapping(value="/saveFullAchievementComparison")
    @ResponseBody
    public Response saveFullAchievementComparison(@Valid @RequestBody AchievementComparisonRequestVo achievementComparisonRequestVo) {
        Response response = new Response();
        response.setData(achievementComparisonService.saveFullAchievementComparison(achievementComparisonRequestVo));
        return response;
    }
    
    /**
     * 初始化成绩对比记录
     * @return
     */
    @RequestMapping(value="/initAchievementComparisons")
    @ResponseBody
    public Response initAchievementComparisons() {
        Response response = new Response();
        achievementComparisonService.initAchievementComparisons();
        return response;
    }
    
    /**
     * 根据学生编号初始化成绩对比记录
     * @param studentId
     * @return
     */
    @RequestMapping(value="/initAchievementComparisonByStudentId")
    @ResponseBody
    public Response initAchievementComparisonByStudentId(String studentId) {
        Response response = new Response();
        achievementComparisonService.initAchievementComparisonByStudentId(studentId);
        return response;
    }
    
    /**
     * 根据学生编号查询学生分公司城市和年级
     * @param studentId
     * @return
     */
    @RequestMapping(value="/findStudentCityGradeVoByStudentId")
    @ResponseBody
    public StudentCityGradeVo findStudentCityGradeVoByStudentId(@RequestParam(required=true)String studentId) {
        return studentService.findStudentCityGradeVoByStudentId(studentId);
    }
    
    /**
     * 根据成绩对比记录编号查询成绩对比详情vo
     * @param comparisonId
     * @return
     */
    @RequestMapping(value="/findAchievementComparisonDetailById")
    @ResponseBody
    public AchievementComparisonDetailVo findAchievementComparisonDetailById(@RequestParam(required=true)Integer comparisonId) {
        return achievementComparisonService.findAchievementComparisonDetailById(comparisonId);
    }
    
    /**
     * 根据学生编号查询成绩对比记录列表
     * @param achievementComparisonSearchVo
     * @return
     */
    @RequestMapping(value="/listAllAchievementComparisonByStudentId")
    @ResponseBody
    public List<AchievementComparisonVo> listAllAchievementComparisonByStudentId(@Valid AchievementComparisonSearchVo achievementComparisonSearchVo) {
        return achievementComparisonService.listAchievementComparisonForSearch(achievementComparisonSearchVo);
    };
    
    /**
     * 根据成绩对比编号查询学生基准科目列表
     * @param comparisonId
     * @return
     */
    @RequestMapping(value="/listAllAchievementBenchmarkByComparisonId")
    @ResponseBody
    public List<AchievementBenchmarkVo> listAllAchievementBenchmarkByComparisonId(@RequestParam(required=true)Integer comparisonId) {
        return achievementBenchmarkService.listAllAchievementBenchmarkByComparisonId(comparisonId);
    }
    
    /**
     * 进步判断更改
     * @param achievementComparisonUpdateVo
     * @return
     */
    @RequestMapping(value="/updateStandardCategoryForAchievementBenchmark")
    @ResponseBody
    public Response updateStandardCategoryForAchievementBenchmark(@Valid @RequestBody AchievementComparisonUpdateVo achievementComparisonUpdateVo) {
        achievementBenchmarkService.updateStandardCategoryForAchievementBenchmark(achievementComparisonUpdateVo);
        return new Response();
    }
    
    /**
     * 根据学生编号查询最近的成绩对比详情vo
     * @param studentId
     * @return
     */
    @RequestMapping(value="/findLastAchievementComparisonDetail")
    @ResponseBody
    public AchievementComparisonDetailVo findLastAchievementComparisonDetail(@RequestParam(required=true)String studentId) {
        return achievementComparisonService.findLastAchievementComparisonDetail(studentId);
    }
    
}
