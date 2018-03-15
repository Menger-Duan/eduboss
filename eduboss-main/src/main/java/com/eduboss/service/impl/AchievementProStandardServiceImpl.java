package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementType;
import com.eduboss.common.CompareType;
import com.eduboss.common.StandardCategory;
import com.eduboss.dao.AchievementProStandardDao;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementProStandard;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AchievementProStandardVo;
import com.eduboss.dto.AchievementProStandardEditVo;
import com.eduboss.dto.AchievementProStandardRequestVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AchievementProStandardService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

/**
 * 成绩进步标准serviceImpl
 * @author arvin
 *
 */
@Service("AchievementProStandardService")
public class AchievementProStandardServiceImpl implements AchievementProStandardService {
    
    private final static Logger logger =Logger.getLogger(AchievementProStandardServiceImpl.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private AchievementProStandardDao achievementProStandardDao;
    
    /**
     * 获取成绩进步标准列表
     */
    @Override
    public List<AchievementProStandardVo> listAllAchievementProStandard() {
        List<AchievementProStandard> list = achievementProStandardDao.findAll();
        List<AchievementProStandardVo> voList = HibernateUtils.voListMapping(list, AchievementProStandardVo.class);
        return voList;
    }

    /**
     * 修改成绩进步标准
     */
    @Override
    public void updateAchievementProStandard(
            AchievementProStandardRequestVo achievementProStandardRequestVo) {
        logger.info("achievementProStandardRequestVo:" + achievementProStandardRequestVo);
        Set<AchievementProStandardEditVo> set = achievementProStandardRequestVo.getAchievementProStandards();
        User currentUser = userService.getCurrentLoginUser();
        this.validateAchievementProStandardEditVo(set);
        for (AchievementProStandardEditVo vo : set) {
            AchievementProStandard achievementProStandard = achievementProStandardDao.findById(vo.getId());
            if (achievementProStandard == null) {
                throw new ApplicationException("编号:" + vo.getId() + "查找不到记录");
            }
            achievementProStandard.checkVesion(vo.getVersion());
            String name = achievementProStandard.getName();
            achievementProStandard = HibernateUtils.voObjectMapping(vo, AchievementProStandard.class);
            achievementProStandard.setName(name);
            if (vo.getScoreBetweenStart() == null && vo.getScoreBetweenEnd() == null) {
                throw new ApplicationException(vo.getName() + vo.getStandardCategory() + "的分数都为空");
            }
            if (vo.getClassRankingBtwStart() == null && vo.getClassRankingBtwEnd() == null) {
                throw new ApplicationException(vo.getName() + vo.getStandardCategory() + "的班级排名都为空");
            }
            if (vo.getGradeRankingBtwStart() == null && vo.getGradeRankingBtwEnd() == null) {
                throw new ApplicationException(vo.getName() + vo.getStandardCategory() + "的年级排名都为空");
            }
            if ((vo.getScoreBetweenStart() != null && vo.getGreaterType() == null) || 
                    (vo.getScoreBetweenStart() == null && vo.getGreaterType() != null)) {
                throw new ApplicationException(vo.getName() + vo.getStandardCategory() + "的大于分数和大于类型没匹配");
            }
            if ((vo.getScoreBetweenEnd() != null && vo.getLessType() == null) || 
                    (vo.getScoreBetweenEnd() == null && vo.getLessType() != null)) {
                throw new ApplicationException(vo.getName() + vo.getStandardCategory() + "的小于分数和小于类型没匹配");
            }
            achievementProStandard.setModifyUser(currentUser);
            achievementProStandardDao.merge(achievementProStandard);
        }
    }
    
    private void validateAchievementProStandardEditVo(Set<AchievementProStandardEditVo> set) {
        for (AchievementProStandardEditVo vo : set) {
            if (vo.getId() == null) {
                throw new ApplicationException("编号不能为空");
            }
            if (vo.getStandardCategory() == null) {
                throw new ApplicationException("进步类别不能为空");
            }
            if (vo.getVersion() == null) {
                throw new ApplicationException("版本不能为空");
            }
        }
    }

    /**
     * 根据分数差和成绩类别判断进步类别
     */
    @Override
    public StandardCategory judgeStandardCategoryByScore(BigDecimal difScore,
            AchievementCategory benchmarkCategory) {
        List<AchievementProStandard> list = this.listAchievementProStandardByName(benchmarkCategory.getName());
        if (list != null && list.size() > 0) {
//            -60
            for (AchievementProStandard standard : list) {
                if (standard.getGreaterType() != null && standard.getLessType() == null) { // 分数相差起不为空, 分数相差止为空
                    if ((standard.getGreaterType() == CompareType.GREATER_THAN_EQUAL && 
                            difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) >= 0) // 大于等于分数相差起
                         || (standard.getGreaterType() == CompareType.GREATER_THAN && 
                         difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) > 0)) { // 大于分数相差起
                         return standard.getStandardCategory();
                    }
                }
                if (standard.getGreaterType() != null && standard.getLessType() != null) { // 分数相差起,分数相差止都不为空
                    if (standard.getGreaterType() == CompareType.GREATER_THAN_EQUAL 
                            && standard.getLessType() == CompareType.LESS_THAN_EQUAL) { // 满足大于等于分数相差起，小于等于分数相差止
                        if (difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) >= 0 && 
                                difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) <= 0) {
                            return standard.getStandardCategory();
                        }
                    } else if (standard.getGreaterType() == CompareType.GREATER_THAN_EQUAL 
                            && standard.getLessType() == CompareType.LESS_THAN) { // 满足大于等于分数相差起，小于分数相差止
                        if (difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) >= 0 && 
                                difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) < 0) {
                            return standard.getStandardCategory();
                        }
                    } else if (standard.getGreaterType() == CompareType.GREATER_THAN
                            && standard.getLessType() == CompareType.LESS_THAN_EQUAL) { // 满足大于分数相差起，小于等于分数相差止
                        if (difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) > 0 && 
                                difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) <= 0) {
                            return standard.getStandardCategory();
                        }
                    } else { // 满足大于分数相差起，小于分数相差止
                        if (difScore.compareTo(new BigDecimal(standard.getScoreBetweenStart())) > 0 && 
                                difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) < 0) {
                            return standard.getStandardCategory();
                        }
                    }
                }
                if (standard.getGreaterType() == null && standard.getLessType() != null) { // 分数相差起为空,分数相差止不为空
                    if ((standard.getLessType() == CompareType.LESS_THAN_EQUAL && 
                            difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) <= 0) // 小于等于分数相差止
                         || (standard.getLessType() == CompareType.LESS_THAN && 
                         difScore.compareTo(new BigDecimal(standard.getScoreBetweenEnd())) < 0)) { // 小于分数相差止
                         return standard.getStandardCategory();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 根据成绩类型和成绩类别判断进步类别
     */
    @Override
    public StandardCategory judgeStandardCategoryByEvaluationType(
            String evaluationTypes, AchievementCategory benchmarkCategory) {
        List<AchievementProStandard> list = this.listAchievementProStandardByName(benchmarkCategory.getName());
        if (list != null && list.size() > 0) {
            for (AchievementProStandard standard : list) {
                if (evaluationTypes.contains(",")) {
                    if (standard.getEvaluationList().contains(evaluationTypes.split(",")[0])) { // 选第一个
                        return standard.getStandardCategory();
                    }
                } else {
                    if (standard.getEvaluationList() != null && standard.getEvaluationList().contains(evaluationTypes)) {
                        return standard.getStandardCategory();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 根据班级排名或者年级排名判断进步类别
     */
    @Override
    public StandardCategory judgeStandardCategoryByClassOrGradeRanking(
            Integer difRanking, AchievementType achievementType,
            AchievementCategory benchmarkCategory) {
        List<AchievementProStandard> list = this.listAchievementProStandardByName(benchmarkCategory.getName());
        if (list != null && list.size() > 0) {
            if (achievementType == AchievementType.CLASS_RANKING) { // 班级排名
                for (AchievementProStandard standard : list) {
                    if (standard.getClassRankingBtwStart() != null && standard.getClassRankingBtwEnd() != null  // 班级排名起，止都不为空
                            && difRanking.intValue() >= standard.getClassRankingBtwStart().intValue() 
                            && difRanking.intValue() <= standard.getClassRankingBtwEnd().intValue() ) { // 大于等于班级排名起 并且小于等于班级排名止
                        return standard.getStandardCategory();
                    } else if (standard.getClassRankingBtwStart() == null && standard.getClassRankingBtwEnd() != null // 班级排名起为空，班级排名止不为空
                            && difRanking.intValue() <= standard.getClassRankingBtwEnd().intValue() ) { // 小于等于班级排名止
                        return standard.getStandardCategory();
                    } else if (standard.getClassRankingBtwStart() != null && standard.getClassRankingBtwEnd() == null // 班级排名起不为空，班级排名止为空
                            && difRanking.intValue() >= standard.getClassRankingBtwStart().intValue()) { // 大于等于班级排名始
                        return standard.getStandardCategory();
                    }
                }
            } else { // 年级排名
                for (AchievementProStandard standard : list) {
                    if (standard.getGradeRankingBtwStart() != null && standard.getGradeRankingBtwEnd() != null  // 年级排名起，止都不为空
                            && difRanking.intValue() >= standard.getGradeRankingBtwStart().intValue() 
                            && difRanking.intValue() <= standard.getGradeRankingBtwEnd().intValue() ) { // 大于等于年级排名起 并且小于等于年级排名止
                        return standard.getStandardCategory();
                    } else if (standard.getGradeRankingBtwStart() == null && standard.getGradeRankingBtwEnd() != null // 年级排名起为空，年级排名止不为空
                            && difRanking.intValue() <= standard.getGradeRankingBtwEnd().intValue() ) { // 小于等于年级排名止
                        return standard.getStandardCategory();
                    } else if (standard.getGradeRankingBtwStart() != null && standard.getGradeRankingBtwEnd() == null // 年级排名起不为空，年级排名止为空
                            && difRanking.intValue() >= standard.getGradeRankingBtwStart().intValue()) { // 大于等于年级排名始
                        return standard.getStandardCategory();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取名称查询所有成绩进步标准
     */
    @Override
    public List<AchievementProStandard> listAchievementProStandardByName(
            String name) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", name);
        String hql = " from AchievementProStandard where name = :name ";
        return achievementProStandardDao.findAllByHQL(hql, params);
    }

}
