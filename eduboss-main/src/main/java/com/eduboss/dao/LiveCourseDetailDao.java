package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.LiveCourseDetail;

public interface LiveCourseDetailDao extends GenericDAO<LiveCourseDetail, String> {
	
	/**
	 * 根据recordId查询对应课程详情
	 * @author: duanmenrun 
	 * @Title: findListByPayRecordId 
	 * @Description: TODO 
	 * @param id
	 * @return
	 */
	List<LiveCourseDetail> findListByPayRecordId(String id);
	/**
	 * 根基recordId删除对应课程详情
	 * @author: duanmenrun
	 * @Title: deleteByRecordId 
	 * @Description: TODO 
	 * @param recordId
	 */
	void deleteByRecordId(String recordId);

}
