package com.eduboss.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.eduboss.service.AchievementComparisonService;

@Component
public class AchievmentAsyncTask {
    
    @Autowired
    private AchievementComparisonService achievementComparisonService;
    
	@Async("taskExecutor")
	public Future<String> initAchievementComparisons(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            try {
                achievementComparisonService.initAchievementComparisonByStudentId(map.get("studentId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AsyncResult<String>("success");
	}
    
}
