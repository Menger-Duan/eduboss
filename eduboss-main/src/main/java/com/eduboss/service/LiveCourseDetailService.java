package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.LiveCourseDetail;

public interface LiveCourseDetailService {
	
    
	LiveCourseDetail saveOrUpdateLiveCourseDetail(LiveCourseDetail liveCourseDetail);

	/**
	 * 根据recordId查询对应课程详情
	 * @author: duanmenrun 
	 * @Title: findListByPayRecordId 
	 * @Description: TODO 
	 * @param id
	 * @return
	 */
	List<LiveCourseDetail> findListByPayRecordId(String id);
	
	
}
