package com.eduboss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.AchievementCategoryScoreDao;
import com.eduboss.domain.AchievementCategoryScore;
import com.eduboss.service.AchievementCategoryScoreService;

/**
 * 成绩类别分数service
 * @author arvin
 *
 */
@Service("AchievementCategoryScoreService")
public class AchievementCategoryScoreServiceImpl implements AchievementCategoryScoreService {

    @Autowired
    private AchievementCategoryScoreDao achievementCategoryScoreDao;
    
    /**
     * 新增或修改成绩类别分数
     */
    @Override
    public void saveOrUpdateAchievementCategoryScore(
            AchievementCategoryScore score) {
        achievementCategoryScoreDao.save(score);
    }

    /**
     * 删除所有成绩类别分数
     */
    @Override
    public void deleteAllAchievementCategoryScore() {
        String hql = " delete from AchievementCategoryScore where 1=1 ";
        achievementCategoryScoreDao.excuteHql(hql, null);
    }

}
