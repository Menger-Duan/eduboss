package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementType;
import com.eduboss.common.BenchmarkType;
import com.eduboss.dao.AchievementBenchmarkDao;
import com.eduboss.domain.AchievementBenchmark;
import com.eduboss.domain.AchievementBenchmarkLog;
import com.eduboss.domain.AchievementComparison;
import com.eduboss.domain.StudentAchievement;
import com.eduboss.domain.StudentAchievementSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AchievementBenchmarkVo;
import com.eduboss.dto.AchievementBenchmarkUpdateVo;
import com.eduboss.dto.AchievementComparisonUpdateVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AchievementBenchmarkLogService;
import com.eduboss.service.AchievementBenchmarkService;
import com.eduboss.service.AchievementComparisonService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

/**
 * 成绩类别serviceImpl
 * @author arvin
 *
 */
@Service("AchievementBenchmarkService")
public class AchievementBenchmarkServiceImpl implements AchievementBenchmarkService {
    
    @Autowired
    private AchievementComparisonService achievementComparisonService;
    
    @Autowired
    private AchievementBenchmarkLogService achievementBenchmarkLogService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AchievementBenchmarkDao achievementBenchmarkDao;
    
    /**
     * 新增或修改成绩基准科目
     */
    @Override
    public void saveOrUpdateAchievementBenchmark(
            AchievementBenchmark achievementBenchmark) {
        achievementBenchmarkDao.save(achievementBenchmark);
    }

    @Override
    public List<AchievementBenchmark> listAchievementBenchmarkByComparisonId(
            Integer comparisonId) {
        String hql = " from AchievementBenchmark where achievementComparison.id = :comparisonId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("comparisonId", comparisonId);
        return achievementBenchmarkDao.findAllByHQL(hql, params);
    }
    
    /**
     * 根据成绩对比编号查询学生基准科目列表
     */
    @Override
    public List<AchievementBenchmarkVo> listAllAchievementBenchmarkByComparisonId(
            Integer comparisonId) {
        List<AchievementBenchmarkVo> voList = new ArrayList<AchievementBenchmarkVo>();
        List<AchievementBenchmark> list = this.listAchievementBenchmarkByComparisonId(comparisonId);
        for (AchievementBenchmark achievementBenchmark : list) {
            AchievementBenchmarkVo vo = HibernateUtils.voObjectMapping(achievementBenchmark, AchievementBenchmarkVo.class);
            StudentAchievement studentAchievement = achievementBenchmark.getStudentAchievement();
            StudentAchievementSubject benchmarkSubject = achievementBenchmark.getBenchmarkSubject();
            String studentAchievementName = studentAchievement.getSchoolYear().getName() + studentAchievement.getSemester().getName()
                    + studentAchievement.getExaminationType().getName() + benchmarkSubject.getSubject().getName();
            vo.setStudentAchievementName(studentAchievementName);
            vo.setExaminationDate(studentAchievement.getExaminationDate());
            AchievementType benchmarkAchievementType = achievementBenchmark.getBenchmarkAchievementType();
            String benchmarkValue = getBenchmarkValue(benchmarkSubject, benchmarkAchievementType);
            vo.setValue(benchmarkValue);
            voList.add(vo);
        }
        return voList;
    }
    
    /**
     * 根据成绩对比编号删除学生基准科目列表
     */
    @Override
    public void deleteAchievementBenchmarkByComparisonId(Integer comparisonId) {
        String hql = " delete from AchievementBenchmark where achievementComparison.id = :comparisonId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("comparisonId", comparisonId);
        achievementBenchmarkDao.excuteHql(hql, params);
    }
    
    /**
     * 进步判断更改
     */
    @Override
    public void updateStandardCategoryForAchievementBenchmark(
            AchievementComparisonUpdateVo achievementComparisonUpdateVo) {
        AchievementComparison achievementComparison = achievementComparisonService.findAchievementComparisonById(achievementComparisonUpdateVo.getId());
        String createDate, currentDate = "";
        currentDate = DateTools.getCurrentDate();
        try {
            createDate = DateTools.dateConversString(achievementComparison.getCreateTime(), "yyyy-MM-dd");
            if (DateTools.daysBetween(createDate, currentDate) > 7) {
                throw new ApplicationException("已超过7天");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        User currentUser = userService.getCurrentLoginUser();
        this.validAchievementBenchmarkUpdateVoSet(achievementComparisonUpdateVo.getAchievementBenchmarks());
        for (AchievementBenchmarkUpdateVo vo : achievementComparisonUpdateVo.getAchievementBenchmarks()) {
            AchievementBenchmark benchmark = achievementBenchmarkDao.findById(vo.getId());
            benchmark.checkVesion(vo.getVersion());
            AchievementBenchmarkLog log = new AchievementBenchmarkLog(); // 操作日志
            log.setAchievementBenchmarkId(vo.getId());
            log.setStandardCategoryOri(benchmark.getStandardCategory());
            log.setStandardCategoryTar(vo.getStandardCategory());
            log.setCreateUser(currentUser);
            log.setModifyUser(currentUser);
            benchmark.setModifyUser(currentUser);
            if (benchmark.getBenchmarkType() != BenchmarkType.EFFECTIVE) {
                throw new ApplicationException("只有有效才能修改");
            }
            benchmark.setStandardCategory(vo.getStandardCategory());
            achievementBenchmarkDao.merge(benchmark);
            achievementBenchmarkLogService.saveOrUpdateAchievementBenchmarkLog(log); // 保存操作日志
        }
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
            benchmarkValue = benchmarkSubject.getScore().toString();
        } else if (benchmarkAchievementType == AchievementType.CLASS_RANKING) {
            benchmarkValue = benchmarkSubject.getClassRanking().toString();
        } else if (benchmarkAchievementType == AchievementType.GRADE_RANKING) {
            benchmarkValue = benchmarkSubject.getGradeRanking().toString();
        } else {
            benchmarkValue = benchmarkSubject.getEvaluationType().getName();
        }
        return benchmarkValue;
    }
    
    /**
     * 校验成绩基准科目更新set
     * @param achievementBenchmarks
     */
    private void validAchievementBenchmarkUpdateVoSet(Set<AchievementBenchmarkUpdateVo> achievementBenchmarks) {
        for (AchievementBenchmarkUpdateVo vo : achievementBenchmarks) {
            if (vo.getId() == null || vo.getStandardCategory() == null) {
                throw new ApplicationException("成绩基准科目更新set校验出错");
            }
        }
    }

    /**
     * 根据学生成绩科目编号删除学生基准科目
     */
    @Override
    public void deleteAchievementBenchmarkByBenchmarkSubjectIds(
            Set<Integer> benchmarkSubjectIds) {
        String hql = " delete from AchievementBenchmark where benchmarkSubject.id in (:benchmarkSubjectIds) or compareSubject.id in (:benchmarkSubjectIds) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("benchmarkSubjectIds", benchmarkSubjectIds);
        achievementBenchmarkDao.excuteHql(hql, params);
    }

    /**
     * 根据学生学生成绩科目编号计算学生基准科目
     */
    @Override
    public int countAchievementBenchmarkByBenchmarkSubjectIds(
            Set<Integer> benchmarkSubjectIds) {
        String hql = " select count(*) from AchievementBenchmark where benchmarkSubject.id in (:benchmarkSubjectIds) or compareSubject.id in (:benchmarkSubjectIds) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("benchmarkSubjectIds", benchmarkSubjectIds);
        return achievementBenchmarkDao.findCountHql(hql, params);
    }

}
