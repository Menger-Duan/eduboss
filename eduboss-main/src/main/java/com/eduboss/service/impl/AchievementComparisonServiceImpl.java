package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.AchievementType;
import com.eduboss.common.BenchmarkType;
import com.eduboss.common.Semester;
import com.eduboss.common.StandardCategory;
import com.eduboss.dao.AchievementComparisonDao;
import com.eduboss.domain.AchievementBenchmark;
import com.eduboss.domain.AchievementBenchmarkLog;
import com.eduboss.domain.AchievementComparison;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentAchievement;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AchievementBenchmarkDetailVo;
import com.eduboss.domainVo.AchievementComparisonDetailVo;
import com.eduboss.domainVo.AchievementComparisonVo;
import com.eduboss.dto.AchievementBenchmarkRequestVo;
import com.eduboss.dto.AchievementComparisonRequestVo;
import com.eduboss.dto.AchievementComparisonSearchVo;
import com.eduboss.dto.BenchmarkSubjectSearchVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AchievementBenchmarkLogService;
import com.eduboss.service.AchievementBenchmarkService;
import com.eduboss.service.AchievementComparisonService;
import com.eduboss.service.CourseService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.StudentAchievementService;
import com.eduboss.service.StudentAchievementSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.task.AchievmentAsyncTask;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * 成绩对比记录serviceImpl
 * @author arvin
 *
 */
@Service("AchievementComparisonService")
public class AchievementComparisonServiceImpl implements AchievementComparisonService {
    
    private final static Logger logger =Logger.getLogger(AchievementComparisonServiceImpl.class);
    
    @Autowired
    private AchievmentAsyncTask achievmentAsyncTask;
    
    @Autowired
    private AchievementBenchmarkService achievementBenchmarkService;
    
    @Autowired
    private StudentAchievementService studentAchievementService;
    
    @Autowired
    private StudentAchievementSubjectService studentAchievementSubjectService;
    
    @Autowired
    private AchievementBenchmarkLogService achievementBenchmarkLogService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private AchievementComparisonDao achievementComparisonDao;
    
    /**
     * 保存成绩对比记录
     */
    @Override
    public Integer saveFullAchievementComparison(
            AchievementComparisonRequestVo achievementComparisonRequestVo) {
        logger.info("AchievementComparisonRequestVo:" + achievementComparisonRequestVo);
        // 检查唯一性
        this.checkUniqueAchievementComparison(achievementComparisonRequestVo.getId(), achievementComparisonRequestVo.getStudentId(), 
                achievementComparisonRequestVo.getSchoolYearId(), achievementComparisonRequestVo.getSemester());
        // 检查基准成绩的考试日期不能比对比成绩的考试日期大
        this.checkAchievementBenchmarkComparison(achievementComparisonRequestVo);
        User currentUser = userService.getCurrentLoginUser();
        if (currentUser == null) {
            currentUser = new User("112233");
        }
        AchievementComparison achievementComparison = null;
        String createDate = null;
        if (achievementComparisonRequestVo.getId() != null) {
            achievementComparison = achievementComparisonDao.findById(achievementComparisonRequestVo.getId());
            if (achievementComparison == null) {
                throw new ApplicationException("编号" + achievementComparisonRequestVo.getId() + "查找不到成绩对比记录");
            }
            createDate = DateTools.getDateToString(achievementComparison.getCreateTime());
            achievementComparison.checkVesion(achievementComparisonRequestVo.getVersion());
        }
        achievementComparison = HibernateUtils.voObjectMapping(achievementComparisonRequestVo, AchievementComparison.class);
        achievementComparison.setStudent(new Student(achievementComparisonRequestVo.getStudentId()));
        achievementComparison.setSchoolYear(new DataDict(achievementComparisonRequestVo.getSchoolYearId()));
        achievementComparison.setComparativeAchievement(new StudentAchievement(achievementComparisonRequestVo.getComparativeAchievementId()));
        achievementComparison.setModifyUser(currentUser);
        achievementComparison.setBenchmarks(null);
        achievementComparison.setIsDeleted(1);
        if (achievementComparisonRequestVo.getId() != null) {
            achievementComparisonDao.merge(achievementComparison);
            achievementBenchmarkService.deleteAchievementBenchmarkByComparisonId(achievementComparisonRequestVo.getId());
        } else {
            achievementComparison.setCreateUser(currentUser);
            achievementComparisonDao.save(achievementComparison);
        }
        this.validAchievementBenchmarkRequestVo(achievementComparisonRequestVo.getBenchmarks());
        for (AchievementBenchmarkRequestVo benchmarkVo: achievementComparisonRequestVo.getBenchmarks()) {
            AchievementBenchmark benchmark = HibernateUtils.voObjectMapping(benchmarkVo, AchievementBenchmark.class);
            benchmark.setId(null);
            benchmark.setAchievementComparison(achievementComparison);
            benchmark.setStudentAchievement(new StudentAchievement(benchmarkVo.getStudentAchievementId()));
            benchmark.setBenchmarkSubject(new StudentAchievementSubject(benchmarkVo.getStudentAchievementSubjectId()));
            benchmark.setBenchmarkAchievementType(benchmarkVo.getAchievementType());
            benchmark.setCreateUser(currentUser);
            benchmark.setModifyUser(currentUser);
            // 获取学生成绩对比科目
            StudentAchievementSubject benchmarkSubject = studentAchievementSubjectService.findStudentAchievementSubjectById(benchmarkVo.getStudentAchievementSubjectId());
            StudentAchievementSubject compareSubject = studentAchievementSubjectService
                    .findStudentAchievementSubjectByStuAchIdAndSubjectId(achievementComparisonRequestVo.getComparativeAchievementId(), benchmarkSubject.getSubject().getId());
            benchmark.setCompareSubject(compareSubject);
            Integer compareSubjectId = compareSubject != null ? compareSubject.getId() : null;
            // 根据学生成绩基准科目，对比科目，成绩类型设置基准、对比成绩类别
            studentAchievementService.setAchievementBenchmarkAchievementCategory(benchmark, benchmarkVo.getStudentAchievementSubjectId(), 
            		compareSubjectId, benchmarkVo.getAchievementType());
            // 根据学生成绩基准科目，对比科目，成绩类型来判断进步状态
            StandardCategory standardCategory = null;
            BenchmarkType benchmarkType = null;
            if (benchmark.getCompareAchievementType() != null && benchmark.getCompareCategory() != null) {
                standardCategory = studentAchievementService
                        .judgeStandardCategory(benchmarkVo.getStudentAchievementSubjectId(), benchmarkVo.getAchievementType(), benchmark.getBenchmarkCategory(),
                                compareSubject.getId(), benchmark.getCompareAchievementType(), benchmark.getCompareCategory());
                // 判断基准判定类型
                Map<String, Object> meetMap = courseService.checkCourseMeetConditions(benchmarkSubject.getStudentAchievement().getStudent().getId(), 
                        benchmarkSubject.getSubject().getId(), 24, 30, createDate);
                if ((Boolean)meetMap.get("status")) { // 满足录入前30天内本科目一对一课消>0，课消>=24课时
                    benchmarkType = BenchmarkType.EFFECTIVE;
                } else {
                    benchmarkType = BenchmarkType.RECORD_ONLY;
                    benchmark.setReadOnlyDesc(meetMap.get("readOnlyDecs").toString());
                }
            } else {
                benchmarkType = BenchmarkType.INVALID;
            }
            benchmark.setStandardCategory(standardCategory);
            
            benchmark.setBenchmarkType(benchmarkType);
            achievementBenchmarkService.saveOrUpdateAchievementBenchmark(benchmark);
            AchievementBenchmarkLog log = new AchievementBenchmarkLog();
            log.setAchievementBenchmarkId(benchmark.getId());
            log.setStandardCategoryTar(standardCategory);
            log.setCreateUser(currentUser);
            log.setModifyUser(currentUser);
            achievementBenchmarkLogService.saveOrUpdateAchievementBenchmarkLog(log);
        }
        return achievementComparison.getId();
    }
    
    /**
     * 初始化成绩对比记录
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initAchievementComparisons() {
        int pageNo = 0;
        int pageSize = 1;
        DataPackage dp = new DataPackage(pageNo, pageSize);
        dp = this.findPageStudentForInitAchivementComparisions(dp);
        int size = dp.getRowCount();
        int times = 20;
        pageSize = size/times == 0L ? (int) size : (int) size/times;
        long current = 0L;
        List<Future<String>> taskResults = new ArrayList<Future<String>>();
        while (current < size) {
            dp.setPageNo(pageNo);
            dp.setPageSize(pageSize);
            dp = this.findPageStudentForInitAchivementComparisions(dp);
            List<Map<String, String>> list = (List<Map<String, String>>) dp.getDatas();
            logger.info("initAchievementComparisons studentIds:" + list.toString());
            taskResults.add(achievmentAsyncTask.initAchievementComparisons(list));
            pageNo++;
            current += pageSize;
        }
        if (taskResults != null && !taskResults.isEmpty()) {
            this.checkAllDone(taskResults, "initAchievementComparisons");
        }
        this.deleteAchievementComparisonWithoutBenchmark();
    }
    
    /**
     * 删除没有基准成绩的成绩对比记录
     */
    private void deleteAchievementComparisonWithoutBenchmark() {
        String sql = " delete from achievement_comparison where id not in (select distinct achievement_comparison_id from achievement_benchmark)  ";
        achievementComparisonDao.excuteSql(sql, null);
    }
    
    /**
     * 根据学生编号初始化成绩对比记录
     */
    public void initAchievementComparisonByStudentId(String studentId) {
        // 【学年】固定为“2017-2018”，【学期】固定为“上学期”
        DataDict examinationType = dataDictService.findDataDictByNameAndCateGory("基准成绩", "EXAMINATION_TYPE");
        DataDict schoolYear = dataDictService.findDataDictByNameAndCateGory("2017-2018", "SCHOOL_YEAR");
        if (examinationType == null || schoolYear == null) {
            throw new ApplicationException("没设置考试类型：基准成绩,或学年：2017-2018");
        }
        String schoolYearId = schoolYear.getId();
        List<StudentAchievement> benchmarkList = studentAchievementService.listStudentAchievement(studentId, examinationType.getId());
        List<StudentAchievement> compareList = studentAchievementService.listStudentAchievement(studentId, null);
        // 【基准成绩】默认拿取该学生成绩列表里面考试类型为“基准成绩”的所有科目，如果有多个基准成绩，默认拿取考试日期最早（远）的那个；如果该学生没有考试类型为“基准成绩”的成绩，则拿取考试日期最早（远）的那次成绩中的所有科目
        StudentAchievement benchmarAchievment = null;
        // 【对比成绩】默认拿取该学生成绩列表中考试日期最新（近）的成绩
        StudentAchievement campreAchievment = null;
        if (benchmarkList != null && !benchmarkList.isEmpty()) {
            benchmarAchievment = benchmarkList.get(0);
        }
        if (compareList != null && !compareList.isEmpty()) {
            if (benchmarAchievment == null) {
                benchmarAchievment = compareList.get(0);
            }
            campreAchievment = compareList.get(compareList.size() - 1);
        }
        AchievementComparisonRequestVo vo = new AchievementComparisonRequestVo();
        List<AchievementBenchmarkRequestVo> benchmarks = new ArrayList<AchievementBenchmarkRequestVo>();
        for (StudentAchievementSubject studentAchievementSubject :benchmarAchievment.getSubjectSet()) {
            AchievementBenchmarkRequestVo benchmark = new AchievementBenchmarkRequestVo();
            benchmark.setStudentAchievementId(benchmarAchievment.getId());
            benchmark.setStudentAchievementSubjectId(studentAchievementSubject.getId());
            AchievementType achievmentType = null;
            AchievementAbstractType achievementAbstractType = benchmarAchievment.getAchievementType();
            if (achievementAbstractType == AchievementAbstractType.SCORE_RANKING) {
                achievmentType = AchievementType.SCORE;
            } else {
                achievmentType = AchievementType.GRADE;
            }
            if (achievmentType != null)
            benchmark.setAchievementType(achievmentType);
            DataDict subject = studentAchievementSubject.getSubject();
            benchmark.setSubjectId(subject.getId());
            benchmarks.add(benchmark);
        }
        vo.setSchoolYearId(schoolYearId);
        vo.setSemester(Semester.LAST_SEMESTER);
        vo.setStudentId(studentId);
        vo.setVersion(0);
        vo.setBenchmarks(benchmarks);
        vo.setComparativeAchievementId(campreAchievment.getId());
        BenchmarkSubjectSearchVo benchmarkSubjectSearchVo = new BenchmarkSubjectSearchVo();
        benchmarkSubjectSearchVo.setSchoolYearId(schoolYearId);
        benchmarkSubjectSearchVo.setSemester(Semester.LAST_SEMESTER);
        benchmarkSubjectSearchVo.setStudentId(studentId);
        NameValue nameValue = courseService.getSubjectsByBenchmark(benchmarkSubjectSearchVo);
        if (nameValue != null) {
            vo.setSubjectIds(nameValue.getValue());
            vo.setSubjectNames(nameValue.getName());
        }
        try {
            this.saveFullAchievementComparison(vo);
        } catch (ApplicationException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 检查多线程是否所有的执行完毕
     */
    private void checkAllDone(List<Future<String>> taskResults, String method) {
        while (true) {
            boolean isAllDone = true;
            for (Future<String> taskResult : taskResults) {
                isAllDone &= ( taskResult.isDone() || taskResult.isCancelled() );
            }
            if (isAllDone) {
                // 任务都执行完毕，跳出循环
                logger.info(method + " end waiting");
                break;
            }
            try {
                logger.info(method + " waiting and sleep 1000 ...");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.toString());
                break;
            }
        }
    }
    
    private DataPackage findPageStudentForInitAchivementComparisions(DataPackage dp) {
        String sql = " select distinct student_id studentId ";
        String countSql = " select count(distinct student_id) ";
        String tmpSql = " from student_achievement where is_deleted = 1 ";
        tmpSql += " and student_id not in (select distinct student_id from achievement_comparison) ";
        sql += tmpSql;
        sql += " order by student_id asc ";
        dp = achievementComparisonDao.findMapPageBySQL(sql, dp, false, null);
        countSql += tmpSql;
        dp.setRowCount(achievementComparisonDao.findCountSql(countSql, null));
        return dp;
    }
    
    /**
     * 检查唯一性
     * @param id
     * @param studentId
     * @param schoolYearId
     * @param semester
     */
    private void checkUniqueAchievementComparison(Integer id, String studentId, String schoolYearId, Semester semester) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " select count(*) from AchievementComparison where isDeleted = 1 "
                + " and student.id = :studentId and schoolYear.id = :schoolYearId and semester = :semester ";
        if (id != null) {
            hql += " and id != :id ";
            params.put("id", id);
        }
        params.put("studentId", studentId);
        params.put("schoolYearId", schoolYearId);
        params.put("semester", semester);
        int count = achievementComparisonDao.findCountHql(hql, params);
        if (count > 0) {
            throw new ApplicationException("该学生已存在相同学年，学期的成绩对比记录");
        }
    }
    
    /**
     * 检查基准成绩的考试日期不能比对比成绩的考试日期大
     * @param achievementComparisonRequestVo
     */
    private void checkAchievementBenchmarkComparison(AchievementComparisonRequestVo achievementComparisonRequestVo) {
        List<AchievementBenchmarkRequestVo> list = achievementComparisonRequestVo.getBenchmarks();
        Integer[] studentAchievementIds = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            studentAchievementIds[i] = list.get(i).getStudentAchievementId();
        }
        String benchmarkLastDate = studentAchievementService.getLastExamizationDateByAchievementIds(studentAchievementIds);
        StudentAchievement comprarativeStudentAchievement = studentAchievementService
                .findStudentAchievementById(achievementComparisonRequestVo.getComparativeAchievementId());
        if (DateTools.daysBetween(benchmarkLastDate, comprarativeStudentAchievement.getExaminationDate()) < 0) {
            throw new ApplicationException("基准成绩的考试日期不能比对比成绩的考试日期大");
        }
    }
    
    @Override
    public AchievementComparisonDetailVo findAchievementComparisonDetailById(
            Integer comparisonId) {
        List<AchievementBenchmarkDetailVo> benchmarkVos = new ArrayList<AchievementBenchmarkDetailVo>();
        AchievementComparison achievementComparison = achievementComparisonDao.findById(comparisonId);
        if (achievementComparison == null || achievementComparison.getIsDeleted() == 0) {
            throw new ApplicationException("编号:" + comparisonId + "对比记录已删除");
        }
        Set<AchievementBenchmark> benchmarks = achievementComparison.getBenchmarks();
        StudentAchievement comparativeAchievement = achievementComparison.getComparativeAchievement();
        for (AchievementBenchmark benchmark : benchmarks) {
            AchievementBenchmarkDetailVo benchmarkVo = HibernateUtils
                    .voObjectMapping(benchmark, AchievementBenchmarkDetailVo.class, "detailBenchmarkVo");
            // 设置基准学生成绩信息
            StudentAchievement studentAchievement = benchmark.getStudentAchievement();
            benchmarkVo.setBenchmarkAchievementId(studentAchievement.getId()); // 基准学生成绩编号
            StudentAchievementSubject benchmarkSubject = benchmark.getBenchmarkSubject();
            benchmarkVo.setBenchmarkSubjectId(benchmarkSubject.getId()); // 基准成绩科目编号
//            benchmarkVo.setSubjectId(benchmarkSubject.getSubject().getId()); // 科目编号
            String benchmarkSubjectName = studentAchievement.getSchoolYear().getName() + studentAchievement.getSemester().getName()
                    + studentAchievement.getExaminationType().getName() + "（" + studentAchievement.getExaminationDate() + "）";
            benchmarkVo.setBenchmarkSubjectName(benchmarkSubjectName);
            AchievementType benchmarkAchievementType = benchmark.getBenchmarkAchievementType();
            benchmarkVo.setBenchmarkTotalScore(benchmarkSubject.getTotalScore());
            if (benchmarkSubject != null) {
                String benchmarkValue = this.getBenchmarkValue(benchmarkSubject, benchmarkAchievementType);
                benchmarkVo.setBenchmarkValue(benchmarkValue);
            }
            
            // 设置对比学生成绩信息
            String compareSubjectName = null;
            StudentAchievementSubject compareSujbect = benchmark.getCompareSubject();
            if (compareSujbect != null) {
                benchmarkVo.setCompareTotalScore(compareSujbect.getTotalScore());
                compareSubjectName = comparativeAchievement.getSchoolYear().getName() + comparativeAchievement.getSemester().getName()
                        + comparativeAchievement.getExaminationType().getName() + "（" + comparativeAchievement.getExaminationDate() + "）";
                benchmarkVo.setCompareSubjectName(compareSubjectName);
                AchievementType compareAchievementType = benchmark.getCompareAchievementType();
                String compareValue =this.getBenchmarkValue(compareSujbect, compareAchievementType);
                benchmarkVo.setCompareValue(compareValue);
            }
            benchmarkVo.setCompareExaminationDate(achievementComparison.getComparativeAchievement().getExaminationDate());
            if (achievementBenchmarkLogService.countAchievementBenchmarkLog(benchmark.getId()) > 1) {
                benchmarkVo.setModifyUserName(benchmark.getModifyUser().getName());
            }
            benchmarkVos.add(benchmarkVo);
        }
        AchievementComparisonDetailVo result = new AchievementComparisonDetailVo();
        String createDate, currentDate = "";
        currentDate = DateTools.getCurrentDate();
        try {
            createDate = DateTools.dateConversString(achievementComparison.getCreateTime(), "yyyy-MM-dd");
            result.setCanUpdated(!(DateTools.daysBetween(createDate, currentDate) > 7)); //是否超过7天
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setName(achievementComparison.getSchoolYear().getName() + achievementComparison.getSemester().getName());
        result.setComparativeName(comparativeAchievement.getSchoolYear().getName() + comparativeAchievement.getSemester().getName());
        result.setId(comparisonId);
        result.setAchievementBenchmarks(benchmarkVos);
        return result;
    }

    @Override
    public List<AchievementComparisonVo> listAchievementComparisonForSearch(
            AchievementComparisonSearchVo achievementComparisonSearchVo) {
        List<AchievementComparisonVo> reslutList = null;
        String hql = " from AchievementComparison where isDeleted = 1 and student.id = :studentId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("studentId", achievementComparisonSearchVo.getStudentId());
        if (StringUtil.isNotBlank(achievementComparisonSearchVo.getSchoolYearId())) {
            hql += " and schoolYear.id = :schoolYearId ";
            params.put("schoolYearId", achievementComparisonSearchVo.getSchoolYearId());
        }
        if (achievementComparisonSearchVo.getSemester() != null) {
            hql += " and semester = :semester ";
            params.put("semester", achievementComparisonSearchVo.getSemester());
        }
        hql += " order by schoolYear.id desc, semester desc ";
        List<AchievementComparison> list  = achievementComparisonDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            reslutList = HibernateUtils.voListMapping(list, AchievementComparisonVo.class);
        }
        return reslutList;
    }
    
    /**
     * 根据id查找学生成绩对比记录
     */
    @Override
    public AchievementComparison findAchievementComparisonById(
            Integer comparisonId) {
        return achievementComparisonDao.findById(comparisonId);
    }
    
    /**
     * 根据学生编号查询最近的成绩对比详情vo
     */
    @Override
    public AchievementComparisonDetailVo findLastAchievementComparisonDetail(
            String studentId) {
        AchievementComparisonDetailVo result = null;
        AchievementComparisonSearchVo searVo = new AchievementComparisonSearchVo();
        searVo.setStudentId(studentId);
        List<AchievementComparisonVo> list = this.listAchievementComparisonForSearch(searVo);
        if( list != null && list.size() > 0) {
            result = this.findAchievementComparisonDetailById(list.get(0).getId());
        }
        return result;
    }
    
    /**
     * 获取基准或对比成绩值
     * @param benchmarkSubject
     * @param benchmarkAchievementType
     * @return
     */
    private String getBenchmarkValue(StudentAchievementSubject benchmarkSubject, AchievementType benchmarkAchievementType) {
        String benchmarkValue = null;
        if (benchmarkAchievementType == AchievementType.SCORE) {
            benchmarkValue = benchmarkSubject.getScore() != null ? benchmarkSubject.getScore().toString() : null;
        } else if (benchmarkAchievementType == AchievementType.CLASS_RANKING) {
            benchmarkValue = benchmarkSubject.getClassRanking() != null ? benchmarkSubject.getClassRanking().toString() : null;
        } else if (benchmarkAchievementType == AchievementType.GRADE_RANKING) {
            benchmarkValue = benchmarkSubject.getGradeRanking() != null ? benchmarkSubject.getGradeRanking().toString() : null;
        } else {
            benchmarkValue = benchmarkSubject.getEvaluationType() != null ? benchmarkSubject.getEvaluationType().getName() : null;
        }
        return benchmarkValue;
    }
    
    /**
     * 检验成绩基准科目set
     * @param benchmarks
     */
    private void validAchievementBenchmarkRequestVo(List<AchievementBenchmarkRequestVo> benchmarks) {
        if (benchmarks.size() <= 0) {
            throw new ApplicationException("成绩基准科目set大小不能为0");
        }
        String subjectIds = "";
        for (AchievementBenchmarkRequestVo benchmark : benchmarks) {
            if (!subjectIds.contains(benchmark.getSubjectId())) {
                subjectIds += benchmark.getSubjectId() + ",";
            } else {
                throw new ApplicationException("科目不可以重复");
            }
            if (benchmark.getStudentAchievementId() == null || benchmark.getStudentAchievementSubjectId() == null
                    || benchmark.getAchievementType() == null) {
                throw new ApplicationException("成绩基准科目set校验出错");
            }
        }
    }

    /**
     * 根据对比学生成绩编号删除对比记录
     */
    @Override
    public void deleteComparisonByAchievementId(
            Integer achievementId) {
        String hql = " update AchievementComparison set isDeleted = 0 where comparativeAchievement.id = :achievementId ";
        String hql2 = " update AchievementComparison set isDeleted = 0 where id in (select achievementComparison.id from AchievementBenchmark where studentAchievement.id = :achievementId) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("achievementId", achievementId);
        achievementComparisonDao.excuteHql(hql, params);
        achievementComparisonDao.excuteHql(hql2, params);
    }

}
