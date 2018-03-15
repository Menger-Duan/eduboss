package com.eduboss.scheduler;

import com.eduboss.service.CourseConflictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by xuwen on 2015/1/7.
 */
@Component
public class ClearExpiredCourseConflict {

    @Autowired
    private CourseConflictService courseConflictService;

    @Scheduled(cron = "0 10 0 * * ?")
    public void handle(){
        courseConflictService.deleteDatasBeforeYesterday();
    }

}
