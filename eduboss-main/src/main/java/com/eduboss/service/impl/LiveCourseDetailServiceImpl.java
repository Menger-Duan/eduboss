package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.LiveCourseDetailDao;
import com.eduboss.domain.LiveCourseDetail;
import com.eduboss.service.LiveCourseDetailService;

@Service("liveCourseDetailService")
public class LiveCourseDetailServiceImpl implements LiveCourseDetailService {

	@Autowired
	private LiveCourseDetailDao liveCourseDetailDao;


	@Override
	public LiveCourseDetail saveOrUpdateLiveCourseDetail(LiveCourseDetail liveCourseDetail) {
		// TODO Auto-generated method stub
		liveCourseDetailDao.save(liveCourseDetail);
        return liveCourseDetail;
	}


	@Override
	public List<LiveCourseDetail> findListByPayRecordId(String id) {
		// TODO Auto-generated method stub 
		return liveCourseDetailDao.findListByPayRecordId(id);
	}
	
}
