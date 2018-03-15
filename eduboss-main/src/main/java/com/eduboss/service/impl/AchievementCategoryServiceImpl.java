package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AchievementType;
import com.eduboss.common.EvaluationType;
import com.eduboss.dao.AchievementCategoryDao;
import com.eduboss.domain.AchievementCategory;
import com.eduboss.domain.AchievementCategoryScore;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AchievementCategoryScoreVo;
import com.eduboss.domainVo.AchievementCategoryVo;
import com.eduboss.dto.AchievementCategoryRequestVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AchievementCategoryScoreService;
import com.eduboss.service.AchievementCategoryService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * 成绩类别serviceImpl
 * @author arvin
 *
 */
@Service("AchievementCategoryService")
public class AchievementCategoryServiceImpl implements AchievementCategoryService {
    
    private final static Logger logger =Logger.getLogger(AchievementCategoryServiceImpl.class);
    
    @Autowired
    private AchievementCategoryScoreService achievementCategoryScoreService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AchievementCategoryDao achievementCategoryDao;

    /**
     * 获取成绩类别列表
     */
    @Override
    public List<AchievementCategoryVo> listAllAchievementCategory() {
        List<AchievementCategory> list = this.findAllAchievementCategory();
        List<AchievementCategoryVo> voList = (List<AchievementCategoryVo>) HibernateUtils.voListMapping(list, AchievementCategoryVo.class);
        return voList;
    }

    /**
     * 修改成绩类别
     */
    @Override
    public void updateAchievementCategory(
            AchievementCategoryRequestVo achievementCategoryRequestVo) {
        logger.info("achivementCategoryRequestVo:" + achievementCategoryRequestVo);
        Set<AchievementCategoryVo> set = achievementCategoryRequestVo.getAchievementCategorys();
        if (set == null) {
            throw new ApplicationException("提交内容为空");
        }
        boolean isContain100 = false;
        User currentUser = userService.getCurrentLoginUser();
        this.validateAchievementCategoryVo(set);
        achievementCategoryScoreService.deleteAllAchievementCategoryScore();
        for (AchievementCategoryVo vo : set) {
            AchievementCategory category = achievementCategoryDao.findById(vo.getId());
            if (category == null) {
                throw new ApplicationException("编号:" + vo.getId() + "查找不到记录");
            }
            String name = category.getName();
            category.checkVesion(vo.getVersion()); // 乐观锁处理
            category = HibernateUtils.voObjectMapping(vo, AchievementCategory.class);
            category.setName(name);
            category.setModifyUser(currentUser);
            category.setScoreSet(null);
            achievementCategoryDao.merge(category);
            isContain100 = false;
            for (AchievementCategoryScoreVo scoreVo : vo.getScoreSet()) {
                if (scoreVo.getTotalScore() == 100) {
                    isContain100 = true;
                }
                AchievementCategoryScore score = HibernateUtils.voObjectMapping(scoreVo, AchievementCategoryScore.class);
                score.setId(null);
                score.setAchievementCategory(category);
                score.setModifyUser(currentUser);
                achievementCategoryScoreService.saveOrUpdateAchievementCategoryScore(score);
            }
            if (!isContain100) {
                throw new ApplicationException("总分100不能删除");
            }
        }
        
    }
    
    private void validateAchievementCategoryVo(Set<AchievementCategoryVo> set) {
        for (AchievementCategoryVo vo : set) {
            if (vo.getId() == null) {
                throw new ApplicationException("编号不能为空");
            }
            if (vo.getClassRankingStart() == null) {
                throw new ApplicationException("班级排名起不能为空");
            }
            if (vo.getClassRankingEnd() == null) {
                throw new ApplicationException("班级排名止不能为空");
            }
            if (vo.getGradeRankingStart() == null) {
                throw new ApplicationException("年级排名起不能为空");
            }
            if (vo.getGradeRankingEnd() == null) {
                throw new ApplicationException("年级排名止不能为空");
            }
            if (vo.getVersion() == null) {
                throw new ApplicationException("版本不能为空");
            }
            if (StringUtil.isBlank(vo.getEvaluationList())) {
                throw new ApplicationException("评级列表不能为空");
            }
            Set<AchievementCategoryScoreVo> scoreSet = vo.getScoreSet();
            Map<Integer, AchievementCategoryScoreVo> map = Maps.newHashMap();
            for (AchievementCategoryScoreVo scoreVo : scoreSet) {
                if (map.containsKey(scoreVo.getTotalScore())) {
                    throw new ApplicationException("成绩不能重复");
                }
                map.put(scoreVo.getTotalScore(), scoreVo);
            }
        }
    }

    /**
     * 根据分数来判断成绩类别
     */
    @Override
    public AchievementCategory judgeAchievementCategoryByScore(Integer totalScore,
            BigDecimal score) {
        List<AchievementCategory> list = this.findAllAchievementCategory();
        for (AchievementCategory category : list) {
            Set<AchievementCategoryScore> scores = category.getScoreSet();
            boolean isMatchTotalScore = false;
            Integer scoreStart100 = null;
            Integer scoreEnd100 = null;
            for (AchievementCategoryScore s : scores) {
                if (s.getTotalScore().intValue() == totalScore.intValue()) {
                    isMatchTotalScore = true;
                    if (score.compareTo(new BigDecimal(s.getScoreStart())) >= 0
                            && score.compareTo(new BigDecimal(s.getScoreEnd())) < 0) {
                                return category;
                    }
                }
                if (s.getTotalScore().intValue() == 100) {
                    scoreStart100 = s.getScoreStart();
                    scoreEnd100 = s.getScoreEnd();
                }
            }
            if (!isMatchTotalScore) {
                BigDecimal tmpScore = score.multiply(new BigDecimal(100.0/totalScore));
                if (tmpScore.compareTo(new BigDecimal(scoreStart100)) >= 0
                        && tmpScore.compareTo(new BigDecimal(scoreEnd100)) < 0) {
                            return category;
                }
            }
        }
        return null;
    }

    /**
     * 根据排名来判断成绩类别
     */
    @Override
    public AchievementCategory judgeAchievementCategoryByRanking(Integer ranking,
            AchievementType achievementType) {
        List<AchievementCategory> list = this.findAllAchievementCategory();
        for (AchievementCategory category : list) {
            if (achievementType == AchievementType.CLASS_RANKING) {
                if (ranking.intValue() > category.getClassRankingStart().intValue() 
                        && ranking.intValue() <= category.getClassRankingEnd().intValue()) {
                    return category;
                }
            } else if (achievementType == AchievementType.GRADE_RANKING) {
                if (ranking.intValue() > category.getGradeRankingStart().intValue() 
                        && ranking.intValue() <= category.getGradeRankingEnd().intValue()) {
                    return category;
                }
            }
        }
        return null;
    }

    /**
     * 根据评级来判断成绩类别
     */
    @Override
    public AchievementCategory judgeAchievementCategoryByEvaluationType(
            EvaluationType evaluationType) {
        List<AchievementCategory> list = this.findAllAchievementCategory();
        for (AchievementCategory category : list) {
            if (category.getEvaluationList().contains(evaluationType.getValue())) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * 查询全部成绩类别
     * @return
     */
    private List<AchievementCategory> findAllAchievementCategory() {
        return achievementCategoryDao.findAll();
    }

}
