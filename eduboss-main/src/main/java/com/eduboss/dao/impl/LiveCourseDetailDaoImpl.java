package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.LiveCourseDetailDao;
import com.eduboss.domain.LiveCourseDetail;
import com.google.common.collect.Maps;

@Repository("LiveCourseDetailDao")
public class LiveCourseDetailDaoImpl extends GenericDaoImpl<LiveCourseDetail,String> implements LiveCourseDetailDao {

	@Override
	public List<LiveCourseDetail> findListByPayRecordId(String id) {
		// TODO Auto-generated method stub 
		String hql = " from LiveCourseDetail where payRecordId = :id  ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		return super.findAllByHQL(hql, params);
	}

	@Override
	public void deleteByRecordId(String recordId) {
		// TODO Auto-generated method stub
		String hql = " delete from LiveCourseDetail where payRecordId = :id  ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", recordId);
		super.excuteHql(hql, params);
	}

}
